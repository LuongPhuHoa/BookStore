package org.example.bookstore.mapper;

import org.example.bookstore.dto.BookRequestDto;
import org.example.bookstore.dto.BookResponseDto;
import org.example.bookstore.entity.Book;
import org.mapstruct.Mapper;

/**
 * BookMapper - Converts between Book entity and DTOs using MapStruct.
 *
 * ═══════════════════════════════════════════════════════════════════════════════
 * WHAT IS MAPSTRUCT?
 * ═══════════════════════════════════════════════════════════════════════════════
 * MapStruct is a code generator for object mappings.
 *
 * KEY CHARACTERISTICS:
 * 1. Annotation-driven: You write interfaces with @Mapping annotations
 * 2. Compile-time generation: Creates mapping code during Maven compilation
 * 3. NOT runtime reflection: No performance penalty like ModelMapper
 * 4. Type-safe: Errors caught at compile time, not runtime
 * 5. Spring-aware: @Mapper(componentModel = "spring") registers as @Component
 *
 * ═══════════════════════════════════════════════════════════════════════════════
 * HOW IT WORKS:
 * ═══════════════════════════════════════════════════════════════════════════════
 * 1. Maven compilation phase:
 *    - MapStruct processor scans your interfaces
 *    - Reads @Mapper and @Mapping annotations
 *    - Generates implementation class (BookMapperImpl)
 *
 * 2. Spring boot initialization:
 *    - componentModel = "spring" tells MapStruct to add @Component
 *    - Spring detects BookMapperImpl and registers as bean
 *    - @Autowired BookMapper injects the implementation
 *
 * 3. At runtime:
 *    - Call bookMapper.toEntity(dto)
 *    - Uses generated code (fast, no reflection)
 *    - Returns converted object
 *
 * GENERATED CODE LOCATION:
 * target/generated-sources/annotations/org/example/bookstore/mapper/BookMapperImpl.java
 *
 * ═══════════════════════════════════════════════════════════════════════════════
 * MAPPING FLOW:
 * ═══════════════════════════════════════════════════════════════════════════════
 *
 * INPUT MAPPING (DTO → Entity):
 * BookRequestDto requestDto
 *   ├─ title: String → Book.title: String (auto-match by name)
 *   ├─ isbn: String → Book.isbn: String (auto-match by name)
 *   ├─ price: BigDecimal → Book.price: BigDecimal (auto-match by name)
 *   └─ publishedYear: Integer → Book.publishedYear: Integer (auto-match by name)
 * + Book.id = null (not in requestDto, stays null)
 *
 * OUTPUT MAPPING (Entity → DTO):
 * Book entity
 *   ├─ id: Long → BookResponseDto.id: Long (auto-match by name)
 *   ├─ title: String → BookResponseDto.title: String (auto-match by name)
 *   ├─ isbn: String → BookResponseDto.isbn: String (auto-match by name)
 *   ├─ price: BigDecimal → BookResponseDto.price: BigDecimal (auto-match by name)
 *   └─ publishedYear: Integer → BookResponseDto.publishedYear: Integer (auto-match by name)
 *
 * ═══════════════════════════════════════════════════════════════════════════════
 * WHY NOT MANUAL MAPPING?
 * ═══════════════════════════════════════════════════════════════════════════════
 * MANUAL APPROACH (❌ Bad):
 * public Book toEntity(BookRequestDto dto) {
 *     Book book = new Book();
 *     book.setTitle(dto.title());
 *     book.setIsbn(dto.isbn());
 *     book.setPrice(dto.price());
 *     book.setPublishedYear(dto.publishedYear());
 *     return book;
 * }
 *
 * PROBLEMS:
 * ❌ Error-prone: Easy to miss a field
 * ❌ Hard to maintain: Adding a field? Need to update two places
 * ❌ Verbose: Repetitive boilerplate code
 * ❌ Null-safety: Must remember to handle nulls
 * ❌ Type conversion: Must remember to convert if needed
 *
 * MAPSTRUCT APPROACH (✅ Good):
 * @Mapper(componentModel = "spring")
 * public interface BookMapper {
 *     Book toEntity(BookRequestDto dto);
 *     BookResponseDto toResponseDto(Book entity);
 * }
 *
 * BENEFITS:
 * ✅ Declarative: Just declare the method, MapStruct generates code
 * ✅ Maintainable: Add a field? No changes needed (if names match)
 * ✅ Type-safe: Compile errors if types don't match
 * ✅ Null-safe: Handles nulls automatically
 * ✅ Fast: Generated code runs at runtime (no reflection)
 *
 * ═══════════════════════════════════════════════════════════════════════════════
 * MAPPING RULES (What MapStruct does):
 * ═══════════════════════════════════════════════════════════════════════════════
 * 1. NAME MATCHING:
 *    If source and target field names are identical → Auto-map
 *    Example: dto.title() → book.setTitle(dto.title())
 *
 * 2. CASE SENSITIVITY:
 *    bookRequestDto.publishedYear → Book.publishedYear ✅ Match
 *    bookRequestDto.publishedYear → Book.PublishedYear ✅ Match
 *    bookRequestDto.publishedYear → Book.published_year ✅ Match
 *
 * 3. TYPE CONVERSION (Auto):
 *    Long → Integer (auto-converts)
 *    String → Enum (if standard conversion exists)
 *    Date → LocalDate (if configured)
 *
 * 4. NESTED MAPPINGS (Future commits):
 *    If Book had an Author field:
 *    @Mapping(source = "author.name", target = "authorName")
 *
 * 5. UNMAPPED PROPERTIES:
 *    If a field can't be auto-mapped:
 *    Use @Mapping(target = "fieldName", source = "sourceName")
 *    Or use expression: @Mapping(target = "field", expression = "java(someMethod())")
 *
 * ═══════════════════════════════════════════════════════════════════════════════
 * @Mapper(componentModel = "spring") EXPLAINED:
 * ═══════════════════════════════════════════════════════════════════════════════
 * componentModel = "spring" tells MapStruct to:
 * 1. Add @Component annotation to generated BookMapperImpl
 * 2. Inject any dependencies via constructor
 * 3. Make it a Spring bean (can @Autowired inject)
 *
 * Alternative values:
 * - "default" (no component model, manual instantiation)
 * - "jsr330" (uses @Inject for JSR-330)
 * - "cdi" (uses CDI, for Java EE)
 *
 * ═══════════════════════════════════════════════════════════════════════════════
 * USAGE IN SPRING SERVICE:
 * ═══════════════════════════════════════════════════════════════════════════════
 * @Service
 * public class BookService {
 *     private final BookRepository bookRepository;
 *     private final BookMapper bookMapper;  // Injected by Spring
 *
 *     @Transactional
 *     public BookResponseDto createBook(BookRequestDto requestDto) {
 *         // Step 1: DTO → Entity (using generated mapper)
 *         Book book = bookMapper.toEntity(requestDto);
 *         //
 *         // Generated code does:
 *         // Book book = new Book();
 *         // book.setTitle(requestDto.title());
 *         // book.setIsbn(requestDto.isbn());
 *         // book.setPrice(requestDto.price());
 *         // book.setPublishedYear(requestDto.publishedYear());
 *         // book.setId(null);  // Not in requestDto
 *
 *         // Step 2: Persist to database
 *         Book savedBook = bookRepository.save(book);
 *         // savedBook.id is now set (auto-generated by DB)
 *
 *         // Step 3: Entity → DTO (using generated mapper)
 *         return bookMapper.toResponseDto(savedBook);
 *         //
 *         // Generated code does:
 *         // return new BookResponseDto(
 *         //     savedBook.getId(),
 *         //     savedBook.getTitle(),
 *         //     savedBook.getIsbn(),
 *         //     savedBook.getPrice(),
 *         //     savedBook.getPublishedYear()
 *         // );
 *     }
 * }
 *
 * ═══════════════════════════════════════════════════════════════════════════════
 * DEBUGGING:
 * ═══════════════════════════════════════════════════════════════════════════════
 * If mapping isn't working:
 * 1. Check generated class: target/generated-sources/annotations/
 * 2. Rebuild: mvn clean compile
 * 3. Add explicit @Mapping if field names don't match
 * 4. Check for compilation errors in build output
 *
 * @see org.mapstruct.Mapper
 * @see org.mapstruct.Mapping
 */
