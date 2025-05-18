package com.ghadynazha.recipeapi.model;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Represents a sequence counter used to auto-increment IDs for documents.
 * Stored in the "counters" collection in MongoDB.
 *
 * Author: Ghady Nazha
 */
@Document(collection = "counters")
@Data
@AllArgsConstructor
public class Counter {
    /**
     * Default constructor for Counter.
     * Required by Spring Data and serialization frameworks.
     */
    public Counter() {}

    /**
     * Identifier for which sequence this counter applies (e.g., "recipe", "user").
     */
    @Id
    private String id;

    /**
     * Current sequence number used for auto-incrementing.
     */
    private int seq;
}