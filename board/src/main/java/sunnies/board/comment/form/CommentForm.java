package sunnies.board.comment.form;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentForm {
    @NotEmpty(message = "댓글을 입력하세요")
    @Size(max = 200)
    private String content;
}