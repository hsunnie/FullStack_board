package sunnies.board.comment.controller;

import java.security.Principal;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sunnies.board.article.entity.Article;
import sunnies.board.article.service.ArticleService;
import sunnies.board.comment.entity.Comment;
import sunnies.board.comment.form.CommentForm;
import sunnies.board.comment.service.CommentService;
import sunnies.board.exception.DataNotFoundException;
import sunnies.board.user.entity.LocalUser;
import sunnies.board.user.service.UserService;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.server.ResponseStatusException;


@Controller
@RequiredArgsConstructor
@RequestMapping("/comment")
@Slf4j
public class CommentContoller {
    private final CommentService commentService;
    private final UserService userService;
    private final ArticleService articleService;

    @PostMapping("/create/{articleId}")
    @PreAuthorize("isAuthenticated()") // 1. 로그인이 되었는지 검증(@PreAuthorize)
    public String createComment(@PathVariable(name="articleId") int aid, @Valid CommentForm commentForm, BindingResult bindingResult, Model model, Principal principal) {
        // 2. 게시글이 존재하는지 검증
        Article article = null;
        try {
            article = articleService.getDetail(aid);
        } catch (DataNotFoundException e) {
            return "redirect:/";
        } catch (Exception e) {
            log.error(">>> 댓글 작성 : 게시글 GET 에러", e);
            return "redirect:/";
        }

        // 3. 댓글 내용이 있는지 검증 - form에 대한 validation check
        if (bindingResult.hasErrors()) {
            model.addAttribute("article", article);
            return "article/detail";
        }
        // 4. 댓글 적용
        LocalUser writer = userService.getUser(principal.getName());
        commentService.createComment(commentForm.getContent(), writer, article);

        return "redirect:/article/detail/"+aid;
    }

    // 수정 -----------------------------------------------------------------------------------------------------------------
    @GetMapping("/modify/{id}")
    @PreAuthorize("isAuthenticated()")
    public String modify(@PathVariable(name = "id") Long id, CommentForm commentForm, Principal principal) {
        //게시글 체크
        Comment comment = commentService.getComment(id);
        
        //작성자 체크
        if (!comment.getWriter().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
        }
        commentForm.setContent(comment.getContent());
        return "comment/modify";
    }
    
    @PostMapping("/modify/{id}")
    public String postMethodName(@PathVariable(name = "id") Long id, @Valid CommentForm commentForm, BindingResult bindingResult, Principal principal) {
        Comment comment = commentService.getComment(id);
        if (!comment.getWriter().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
        }
        if (bindingResult.hasErrors()) {
            return "comment/modify";
        }
        commentService.updateComment(comment, commentForm.getContent());
        return "redirect:/article/detail/"+comment.getArticle().getId();
    }
    

    // 삭제 ------------------------------------------------------------------------------------------------------------------
    @GetMapping("/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    public String delete(@PathVariable(name = "id") Long id, Principal principal) {
        Comment comment = commentService.getComment(id);
        if (!comment.getWriter().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제 권한이 없습니다.");
        }
        commentService.deleteComment(comment);
        return "redirect:/article/detail/"+comment.getArticle().getId();
    }

}
