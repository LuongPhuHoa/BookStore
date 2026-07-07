package org.example.bookstore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * BookStoreApplication - Main entry point for the Hibernate Learning Laboratory.
 *
 * ═══════════════════════════════════════════════════════════════════════════════
 * PURPOSE:
 * ═══════════════════════════════════════════════════════════════════════════════
 * This is the Spring Boot application main class.
 * Contains the main() method to start the application.
 *
 * ═══════════════════════════════════════════════════════════════════════════════
 * @SpringBootApplication ANNOTATION:
 * ═══════════════════════════════════════════════════════════════════════════════
 * @SpringBootApplication is shorthand for three annotations:
 *
 * 1. @Configuration
 *    - Marks class as a Spring configuration class
 *    - Can define @Bean methods
 *    - Current class not defining any beans, so minimal usage
 *
 * 2. @ComponentScan
 *    - Tells Spring to scan this package (org.example.bookstore)
 *    - Finds all @Component, @Service, @Controller, @RestController classes
 *    - Also finds @Entity, @Repository classes
 *    - Automatically discovers:
 *      * BookController (@RestController)
 *      * LabController (@RestController)
 *      * BookService (@Service)
 *      * BookRepository (extends JpaRepository, auto-discovered)
 *      * GlobalExceptionHandler (@RestControllerAdvice)
 *      * BookMapper (@Mapper with componentModel="spring")
 *      * Book entity (@Entity)
 *
 * 3. @EnableAutoConfiguration (aka @SpringBootConfiguration)
 *    - Enables Spring Boot's auto-configuration
 *    - Detects dependencies in classpath (pom.xml)
 *    - Auto-configures:
 *      * Spring Data JPA (repository proxy generation)
 *      * Hibernate (entity scanning, DDL generation)
 *      * PostgreSQL JDBC (database connection)
 *      * Jackson (JSON serialization)
 *      * Tomcat (embedded web server)
 *      * Logging (SLF4J with Logback)
 *
 * ═══════════════════════════════════════════════════════════════════════════════
 * APPLICATION STARTUP SEQUENCE:
 * ═══════════════════════════════════════════════════════════════════════════════
 * 1. JVM starts
 * 2. main() method called
 * 3. SpringApplication.run() initialized
 *
 * 4. AUTO-CONFIGURATION (Spring Boot magic):
 *    - Detects classpath dependencies
 *    - Loads spring-boot-autoconfigure JAR
 *    - Auto-configures based on presence of classes
 *
 *    Example:
 *    - If org.springframework.data.jpa.repository.JpaRepository is on classpath
 *      → Auto-enable Spring Data JPA
 *    - If org.hibernate.cfg.Configuration is on classpath
 *      → Auto-enable Hibernate
 *    - If org.postgresql.Driver is on classpath
 *      → Auto-enable PostgreSQL JDBC driver
 *
 * 5. PROPERTY LOADING:
 *    - Reads application.properties
 *    - Sets:
 *      * spring.datasource.url
 *      * spring.datasource.username
 *      * spring.datasource.password
 *      * spring.jpa.hibernate.ddl-auto
 *      * Logging levels
 *      * Virtual threads configuration
 *
 * 6. DATABASE INITIALIZATION:
 *    - Connects to PostgreSQL (using JDBC driver + properties)
 *    - Detects @Entity classes (component scan)
 *    - Generates DDL (CREATE TABLE, etc.)
 *    - Executes DDL based on ddl-auto setting
 *      * create: DROP + CREATE all tables
 *      * create-drop: CREATE on startup, DROP on shutdown
 *      * validate: Check schema exists
 *      * update: Create tables if missing
 *
 * 7. SPRING CONTEXT INITIALIZATION:
 *    - Registers all @Component beans
 *    - Dependency injection happens
 *    - Constructor injection resolves @Autowired fields
 *
 * 8. TOMCAT STARTUP:
 *    - Embedded Tomcat starts on port 8080 (default)
 *    - Registers servlet mapping (Spring DispatcherServlet)
 *    - Ready to handle HTTP requests
 *
 * 9. APPLICATION READY:
 *    - Logs "Started BookStoreApplication in X seconds"
 *    - Listening on http://localhost:8080
 *
 * ═══════════════════════════════════════════════════════════════════════════════
 * HOW TO RUN:
 * ═══════════════════════════════════════════════════════════════════════════════
 * Option 1: Maven CLI
 * mvn spring-boot:run
 *
 * Option 2: IDE (IntelliJ)
 * - Click green arrow next to class name
 * - Or: Run → Run 'BookStoreApplication'
 *
 * Option 3: JAR
 * mvn clean package
 * java -jar target/bookstore-0.0.1-SNAPSHOT.jar
 *
 * ═══════════════════════════════════════════════════════════════════════════════
 * STARTUP LOGGING (Example):
 * ═══════════════════════════════════════════════════════════════════════════════
 * When you run the application, you'll see logs like:
 *
 * 2024-11-15 10:30:40.123  INFO 12345 --- [main] ...BookStoreApplication : Starting BookStoreApplication
 * 2024-11-15 10:30:40.234  INFO 12345 --- [main] ...BookStoreApplication : No active profile set, falling back to 1 default profile: "default"
 * 2024-11-15 10:30:42.456  INFO 12345 --- [main] ...HibernateJpaAutoConfiguration : Initialized JPA EntityManagerFactory for persistence unit 'default'
 * 2024-11-15 10:30:42.567  DEBUG 12345 --- [main] ...DDLDatabase : DROP TABLE books CASCADE
 * 2024-11-15 10:30:42.578  DEBUG 12345 --- [main] ...DDLDatabase : CREATE TABLE books (id BIGSERIAL PRIMARY KEY, ...)
 * 2024-11-15 10:30:42.890  INFO  12345 --- [main] ...TomcatWebServer : Tomcat started on port(s): 8080 (http)
 * 2024-11-15 10:30:42.900  INFO  12345 --- [main] ...BookStoreApplication : Started BookStoreApplication in 2.777 seconds
 *
 * INTERPRETATION:
 * - Hibernate initialized → DDL executed
 * - Tables dropped and recreated (because ddl-auto=create)
 * - Tomcat started on port 8080
 * - Total startup time: ~2.8 seconds
 *
 * ═══════════════════════════════════════════════════════════════════════════════
 * VIRTUAL THREADS (Java 21 Feature):
 * ═══════════════════════════════════════════════════════════════════════════════
 * application.properties has: spring.threads.virtual.enabled=true
 *
 * Virtual threads are lightweight threads for high concurrency:
 * - Thousands can run on single OS thread
 * - Perfect for I/O-bound operations (database, REST calls)
 * - Simpler concurrency model than traditional threads
 *
 * In startup logs, you might see:
 * thread=VirtualThread[#123]/runnable@ForkJoinPool-1-worker-1
 *
 * This indicates Spring is using virtual threads for request handling.
 *
 * ═══════════════════════════════════════════════════════════════════════════════
 * STOPPING THE APPLICATION:
 * ═══════════════════════════════════════════════════════════════════════════════
 * Ctrl+C in terminal (sends SIGTERM signal)
 * - Spring gracefully shuts down
 * - Closes database connection
 * - Logs cleanup messages
 * - Process exits
 *
 * @see org.springframework.boot.autoconfigure.SpringBootApplication
 * @see org.springframework.boot.SpringApplication
 */
@SpringBootApplication  // Enables @Configuration + @ComponentScan + @EnableAutoConfiguration
public class BookStoreApplication {

    /**
     * Main entry point for the Spring Boot application.
     *
     * EXECUTION FLOW:
     * 1. SpringApplication.run() creates Spring context
     * 2. Auto-configures based on classpath + application.properties
     * 3. Initializes Hibernate (scans @Entity, generates DDL)
     * 4. Registers all @Component beans (controllers, services, etc.)
     * 5. Starts embedded Tomcat
     * 6. Application ready to accept HTTP requests
     *
     * @param args - command-line arguments (typically empty for development)
     *
     * @see org.springframework.boot.SpringApplication#run(Class, String...)
     */
    public static void main(String[] args) {
        // SpringApplication.run() is the magic line that starts everything
        // First argument: this class (BookStoreApplication.class)
        // Second argument: command-line args (if any)
        SpringApplication.run(BookStoreApplication.class, args);
    }
}
