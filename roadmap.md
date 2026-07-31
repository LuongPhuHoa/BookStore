# Java Backend Mastery Roadmap (Implementation First)

> **Triết lý học:** Không học API trước, học implementation trước.
>
> Chu trình học:
>
> **Question → Prediction → Code → JDK Source → Implementation → Interview → Best Practice**

---

# Phase 1 - Java Language

## Module 1 - OOP

### 1. Fundamentals

* [x] Encapsulation
* [x] Inheritance
* [x] Polymorphism
* [x] Abstract Class vs Interface
* [x] Method Overloading
* [x] Method Overriding
* [x] Static Binding
* [x] Dynamic Binding

### 2. Object Class

* [x] equals()
* [x] hashCode()
* [x] toString() ⭐⭐⭐⭐
* [x] clone() ⭐⭐

### 3. Design Principles

* [x] Composition over Inheritance ⭐⭐⭐⭐⭐
* [x] SOLID Principles ⭐⭐⭐⭐⭐

### 4. Modern Java

* [x] Record ⭐⭐⭐⭐
* [x] Sealed Class ⭐⭐⭐⭐

---

# Module 2 - Collections Framework

## List

* [x] ArrayList ⭐⭐⭐⭐⭐
* [x] LinkedList ⭐⭐⭐⭐
* [ ] Vector ⭐⭐
* [ ] CopyOnWriteArrayList ⭐⭐⭐⭐

## Set

* [x] HashSet ⭐⭐⭐⭐⭐
* [ ] LinkedHashSet ⭐⭐⭐
* [ ] TreeSet ⭐⭐⭐⭐

## Queue / Deque

* [ ] Queue Interface ⭐⭐⭐
* [ ] PriorityQueue ⭐⭐⭐⭐
* [ ] ArrayDeque ⭐⭐⭐⭐⭐

## Map

* [x] HashMap ⭐⭐⭐⭐⭐
* [ ] LinkedHashMap ⭐⭐⭐⭐
* [ ] TreeMap ⭐⭐⭐⭐
* [ ] Hashtable ⭐⭐
* [ ] ConcurrentHashMap ⭐⭐⭐⭐⭐

## Collection Internals

* [x] Hash Function
* [x] Bucket
* [x] Collision
* [x] Resize
* [x] Capacity
* [x] Load Factor
* [x] Treeify
* [x] Iterator
* [x] ConcurrentModificationException
* [ ] Fail-Fast Mechanism ⭐⭐⭐⭐

### Mini Projects

* [ ] Implement HashMap
* [ ] LRU Cache
* [ ] Custom Collection

---

# Module 3 - Generics

* [x] Generic Class
* [x] Generic Method
* [x] Type Erasure
* [x] Wildcards
* [x] `? extends`
* [x] `? super`
* [x] PECS

## Advanced Generics

* [ ] Bridge Method ⭐⭐⭐⭐
* [ ] Heap Pollution ⭐⭐⭐⭐
* [ ] Generic Array ⭐⭐⭐
* [ ] Raw Type ⭐⭐⭐

### Mini Projects

* [ ] Generic Repository
* [ ] Generic Cache

---

# Module 4 - Exception Handling

* [ ] Throwable Hierarchy
* [ ] Error vs Exception
* [ ] Checked Exception
* [ ] Unchecked Exception
* [ ] throw / throws
* [ ] try-with-resources
* [ ] Suppressed Exception
* [ ] Multi Catch
* [ ] Custom Exception
* [ ] Exception Best Practices

---

# Module 5 - IO & NIO

* [ ] File
* [ ] Path
* [ ] Files
* [ ] InputStream
* [ ] OutputStream
* [ ] Reader
* [ ] Writer
* [ ] Buffered Streams
* [ ] Serialization
* [ ] NIO
* [ ] ByteBuffer

---

# Phase 2 - Functional Programming

## Lambda

