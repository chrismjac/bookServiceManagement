package com.edureka.bookServiceManagement.entity;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class Author {

    private Long id;
    private String name;
    private String country;
}
