package com.moyan.util;

import com.moyan.entity.Book;
import com.moyan.entity.Tag;
import com.moyan.common.dto.BookRequest;
import com.moyan.common.dto.BookRespond;
import com.moyan.entity.BookTmp;

import java.util.ArrayList;
import java.util.List;

public class BookUtil {


    // 将相同的标签整合在一起
    public static List<BookRespond> BookTmpToFindRespond(List<BookTmp> bookTmps){
        int n = bookTmps.size();
        List<BookRespond> bookRespondList = new ArrayList<>();

        for(int i = 0; i < n; i++){
            boolean key = false;
            for(int j = 0; j < bookRespondList.size(); j++){
                if(bookTmps.get(i).getBookId() == bookRespondList.get(j).getBookId()){
                    bookRespondList.get(j).tags.add(bookTmps.get(i).tag);
                    key = true;
                    break;
                }
            }
            if(!key)
                bookRespondList.add(new BookRespond(bookTmps.get(i)));
        }

        return bookRespondList;
    }

    // 将BookRequest转为Book
    public static Book BookRequestToBook(BookRequest bookRequest){
        Book book = new Book();
        book.setAuthor(bookRequest.getAuthor());
        book.setTitle(bookRequest.getTitle());
        book.setPhoto(bookRequest.getPhoto());
        book.setBookId(bookRequest.getBookId());
        book.setDescription(bookRequest.getDescription());
        book.setDeadline(null);
        book.setStatus(false);
        book.setUserId(null);
        return book;
    }


    // 将BookRequest转为List<Tag>
    public static List<Tag> BookRequestToTag(BookRequest bookRequest, Long bookId){
        List<Tag> res = new ArrayList<>();

        for(String tag : bookRequest.getTags()){
            res.add(new Tag()
                    .setTitle(tag)
                    .setBookId(bookId));
        }
        return res;
    }

}
