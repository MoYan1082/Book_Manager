package com.moyan.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.moyan.common.Result;
import com.moyan.common.dto.InformAdd;
import com.moyan.common.dto.InformDel;
import com.moyan.entity.Inform;
import com.moyan.entity.User;
import com.moyan.mapper.InformMapper;
import com.moyan.service.InformService;
import com.moyan.service.UserService;
import com.moyan.util.JwtUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;


/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author moyan
 * @since 2021-05-14
 */
@RestController
@RequestMapping("/inform")
public class InformController {

    @Autowired
    InformService informService;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    UserService userService;

    @PostMapping("/del")
    @RequiresAuthentication
    public Result informDel(@RequestBody InformDel informDel, HttpServletRequest httpServletRequest){
        String jwt = httpServletRequest.getHeader("Authorization");
        Long userId = Long.valueOf(jwtUtils.getClaimByToken(jwt).getSubject());


        QueryWrapper<User> userQueryWrapper = new QueryWrapper<User>()
                .eq("user_id", userId);
        List<User> users = userService.list(userQueryWrapper);
        if(!users.get(0).getAdmine()){

            return Result.fail("当前用户没有权限！");
        }


        QueryWrapper<Inform> informQueryWrapper = new QueryWrapper<Inform>()
                .eq("inform_id", informDel.getInformId());
        List<Inform> informs = informService.list(informQueryWrapper);
        if(informs.isEmpty()){
            return Result.fail("没有找到当前公告!");
        }


        informService.remove(informQueryWrapper);
        return Result.succ("删除公告操作成功");
    }

    @PostMapping("/add")
    @RequiresAuthentication
    public Result informAdd(@RequestBody InformAdd informAdd, HttpServletRequest httpServletRequest){
        String jwt = httpServletRequest.getHeader("Authorization");
        Long userId = Long.valueOf(jwtUtils.getClaimByToken(jwt).getSubject());

        QueryWrapper<User> userQueryWrapper = new QueryWrapper<User>()
                .eq("user_id", userId);
        List<User> users = userService.list(userQueryWrapper);
        if(!users.get(0).getAdmine()){

            return Result.fail("当前用户没有权限！");
        }


        Inform inform = new Inform();
        inform.setUserId(userId);
        inform.setContent(informAdd.getContent());
        inform.setTitle(informAdd.getTitle());

        informService.save(inform);


        return Result.succ("增加公告操作成功！");
    }

    @GetMapping("/all")
    public Result informFind(){
        List<Inform> informs = informService.list();
        return Result.succ(informs);
    }
}
