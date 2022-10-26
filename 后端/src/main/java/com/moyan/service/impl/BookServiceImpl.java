package com.moyan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.moyan.entity.Book;
import com.moyan.mapper.BookMapper;
import com.moyan.service.BookService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;


/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author moyan
 * @since 2021-05-14
 */


@Service
public class BookServiceImpl extends ServiceImpl<BookMapper, Book> implements BookService {
    @Autowired
    BookMapper bookMapper;


    @Override
    public List<Book> getBookByUser(Long userId) {
        QueryWrapper<Book> bookQueryWrapper = new QueryWrapper<Book>()
                .eq("user_id", userId);
        List<Book> books = bookMapper.selectList(bookQueryWrapper);
        return books;
    }
}
