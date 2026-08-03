package org.example.collectionslab;

import java.util.Objects;

/** Educational separate-chaining map showing buckets, collisions, and resizing. */
public final class SimpleHashMap<K, V> {
    private static final float LOAD_FACTOR = .75f;
    private Node<K, V>[] table;
    private int size;
    private int threshold;

    @SuppressWarnings("unchecked")
    public SimpleHashMap() {
        table = (Node<K, V>[]) new Node[16];
        threshold = 12;
    }

    public V put(K key, V value) {
        Objects.requireNonNull(key, "null keys are outside this learning implementation");
        int hash = spread(key.hashCode());
        int bucket = hash & (table.length - 1);
        for (Node<K, V> node = table[bucket]; node != null; node = node.next) {
            if (node.hash == hash && node.key.equals(key)) {
                V old = node.value;
                node.value = value;
                return old;
            }
        }
        table[bucket] = new Node<>(hash, key, value, table[bucket]);
        if (++size > threshold) resize();
        return null;
    }

    public V get(K key) {
        Objects.requireNonNull(key, "null keys are outside this learning implementation");
        int hash = spread(key.hashCode());
        for (Node<K, V> node = table[hash & (table.length - 1)]; node != null; node = node.next) {
            if (node.hash == hash && node.key.equals(key)) return node.value;
        }
        return null;
    }

    public int size() { return size; }
    int capacityForTest() { return table.length; }

    @SuppressWarnings("unchecked")
    private void resize() {
        Node<K, V>[] old = table;
        table = (Node<K, V>[]) new Node[old.length * 2];
        threshold = (int) (table.length * LOAD_FACTOR);
        for (Node<K, V> node : old) {
            while (node != null) {
                Node<K, V> next = node.next;
                int bucket = node.hash & (table.length - 1);
                node.next = table[bucket];
                table[bucket] = node;
                node = next;
            }
        }
    }

    private static int spread(int hash) { return hash ^ (hash >>> 16); }

    private static final class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;
        private Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash; this.key = key; this.value = value; this.next = next;
        }
    }
}
