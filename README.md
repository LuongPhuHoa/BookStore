# Hibernate Laboratory

A Spring Boot 3.5.x project with Java 21 designed as an educational tool for learning Hibernate and Spring Data JPA internals. This is NOT a production CRUD application—it's a hands-on laboratory for understanding how Hibernate persists objects, manages transactions, and generates SQL.

## Project Overview

The Hibernate Laboratory progressively builds a bookstore backend system through 21 commits, each focusing on specific Hibernate and Spring Data JPA concepts. You'll learn by observing generated SQL, understanding transaction behavior, and implementing various persistence patterns.

## Prerequisites

- **Java 21** (with preview features enabled)
- **PostgreSQL 12+** (local or Docker)
- **Maven 3.8+**
- **IDE**: IntelliJ IDEA or VS Code with Spring Boot extensions (recommended)

## Database Setup

### Option 1: Local PostgreSQL Installation

```bash
# On macOS (Homebrew)
brew install postgresql
brew services start postgresql

# On Ubuntu/Debian
sudo apt-get install postgresql postgresql-contrib
sudo systemctl start postgresql

# On Windows
# Download and install from https://www.postgresql.org/download/windows/
# During installation, set password for 'postgres' user
```

### Option 2: Docker

```bash
# Create PostgreSQL container
docker run --name bookstore-db \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=postgres \
  -e POSTGRES_DB=bookstore \
  -p 5432:5432 \
  -d postgres:15

# Verify connection
docker exec -it bookstore-db psql -U postgres -d bookstore -c "SELECT 1;"
```

### Create Bookstore Database

```bash
# Connect to PostgreSQL
psql -U postgres

# Create database
CREATE DATABASE bookstore;

# Verify
\l

# Exit
\q
```

## Application Startup

```bash
# Build the project
mvn clean compile

# Run the application
mvn spring-boot:run

# Application starts on http://localhost:8080
```

## Package Structure

```
src/main/java/org/example/bookstore/
├── controller/          # REST controllers (HTTP handling only)
├── service/             # Business logic & Hibernate operations
├── repository/          # Spring Data JPA repositories
├── entity/              # JPA entities (database models)
├── dto/                 # Request/Response Data Transfer Objects
├── mapper/              # MapStruct mappers (entity ↔ DTO)
├── config/              # Spring configuration classes
├── lab/                 # Experimental endpoints for learning
└── exception/           # Exception handling & error responses
```

## Key Configuration

### application.properties

Critical settings for observing Hibernate behavior:

```properties
# Database
spring.datasource.url=jdbc:postgresql://localhost:5432/bookstore
spring.datasource.username=postgres
spring.datasource.password=postgres

# Hibernate Logging
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.use_sql_comments=true

# SQL Logging
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.orm.jdbc.bind=TRACE

# DDL Management
spring.jpa.hibernate.ddl-auto=create  # Creates schema on startup
```

## Learning Roadmap: 21 Commits

### Phase 1: Foundation (Commits 01-03)
- **Commit 01**: Simple Book entity, minimal CRUD, observe basic SQL
- **Commit 02**: Add transaction annotations, study transaction behavior
- **Commit 03**: Implement DTO mapping, understand object conversion

### Phase 2: Relationships (Commits 04-08)
- **Commit 04**: One-to-Many relationship (Author ↔ Books)
- **Commit 05**: Many-to-One lazy loading patterns
- **Commit 06**: Eager loading and N+1 query problems
- **Commit 07**: Join fetch queries for optimization
- **Commit 08**: Cascade operations and orphan handling

### Phase 3: Advanced Persistence (Commits 09-13)
- **Commit 09**: Entity lifecycle callbacks (@PrePersist, @PostLoad)
- **Commit 10**: Custom JPQL queries with @Query
- **Commit 11**: Native SQL queries and result mapping
- **Commit 12**: Pagination and sorting
- **Commit 13**: Batch operations and bulk updates

### Phase 4: Performance & Caching (Commits 14-17)
- **Commit 14**: First-level cache (Persistence Context)
- **Commit 15**: Query caching
- **Commit 16**: Understanding Session.flush() vs commit
- **Commit 17**: Dirty checking and update tracking

### Phase 5: Advanced Scenarios (Commits 18-21)
- **Commit 18**: Inheritance strategies (SINGLE_TABLE, JOINED, TABLE_PER_CLASS)
- **Commit 19**: Composite keys (@EmbeddedId)
- **Commit 20**: Event listeners and Hibernate interceptors
- **Commit 21**: Testing persistence layer with @DataJpaTest

## How to Observe Generated SQL

### 1. Console Output
When running the application, check the console for formatted SQL:

```
Hibernate: select b.id, b.isbn, b.price, b.published_year, b.title 
from books b where b.id = ?
[Long: 1]
```

### 2. Debug Mode
Add breakpoints and use IDE debugger to step through Hibernate operations:
- Breakpoints in `BookService.createBook()` to see transaction lifecycle
- Breakpoints in `BookRepository.save()` to observe flush behavior

### 3. Database Logs
Connect to PostgreSQL directly to view executed queries:

```bash
# Terminal 1: Watch PostgreSQL logs
psql -U postgres -d bookstore -c "SELECT pg_current_xact_id();"

# Terminal 2: Run your queries through HTTP
curl -X POST http://localhost:8080/api/books \
  -H "Content-Type: application/json" \
  -d '{"title":"Effective Java","isbn":"978-0134685991","price":45.99,"publishedYear":2018}'
```

