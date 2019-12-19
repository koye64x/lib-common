package koye.lib.expression;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

import static koye.lib.expression.OPTION.IGNORE_CASE;

public abstract class InExpression<T> extends BooleanExpression {

    private final Expression<T> a;
    private final Collection<Expression<T>> collection;
    private final Collection<T> collectionValues;

    public InExpression(Expression<T> a, Collection<T> collectionValues, OPTION... options) {
        super(options);
        checkExpressionType(a);
        this.a = a;
        this.collectionValues = collectionValues;
        this.collection = collectionValues.stream().map(Constant::new).collect(Collectors.toList());
    }

    @Override
    public OPTION[] availableOptions() {
        return OPTION.union(super.availableOptions(), new OPTION[]{IGNORE_CASE});
    }

    public Expression<T> getA() {
        return a;
    }

    public <C,V> T getAValue(C container, ValueGetter<C,V> valueGetter) {
        T res = a.result(container, valueGetter);
        return res;
    }

    public Collection<T> getCollectionValues() {
        return collectionValues;
    }

    public class IN<I> extends InExpression<I> {

        public IN(Expression<I> a, Collection<I> collectionValues, OPTION... options) {
            super(a, collectionValues, options);
        }

        @Override
        public <C, V> Boolean operationResult(C container, ValueGetter<C, V> valueGetter) {
            T aValue = (T) getA().result(container, valueGetter);
            Collection<T> c = (Collection<T>) getCollectionValues();
            if (optionsContain(IGNORE_CASE)) {
                aValue = (T) ((String)aValue).toLowerCase();
                c = (Collection<T>) c.stream().map(v -> ((String)v).toLowerCase()).collect(Collectors.toList());
            }
            return c.contains(aValue);
        }
    }

}

