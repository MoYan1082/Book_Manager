package com.moyan.service;

import com.moyan.common.dto.BookRespond;

import java.util.List;

public interface FindService {
    List<BookRespond> queryBookByFactor(String factor);
    List<BookRespond> queryBookByTag(List<String> tags);
    List<BookRespond> queryBookByUserId(Long userId);

}
