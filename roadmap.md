# Java Backend Roadmap (Junior → Mid)

> **Mục tiêu:** Hiểu bản chất Java Core, JVM và Spring thay vì chỉ biết sử dụng API.

---

# Giai đoạn 1 - Java Core

## Chương 1 - OOP

### Kiến thức

* SOLID Principles
* Encapsulation
* Inheritance
* Polymorphism
* Abstract Class vs Interface
* Composition over Inheritance
* Method Overloading & Overriding
* Static Binding vs Dynamic Binding
* Object Lifecycle
* `Object` class

    * `equals()`
    * `hashCode()`
    * `toString()`
    * `clone()`
* Immutable Object
* Record (Java 16+)
* Sealed Class (Java 17+)

### Mini Project

Library Management System

---

## Chương 2 - Collections Framework

### List

* ArrayList
* LinkedList
* Vector
* CopyOnWriteArrayList

### Set

* HashSet
* LinkedHashSet
* TreeSet

### Queue / Deque

* Queue
* PriorityQueue
* ArrayDeque

### Map

* HashMap
* LinkedHashMap
* TreeMap
* Hashtable
* ConcurrentHashMap

### Internals

* Hash Function
* Hash Collision
* Bucket
* Load Factor
* Capacity
* Resize
* Treeify
* Red-Black Tree
* Iterator
* Fail Fast
* ConcurrentModificationException

### Mini Project

Implement a simplified HashMap from scratch.

---

## Chương 3 - Generics

### Kiến thức

* Generic Class
* Generic Method
* Type Erasure
* Wildcards
* `? extends`
* `? super`
* PECS Principle
* Bridge Method
* Heap Pollution
* Raw Type
* Generic Array

### Mini Project

Build a Generic Repository.

---

## Chương 4 - Exception Handling

### Kiến thức

* Throwable Hierarchy
* Error vs Exception
* Checked Exception
* Unchecked Exception
* `throw`
* `throws`
* try-with-resources
* Suppressed Exception
* Multi Catch
* Custom Exception
* Exception Best Practices

### Mini Project

Bank Transfer Service

---

## Chương 5 - I/O & NIO

### Kiến thức

* File
* Path
* Files
* InputStream
* OutputStream
* Reader
* Writer
* Buffered Streams
* Serialization
* NIO
* ByteBuffer

### Mini Project

File Search Engine

---

# Giai đoạn 2 - Functional Programming

## Chương 6 - Lambda Expressions

### Kiến thức

* Functional Interface
* Lambda Expression
* Method Reference
* Default Methods
* Static Methods in Interface
* Closure
* Effectively Final

### Mini Project

Command Pattern using Lambda.

---

## Chương 7 - Stream API

### Kiến thức

* Stream Pipeline
* Intermediate Operations
* Terminal Operations
* map
* flatMap
* filter
* reduce
* collect
* Collector
* Parallel Stream
* Spliterator

### Mini Project

Sales Analytics Engine

---

## Chương 8 - Optional

### Kiến thức

* Optional Design
* map
* flatMap
* filter
* orElse
* orElseGet
* orElseThrow

### Mini Project

Null-safe Order Service

---

## Chương 9 - Date & Time API

### Kiến thức

* LocalDate
* LocalDateTime
* Instant
* ZonedDateTime
* Duration
* Period
* ZoneId
* DateTimeFormatter

### Mini Project

Booking Calendar

---

# Giai đoạn 3 - Concurrency

## Chương 10 - Thread Basics

* Thread Lifecycle
* Runnable
* Callable
* Future
* FutureTask

---

## Chương 11 - Synchronization

* synchronized
* volatile
* Atomic Classes
* Lock API
* ReentrantLock
* ReadWriteLock

---

## Chương 12 - Java Memory Model (JMM)

* Heap
* Stack
* Method Area
* Happens-Before
* Escape Analysis
* Cache Line
* False Sharing

---

## Chương 13 - Executor Framework

* Executor
* ExecutorService
* ThreadPoolExecutor
* ScheduledExecutorService
* ForkJoinPool
* CompletableFuture

