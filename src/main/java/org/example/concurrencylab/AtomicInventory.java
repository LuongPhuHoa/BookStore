package org.example.concurrencylab;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * A small lock-free inventory counter. Each successful reservation is one
 * compare-and-set (CAS) transition from the current stock to stock - 1.
 */
public final class AtomicInventory {
    private final AtomicInteger stock;

    public AtomicInventory(int initialStock) {
        if (initialStock < 0) {
            throw new IllegalArgumentException("initialStock must not be negative");
        }
        this.stock = new AtomicInteger(initialStock);
    }

    public boolean tryReserveOne() {
        while (true) {
            int current = stock.get();
            if (current == 0) {
                return false;
            }
            if (stock.compareAndSet(current, current - 1)) {
                return true;
            }
            // Another thread changed stock after get(); read and retry.
        }
    }

    public int remaining() {
        return stock.get();
    }
}
