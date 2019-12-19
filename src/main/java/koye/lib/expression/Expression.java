package koye.lib.expression;

public interface Expression<T> {

    <C, V> T result(C container, ValueGetter<C, V> valueGetter);

}
