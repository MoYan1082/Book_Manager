package com.moyan.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookRequest {
    private Long bookId;

    private String title;

    private String author;

    private String photo;

    private String description;

    private List<String> tags;
}
