package org.example.bookstore.controller;

import org.example.bookstore.dto.BookRequestDto;
import org.example.bookstore.dto.BookResponseDto;
import org.example.bookstore.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * BookController - HTTP REST endpoints for book operations.
 *
 * ═══════════════════════════════════════════════════════════════════════════════
 * RESPONSIBILITY:
 * ═══════════════════════════════════════════════════════════════════════════════
 * Controllers are responsible for HTTP handling ONLY:
 * - Accept HTTP requests
 * - Parse URL paths, query params, request body
 * - Validate input using @RequestBody validation
 * - Delegate business logic to SERVICE layer
 * - Transform response to JSON
 * - Return HTTP status codes
 *
 * WHAT CONTROLLERS SHOULD NOT DO:
 * ❌ Access database directly (use service/repository)
 * ❌ Implement business logic (use service)
 * ❌ Handle persistence context (use service)
 * ❌ Catch database exceptions (let service handle)
 *
 * ═══════════════════════════════════════════════════════════════════════════════
 * SEPARATION OF CONCERNS:
 * ═══════════════════════════════════════════════════════════════════════════════
 *
 * REST REQUEST
 * │
 * CONTROLLER (HTTP handling)
 * │ ├─ Parse request
 * │ ├─ Validate input
 * │ └─ Delegate to SERVICE
 * │
 * SERVICE (Business Logic)
 * │ ├─ Execute business rules
 * │ ├─ Manage transactions
 * │ └─ Delegate to REPOSITORY
 * │
 * REPOSITORY (Database Access)
 * │ └─ Query/persist data
 * │
 * DATABASE (PostgreSQL)
 *
 * ═══════════════════════════════════════════════════════════════════════════════
 * @RestController vs @Controller:
 * ═══════════════════════════════════════════════════════════════════════════════
 * @RestController = @Controller + @ResponseBody
 *
 * @RestController:
 * - All methods return serialized objects (JSON)
 * - No view resolution (no JSP, no HTML template)
 * - Perfect for REST APIs
 *
 * @Controller:
 * - Methods can return view names (strings that map to JSP)
 * - Use for server-side rendering (HTML)
 *
 * ═══════════════════════════════════════════════════════════════════════════════
 * @RequestMapping & @PostMapping, @GetMapping:
 * ═══════════════════════════════════════════════════════════════════════════════
 * @RequestMapping("/api/books")
 * - Base path for all endpoints in this controller
 * - All URLs start with /api/books
 *
 * @PostMapping:
 * - HTTP POST method
 * - Used for creating resources
 * - URL path (if omitted): inherits from class-level @RequestMapping
 *
 * @GetMapping("/{id}"):
 * - HTTP GET method
 * - Used for retrieving resources
 * - Full URL: /api/books/{id}
 * - {id} is a path variable (extracted from URL)
 *
 * ═══════════════════════════════════════════════════════════════════════════════
 * REST ENDPOINTS DEFINED HERE:
 * ═══════════════════════════════════════════════════════════════════════════════
 * Method | Endpoint            | Purpose          | Commit
 * -------|---------------------|------------------|--------
 * POST   | /api/books          | Create book      | 01
 * GET    | /api/books/{id}     | Get book by ID   | 01
 *
 * FUTURE COMMITS may add:
 * GET    | /api/books          | Get all books    | Commit 09
 * PUT    | /api/books/{id}     | Update book      | Commit 14
 * DELETE | /api/books/{id}     | Delete book      | Commit 15
 * GET    | /api/books?price>50 | Filter by price  | Commit 17
 *
 * @see org.springframework.web.bind.annotation.RestController
 * @see org.springframework.web.bind.annotation.RequestMapping
 * @see org.springframework.web.bind.annotation.PostMapping
 * @see org.springframework.web.bind.annotation.GetMapping
 */
@RestController
@RequestMapping("/api/books")  // Base path for all endpoints
@RequiredArgsConstructor        // Generates constructor for BookService
public class BookController {
    private final BookService bookService;  // Injected by Spring

