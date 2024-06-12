package sunnies.board.article.form;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArticleForm {
    
    @NotEmpty(message = "제목 없음") // message : 비어있는 경우, 넣어줄 내용
    @Size(max = 128)
    private String subject;
    
    @NotEmpty(message = "내용 없음")
    private String content;
}