package com.edureka.bookServiceManagement.entity;

import com.edureka.bookServiceManagement.utils.BookStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "book")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String ISBN;
    private Long authorId; // Instead of the whole author object, we're storing authorId
    @Enumerated(EnumType.STRING)
    private BookStatus status;
}
