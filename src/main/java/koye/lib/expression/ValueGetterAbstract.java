package koye.lib.expression;

public abstract class ValueGetterAbstract<C, T> implements ValueGetter<C, T> {

    public abstract Object getValue(C container, String name);

    @Override
    public T getValue(C container, Path<T> path) {
        Object o = getValue(container, path.currentPath());
        Path nextPath = path.nextPath();
        if (nextPath.isEmpty()) {
            return (T) o;
        } else {
            if (o == null) {
                throw new IllegalArgumentException("Value '"+path.currentPath()+"' is null. Get next path unavailable");
            }
            return (T) getValue((C)o, nextPath);
        }
    }

}
