package sunnies.board.article.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import sunnies.board.article.entity.Article;


//JpaRepository generics <T, id type>
public interface ArticleRepository extends JpaRepository<Article, Integer> {

    //SELECT * FROM ARTICLE WHERE SUBJECT={subject};
    public Article findBySubject(String subject);

    //페이지 구현
    public Page<Article> findAll(Pageable pageable);
}
