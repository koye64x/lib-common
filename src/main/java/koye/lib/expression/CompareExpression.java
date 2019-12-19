package koye.lib.expression;

import static koye.lib.expression.OPTION.IGNORE_CASE;

public abstract class CompareExpression<T extends Comparable<T>> extends BooleanExpression {

    private final Expression<T> a;
    private final Expression<T> b;

    public CompareExpression(Expression<T> a, Expression<T> b, OPTION... options) {
        super(options);
        this.a = a;
        this.b = b;
        checkExpressionType(a);
    }

    @Override
    public OPTION[] availableOptions() {
        return OPTION.union(super.availableOptions(), new OPTION[]{IGNORE_CASE});
    }

    public Expression<T> getA() {
        return a;
    }

    public Expression<T> getB() {
        return b;
    }

    private <C, V> int compareResult(C container, ValueGetter<C, V> valueGetter) {
        T a = this.a.result(container, valueGetter);
        T b = this.b.result(container, valueGetter);
        if (optionsContain(IGNORE_CASE)) {
            return ((String)a).compareToIgnoreCase((String)b);
        } else {
            return a.compareTo(b);
        }
    }

    public class EQUAL<T extends Comparable<T>> extends CompareExpression<T> {

        public EQUAL(Expression<T> a, Expression<T> b, OPTION... options) {
            super(a, b, options);
        }

        @Override
        public <C, V> Boolean operationResult(C container, ValueGetter<C, V> valueGetter) {
            return compareResult(container, valueGetter) == 0;
        }

    }

    public class GREAT<T extends Comparable<T>> extends CompareExpression<T> {

        public GREAT(Expression<T> a, Expression<T> b, OPTION... options) {
            super(a, b, options);
        }

        @Override
        public <C, V> Boolean operationResult(C container, ValueGetter<C, V> valueGetter) {
            return compareResult(container, valueGetter) > 0;
        }

    }

    public class GREAT_OR_EQUAL<T extends Comparable<T>> extends CompareExpression<T> {

        public GREAT_OR_EQUAL(Expression<T> a, Expression<T> b, OPTION... options) {
            super(a, b, options);
        }

        @Override
        public <C, V> Boolean operationResult(C container, ValueGetter<C, V> valueGetter) {
            return compareResult(container, valueGetter) >= 0;
        }

    }

    public class LESS<T extends Comparable<T>> extends CompareExpression<T> {

        public LESS(Expression<T> a, Expression<T> b, OPTION... options) {
            super(a, b, options);
        }

        @Override
        public <C, V> Boolean operationResult(C container, ValueGetter<C, V> valueGetter) {
            return compareResult(container, valueGetter) < 0;
        }
    }

    public class LESS_OR_EQUAL<T extends Comparable<T>> extends CompareExpression<T> {

        public LESS_OR_EQUAL(Expression<T> a, Expression<T> b, OPTION... options) {
            super(a, b, options);
        }

        @Override
        public <C, V> Boolean operationResult(C container, ValueGetter<C, V> valueGetter) {
            return compareResult(container, valueGetter) <= 0;
        }
    }

    public class BETWEEN<T extends Comparable<T>> extends BooleanExpression {

        private final Expression<T> a;
        private final Expression<T> b;
        private final Expression<T> c;

        public BETWEEN(Expression<T> a, Expression<T> b, Expression<T> c, OPTION... options) {
            super(options);
            checkExpressionType(a);
            this.a = a;
            this.b = b;
            this.c = c;
        }

        public Expression<T> getA() {
            return a;
        }

        public Expression<T> getB() {
            return b;
        }

        public Expression<T> getC() {
            return c;
        }

        @Override
        public <C, V> Boolean operationResult(C container, ValueGetter<C, V> valueGetter) {
            T a = this.a.result(container, valueGetter);
            T b = this.b.result(container, valueGetter);
            T c = this.c.result(container, valueGetter);
            if (optionsContain(IGNORE_CASE)) {
                a = (T) ((String)a).toLowerCase();
                b = (T) ((String)b).toLowerCase();
                c = (T) ((String)c).toLowerCase();
            }
            return a.compareTo(b) >= 0 && a.compareTo(c) <= 0;
        }
    }

}