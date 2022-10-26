package com.moyan.service.impl;

import com.moyan.entity.History;
import com.moyan.common.dto.UserHistory;
import com.moyan.mapper.HistoryMapper;
import com.moyan.service.FindService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class HistoryServer {

    @Autowired
    FindService findService;

    @Autowired
    HistoryMapper historyMapper;

    public UserHistory getUserHistory(Long id){
        UserHistory res = new UserHistory();
        res.setUserId(id);

        HashMap<String, Object> queryMap = new HashMap<>();
        queryMap.put("user_id", id);
        List<History> histories = historyMapper.selectByMap(queryMap);
        for(History history : histories){
            Long tmpBookId = history.getBookId();

        }

        return res;
    }

}