@Mapper(componentModel = "spring")  // componentModel="spring" → generates @Component with Spring dependency injection
public interface BookMapper {
    
    /**
     * Convert BookRequestDto (REST input) → Book entity (JPA entity).
     *
     * MAPPING LOGIC:
     * - dto.title() → book.title
     * - dto.isbn() → book.isbn
     * - dto.price() → book.price
     * - dto.publishedYear() → book.publishedYear
     * - book.id remains null (not in DTO, will be auto-generated by DB)
     *
     * GENERATED CODE:
     * public Book toEntity(BookRequestDto dto) {
     *     if (dto == null) return null;
     *     Book book = new Book();
     *     book.setTitle(dto.title());
     *     book.setIsbn(dto.isbn());
     *     book.setPrice(dto.price());
     *     book.setPublishedYear(dto.publishedYear());
     *     return book;
     * }
     *
     * WHEN IS THIS CALLED?
     * In BookService.createBook():
     * Book book = bookMapper.toEntity(requestDto);  // ← Converts here
     *
     * @param dto Input DTO from REST API
     * @return Book entity (ready to persist)
     */
    Book toEntity(BookRequestDto dto);
    
    /**
     * Convert Book entity (from DB) → BookResponseDto (REST output).
     *
     * MAPPING LOGIC:
     * - book.id → dto.id (includes database-generated ID)
     * - book.title → dto.title
     * - book.isbn → dto.isbn
     * - book.price → dto.price
     * - book.publishedYear → dto.publishedYear
     *
     * GENERATED CODE:
     * public BookResponseDto toResponseDto(Book entity) {
     *     if (entity == null) return null;
     *     return new BookResponseDto(
     *         entity.getId(),
     *         entity.getTitle(),
     *         entity.getIsbn(),
     *         entity.getPrice(),
     *         entity.getPublishedYear()
     *     );
     * }
     *
     * WHEN IS THIS CALLED?
     * In BookService.createBook():
     * return bookMapper.toResponseDto(savedBook);  // ← Converts here
     *
     * @param entity Book entity from database
     * @return DTO for HTTP response (includes ID)
     */
    BookResponseDto toResponseDto(Book entity);
}
