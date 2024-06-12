package sunnies.board.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import sunnies.board.comment.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {}
