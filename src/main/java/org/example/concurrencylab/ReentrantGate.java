package org.example.concurrencylab;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/** Shows both reentrancy and a Condition associated with the same lock. */
public final class ReentrantGate {
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition opened = lock.newCondition();
    private boolean open;

    public void open() {
        lock.lock();
        try {
            open = true;
            opened.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public void awaitOpen() throws InterruptedException {
        lock.lockInterruptibly();
        try {
            while (!open) { // await can wake spuriously; always re-check the condition.
                opened.await();
            }
        } finally {
            lock.unlock();
        }
    }

    public int reentrantHoldCount() {
        lock.lock();
        try {
            return nestedHoldCount();
        } finally {
            lock.unlock();
        }
    }

    private int nestedHoldCount() {
        lock.lock();
        try {
            return lock.getHoldCount();
        } finally {
            lock.unlock();
        }
    }
}