---

## Chương 14 - Modern Concurrency (Java 21)

* Virtual Threads
* Project Loom
* Structured Concurrency (Preview)
* Scoped Values
* Pinning
* Carrier Threads

### Mini Project

Concurrent Web Crawler

---

# Giai đoạn 4 - JVM Internals

## Chương 15 - Class Loading

* Bootstrap ClassLoader
* Platform ClassLoader
* Application ClassLoader
* Delegation Model

---

## Chương 16 - JVM Memory

* Heap
* Stack
* Metaspace
* String Pool
* Direct Memory

---

## Chương 17 - Garbage Collection

* Serial GC
* Parallel GC
* CMS (Historical)
* G1 GC
* ZGC
* Shenandoah
* Young GC
* Full GC

---

## Chương 18 - JVM Diagnostics & Tuning

* Heap Dump
* Thread Dump
* jcmd
* jmap
* jstack
* VisualVM
* Java Flight Recorder (JFR)

### Mini Project

GC Benchmark

---

# Giai đoạn 5 - Reflection & Annotation

## Chương 19

### Kiến thức

* Reflection API
* Annotation
* Dynamic Proxy
* MethodHandle
* Bytecode Basics

### Mini Project

Mini Spring IoC Container

---

# Giai đoạn 6 - Spring Foundation

## Spring Core

* IoC Container
* Dependency Injection
* Bean Lifecycle
* Bean Scope

## Spring AOP

* Proxy
* Advice
* Pointcut

## Spring Transaction

* @Transactional
* Propagation
* Isolation
* Rollback Rules

## Spring MVC

* DispatcherServlet
* Controller
* Request Mapping
* Validation

## Spring Data JPA

* Entity Lifecycle
* Persistence Context
* Dirty Checking
* Lazy vs Eager Loading
* N+1 Problem

## Spring Security

* Authentication
* Authorization
* JWT
* OAuth2 Basics

### Mini Project

E-commerce Backend

---

# Giai đoạn 7 - Java 21+

### Kiến thức

* Record
* Sealed Classes
* Pattern Matching
* Pattern Matching for switch
* Virtual Threads
* Scoped Values
* Foreign Function & Memory API
* String Templates (nếu phiên bản hỗ trợ)

---

# Giai đoạn 8 - Design Patterns

Tập trung vào các pattern thường gặp trong Java Backend.

* Singleton
* Factory
* Builder
* Strategy
* Template Method
* Observer
* Decorator
* Adapter
* Proxy
* Chain of Responsibility

---

# Giai đoạn 9 - Source Code Reading

Đọc source code của JDK để hiểu cách các kỹ sư Java thiết kế API.

## Collections

* ArrayList
* HashMap
* ConcurrentHashMap
* LinkedHashMap

## Concurrency

* ThreadPoolExecutor
* CompletableFuture
* ReentrantLock

## Functional Programming

* Stream
* Collectors
* Optional

---

# Lộ trình học đề xuất

## Java Core

* [x] OOP
* [x] Collections
* [ ] Generics
* [ ] Exception Handling
* [ ] I/O & NIO

## Functional Programming

* [ ] Lambda
* [ ] Stream API
* [ ] Optional
* [ ] Date & Time API

## Concurrency

* [ ] Thread
* [ ] Synchronization
* [ ] Java Memory Model
* [ ] Executor Framework
* [ ] Virtual Threads

## JVM

* [ ] Class Loading
* [ ] Memory Management
* [ ] Garbage Collection
* [ ] JVM Tuning

## Advanced Java

* [ ] Reflection
* [ ] Design Patterns
* [ ] Source Code Reading

## Spring Ecosystem

* [ ] Spring Core
* [ ] Spring Boot
* [ ] Spring MVC
* [ ] Spring Data JPA
* [ ] Spring Security

## Backend Ecosystem

* [ ] Docker
* [ ] Kubernetes
* [ ] Kafka
* [ ] Redis
* [ ] Elasticsearch
* [ ] AWS Fundamentals
* [ ] System Design Basics
