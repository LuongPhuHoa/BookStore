package org.example.bookstore.dto;

import java.util.List;

/**
 * EntityMetadataDto - summary information about an entity used in lab experiments.
 *
 * Contains the entity name, Java type, mapped table name, and a list of attributes
 * with their JPA column mapping details.
 */
public record EntityMetadataDto(
        String entityName,
        String javaType,
        String tableName,
        List<AttributeDto> attributes
) {}
