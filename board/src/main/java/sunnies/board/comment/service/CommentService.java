package sunnies.board.comment.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import sunnies.board.article.entity.Article;
import sunnies.board.comment.entity.Comment;
import sunnies.board.comment.repository.CommentRepository;
import sunnies.board.exception.DataNotFoundException;
import sunnies.board.user.entity.LocalUser;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;

    public Comment getComment(Long id) {
        Optional<Comment> oc = commentRepository.findById(id);
        if (oc.isPresent()) {
            return oc.get();
        } else {
            throw new DataNotFoundException("comment not found");
        }
    }

    public void createComment(String content, LocalUser writer, Article article) {
        Comment c = new Comment();
        c.setContent(content);
        c.setArticle(article);
        c.setWriter(writer);
        c.setCreatedAt(LocalDateTime.now());
        commentRepository.save(c);
    }

    public void updateComment(Comment c, String content) {
        c.setContent(content);
        c.setUpdatedAt(LocalDateTime.now());
        commentRepository.save(c);
    }

    public void deleteComment(Comment c) {
        commentRepository.delete(c);
    }
}