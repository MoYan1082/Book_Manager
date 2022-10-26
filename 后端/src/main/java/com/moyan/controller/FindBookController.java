package com.moyan.controller;

import com.moyan.common.Result;
import com.moyan.common.dto.FindByUserRequest;
import com.moyan.common.dto.FindRequest;
import com.moyan.common.dto.BookRespond;
import com.moyan.service.FindService;
import com.moyan.service.UserService;
import com.moyan.shiro.JwtToken;
import com.moyan.util.JwtUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping
public class FindBookController {

    @Autowired
    FindService findService;

    @Autowired
    UserService userService;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/find")
    public Result FilterBook(@RequestBody FindRequest findRequest){
//        System.out.println(findRequest);
        String factor = findRequest.getFactor();
        List<String> tags = findRequest.getTags();
        factor = "%"+factor+"%";

        List<BookRespond> findRespond = findService.queryBookByFactor(factor);
        List<BookRespond> res = new ArrayList<>();

//        System.out.println(tags);
//        System.out.println(findRespond);

        if(tags.isEmpty()){
            return Result.succ(findRespond);
        }
//        System.out.println(findRespond);

        for(BookRespond bookRespond : findRespond){
            for(String tag : tags){
                boolean kk = false;
                for(String tagRes : bookRespond.getTags()){
//                    System.out.println(tagRes + " " + tag);
                    if(tagRes == null){
                        continue;
                    }
                    try {
                        if(tagRes.equals(tag)){
                            kk = true;
                            res.add(bookRespond);
                            break;
                        }
                    }
                    catch (Exception e){
                        System.out.println(e);
                        System.out.println(tagRes + " " + tag);
                    }

                }
                if(kk){
                    break;
                }
            }
        }
//        System.out.println(res);
        return Result.succ(res);
    }

    @GetMapping("/findbyid")
    @RequiresAuthentication
    public Result getBookByUser(HttpServletRequest httpServletRequest){
        String jwt = httpServletRequest.getHeader("Authorization");
        String userId = jwtUtils.getClaimByToken(jwt).getSubject();
        List<BookRespond> res = findService.queryBookByUserId(Long.valueOf(userId));
        return Result.succ(res);
    }

}
