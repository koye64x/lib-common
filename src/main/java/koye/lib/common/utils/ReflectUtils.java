package koye.lib.common.utils;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ReflectUtils {

    public static List<Field> getDeclaredInheritedFields(Class<?> objectClass) {
        if (Object.class.equals(objectClass)) {
            return null;
        } else {
            List<Field> list = null;
            if (objectClass.getSuperclass() != null) {
                list = getDeclaredInheritedFields(objectClass.getSuperclass());
            }
            if (list == null) {
                list = new ArrayList<>();
            }

            for (Field f: objectClass.getDeclaredFields()) {
                if (!Modifier.isStatic(f.getModifiers())
                    && !Modifier.isStatic(f.getModifiers())
                        && !Modifier.isFinal(f.getModifiers())) {
                    list.add(f);
                }
            }
            return list;
        }
    }

    public static boolean isPrimitiveType(Class<?> type) {
        if (type.isPrimitive()) {
            return true;
        }
        if (Number.class.isAssignableFrom(type)
                || Boolean.class.equals(type)
                || String.class.equals(type)
                || Character.class.equals(type)
        ) {
            return true;
        }
        return false;
    }

    public static Class<?> getFieldGenericType(Field f) {
        ParameterizedType pt = (ParameterizedType) f.getGenericType();
        return (Class<?>) pt.getActualTypeArguments()[0];
    }

    public static Class<?> getClassGenericType(Class<?> type) {
        ParameterizedType pt = (ParameterizedType) type.getGenericSuperclass();
        return (Class<?>) pt.getActualTypeArguments()[0];
    }

    public static Field findField(Class<?> type, String fieldName) {
        if (type == null || type == Object.class) {
            return null;
        }
        for (Field declaredField : type.getDeclaredFields()) {
            if (declaredField.getName().equals(fieldName)) {
                return declaredField;
            }
        }
        return findField(type.getSuperclass(), fieldName);
    }

    private static Method getGetterMethod(Class<?> aClass, String fieldName) throws IntrospectionException {
        PropertyDescriptor pd = new PropertyDescriptor(fieldName, aClass);
        return pd.getReadMethod();
    }

    public static <T extends Annotation> List<Field> getFieldsByPropAnnotation(Class<?> entityClass, Class<T> annotation) {
        return getDeclaredInheritedFields(entityClass).stream().filter(f -> getPropAnnotation(entityClass, f.getName(), annotation) != null)
                .collect(Collectors.toList());
    }

    public static <T extends Annotation> T getPropAnnotation(Class<?> objectClass, String fieldName, Class<T> annotationClass) {
        Field f = findField(objectClass, fieldName);
        assert f != null;
        T res = f.getAnnotation(annotationClass);
        if (res == null) {
            Method m;
            try {
                m = getGetterMethod(objectClass, fieldName);
                res = m.getAnnotation(annotationClass);
            } catch (IntrospectionException e) {
                e.printStackTrace();
            }
        }
        return res;
    }

    public static <T> Class<?> getPropEntityClass(Class<T> entityClass, String fieldName) {
        Field field = findField(entityClass, fieldName);
        if (Collection.class.isAssignableFrom(field.getType())) {
            return getFieldGenericType(field);
        } else {
            return field.getType();
        }
    }

    public static Object getFieldValueByGetter(Object o, String propName) {
        try {
            Method m = getGetterMethod(o.getClass(), propName);
            return m.invoke(o);
        } catch (IllegalAccessException | InvocationTargetException | IntrospectionException e) {
            throw new RuntimeException(e);
        }
    }

}
