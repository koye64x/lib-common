package koye.lib.expression;

public abstract class NullExpression extends BooleanExpression {

    private final Expression a;

    public NullExpression(Expression a, OPTION... options) {
        super(options);
        this.a = a;
    }

    public class IS_NULL extends NullExpression {

        public IS_NULL(Expression a) {
            super(a);
        }

        @Override
        public <C, V> Boolean operationResult(C container, ValueGetter<C, V> valueGetter) {
            return a.result(container, valueGetter) == null;
        }

    }

}
