package koye.lib.expression;

public abstract class FunctionExpression<T> extends AbstractExpression<T> {

    public class LENGTH extends FunctionExpression<Integer> {

        private final Expression<String> expression;

        public LENGTH(Expression<String> expression) {
            this.expression = expression;
        }

        @Override
        public OPTION[] availableOptions() {
            return new OPTION[0];
        }

        @Override
        public <C, V> Integer result(C container, ValueGetter<C, V> valueGetter) {
            return expression.result(container, valueGetter).length();
        }

    }

}
