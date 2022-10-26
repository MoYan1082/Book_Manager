package com.moyan.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.moyan.common.Result;
import com.moyan.common.dto.BookRespond;
import com.moyan.common.dto.ProblemAdd;
import com.moyan.common.dto.ProblemDel;
import com.moyan.entity.Problem;
import com.moyan.entity.Reply;
import com.moyan.service.ProblemService;
import com.moyan.service.ReplyService;
import com.moyan.util.JwtUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping("/problem")
public class ProblemController {
    @Autowired
    ProblemService problemService;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    ReplyService replyService;


    @PostMapping("/del")
    @RequiresAuthentication
    public Result problemDel(@RequestBody ProblemDel problemDel, HttpServletRequest httpServletRequest){

        QueryWrapper<Problem> problemQueryWrapper = new QueryWrapper<Problem>()
                .eq("problem_id", problemDel.getProblemId());
        QueryWrapper<Reply> replyQueryWrapper = new QueryWrapper<Reply>()
                .eq("problem_id", problemDel.getProblemId());

        List<Problem> problems = problemService.list(problemQueryWrapper);
        if(problems.isEmpty()){
            return Result.fail("没有找到当前问题!");
        }
        String jwt = httpServletRequest.getHeader("Authorization");
        String userId = jwtUtils.getClaimByToken(jwt).getSubject();

        List<Problem> problemList = problemService.list(new QueryWrapper<Problem>()
                .eq("user_id", Long.valueOf(userId)));

//        System.out.println(problemDel);
//        System.out.println(problemList);

        for (Problem tmp : problemList){
            if(tmp.getProblemId().equals(problemDel.getProblemId())){

                replyService.remove(replyQueryWrapper);
                problemService.remove(problemQueryWrapper);
                return Result.succ("删除问题操作成功！");
            }
        }
        return Result.fail("当前用户没有权限！");
    }

    @PostMapping("/add")
    @RequiresAuthentication
    public Result problemAdd(@RequestBody ProblemAdd problemAdd, HttpServletRequest httpServletRequest){
        String jwt = httpServletRequest.getHeader("Authorization");
        Long userId = Long.valueOf(jwtUtils.getClaimByToken(jwt).getSubject());

        Problem problem = new Problem();
        problem.setUserId(userId);
        problem.setContent(problemAdd.getContent());
        problem.setTitle(problemAdd.getTitle());

        // 防止title重复
        /*
        List<Problem> problems = problemService.list(new QueryWrapper<Problem>()
                .eq("title", problemAdd.getTitle()));
        if(!problems.isEmpty()){
            return Result.fail("已经存在当前问题!");
        }
        */

        problemService.save(problem);
        return Result.succ("新增问题操作成功！");
    }

    @GetMapping("/all")
    public Result problemFind(){
        List<Problem> problems = problemService.list();
        return Result.succ(problems);
    }

}
