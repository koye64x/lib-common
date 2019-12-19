package koye.lib.expression;

public interface ValueGetter<C, T> {

    Object getValue(C container, Path<T> path);

}
