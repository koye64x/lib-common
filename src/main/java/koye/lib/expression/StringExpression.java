package koye.lib.expression;

import static koye.lib.expression.OPTION.IGNORE_CASE;

public abstract class StringExpression extends BooleanExpression {

    private final Expression<String> a;
    private final String bValue;
    private final Constant<String> b;

    public StringExpression(Expression<String> a, String bValue, OPTION... options) {
        super(options);
        this.a = a;
        this.bValue = bValue;
        this.b = new Constant<>(bValue);
    }

    public Expression<String> getA() {
        return a;
    }

    public String getBValue() {
        return bValue;
    }

    @Override
    public OPTION[] availableOptions() {
        return OPTION.union(super.availableOptions(), new OPTION[]{IGNORE_CASE});
    }

    public class START_WITH extends StringExpression {

        public START_WITH(Expression<String> a, String bValue, OPTION... options) {
            super(a, bValue, options);
        }

        @Override
        public <C, V> Boolean operationResult(C container, ValueGetter<C, V> valueGetter) {
            if (optionsContain(IGNORE_CASE)) {
                return a.result(container, valueGetter).toLowerCase().startsWith(b.result(container, valueGetter).toLowerCase());
            } else {
                return a.result(container, valueGetter).startsWith(b.result(container, valueGetter));
            }
        }

    }

    public class END_WITH extends StringExpression {

        public END_WITH(Expression<String> a, String bValue, OPTION... options) {
            super(a, bValue, options);
        }

        @Override
        public <C, V> Boolean operationResult(C container, ValueGetter<C, V> valueGetter) {
            if (optionsContain(IGNORE_CASE)) {
                return a.result(container, valueGetter).toLowerCase().endsWith(b.result(container, valueGetter).toLowerCase());
            } else {
                return a.result(container, valueGetter).endsWith(b.result(container, valueGetter));
            }
        }

    }

    public class CONTAIN extends StringExpression {

        public CONTAIN(Expression<String> a, String bValue, OPTION... options) {
            super(a, bValue, options);
        }

        @Override
        public <C, V> Boolean operationResult(C container, ValueGetter<C, V> valueGetter) {
            if (optionsContain(IGNORE_CASE)) {
                return a.result(container, valueGetter).toLowerCase().contains(b.result(container, valueGetter).toLowerCase());
            } else {
                return a.result(container, valueGetter).contains(b.result(container, valueGetter));
            }
        }

    }

}
