package sunnies.board.article.controller;

import java.security.Principal;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import sunnies.board.article.entity.Article;
import sunnies.board.article.form.ArticleForm;
import sunnies.board.article.service.ArticleService;
import sunnies.board.comment.form.CommentForm;
import sunnies.board.user.entity.LocalUser;
import sunnies.board.user.service.UserService;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;


@Controller
@RequiredArgsConstructor
@RequestMapping("/article")
public class ArticleController {

    private final ArticleService articleService;
    private final UserService userService;

    @GetMapping("/list")
    public String article_list(Model model, @RequestParam(name = "page", defaultValue = "1") int page, Principal principal) {
        Page<Article> paginator = articleService.getList(page-1);
        model.addAttribute("paginator", paginator);
        // th:each="a : ${articles}"
            // th:text="${a.subject}"
        if (principal != null) {
            LocalUser user = userService.getUser(principal.getName());
            model.addAttribute("user", user);
        }
        return "article/list";
    }
    
    @GetMapping("/detail/{id}")
    public String article_detail(Model model, @PathVariable(name="id") int id, CommentForm commentForm, Principal principal) {
        
        Article article = articleService.getDetail(id);
        model.addAttribute("article", article);
        if (principal != null) {
            LocalUser user = userService.getUser(principal.getName());
            Boolean isLiked = article.getLikes().contains(user);
            model.addAttribute("isLiked", isLiked);
        } else {
            model.addAttribute("isLiked", false);
        }
        return "article/detail";
    }
    
    // 생성 페이지 보여주는 역할
    @GetMapping("/create")
    @PreAuthorize("isAuthenticated()")
    public String form(ArticleForm articleForm) {
        // System.out.println(principal.getName()); // username 확인
        return "article/form";
    }

    // DB에 데이터 적재
    @PostMapping("/create")
    @PreAuthorize("isAuthenticated()")
    public String create(@Valid ArticleForm articleForm, BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "article/form"; // 오류 확인 후, 재작성할 수 있도록 article_form을 반환해줌
        }
        LocalUser writer = userService.getUser(principal.getName());
        articleService.createArticle(articleForm.getSubject(), articleForm.getContent(), writer); // createArticle에 의해 DB에 INSERT됨
        return "redirect:/article/list";
    }

    @GetMapping("/modify/{id}")
    @PreAuthorize("isAuthenticated()")
    public String modify(ArticleForm articleForm, @PathVariable("id") int id, Principal principal) {
        Article article = articleService.getDetail(id);
        if(!article.getWriter().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
        }
        articleForm.setSubject(article.getSubject());
        articleForm.setContent(article.getContent());
        return "article/form";
    }
    
    @PostMapping("/modify/{id}")
    @PreAuthorize("isAuthenticated()")
    public String update(@Valid ArticleForm articleForm, @PathVariable("id") int id, BindingResult bindingResult, Principal principal) {
        
        Article article = articleService.getDetail(id);
        if(!article.getWriter().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
        }
        
        if (bindingResult.hasErrors()) {
            return "article/form"; // 오류 확인 후, 재작성할 수 있도록 article_form을 반환해줌
        }
        
        articleService.updateArticle(article, articleForm.getSubject(), articleForm.getContent()); // createArticle에 의해 DB에 INSERT됨
        return "redirect:/article/detail/"+id;
    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    public String delete(@PathVariable("id") int id, Principal principal) {
        Article article = articleService.getDetail(id);
        if(!article.getWriter().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제 권한이 없습니다.");
        }
        articleService.deleteArticle(article);
        return "redirect:/";
    }

    @GetMapping("/like/{id}")
    @PreAuthorize("isAuthenticated()")
    public String like(@PathVariable("id") int id, Principal principal) {
        Article article = articleService.getDetail(id);
        LocalUser user = userService.getUser(principal.getName());
        this.articleService.likeArticle(article, user);
        return "redirect:/article/detail/"+id;
    }
    

}
