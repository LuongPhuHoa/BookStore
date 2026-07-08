package org.example.bookstore.exception;

/** Resource not found runtime exception (compact). */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) { super(message); }
}
