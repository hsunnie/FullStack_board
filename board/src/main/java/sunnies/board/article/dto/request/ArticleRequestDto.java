package sunnies.board.article.dto.request;

import java.time.LocalDateTime;

import lombok.Data;
import sunnies.board.article.entity.Article;
import sunnies.board.user.entity.LocalUser;

@Data
public class ArticleRequestDto {
    private String subject;
    private String content;
    private Integer userId;

    public Article toEntity(LocalUser user) {
        return Article.builder()
                .subject(subject)
                .content(content)
                .createdAt(LocalDateTime.now())
                .writer(user)
                .build();
    }
}
