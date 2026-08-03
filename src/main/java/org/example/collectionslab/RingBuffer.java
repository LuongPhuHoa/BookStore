package org.example.collectionslab;

import java.util.Iterator;
import java.util.NoSuchElementException;

/** Bounded FIFO collection backed by a circular array. */
public final class RingBuffer<E> implements Iterable<E> {
    private final Object[] elements;
    private int head;
    private int size;

    public RingBuffer(int capacity) {
        if (capacity < 1) throw new IllegalArgumentException("capacity must be positive");
        elements = new Object[capacity];
    }
    public boolean offer(E element) {
        if (size == elements.length) return false;
        elements[(head + size) % elements.length] = element;
        size++;
        return true;
    }
    @SuppressWarnings("unchecked")
    public E poll() {
        if (size == 0) return null;
        E value = (E) elements[head];
        elements[head] = null;
        head = (head + 1) % elements.length;
        size--;
        return value;
    }
    public int size() { return size; }
    @Override public Iterator<E> iterator() {
        return new Iterator<>() {
            private int offset;
            public boolean hasNext() { return offset < size; }
            @SuppressWarnings("unchecked") public E next() {
                if (!hasNext()) throw new NoSuchElementException();
                return (E) elements[(head + offset++) % elements.length];
            }
        };
    }
}
