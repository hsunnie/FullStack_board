package sunnies.board.article.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ArticleResponseDto {
    private int id;
    private String subject;
    private String content;
    private String nickname;
    private LocalDateTime createdAt;
}
