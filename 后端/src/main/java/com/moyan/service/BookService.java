package com.moyan.service;

import com.moyan.entity.Book;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;


/**
 * <p>
 *  服务类
 * </p>
 *
 * @author moyan
 * @since 2021-05-14
 */

public interface BookService extends IService<Book> {
    List<Book> getBookByUser(Long userId);

}
