package org.example.bookstore.dto;

/**
 * ColumnInfoDto - represents a single column's information from the database
 * information_schema. Used by the /lab/schema-info endpoint to show actual
 * database column types and defaults created by Hibernate.
 */
public record ColumnInfoDto(
        String columnName,
        String dataType,
        String isNullable,
        String columnDefault
) {}