## Recommended Debugging Breakpoints

### For Commit 01
1. **BookController.createBook()** - Line 1
   - Observe HTTP request reception
   - Add watch expression: `requestDto`

2. **BookService.createBook()** - Line 1
   - Observe DTO-to-Entity mapping
   - Add watch expression: `book`

3. **BookRepository.save()** - After mapping
   - See Hibernate persistence context interaction
   - Watch for SQL generation

4. **EntityManager.flush()** - At transaction commit
   - Understand when SQL is actually sent to database
   - See dirty checking in action

### Debugging Tips
- Use "Step Into" (F7) to enter Lombok-generated getters/setters
- Use "Step Over" (F8) to skip generated code
- Add conditional breakpoints: `id == null` to break only on inserts
- Watch the "Variables" panel to see entity state changes

## HTTP Endpoints for Testing

### Standard API
```bash
# Create a book
POST /api/books
Content-Type: application/json

{
  "title": "Clean Code",
  "isbn": "978-0132350884",
  "price": 50.99,
  "publishedYear": 2008
}

# Get a book
GET /api/books/1
```

### Laboratory Endpoints (Commit 01)
```bash
# Create book via lab endpoint (identical to /api/books)
POST /lab/create-book
Content-Type: application/json

{
  "title": "The Pragmatic Programmer",
  "isbn": "978-0201616224",
  "price": 45.99,
  "publishedYear": 2019
}

# Get book via lab endpoint
GET /lab/get-book/1
```

Use Postman, Thunder Client, or curl to test these endpoints.

## Commit 01 Experiment

After starting the application, run these experiments to understand basic persistence:

### Experiment 1.1: Basic Create and Read
```bash
# Create a book
curl -X POST http://localhost:8080/api/books \
  -H "Content-Type: application/json" \
  -d '{
    "title":"Java Concurrency in Practice",
    "isbn":"978-0321349606",
    "price":59.99,
    "publishedYear":2006
  }'

# Check console for generated SQL (INSERT statement)

# Read the book
curl http://localhost:8080/api/books/1

# Check console for generated SQL (SELECT statement)
```

### Experiment 1.2: Observe Transaction Behavior
1. Add a breakpoint in `BookService.createBook()` at the `@Transactional` method entry
2. Step through the method
3. Notice when SQL is logged (at the end of the method, not in the middle)
4. This is the transaction commit point

### Experiment 1.3: Check Database State
```bash
# Connect to database
psql -U postgres -d bookstore

# Query books table
SELECT * FROM books;

# Exit
\q
```

Expected output:
```
 id |               title               |        isbn        | price | published_year
----+-----------------------------------+--------------------+-------+----------------
  1 | Java Concurrency in Practice      | 978-0321349606     | 59.99 |           2006
```

## Virtual Threads (Java 21 Feature)

This project enables virtual threads via `spring.threads.virtual.enabled=true`. Benefits:
- Improved throughput for I/O-bound operations
- Simplified concurrency model
- Better CPU utilization

Observe virtual thread usage in logs:
```
thread=VirtualThread[#123]/runnable@ForkJoinPool-1-worker-1
```

## Common Pitfalls to Avoid

1. **LazyInitializationException**: Don't access lazy-loaded relationships outside a transaction
2. **N+1 Query Problem**: Don't loop through results and fetch related entities individually
3. **Detached Entity Updates**: Re-attach entities to session before updating
4. **Open Session in View Anti-pattern**: `spring.jpa.open-in-view=false` (already set)
5. **Modifying Entity in Service**: Changes are auto-flushed at transaction end

## Tools & Resources

### IDE Integration
- **IntelliJ**: Run → Run with Debugger (Shift+F9)
- **VS Code**: Spring Boot Dashboard extension for easy debugging

### Database GUI
```bash
# Option 1: pgAdmin (Docker)
docker run -e PGADMIN_DEFAULT_EMAIL=admin@example.com \
  -e PGADMIN_DEFAULT_PASSWORD=admin \
  -p 5050:80 dpage/pgadmin4

# Then visit http://localhost:5050

# Option 2: DBeaver (Desktop app)
# Download from https://dbeaver.io/
```

### SQL Analysis
- Enable `spring.jpa.properties.hibernate.generate_statistics=true` for detailed stats
- Use Hibernate profiler plugins to visualize query execution

## Next Steps After Commit 01

1. Review generated SQL in console output
2. Run breakpoint experiments to understand transaction lifecycle
3. Examine database using `SELECT * FROM books;`
4. Prepare for Commit 02: Add transaction method variations
5. Plan for Commit 03: Implement find-all and caching

## Troubleshooting

### "FATAL: role 'postgres' does not exist"
```bash
# Reset PostgreSQL on macOS
rm -rf /usr/local/var/postgres
brew reinstall postgresql
```

### "Connection refused"
- Verify PostgreSQL is running: `pg_isready -h localhost`
- Check port 5432 is not blocked

### "Database 'bookstore' does not exist"
```bash
psql -U postgres -c "CREATE DATABASE bookstore;"
```

### "ddl-auto=create keeps dropping my data"
- This is intentional for learning
- Change to `ddl-auto=update` if you want to preserve data between runs

---

**Author**: Hibernate Laboratory  
**Purpose**: Educational tool for learning Hibernate and Spring Data JPA  
**Version**: 1.0.0 (Commit 01)
