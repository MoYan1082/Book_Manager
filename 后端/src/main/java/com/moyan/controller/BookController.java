package com.moyan.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.moyan.common.Result;
import com.moyan.common.dto.BookDel;
import com.moyan.common.dto.BookRespond;
import com.moyan.entity.Book;
import com.moyan.entity.History;
import com.moyan.entity.Tag;
import com.moyan.common.dto.BookToUser;
import com.moyan.common.dto.BookRequest;
import com.moyan.entity.User;
import com.moyan.service.*;
import com.moyan.util.BookUtil;
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
@RequestMapping("/book")
@RequiresAuthentication
public class BookController {
    @Autowired
    BookService bookService;

    @Autowired
    FindService findService;

    @Autowired
    TagService tagService;

    @Autowired
    HistoryService historyService;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    UserService userService;

    @PostMapping("/del")
    public Result bookDel(@RequestBody BookDel bookDel, HttpServletRequest httpServletRequest){
        // =====================================
        // 判断是否为管理员
        String jwt = httpServletRequest.getHeader("Authorization");
        Long userId = Long.valueOf(jwtUtils.getClaimByToken(jwt).getSubject());
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<User>()
                .eq("user_id", userId);
        List<User> users = userService.list(userQueryWrapper);
        if(!users.get(0).getAdmine()){
            return Result.fail("当前用户没有权限！");
        }
        // =====================================

        QueryWrapper<Book> bookQueryWrapper = new QueryWrapper<Book>()
                .eq("book_id", bookDel.getBookId());
        List<Book> res = bookService.list(bookQueryWrapper);
        if(res.isEmpty()){
            return Result.fail("没有找到当前书籍!");
        }
        if(res.get(0).getStatus()){
            return Result.fail("当前书籍已经被借走了！");
        }
        bookService.remove(bookQueryWrapper);

        // 删除旧的标签
        QueryWrapper<Tag> tagQueryWrapper = new QueryWrapper<Tag>()
                .eq("book_id", bookDel.getBookId());
        tagService.remove(tagQueryWrapper);
        return Result.succ(200);
    }

    @PostMapping("/add")
    public Result bookAdd(@RequestBody BookRequest bookRequest, HttpServletRequest httpServletRequest){
        // =====================================
        // 判断是否为管理员
        String jwt = httpServletRequest.getHeader("Authorization");
        Long userId = Long.valueOf(jwtUtils.getClaimByToken(jwt).getSubject());
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<User>()
                .eq("user_id", userId);
        List<User> users = userService.list(userQueryWrapper);
        if(!users.get(0).getAdmine()){
            return Result.fail("当前用户没有权限！");
        }
        // =====================================

        QueryWrapper<Book> bookQueryWrapper = new QueryWrapper<Book>()
                .eq("title", bookRequest.getTitle());
        List<Book> bookList = bookService.list(bookQueryWrapper);
        if(!bookList.isEmpty())
            return Result.fail("已经存在当前图书!");
        // 存放图书
        bookService.save(BookUtil.BookRequestToBook(bookRequest));

        // 存放tag
        bookList = bookService.list(bookQueryWrapper);
        for(Tag tag : BookUtil.BookRequestToTag(bookRequest, bookList.get(0).getBookId())){
            tagService.save(tag);
        }
        return Result.succ(200);
    }

    @PostMapping("/update")
    public Result bookUpdate(@RequestBody BookRequest bookRequest, HttpServletRequest httpServletRequest){
        // =====================================
        // 判断是否为管理员
        String jwt = httpServletRequest.getHeader("Authorization");
        Long userId = Long.valueOf(jwtUtils.getClaimByToken(jwt).getSubject());
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<User>()
                .eq("user_id", userId);
        List<User> users = userService.list(userQueryWrapper);
        if(!users.get(0).getAdmine()){
            return Result.fail("当前用户没有权限！");
        }
        // =====================================

        QueryWrapper<Book> bookQueryWrapper = new QueryWrapper<Book>()
                .eq("book_id", bookRequest.getBookId());
        List<Book> res = bookService.list(bookQueryWrapper);
        if(res.isEmpty()){
            return Result.fail("没有找到当前书籍!");
        }
        Book target = BookUtil.BookRequestToBook(bookRequest);
        target.setBookId(res.get(0).getBookId());

        // 更新图书
        bookService.update(target, bookQueryWrapper);

        // 删除旧的tag
        QueryWrapper<Tag> tagQueryWrapper = new QueryWrapper<Tag>().eq("book_id", res.get(0).getBookId());
        tagService.remove(tagQueryWrapper);

        // 增加新的tag
        List<Tag> tags = BookUtil.BookRequestToTag(bookRequest, res.get(0).getBookId());
        for (Tag tag : tags){
            tagService.save(tag);
        }

        return Result.succ(200);
    }

    @GetMapping("/all")
    public Result bookAll(){
        List<BookRespond> res = findService.queryBookByFactor("%%");
        return Result.succ(res);
    }

    @PostMapping("/borrow")
    @RequiresAuthentication
    public Result bookBorrow(@RequestBody BookToUser bookToUser, HttpServletRequest httpServletRequest){
        String jwt = httpServletRequest.getHeader("Authorization");
        Long userId = Long.valueOf(jwtUtils.getClaimByToken(jwt).getSubject());

        QueryWrapper<Book> bookQueryWrapper = new QueryWrapper<Book>()
                .eq("book_id", bookToUser.getBookId());
        List<Book> books = bookService.list(bookQueryWrapper);
        if(books.isEmpty())
            return Result.fail("不存在当前图书!");
        if(books.get(0).getStatus())
            return Result.fail("当前图书正在被借用!");

        books.get(0).setStatus(true);
        books.get(0).setUserId(userId);
        books.get(0).setDeadline(null);
        bookService.update(books.get(0), bookQueryWrapper);

        historyService.save(new History()
                    .setBookId(bookToUser.getBookId())
                    .setUserId(userId));

        return Result.succ("借书操作成功！");
    }


    @PostMapping("/return")
    @RequiresAuthentication
    public Result bookReturn(@RequestBody BookToUser bookToUser, HttpServletRequest httpServletRequest){
        // 从jwt中获取user_id
        String jwt = httpServletRequest.getHeader("Authorization");
        Long userId = Long.valueOf(jwtUtils.getClaimByToken(jwt).getSubject());

        QueryWrapper<Book> bookQueryWrapper = new QueryWrapper<Book>()
                .eq("book_id", bookToUser.getBookId());
        List<Book> books = bookService.list(bookQueryWrapper);
        if(books.isEmpty())
            return Result.fail("不存在当前图书!");
        if(!books.get(0).getStatus())
            return Result.fail("当前图书没有被借用!");

        if(!books.get(0).getUserId().equals(userId))
            return Result.fail("当前图书不是被该用户借走的!");
        books.get(0).setStatus(false);
        books.get(0).setUserId(null);
        books.get(0).setDeadline(null);
        bookService.update(books.get(0), bookQueryWrapper);

        return Result.succ("还书操作成功！");
    }
}