package com.edureka.bookServiceManagement.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
@Component
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BorrowingRecord {
    private Long bookId;
    private LocalDate borrowingDate;
    private LocalDate returnDate;
    private String user;
}
