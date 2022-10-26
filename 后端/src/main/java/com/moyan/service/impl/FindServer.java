package com.moyan.service.impl;

import com.moyan.common.dto.BookRespond;
import com.moyan.entity.BookTmp;
import com.moyan.mapper.BookMapper;
import com.moyan.service.FindService;
import com.moyan.util.BookUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FindServer implements FindService {

    @Autowired
    private BookMapper bookMapper;

    @Override
    public List<BookRespond> queryBookByFactor(String factor) {
        List<BookTmp> bookTmps = bookMapper.queryBookByFactor(factor);
        return BookUtil.BookTmpToFindRespond(bookTmps);
    }

    @Override
    public List<BookRespond> queryBookByTag(List<String> tags) {
        List<BookTmp> res = new ArrayList<>();
        for (String tag : tags) {
            List<BookTmp> tmps = bookMapper.queryBookByTag(tag);
            for(BookTmp tmp : tmps){
                for(BookTmp bookTmpQueryByTag : bookMapper.queryBookByFactor(tmp.getTitle())){
                    res.add(bookTmpQueryByTag);
                }
            }
        }
        return BookUtil.BookTmpToFindRespond(res);
    }

    @Override
    public List<BookRespond> queryBookByUserId(Long userId){
        List<BookTmp> bookTmps = bookMapper.queryBookByUserId(userId);

        return BookUtil.BookTmpToFindRespond(bookTmps);
    }
}
