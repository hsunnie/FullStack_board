package sunnies.board.article.dto.response;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;

import lombok.Builder;
import lombok.Getter;
import sunnies.board.article.entity.Article;

@Builder
@Getter
public class ArticleListResponseDto {
    private List<ArticleResponseDto> articles;
    private int maxPage;
    private int pageSize;
    private int page;

    public static ArticleListResponseDto toArticleListResponseDto(Page<Article> paginator) {
        return ArticleListResponseDto.builder()
                .maxPage(paginator.getTotalPages())
                .pageSize(paginator.getSize())
                .page(paginator.getNumber())
                .articles(paginator.getContent().stream() // List<Article>
                        .map(article -> article.toArticleResponseDto()) // List<ArticleResponseDto>
                        .collect(Collectors.toList()))
                .build();
    }
}
