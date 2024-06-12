package sunnies.board.article.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import sunnies.board.article.dto.request.ArticleRequestDto;
import sunnies.board.article.dto.response.ArticleResponseDto;
import sunnies.board.article.entity.Article;
import sunnies.board.article.repository.ArticleRepository;
import sunnies.board.exception.DataNotFoundException;
import sunnies.board.user.entity.LocalUser;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;

    public Page<Article> getList(int page) {
        // 최신 게시물 순서로 정렬
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createdAt"));

        // 페이지 구현
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        return articleRepository.findAll(pageable);
    }

    public Article getDetail(int id) {
        Optional<Article> oa = articleRepository.findById(id);
        if (oa.isPresent()) {
            Article a = oa.get();
            return a;
        } else {
            throw new DataNotFoundException("article not found");// error를 던진다??
        }
    }

    public Article createArticle(String subject, String content, LocalUser writer) {
        Article article = new Article();
        article.setSubject(subject);
        article.setContent(content);
        article.setWriter(writer);
        article.setCreatedAt(LocalDateTime.now());
        this.articleRepository.save(article);
        return article;
    }

    public ArticleResponseDto createArticle(ArticleRequestDto articleRequestDto, LocalUser writer) {
        Article article = articleRequestDto.toEntity(writer);
        articleRepository.save(article);
        return article.toArticleResponseDto();
    }

    // article : (DB에 저장되어있는) 기존 게시글
    // subject, contetn : articleForm을 통해 새로 수정된 내용
    public Article updateArticle(Article article, String subject, String content) {
        article.setSubject(subject);
        article.setContent(content);
        article.setUpdatedAt(LocalDateTime.now());
        this.articleRepository.save(article);
        return article;
    }

    public void deleteArticle(Article article) {
        articleRepository.delete(article);
    }

    public void likeArticle(Article article, LocalUser user) {
        Set<LocalUser> likes = article.getLikes();
        if (likes.contains(user)) { // 유저가 좋아요 목록에 있는지 확인
            likes.remove(user);
        } else {
            likes.add(user);
        }
        this.articleRepository.save(article);
    }
}