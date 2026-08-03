package org.example.concurrencylab;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/** Demonstrates Lock API acquisition styles and the mandatory finally unlock. */
public final class LockingLedger {
    private final ReentrantLock lock = new ReentrantLock();
    private int balance;

    public LockingLedger(int openingBalance) {
        this.balance = openingBalance;
    }

    public void deposit(int amount) {
        validatePositive(amount);
        lock.lock();
        try {
            balance += amount;
        } finally {
            lock.unlock();
        }
    }

    /** Returns false instead of waiting when another thread owns the lock. */
    public boolean tryDeposit(int amount) {
        validatePositive(amount);
        if (!lock.tryLock()) {
            return false;
        }
        try {
            balance += amount;
            return true;
        } finally {
            lock.unlock();
        }
    }

    /** A waiting caller can cancel by interrupting its thread. */
    public boolean depositInterruptibly(int amount, Duration timeout) throws InterruptedException {
        validatePositive(amount);
        if (!lock.tryLock(timeout.toNanos(), TimeUnit.NANOSECONDS)) {
            return false;
        }
        try {
            balance += amount;
            return true;
        } finally {
            lock.unlock();
        }
    }

    public int balance() {
        lock.lock();
        try {
            return balance;
        } finally {
            lock.unlock();
        }
    }

    void lockForTest() {
        lock.lock();
    }

    void unlockForTest() {
        lock.unlock();
    }

    private static void validatePositive(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("amount must be positive");
        }
    }
}
