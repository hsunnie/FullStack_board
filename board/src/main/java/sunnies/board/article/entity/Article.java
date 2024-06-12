package sunnies.board.article.entity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sunnies.board.article.dto.response.ArticleResponseDto;
import sunnies.board.comment.entity.Comment;
import sunnies.board.user.entity.LocalUser;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Article {

    @Id
    // Id에 primary key 설정 // IDENTITY : 1부터 1씩 증가
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 128)
    private String subject;

    @Column(columnDefinition = "TEXT")
    private String content;

    // 한 명의 작성자가 여러개의 게시글 쓸 수 있도록 @ManyToOne 사용
    @ManyToOne
    private LocalUser writer;

    @OneToMany(mappedBy = "article", cascade = CascadeType.REMOVE)
    private List<Comment> comments;

    // 좋아요 구현을 위한 설정
    @ManyToMany
    private Set<LocalUser> likes;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ArticleResponseDto toArticleResponseDto() {
        return ArticleResponseDto.builder()
                .id(id)
                .subject(subject)
                .content(content)
                .nickname(writer != null ? writer.getNickname() : "")
                .createdAt(createdAt)
                .build();
    }
}