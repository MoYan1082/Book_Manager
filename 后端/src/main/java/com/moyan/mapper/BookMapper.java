package com.moyan.mapper;

import com.moyan.entity.Book;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.moyan.entity.BookTmp;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author moyan
 * @since 2021-05-14
 */
@Mapper
@Repository
public interface BookMapper extends BaseMapper<Book> {
    List<BookTmp> queryBookByFactor(String factor);
    List<BookTmp> queryBookByTag(String tag);
    List<BookTmp> queryBookByUserId(Long userId);
}
