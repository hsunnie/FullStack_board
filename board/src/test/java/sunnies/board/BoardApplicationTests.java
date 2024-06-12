package sunnies.board;

// import java.time.LocalDateTime;

// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

// import sunnies.board.article.entity.Article;
// import sunnies.board.article.repository.ArticleRepository;

@SpringBootTest
class BoardApplicationTests {

	// @Autowired // ArticleRepository 객체 가져옴
	// private ArticleRepository articleRepository;

	// @Test
	// void testJpa() {
		// //select -> all
		// List<Article> articles = articleRepository.findAll();
		// assertEquals(articles.size(), 2); // 같지않으면, 에러 반환함

		// // select one
		// // Optional : Article이나 null 값
		// Optional<Article> oa = articleRepository.findById(articles.get(0).getId());
		// if (oa != null) { // (oa != null)은 oa.isPresent()와 같음
		// 	Article article = oa.get();
		// 	assertEquals(article.getSubject(), articles.get(0).getSubject());
		// }

		// for (int i = 0; i < 300; i++) {
			
		// 	Article a1 = new Article();
		// 	a1.setSubject(String.format("테스트 [%03d] 제목", i));
		// 	a1.setContent("내용부분");
		// 	a1.setCreatedAt(LocalDateTime.now());
		// 	articleRepository.save(a1); // 첫번째 게시글을 DB에 저장
		// }

		// Article a2 = new Article();
		// a2.setSubject("테스트제목 2");
		// a2.setContent("내용부분 2");
		// a2.setCreatedAt(LocalDateTime.now());
		// articleRepository.save(a2); // 두번째 게시글을 DB에 저장
	// }

}