* [ ] Functional Interface
* [ ] Lambda Expression
* [ ] Method Reference
* [ ] Closure
* [ ] Effectively Final

## Stream API

* [ ] Stream Pipeline
* [ ] Intermediate Operations
* [ ] Terminal Operations
* [ ] Collector
* [ ] Reduce
* [ ] flatMap
* [ ] Parallel Stream
* [ ] Spliterator

## Optional

* [ ] map
* [ ] flatMap
* [ ] filter
* [ ] orElse
* [ ] orElseGet
* [ ] orElseThrow

## Date & Time API

* [ ] LocalDate
* [ ] LocalDateTime
* [ ] Instant
* [ ] Duration
* [ ] Period
* [ ] ZoneId
* [ ] Formatter

---

# Phase 3 - Concurrency

## Thread Basics

* [ ] Thread
* [ ] Runnable
* [ ] Callable
* [ ] Future

## Synchronization

* [ ] synchronized
* [ ] volatile
* [ ] Atomic Classes
* [ ] Lock API
* [ ] ReentrantLock

## Executor Framework

* [ ] ExecutorService
* [ ] ThreadPoolExecutor
* [ ] ForkJoinPool
* [ ] CompletableFuture

## Java Memory Model

* [ ] Happens-Before
* [ ] Visibility
* [ ] Cache Line
* [ ] False Sharing

## Java 21

* [x] Virtual Threads
* [x] Scoped Values
* [x] Structured Concurrency

---

# Phase 4 - JVM

* [ ] Class Loading
* [ ] Bytecode
* [ ] Heap
* [ ] Stack
* [ ] Metaspace
* [ ] String Pool
* [ ] Garbage Collection
* [ ] JIT Compiler
* [ ] Escape Analysis
* [ ] JVM Tuning

---

# Phase 5 - Reflection & Annotation

* [ ] Reflection
* [ ] Annotation
* [ ] Dynamic Proxy
* [ ] MethodHandle
* [ ] Bytecode Basics

---

# Phase 6 - Spring Ecosystem

## Spring Core

* [x] IoC
* [x] Dependency Injection
* [x] Bean Lifecycle
* [x] Bean Scope

## Spring AOP

* [ ] Proxy
* [ ] Advice
* [ ] Pointcut

## Spring Transaction

* [ ] Propagation
* [ ] Isolation
* [ ] Rollback Rules

## Spring MVC

* [ ] DispatcherServlet
* [ ] Controller
* [ ] Validation
* [ ] Exception Handling

## Spring Data JPA

* [ ] Entity Lifecycle
* [ ] Persistence Context
* [ ] Dirty Checking
* [ ] Fetch Strategies
* [ ] N+1 Problem

## Spring Security

* [x] Authentication
* [x] Authorization
* [x] JWT
* [ ] OAuth2

---

# Phase 7 - Design Patterns

* [x] Singleton
* [x] Factory
* [x] Builder
* [x] Strategy
* [ ] Template Method
* [ ] Observer
* [ ] Decorator
* [ ] Adapter
* [ ] Proxy
* [ ] Chain of Responsibility

---

# Phase 8 - Source Code Reading

## JDK Collections

* [ ] ArrayList
* [ ] HashMap
* [ ] LinkedHashMap
* [ ] ConcurrentHashMap

## JDK Concurrency

* [ ] ThreadPoolExecutor
* [ ] CompletableFuture
* [ ] ReentrantLock

## Functional Programming

* [x] Stream
* [ ] Collectors
* [ ] Optional

---

# Learning Strategy

Đối với mỗi chủ đề:

* [ ] Dự đoán kết quả (Prediction)
* [ ] Viết code kiểm chứng
* [ ] Đọc source JDK
* [ ] Hiểu implementation
* [ ] Trả lời câu hỏi phỏng vấn
* [ ] Áp dụng vào mini project
* [ ] Tổng kết best practices
