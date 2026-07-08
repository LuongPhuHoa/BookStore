package org.example.bookstore.lab;

import jakarta.persistence.Column;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Table;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.Metamodel;
import lombok.RequiredArgsConstructor;
import org.example.bookstore.dto.AttributeDto;
import org.example.bookstore.dto.ColumnInfoDto;
import org.example.bookstore.dto.EntityMetadataDto;
import org.example.bookstore.entity.Book;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

/**
 * EntityLabController - lab endpoints to inspect basic entity mapping.
 *
 * Endpoints:
 *  - GET /lab/entity-metadata  -> returns JPA/annotation-based mapping details for Book
 *  - GET /lab/schema-info      -> returns actual database column metadata for 'books' table
 *
 * Learning objective (Commit 01):
 *  - Understand how the Java entity maps to a table and columns
 *  - Observe differences (if any) between annotation mapping and actual DB schema
 */
@RestController
@RequestMapping("/lab")
@RequiredArgsConstructor
public class EntityLabController {

    private final EntityManagerFactory emf;
    private final JdbcTemplate jdbcTemplate;

    /**
     * Return JPA-level metadata for the Book entity.
     * This uses the JPA Metamodel and Java reflection to show how fields map to columns
     * based on @Column and @Table annotations.
     */
    @GetMapping("/entity-metadata")
    public ResponseEntity<EntityMetadataDto> entityMetadata() {
        Metamodel metamodel = emf.getMetamodel();
        EntityType<Book> entityType = metamodel.entity(Book.class);

        Class<Book> clazz = Book.class;
        Table table = clazz.getAnnotation(Table.class);
        String tableName = (table != null && !table.name().isEmpty()) ? table.name() : clazz.getSimpleName().toLowerCase();

        List<AttributeDto> attributes = entityType.getAttributes().stream().map(a -> {
            String name = a.getName();
            String javaType = a.getJavaType().getSimpleName();
            String columnName = name;
            boolean nullable = true;
            boolean unique = false;
            try {
                Field f = clazz.getDeclaredField(name);
                Column col = f.getAnnotation(Column.class);
                if (col != null) {
                    if (col.name() != null && !col.name().isEmpty()) columnName = col.name();
                    nullable = col.nullable();
                    unique = col.unique();
                }
            } catch (NoSuchFieldException e) {
                // ignore — attribute might be a synthetic or access-method based attribute
            }
            return new AttributeDto(name, javaType, columnName, nullable, unique);
        }).collect(Collectors.toList());

        EntityMetadataDto dto = new EntityMetadataDto(entityType.getName(), clazz.getName(), tableName, attributes);
        return ResponseEntity.ok(dto);
    }

    /**
     * Query the database information_schema to show the actual columns created
     * for the "books" table in the current schema. This helps you compare
     * annotation-driven mapping with the real database structure created by Hibernate.
     */
    @GetMapping("/schema-info")
    public ResponseEntity<List<ColumnInfoDto>> schemaInfo() {
        String sql = "SELECT column_name, data_type, is_nullable, column_default " +
                "FROM information_schema.columns " +
                "WHERE table_name = ? AND table_schema = current_schema() " +
                "ORDER BY ordinal_position";
        List<ColumnInfoDto> cols = jdbcTemplate.query(sql, new Object[]{"books"}, (rs, rowNum) ->
                new ColumnInfoDto(
                        rs.getString("column_name"),
                        rs.getString("data_type"),
                        rs.getString("is_nullable"),
                        rs.getString("column_default")
                )
        );
        return ResponseEntity.ok(cols);
    }
}
