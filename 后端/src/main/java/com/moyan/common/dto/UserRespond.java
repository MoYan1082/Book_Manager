package com.moyan.common.dto;

import com.moyan.entity.Book;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRespond {
    private static final long serialVersionUID = 1L;
    private Long userId;
    private String username;
    private Boolean admine;
    private Integer credit;
    private List<Book> books;
}
