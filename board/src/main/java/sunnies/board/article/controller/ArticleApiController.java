package sunnies.board.article.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import lombok.RequiredArgsConstructor;
import sunnies.board.article.dto.request.ArticleRequestDto;
import sunnies.board.article.dto.response.ArticleListResponseDto;
import sunnies.board.article.dto.response.ArticleResponseDto;
import sunnies.board.article.entity.Article;
import sunnies.board.article.service.ArticleService;
import sunnies.board.user.entity.LocalUser;
import sunnies.board.user.service.UserService;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/v1/article")
@RequiredArgsConstructor
public class ArticleApiController {

    private final UserService userService;
    private final ArticleService articleService;

    @GetMapping("/")
    public ArticleListResponseDto getArticles(@RequestParam(name = "page", defaultValue = "1") int page) {
        Page<Article> paginator = articleService.getList(page - 1);
        return ArticleListResponseDto.toArticleListResponseDto(paginator);
    }

    @GetMapping("/{id}")
    public ArticleResponseDto getArticle(@PathVariable(name = "id") int id) {
        return articleService.getDetail(id).toArticleResponseDto();
    }

    @PostMapping("/")
    public ArticleResponseDto createArticle(@RequestBody ArticleRequestDto articleRequestDto) {
        LocalUser writer = userService.getUser(articleRequestDto.getUserId());
        // Article article = articleService.createArticle(articleRequestDto, writer);
        return articleService.createArticle(articleRequestDto, writer);
    }

    @PutMapping("/{id}")
    public ArticleResponseDto updateArticle(@PathVariable(name = "id") int id,
            @RequestBody ArticleRequestDto articleRequestDto) {
        // 원래는 access token 또는 인증 정보를 통해서 userId 확인
        Article article = articleService.getDetail(id);
        if (article.getWriter().getId() != articleRequestDto.getUserId()) {
            // Unauthorized error
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "수정 권한이 없습니다.");
        }
        articleService.updateArticle(article, articleRequestDto.getSubject(), articleRequestDto.getContent());
        return article.toArticleResponseDto();
    }

    @DeleteMapping("/{id}")
    public String deleteArticle(@PathVariable(name = "id") int id) {
        // 원래는 인증정보체크 후, // 일치 여부 체크해서 로직 진행하지만, 이번엔 체크하지 않고, 삭제하는 로직 작동하는지만 테스트
        Article article = articleService.getDetail(id);
        articleService.deleteArticle(article);
        return "{\"result\" : \"ok\"}";
    }

    @PostMapping("/{id}/like")
    @PreAuthorize("isAuthenticated()")
    public String likeArticle(@PathVariable(name = "id") int id, Principal principal) {
        Article article = articleService.getDetail(id);
        LocalUser user = userService.getUser(principal.getName());
        articleService.likeArticle(article, user);
        return "{\"result\" : \"ok\"}";
    }

}
