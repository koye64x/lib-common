package koye.lib.common.utils;

import koye.lib.common.meta.FetchSchema;
import koye.lib.expression.*;
import koye.lib.expression.CompareExpression.EQUAL;
import koye.lib.expression.CompareExpression.GREAT;
import koye.lib.expression.CompareExpression.GREAT_OR_EQUAL;
import koye.lib.expression.CompareExpression.LESS;
import koye.lib.expression.CompareExpression.LESS_OR_EQUAL;
import koye.lib.expression.CompareExpression.BETWEEN;
import koye.lib.expression.LogicalExpression.AND;
import koye.lib.expression.LogicalExpression.OR;
import koye.lib.expression.LogicalExpression.NOT;
import koye.lib.expression.InExpression.IN;
import koye.lib.expression.NullExpression.IS_NULL;
import koye.lib.expression.StringExpression.START_WITH;
import koye.lib.expression.StringExpression.END_WITH;
import koye.lib.expression.StringExpression.CONTAIN;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.Subgraph;
import javax.persistence.criteria.*;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

import static koye.lib.expression.OPTION.IGNORE_CASE;
import static koye.lib.expression.OPTION.NEGATIVE;

public class JPAUtils {

    public static <T> Predicate getBooleanExpression(Class<T> entityClass, CriteriaBuilder criteriaBuilder, BooleanExpression filter,
                                                     Map<ParameterExpression, Object> params) {
        Predicate predicate;
        if (filter instanceof LogicalExpression) {
            LogicalExpression logicalExpression = (LogicalExpression) filter;
            List<Predicate> predicates = logicalExpression.getItems().stream()
                    .map(e -> getBooleanExpression(entityClass, criteriaBuilder, e, params))
                    .collect(Collectors.toList());
            if (filter instanceof AND) {
                predicate = criteriaBuilder.and(predicates.toArray(new Predicate[] {}));
            } else if (filter instanceof OR) {
                predicate = criteriaBuilder.or(predicates.toArray(new Predicate[] {}));
            } else {
                throw new IllegalStateException("No implementation for LogicalExpression class " + filter.getClass().getSimpleName());
            }
        } else if (filter instanceof NOT) {
            NOT notExpression = (NOT) filter;
            predicate = criteriaBuilder.not(getBooleanExpression(entityClass, criteriaBuilder, notExpression.getExpression(), params));
        } else {
            if (filter instanceof CompareExpression) {
                CompareExpression compareExpression = (CompareExpression) filter;
                koye.lib.expression.Expression a = compareExpression.getA();
                Class<?> fieldType = getFieldType(entityClass, (koye.lib.expression.Path) a);
                Expression x = getJPAExpression(entityClass, criteriaBuilder, a, null, null);
                Expression y = getJPAExpression(entityClass, criteriaBuilder, compareExpression.getB(), params, fieldType);
                if (filter.optionsContain(IGNORE_CASE)) {
                    x = criteriaBuilder.lower(x);
                    y = criteriaBuilder.lower(y);
                }
                if (filter instanceof EQUAL) {
                    predicate = criteriaBuilder.equal(x, y);
                } else if (filter instanceof GREAT) {
                    predicate = criteriaBuilder.greaterThan(x, y);
                } else if (filter instanceof GREAT_OR_EQUAL) {
                    predicate = criteriaBuilder.greaterThanOrEqualTo(x, y);
                } else if (filter instanceof LESS) {
                    predicate = criteriaBuilder.lessThan(x, y);
                } else if (filter instanceof LESS_OR_EQUAL) {
                    predicate = criteriaBuilder.lessThanOrEqualTo(x, y);
                } else {
                    throw new IllegalStateException("No implementation for CompareExpression class " + filter.getClass().getSimpleName());
                }
            } else if (filter instanceof BETWEEN) {
                BETWEEN between = (BETWEEN) filter;
                koye.lib.expression.Expression a = between.getA();
                Class<?> fieldType = getFieldType(entityClass, (koye.lib.expression.Path) a);
                Expression v = getJPAExpression(entityClass, criteriaBuilder, a, null, null);
                Expression x = getJPAExpression(entityClass, criteriaBuilder, between.getB(), params, fieldType);
                Expression y = getJPAExpression(entityClass, criteriaBuilder, between.getC(), params, fieldType);
                if (filter.optionsContain(IGNORE_CASE)) {
                    v = criteriaBuilder.lower(v);
                    x = criteriaBuilder.lower(x);
                    y = criteriaBuilder.lower(y);
                }
                predicate = criteriaBuilder.between(v, x, y);
            } else if (filter instanceof IN) {
                IN inExpression = (IN) filter;
                Expression e = getJPAExpression(entityClass, criteriaBuilder, inExpression.getA(), null, null);
                Collection c = inExpression.getCollectionValues();
                if (filter.optionsContain(IGNORE_CASE)) {
                    e = criteriaBuilder.lower(e);
                    c = (Collection) c.stream().map(v -> ((String)v).toLowerCase()).collect(Collectors.toList());
                }
                CriteriaBuilder.In in = criteriaBuilder.in(e);
                c.forEach(in::value);
                predicate = in;
            } else if (filter instanceof IS_NULL) {
                IS_NULL isNull = (IS_NULL) filter;
                Expression x = getJPAExpression(entityClass, criteriaBuilder, isNull.getPath(), null, null);
                predicate = criteriaBuilder.isNull(x);
            } else if (filter instanceof StringExpression) {
                StringExpression stringExpression = (StringExpression) filter;
                Expression x = getJPAExpression(entityClass, criteriaBuilder, stringExpression.getA(), null, null);
                String pattern = stringExpression.getBValue();
                if (filter.optionsContain(IGNORE_CASE)) {
                    x = criteriaBuilder.lower(x);
                    pattern = pattern.toLowerCase();
                }
                if (filter instanceof START_WITH) {
                    predicate = criteriaBuilder.like(x, pattern + "%");
                } else if (filter instanceof END_WITH) {
                    predicate = criteriaBuilder.like(x, "%" + pattern);
                } else if (filter instanceof CONTAIN) {
                    predicate = criteriaBuilder.like(x, "%" + pattern + "%");
                } else {
                    throw new IllegalStateException("No implementation for StringExpression class " + filter.getClass().getSimpleName());
                }
            } else {
                throw new IllegalStateException("No implementation for class " + filter.getClass().getSimpleName());
            }
        }
        if (filter.optionsContain(NEGATIVE)) {
            return criteriaBuilder.not(predicate);
        } else {
            return predicate;
        }
    }

