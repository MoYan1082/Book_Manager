package com.moyan;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.moyan.entity.User;
import com.moyan.service.UserService;
import com.moyan.util.JwtUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class TestapiApplicationTests {


    @Autowired
    JwtUtils jwtUtils;


    @Autowired
    UserService userService;

    @Test
    void contextLoads() {
        List<User> users = userService.list(new QueryWrapper<User>().eq("username", "moyan"));

        User user = users.get(0);


        String jwt = jwtUtils.generateToken(user.getUserId());

        System.out.println("jwt: "+jwt);



    }

}
