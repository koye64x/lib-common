package koye.lib.common.containers;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class MapSet<K,V> extends TreeMap<K, Set<V>> {

    private Comparator<V> valueComparator;

    public MapSet(Comparator<K> keyComparator, Comparator<V> valueComparator) {
        super(keyComparator);
        this.valueComparator = valueComparator;
    }

    public MapSet<K,V> putItem(K key, V value) {
        Set<V> valueSet;
        if (get(key) == null) {
            valueSet = new TreeSet<V>(valueComparator);
            put(key, valueSet);
        } else {
            valueSet = get(key);
        }
        valueSet.add(value);
        return this;
    }

}
