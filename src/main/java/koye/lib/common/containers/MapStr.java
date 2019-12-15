package koye.lib.common.containers;

import java.util.TreeMap;

public class MapStr<V> extends TreeMap<String, V> {

    public MapStr<V> putItem(String key, V value) {
        put(key, value);
        return this;
    }
}
