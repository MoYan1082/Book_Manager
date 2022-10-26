package com.moyan.common.dto;

import com.moyan.entity.Book;
import com.moyan.entity.BookTmp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookRespond extends Book {
    public List<String> tags = new ArrayList<>();
    public BookRespond(BookTmp bookTmp){
        super.setBookId(bookTmp.getBookId());

        super.setTitle(bookTmp.getTitle());
        super.setAuthor(bookTmp.getAuthor());
        super.setDescription(bookTmp.getDescription());
        super.setPhoto(bookTmp.getPhoto());

        super.setStatus(bookTmp.getStatus());
        super.setUserId(bookTmp.getUserId());
        super.setDeadline(bookTmp.getDeadline());

        tags = new ArrayList<>();
        tags.add(bookTmp.tag);
    }
}