    private static <T> Class<?> getFieldType(Class<T> entityClass, koye.lib.expression.Path path) {
        Field field = ReflectUtils.findField(entityClass, path.currentPath());
        if (field == null) {
            throw new IllegalArgumentException("No field '"+path.currentPath()+"' found for class " + entityClass.getName());
        }
        Class<?> fieldType = field.getType();
        if (Collection.class.isAssignableFrom(fieldType)) {
            fieldType = ReflectUtils.getFieldGenericType(field);
        }
        if (path.nextPath().isEmpty()) {
            return fieldType;
        } else {
            return getFieldType(fieldType, path.nextPath());
        }
    }

    private static <T, E> Expression<T> getJPAExpression(Class<E> entityClass, CriteriaBuilder criteriaBuilder,
                                                         koye.lib.expression.Expression<T> expression,
                                                         Map<ParameterExpression, Object> params,
                                                         Class<?> fieldType) {
        if (expression instanceof BooleanExpression) {
            return (Expression<T>) getBooleanExpression(entityClass, criteriaBuilder, (BooleanExpression) expression, params);
        } else if (expression instanceof koye.lib.expression.Path) {
            koye.lib.expression.Path expressionPath = (koye.lib.expression.Path) expression;
            CriteriaQuery<E> criteria = criteriaBuilder.createQuery( entityClass );
            Root<E> root = criteria.from(entityClass);
            Path path;
            if (expressionPath.size() > 1) {
                path = root;
                for (Object o : expressionPath) {
                    path = path.get((String) o);
                }
            } else {
                path = root.get(expressionPath.currentPath());
            }
            return path;
        } else if (expression instanceof Constant) {
            Constant constant = (Constant) expression;
            ParameterExpression pe = criteriaBuilder.parameter(fieldType);
            params.put(pe, constant.getConstantValue());
            return pe;
        } else {
            throw new IllegalArgumentException("No implementation for operation " + expression.getClass().getSimpleName());
        }
    }

    public static <T> EntityGraph<T> getEntityGraph(EntityManager em, Class<T> entityClass, FetchSchema fetchSchema) {
        EntityGraph<T> res = em.createEntityGraph(entityClass);
        for (Map.Entry<String, FetchSchema> e : fetchSchema.entrySet()) {
            if (e.getValue() == null) {
                res.addAttributeNodes(e.getKey());
            } else {
                Class<?> propClass = ReflectUtils.getPropEntityClass(entityClass, e.getKey());
                addSubGraph(res, propClass, e.getKey(), e.getValue());
            }
        }
        return res;
    }

    private static <T, P> void addSubGraph(EntityGraph<T> graph, Class<P> propClass, String fieldName, FetchSchema fetchSchema) {
        Subgraph<P> subgraph = graph.addSubgraph(fieldName);
        for (Map.Entry<String, FetchSchema> e : fetchSchema.entrySet()) {
            if (e.getValue() == null) {
                subgraph.addAttributeNodes(e.getKey());
            } else {
                Class<?> subPropClass = ReflectUtils.getPropEntityClass(propClass, e.getKey());
                addSubGraph(subgraph, subPropClass, e.getKey(), e.getValue());
            }

        }
    }

    private static <T, P> void addSubGraph(Subgraph<T> graph, Class<P> propClass, String fieldName, FetchSchema fetchSchema) {
        Subgraph<P> subgraph = graph.addSubgraph(fieldName);
        for (Map.Entry<String, FetchSchema> e : fetchSchema.entrySet()) {
            if (e.getValue() == null) {
                subgraph.addAttributeNodes(e.getKey());
            } else {
                Class<?> subPropClass = ReflectUtils.getPropEntityClass(propClass, e.getKey());
                addSubGraph(subgraph, subPropClass, e.getKey(), e.getValue());
            }
        }
    }

    public static Map<String, Object> toMap(Object entity, FetchSchema fetchSchema) {
        Map<String, Object> res = new HashMap<>();
        for (Field field : Objects.requireNonNull(ReflectUtils.getDeclaredInheritedFields(entity.getClass()))) {
            if (ReflectUtils.isPrimitiveType(field.getType())) {
                res.put(field.getName(), ReflectUtils.getFieldValueByGetter(entity, field.getName()));
            }
        }
        if (fetchSchema != null) {
            for (Map.Entry<String, FetchSchema> e : fetchSchema.entrySet()) {
                Object propValue = ReflectUtils.getFieldValueByGetter(entity, e.getKey());
                res.put(e.getKey(), toMap(propValue, e.getValue()));
            }
        }
        return res;
    }

}
