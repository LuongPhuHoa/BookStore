# Lab Phase 3: Atomic Classes, Lock API, and ReentrantLock

This lab follows the roadmap loop: **Question → Prediction → Code → Implementation → Interview → Best Practice**.

Run it with Java 21:

```powershell
.\mvnw.cmd -Dtest=ConcurrencyLabTest test
```

Code is in `org.example.concurrencylab`; automated experiments are in `ConcurrencyLabTest`.

## 1. Atomic classes: CAS without a lock

### Question

Can 200 threads reserve from a stock of 100 without overselling, while avoiding a mutual-exclusion lock?

### Prediction

Exactly 100 calls to `AtomicInventory.tryReserveOne()` succeed and the final stock is zero.

### Implementation

`AtomicInventory` uses `AtomicInteger.compareAndSet(expected, updated)` in a retry loop:

```text
read current → if zero, fail → CAS(current, current - 1)
                              ↳ failed because another thread won: retry
```

The JDK implements atomic primitives with platform CAS instructions when available. A CAS is atomic: it writes only if the value is still the expected value. Atomic classes also provide volatile-like visibility for their reads/writes.

Use `incrementAndGet`, `updateAndGet`, or `accumulateAndGet` for simple transforms. Use a hand-written CAS loop only when success depends on the old value, as it does for "reserve only if stock remains".

Do not place slow work, I/O, or non-idempotent side effects inside an atomic update function: contention may cause that function to run more than once.

## 2. Lock API: explicit acquisition policy

### Question

What can `Lock` express that `synchronized` cannot express directly?

### Prediction

`tryLock()` can decline immediately, and timed `tryLock(timeout, unit)` can stop waiting after a bounded period.

### Code

`LockingLedger` demonstrates three policies:

| API | Meaning |
|---|---|
| `lock()` | wait until acquired; not interruptible while waiting |
| `tryLock()` | return `false` immediately if unavailable |
| `tryLock(timeout, unit)` | wait only up to the deadline; interruptible |
| `lockInterruptibly()` | wait until acquired, but respond to interruption |

Every successful acquisition must be matched with `unlock()` in `finally`:

```java
lock.lock();
try {
    // protected state
} finally {
    lock.unlock();
}
```

Calling `unlock()` from a thread that does not own a `ReentrantLock` throws `IllegalMonitorStateException`.

## 3. ReentrantLock and Condition

### Question

Can a method that owns a lock safely call another method that locks the same lock? How do threads wait for a state change?

### Prediction

The owning thread can lock a `ReentrantLock` twice; its hold count becomes 2 and it must unlock twice. A waiting gate thread passes only after `open()` changes state and signals the condition.

`ReentrantGate.reentrantHoldCount()` proves reentrancy. `awaitOpen()` uses a `Condition` created by the same lock:

```java
while (!open) {
    opened.await();
}
```

`await()` releases the lock while waiting and reacquires it before returning. The `while`, rather than `if`, is required because a thread can wake spuriously or find that another thread has changed the state first.

`Condition` is the `Lock` equivalent of an object's `wait`/`notifyAll`. Keep both the guarded state and its condition under the same lock.

## 4. Interview checks

1. **Does `volatile int count; count++` make a counter safe?** No. Visibility is guaranteed, but read-modify-write is not atomic. Use `AtomicInteger` or a lock.
2. **Does atomic mean every multi-field invariant is safe?** No. Atomic classes protect one atomic variable/operation. Use a lock or redesign when several fields must change together.
3. **Why does `ReentrantLock` need `finally`?** An exception otherwise leaks the lock and can block other threads indefinitely.
4. **Does reentrancy eliminate deadlocks?** No. It only lets the owner reacquire its own lock. Multiple locks can still form a cycle.
5. **Is `ReentrantLock` always better than `synchronized`?** No. Prefer `synchronized` for simple, lexical mutual exclusion. Choose `Lock` for timed/interruptible acquisition, multiple conditions, fairness, or lock introspection.

## 5. Best practices

- Make the protected state and its locking policy obvious; do not expose mutable state without the same protection.
- Keep critical sections small and avoid network/database calls while holding a lock.
- Establish a global lock order when acquiring multiple locks.
- Prefer `signal()` when one waiter is enough; use `signalAll()` when different predicates share a condition or correctness requires all waiters to re-check.
- A fair `new ReentrantLock(true)` reduces barging but typically lowers throughput; use it only for a real fairness requirement.
