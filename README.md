# Bookstore Backend

Production-style demo backend built with Java 21, Maven, Spring Boot 3.x, Spring Web, Spring Data JPA, H2, MapStruct, Lombok, Jakarta Validation, Java virtual threads, Java scoped values, and JUnit 5.

## Project Structure

```text
src/main/java/com/example/bookstore
├── controller   # REST endpoints and Java 21 demos
├── service      # Business logic and transaction boundaries
├── repository   # Spring Data JPA repositories
├── entity       # JPA entities
├── dto          # Request/response DTOs
├── mapper       # MapStruct mapper interfaces
├── config       # Sample data initialization
├── exception    # Custom exceptions and global API error handling
└── util         # RequestContext based on ScopedValue
```

Controllers never expose JPA entities directly. Requests and responses use DTOs, services own business flow, repositories own persistence, and MapStruct owns object mapping.

## MapStruct

MapStruct generates mapping implementations during Maven compilation. The mapper interfaces are:

- `AuthorMapper`
- `BookMapper`

The project demonstrates:

- `@Mapper(componentModel = "spring")` so generated mappers are Spring beans.
- `@Mapping` for single field mapping and ignored fields.
- `@Mappings` for grouped mappings in `BookMapper`.
- `@BeanMapping` with `NullValuePropertyMappingStrategy.IGNORE`.
- `@MappingTarget` for updating an existing entity from a request DTO.

Generated classes are written under `target/generated-sources/annotations`, usually as `AuthorMapperImpl` and `BookMapperImpl`. You do not edit those files; MapStruct recreates them whenever the project compiles.

## Virtual Threads

Endpoint:

```text
GET /api/demo/virtual-thread
```

The endpoint uses `Executors.newVirtualThreadPerTaskExecutor()` and launches 5 concurrent simulated external calls.

Virtual threads are lightweight Java threads managed by the JVM. They are much cheaper than platform threads, so blocking I/O workloads can run with high concurrency without maintaining a large operating-system thread pool.

Use virtual threads for I/O-bound work such as HTTP calls, database calls, file operations, and message processing. They are not a replacement for CPU-bound parallelism, where a bounded executor sized around CPU cores is still usually better.

## Scoped Values

Endpoint:

```text
GET /api/demo/scoped-value
```

The endpoint creates a `RequestContext` containing:

- `requestId`
- `currentUser`

It binds that context with:

```java
ScopedValue.where(RequestContext.CURRENT, context).call(this::serviceLayerMethod);
```

Nested methods can then call `RequestContext.current()` without passing context parameters through every method.

`ThreadLocal` stores mutable thread-associated state and can leak if it is not cleared correctly. `ScopedValue` is immutable, lexically scoped, and automatically unavailable after the bound operation finishes, which is a better fit for modern Java, especially when code uses virtual threads.

In Java 21, `ScopedValue` is a preview API. The Maven build enables preview features for compilation, tests, and Spring Boot runs.

## API Endpoints

| Method | Endpoint | Description |
| --- | --- | --- |
| `GET` | `/api/authors` | List authors |
| `GET` | `/api/authors/{id}` | Get author by id |
| `POST` | `/api/authors` | Create author |
| `PUT` | `/api/authors/{id}` | Update author |
| `DELETE` | `/api/authors/{id}` | Delete author |
| `GET` | `/api/books` | List books |
| `GET` | `/api/books/{id}` | Get book by id |
| `POST` | `/api/books` | Create book |
| `PUT` | `/api/books/{id}` | Update book |
| `DELETE` | `/api/books/{id}` | Delete book |
| `GET` | `/api/demo/virtual-thread` | Run virtual-thread demo |
| `GET` | `/api/demo/scoped-value` | Run scoped-value demo |

## Sample Data

The application initializes two authors and two books:

- Robert Martin, `Clean Code`
- Martin Fowler, `Refactoring`

The H2 console is available at:

```text
http://localhost:8080/h2-console
```

JDBC URL:

```text
jdbc:h2:mem:bookstoredb
```

## Run

Requires Java 21.

```bash
./mvnw spring-boot:run
```

On Windows PowerShell:

```powershell
.\mvnw.cmd spring-boot:run
```

Run tests:

```bash
./mvnw test
```

## Example Curl Commands

Create an author:

```bash
curl -X POST http://localhost:8080/api/authors \
  -H "Content-Type: application/json" \
  -d '{"name":"Joshua Bloch","nationality":"American"}'
```

List authors:

```bash
curl http://localhost:8080/api/authors
```

Create a book:

```bash
curl -X POST http://localhost:8080/api/books \
  -H "Content-Type: application/json" \
  -d '{"title":"Effective Java","isbn":"9780134685991","price":49.99,"publishedYear":2018,"authorId":1}'
```

Update a book:

```bash
curl -X PUT http://localhost:8080/api/books/1 \
  -H "Content-Type: application/json" \
  -d '{"title":"Clean Code","isbn":"9780132350884","price":44.99,"publishedYear":2008,"authorId":1}'
```

Delete a book:

```bash
curl -X DELETE http://localhost:8080/api/books/1
```

Run Java 21 demos:

```bash
curl http://localhost:8080/api/demo/virtual-thread
curl http://localhost:8080/api/demo/scoped-value
```
