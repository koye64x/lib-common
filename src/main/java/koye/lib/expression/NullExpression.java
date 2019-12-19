package koye.lib.expression;

public abstract class NullExpression extends BooleanExpression {

    private final Path path;

    public NullExpression(Path path, OPTION... options) {
        super(options);
        this.path = path;
    }

    public Path getPath() {
        return path;
    }

    public class IS_NULL extends NullExpression {

        public IS_NULL(Path a) {
            super(a);
        }

        @Override
        public <C, V> Boolean operationResult(C container, ValueGetter<C, V> valueGetter) {
            return path.result(container, valueGetter) == null;
        }

    }

}
