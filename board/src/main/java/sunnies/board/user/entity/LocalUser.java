package sunnies.board.user.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;
import sunnies.board.article.entity.Article;
import sunnies.board.comment.entity.Comment;
import sunnies.board.image.entity.ProfileImage;

// DB table 만들기
@Entity
@Getter
@Setter
public class LocalUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true)
    private String username;

    @Column
    private String password;

    @Column(unique = true)
    private String nickname;

    @OneToMany(mappedBy = "writer", cascade = CascadeType.REMOVE)
    private List<Article> articles;
    
    @OneToMany(mappedBy = "writer", cascade = CascadeType.REMOVE)
    private List<Comment> comments;

    @OneToOne
    private ProfileImage profileImage;

    private LocalDateTime createdAt;
}
