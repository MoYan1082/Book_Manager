package com.moyan.controller;

import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.moyan.common.Result;
import com.moyan.common.dto.LoginDto;
import com.moyan.entity.User;
import com.moyan.service.UserService;
import com.moyan.util.JwtUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
public class AccountController {

    @Autowired
    UserService userService;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/login")
    // @validated来校验数据，如果数据异常则会统一抛出异常，方便异常中心统一处理。
    public Result login(@Validated @RequestBody LoginDto loginDto, HttpServletResponse response) {
        // 通过用户名获取当前用户的信息
        User user = userService.getOne(new QueryWrapper<User>().eq("username", loginDto.getUsername()));
        Assert.notNull(user, "用户不存在");

        // 和当前密码进行对照
        if(!user.getPassword().equals(loginDto.getPassword())){
            return Result.fail("密码不正确");
        }

        // 生成jwt(通过用户的ID)
        String jwt = jwtUtils.generateToken(user.getUserId());
        // 将请求体返回
        response.setHeader("Authorization", jwt);
        response.setHeader("Access-control-Expose-Headers", "Authorization");
        return Result.succ(MapUtil.builder()
                .put("userId", user.getUserId())
                .put("username", user.getUsername())
                .put("admine", user.getAdmine())
                .map()
        );
    }

    @RequiresAuthentication
    @GetMapping("/logout")
    public Result logout() {
        SecurityUtils.getSubject().logout();
        return Result.succ(null);
    }

}
