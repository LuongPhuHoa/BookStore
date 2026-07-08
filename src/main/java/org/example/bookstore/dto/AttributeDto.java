package org.example.bookstore.dto;

/**
 * AttributeDto - describes a single attribute of an entity for lab introspection.
 *
 * Used by the lab endpoint to show how entity fields map to database columns.
 */
public record AttributeDto(
        String name,
        String javaType,
        String columnName,
        boolean nullable,
        boolean unique
) {}
