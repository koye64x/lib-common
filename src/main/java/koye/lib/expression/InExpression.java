package koye.lib.expression;

import java.util.ArrayList;
import java.util.Collection;

import static koye.lib.expression.OPTION.IGNORE_CASE;

public abstract class InExpression<T> extends BooleanExpression {

    private final Expression<T> a;
    private final Collection<Expression<T>> collection;

    public InExpression(Expression<T> a, Collection<Expression<T>> collection, OPTION... options) {
        super(options);
        checkExpressionType(a);
        this.a = a;
        this.collection = collection;
    }

    @Override
    public OPTION[] availableOptions() {
        return OPTION.union(super.availableOptions(), new OPTION[]{IGNORE_CASE});
    }

    private <C,V> T getA(C container, ValueGetter<C,V> valueGetter) {
        T res = a.result(container, valueGetter);
        if (optionsContain(IGNORE_CASE)) {
            res = (T) ((String)res).toLowerCase();
        }
        return res;
    }

    private <C, V> Collection<T> getCollection(C container, ValueGetter<C, V> valueGetter) {
        Collection<T> res = new ArrayList<>();
        for (Expression<T> e : collection) {
            T ci = e.result(container, valueGetter);
            if (optionsContain(IGNORE_CASE)) {
                ci = (T) ((String)ci).toLowerCase();
            }
            res.add(ci);
        }
        return res;
    }

    public class IN<I> extends InExpression<I> {

        public IN(Expression<I> a, Collection<Expression<I>> collection, OPTION... options) {
            super(a, collection, options);
        }

        @Override
        public <C, V> Boolean operationResult(C container, ValueGetter<C, V> valueGetter) {
            T aValue = getA(container, valueGetter);
            Collection<T> c = getCollection(container, valueGetter);
            return c.contains(aValue);
        }
    }

}

