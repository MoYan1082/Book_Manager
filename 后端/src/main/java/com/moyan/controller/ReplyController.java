package com.moyan.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.moyan.common.Result;
import com.moyan.common.dto.ReplyAdd;
import com.moyan.entity.Reply;
import com.moyan.common.dto.FindReplyRequest;
import com.moyan.common.dto.ReplyRequest;
import com.moyan.service.ReplyService;
import com.moyan.util.JwtUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
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
@RequestMapping("/reply")
public class ReplyController {

    @Autowired
    ReplyService replyService;

    @Autowired
    JwtUtils jwtUtils;


    @PostMapping("/add")
    @RequiresAuthentication
    public Result replyAdd(@RequestBody ReplyAdd replyAdd, HttpServletRequest httpServletRequest){
        String jwt = httpServletRequest.getHeader("Authorization");
        Long userId = Long.valueOf(jwtUtils.getClaimByToken(jwt).getSubject());

        Reply reply = new Reply();
        reply.setContent(replyAdd.getContent());
        reply.setProblemId(replyAdd.getProblemId());
        reply.setUserId(userId);

        replyService.save(reply);
        return Result.succ("新增回复操作成功！");
    }

    @PostMapping("/del")
    @RequiresAuthentication
    public Result replyDel(@RequestBody ReplyRequest replyRequest, HttpServletRequest httpServletRequest){
        String jwt = httpServletRequest.getHeader("Authorization");
        Long userId = Long.valueOf(jwtUtils.getClaimByToken(jwt).getSubject());

        QueryWrapper<Reply> replyQueryWrapper = new QueryWrapper<Reply>()
                .eq("reply_id", replyRequest.getReplyId());
        List<Reply> replies = replyService.list(replyQueryWrapper);
        if(replies.isEmpty())
            return Result.fail("不存在当前回复！");
        if(!(replies.get(0).getUserId().equals(userId)))
            return Result.fail("该用户没有权限！");
        replyService.remove(replyQueryWrapper);
        return Result.succ("删除回复操作成功！");
    }


    @PostMapping("find")
    public Result findByProblemId(@RequestBody FindReplyRequest findReplyRequest){
        QueryWrapper<Reply> replyQueryWrapper = new QueryWrapper<Reply>()
                .eq("problem_id", findReplyRequest.getProblemId());

        List<Reply> res = replyService.list(replyQueryWrapper);
//        System.out.println(res);

        return Result.succ(res);
    }
}
