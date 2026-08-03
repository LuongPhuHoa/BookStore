package org.example.collectionslab;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/** Bounded LRU cache, using LinkedHashMap's access-order linked list. */
public final class LruCache<K, V> {
    private final Map<K, V> entries;

    public LruCache(int capacity) {
        if (capacity < 1) throw new IllegalArgumentException("capacity must be positive");
        entries = new LinkedHashMap<>(capacity, .75f, true) {
            @Override protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
                return size() > capacity;
            }
        };
    }
    public void put(K key, V value) { entries.put(key, value); }
    public Optional<V> get(K key) { return Optional.ofNullable(entries.get(key)); }
    public int size() { return entries.size(); }
}
