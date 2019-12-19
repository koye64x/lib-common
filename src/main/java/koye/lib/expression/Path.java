package koye.lib.expression;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;

public class Path<T> extends LinkedList<String> implements Expression<T> {

    public static final String DELIMITER = ".";

    public Path(String... pathItemsArgs) {
        if (pathItemsArgs.length == 1) {
            addAll(Arrays.asList(pathItemsArgs[0].split(DELIMITER)));
        } else {
            addAll(Arrays.asList(pathItemsArgs));
        }
    }

    public Path(Collection<String> paths) {
        addAll(paths);
    }

    public String currentPath() {
        return get(0);
    }

    public Path nextPath() {
        return new Path(subList(1, size()));
    }

    @Override
    public String toString() {
        return String.join(DELIMITER, this);
    }

    @Override
    public <C, V> T result(C container, ValueGetter<C, V> valueGetter) {
        return (T) valueGetter.getValue(container, (Path<V>) this);
    }

}
