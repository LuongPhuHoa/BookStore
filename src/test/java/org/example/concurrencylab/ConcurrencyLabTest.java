package org.example.concurrencylab;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.*;

class ConcurrencyLabTest {

    @Test
    void atomicCompareAndSetNeverReservesMoreThanAvailableStock() throws Exception {
        AtomicInventory inventory = new AtomicInventory(100);
        try (ExecutorService pool = Executors.newFixedThreadPool(8)) {
            List<Callable<Boolean>> attempts = new ArrayList<>();
            for (int i = 0; i < 200; i++) {
                attempts.add(inventory::tryReserveOne);
            }
            List<Future<Boolean>> results = pool.invokeAll(attempts);
            long successfulReservations = results.stream().filter(this::completedTrue).count();

            assertEquals(100, successfulReservations);
            assertEquals(0, inventory.remaining());
        }
    }

    @Test
    void tryLockReturnsImmediatelyWhenTheLockIsHeldByAnotherThread() throws Exception {
        LockingLedger ledger = new LockingLedger(0);
        ledger.lockForTest();
        try (ExecutorService pool = Executors.newSingleThreadExecutor()) {
            Future<Boolean> result = pool.submit(() -> ledger.tryDeposit(10));
            assertFalse(result.get());
            assertEquals(0, ledger.balance());
        } finally {
            ledger.unlockForTest();
        }
    }

    @Test
    void timedTryLockTimesOutInsteadOfWaitingForever() throws Exception {
        LockingLedger ledger = new LockingLedger(0);
        ledger.lockForTest();
        try (ExecutorService pool = Executors.newSingleThreadExecutor()) {
            Future<Boolean> result = pool.submit(() -> ledger.depositInterruptibly(10, Duration.ofMillis(50)));
            assertFalse(result.get());
        } finally {
            ledger.unlockForTest();
        }
    }

    @Test
    void sameThreadMayAcquireReentrantLockAgain() {
        assertEquals(2, new ReentrantGate().reentrantHoldCount());
    }

    @Test
    void conditionWaiterProceedsAfterTheGateIsOpened() throws Exception {
        ReentrantGate gate = new ReentrantGate();
        try (ExecutorService pool = Executors.newSingleThreadExecutor()) {
            Future<String> waiter = pool.submit(() -> {
                gate.awaitOpen();
                return "passed";
            });
            gate.open();
            assertEquals("passed", waiter.get());
        }
    }

    private boolean completedTrue(Future<Boolean> result) {
        try {
            return result.get();
        } catch (Exception exception) {
            throw new AssertionError(exception);
        }
    }
}