    /**
     * Create a new book via HTTP POST.
     *
     * ═══════════════════════════════════════════════════════════════════════════════
     * HTTP REQUEST:
     * ═══════════════════════════════════════════════════════════════════════════════
     * POST /api/books
     * Content-Type: application/json
     *
     * REQUEST BODY:
     * {
     *   "title": "Effective Java",
     *   "isbn": "978-0134685991",
     *   "price": 45.99,
     *   "publishedYear": 2018
     * }
     *
     * ═══════════════════════════════════════════════════════════════════════════════
     * HTTP RESPONSE (Success):
     * ═══════════════════════════════════════════════════════════════════════════════
     * HTTP 201 Created
     * Content-Type: application/json
     * Location: /api/books/1
     *
     * RESPONSE BODY:
     * {
     *   "id": 1,
     *   "title": "Effective Java",
     *   "isbn": "978-0134685991",
     *   "price": 45.99,
     *   "publishedYear": 2018
     * }
     *
     * Notice:
     * - Status code 201 (Created) not 200 (OK)
     * - Response includes generated ID (1)
     * - Resource can be accessed at /api/books/1 (Location header)
     *
     * ═══════════════════════════════════════════════════════════════════════════════
     * DATA FLOW (Step-by-step):
     * ═══════════════════════════════════════════════════════════════════════════════
     * 1. CLIENT sends HTTP POST request with JSON body
     * 2. SPRING CONTROLLER receives request
     * 3. @RequestBody annotation triggers:
     *    - Jackson deserialization: JSON string → BookRequestDto
     *    - Spring Bean Validation: @NotBlank, @Min, etc. (future commits)
     * 4. Controller method receives BookRequestDto (type-safe)
     * 5. Calls bookService.createBook(requestDto)
     * 6. SERVICE layer:
     *    - Starts transaction
     *    - Converts DTO → Entity (MapStruct)
     *    - Persists entity (Hibernate save)
     *    - Converts Entity → DTO (MapStruct)
     *    - Ends transaction
     * 7. Controller receives BookResponseDto
     * 8. @ResponseBody annotation triggers:
     *    - Jackson serialization: BookResponseDto → JSON string
     * 9. Wraps in ResponseEntity with status 201
     * 10. Sends HTTP response to client
     *
     * ═══════════════════════════════════════════════════════════════════════════════
     * WHY @RequestBody?
     * ═══════════════════════════════════════════════════════════════════════════════
     * @RequestBody tells Spring to:
     * 1. Read request body as stream
     * 2. Deserialize using HttpMessageConverter (Jackson by default)
     * 3. Construct BookRequestDto instance
     * 4. Pass to method parameter
     *
     * Without @RequestBody:
     * Spring would look for BookRequestDto as query parameter or path variable
     * ❌ Wrong for JSON body deserialization
     *
     * ═══════════════════════════════════════════════════════════════════════════════
     * @PostMapping (no path):
     * ═══════════════════════════════════════════════════════════════════════════════
     * - No path parameter means inherit from class-level @RequestMapping
     * - Class path: /api/books
     * - Method path: (empty)
     * - Full endpoint: /api/books
     *
     * ═══════════════════════════════════════════════════════════════════════════════
     * ResponseEntity & HttpStatus.CREATED:
     * ═══════════════════════════════════════════════════════════════════════════════
     * ResponseEntity<BookResponseDto> = HTTP response wrapper
     * - Allows setting status code (201 vs 200)
     * - Can add headers
     * - Type-safe response body
     *
     * HttpStatus.CREATED = 201:
     * - Indicates resource was successfully created
     * - Client should redirect to /api/books/1 (from Location header)
     * - Better than 200 OK (which means operation succeeded, but no resource created)
     *
     * @param requestDto - book data from client (title, isbn, price, publishedYear)
     * @return ResponseEntity with 201 status and BookResponseDto (with ID)
     *
     * EXAMPLE CURL:
     * curl -X POST http://localhost:8080/api/books \
     *   -H "Content-Type: application/json" \
     *   -d '{"title":"Clean Code","isbn":"978-0132350884","price":50.99,"publishedYear":2008}'
     *
     * RESPONSE:
     * HTTP/1.1 201 Created
     * Location: /api/books/1
     * {"id":1,"title":"Clean Code",...}
     *
     * @see org.springframework.web.bind.annotation.PostMapping
     * @see org.springframework.web.bind.annotation.RequestBody
     * @see org.springframework.http.ResponseEntity
     * @see Commit 01: Basic CRUD
     */
    @PostMapping  // POST /api/books
    public ResponseEntity<BookResponseDto> createBook(@RequestBody BookRequestDto requestDto) {
        // Call service to create book
        // Service handles:
        // - DTO → Entity mapping
        // - @Transactional transaction management
        // - Hibernate persistence
        // - Entity → DTO response mapping
        BookResponseDto response = bookService.createBook(requestDto);
        
        // Return 201 Created status with response body
        // REST convention: POST that creates returns 201 (not 200)
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Get a book by ID via HTTP GET.
     *
     * ═══════════════════════════════════════════════════════════════════════════════
     * HTTP REQUEST:
     * ═══════════════════════════════════════════════════════════════════════════════
     * GET /api/books/1
     * (No request body)
     *
     * ═══════════════════════════════════════════════════════════════════════════════
     * HTTP RESPONSE (Success):
     * ═══════════════════════════════════════════════════════════════════════════════
     * HTTP 200 OK
     * Content-Type: application/json
     *
     * RESPONSE BODY:
     * {
     *   "id": 1,
     *   "title": "Effective Java",
     *   "isbn": "978-0134685991",
     *   "price": 45.99,
     *   "publishedYear": 2018
     * }
     *
     * ═══════════════════════════════════════════════════════════════════════════════
     * HTTP RESPONSE (Not Found):
     * ═══════════════════════════════════════════════════════════════════════════════
     * HTTP 404 Not Found
     * (Handled by GlobalExceptionHandler catching IllegalArgumentException)
     *
     * ═══════════════════════════════════════════════════════════════════════════════
     * @PathVariable & URL Path Parameters:
     * ═══════════════════════════════════════════════════════════════════════════════
     * @GetMapping("/{id}")
     * - {id} is a path variable (placeholder in URL)
     * - Spring extracts actual value from URL
     *
     * @PathVariable Long id
     * - Binds URL path variable {id} to method parameter id
     * - Spring converts string to Long automatically
     *
     * URL: /api/books/1
     * Spring extracts: id = 1 (Long)
     *
     * URL: /api/books/123
     * Spring extracts: id = 123 (Long)
     *
     * URL: /api/books/abc
     * Spring returns: 400 Bad Request (can't convert "abc" to Long)
     *
     * ═══════════════════════════════════════════════════════════════════════════════
     * FLOW:
     * ═══════════════════════════════════════════════════════════════════════════════
     * 1. Client sends GET /api/books/1
     * 2. Spring extracts id = 1 from URL
     * 3. Calls this method with id = 1
     * 4. Service queries database for Book with id=1
     * 5. If found: returns Book → DTO → JSON response (200 OK)
     * 6. If not found: throws exception → handled by GlobalExceptionHandler (404)
     *
     * @param id - book ID from URL path (e.g., 1, 2, 3)
     * @return ResponseEntity with 200 status and BookResponseDto
     *
     * EXAMPLE CURL:
     * curl http://localhost:8080/api/books/1
     * 
     * RESPONSE:
     * HTTP/1.1 200 OK
     * {"id":1,"title":"Effective Java",...}
     *
     * @see org.springframework.web.bind.annotation.GetMapping
     * @see org.springframework.web.bind.annotation.PathVariable
     * @see Commit 01: Basic CRUD
     */
    @GetMapping("/{id}")  // GET /api/books/{id}
    public ResponseEntity<BookResponseDto> getBook(@PathVariable Long id) {
        // Call service to fetch book by ID
        // Service queries database and converts to DTO
        // If not found: throws IllegalArgumentException
        //   → GlobalExceptionHandler catches and returns 404
        BookResponseDto response = bookService.getBook(id);
        
        // Return 200 OK status with response body
        return ResponseEntity.ok(response);
    }
}
