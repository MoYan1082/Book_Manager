package com.moyan.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.moyan.common.Result;
import com.moyan.common.dto.UserAdd;
import com.moyan.common.dto.UserDel;
import com.moyan.entity.Book;
import com.moyan.entity.User;
import com.moyan.common.dto.UserRespond;
import com.moyan.mapper.UserMapper;
import com.moyan.service.BookService;
import com.moyan.service.UserService;
import com.moyan.util.JwtUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author moyan
 * @since 2021-05-14
 */
@RequiresAuthentication
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    BookService bookService;

    @Autowired
    JwtUtils jwtUtils;

    @GetMapping("/all")
    public Result getAll(HttpServletRequest httpServletRequest){
        String jwt = httpServletRequest.getHeader("Authorization");
        Long userId = Long.valueOf(jwtUtils.getClaimByToken(jwt).getSubject());

        QueryWrapper<User> userQueryWrapper = new QueryWrapper<User>().eq("user_id", userId);

        List<User> curUser = userService.list(userQueryWrapper);
        if(!curUser.get(0).getAdmine()){
            return Result.fail("没有访问的权限");
        }

        List<User> users = userService.list();
        List<UserRespond> res = new ArrayList<>();

        for (User user: users) {
            if(user.getUserId().equals(userId)){
                continue;
            }
            UserRespond userRespond = new UserRespond();
            userRespond.setAdmine(user.getAdmine());
            userRespond.setCredit(user.getCredit());
            userRespond.setUserId(user.getUserId());
            userRespond.setUsername(user.getUsername());

            QueryWrapper<Book> queryWrapper = new QueryWrapper<Book>()
                    .eq("user_id", user.getUserId())
                    .eq("status", true);
            userRespond.setBooks(
                bookService.list(queryWrapper)
            );
//            System.out.println(userRespond.getUsername() + " " + userRespond.getBooks());

            res.add(userRespond);
        }



        return Result.succ(res);
    }

    @PostMapping("/del")
    public Result delUser(@RequestBody  UserDel userDel, HttpServletRequest httpServletRequest){
        String jwt = httpServletRequest.getHeader("Authorization");
        Long userId = Long.valueOf(jwtUtils.getClaimByToken(jwt).getSubject());

        QueryWrapper<User> userQueryWrapper = new QueryWrapper<User>().eq("user_id", userId);
        List<User> curUser = userService.list(userQueryWrapper);
        if(!curUser.get(0).getAdmine()){
            return Result.fail("没有访问的权限");
        }

        QueryWrapper<User> userQueryWrapper1 = new QueryWrapper<User>().eq("user_id", userDel.getUserId());
        userService.remove(userQueryWrapper1);
//        System.out.println(userDel.getUserId());
        return Result.succ("操作成功");
    }

    @PostMapping("/add")
    public Result addUser(@RequestBody UserAdd userAdd, HttpServletRequest httpServletRequest){
        String jwt = httpServletRequest.getHeader("Authorization");
        Long userId = Long.valueOf(jwtUtils.getClaimByToken(jwt).getSubject());

        QueryWrapper<User> userQueryWrapper = new QueryWrapper<User>().eq("user_id", userId);
        List<User> curUser = userService.list(userQueryWrapper);
        if(!curUser.get(0).getAdmine()){
            return Result.fail("没有访问的权限");
        }

        User user = new User();
        user.setAdmine(userAdd.getAdmine());
        user.setUsername(userAdd.getUsername());
        user.setPassword(userAdd.getPassword());

        userService.save(user);
//        System.out.println(userDel.getUserId());
        return Result.succ("操作成功");
    }


}
