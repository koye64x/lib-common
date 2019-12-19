package koye.lib.expression;

public class Constant<T> implements Expression<T> {

    private final T constantValue;

    public Constant(T constantValue) {
        this.constantValue = constantValue;
    }

    @Override
    public <C, V> T result(C container, ValueGetter<C, V> valueGetter) {
        return constantValue;
    }

    public T getConstantValue() {
        return constantValue;
    }

}
