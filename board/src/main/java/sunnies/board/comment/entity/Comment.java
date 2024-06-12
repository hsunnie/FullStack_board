package sunnies.board.comment.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import sunnies.board.article.entity.Article;
import sunnies.board.user.entity.LocalUser;

@Getter
@Setter
@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 200)
    private String content;

    @ManyToOne
    private Article article;

    @ManyToOne
    private LocalUser writer;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}