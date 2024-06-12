import React, { useEffect, useState } from "react";
import "./App.css";

import axios from "axios";

function App() {
  const [articles, setArticles] = useState([]);
  const [article, setArticle] = useState({});
  const [page, setPage] = useState(1);
  useEffect(() => {
    // 페이지에 따른 게시글 목록 가져오기
    axios({
      url: `http://127.0.0.1:8080/api/v1/article/?page=${page}`,
    }).then((res) => setArticles(res.data.articles));
  }, [page]);
  const getArticle = (id) => {
    axios({
      url: `http://127.0.0.1:8080/api/v1/article/${id}`,
    }).then((res) => setArticle(res.data));
  };
  return (
    <div className="App">
      <input
        type="text"
        placeholder={page}
        onKeyDown={(e) => e.key === "Enter" && setPage(e.target.value * 1)}
      />
      <hr />
      {article.id ? ( // article이 선택되지 않아도 빈 리스트가 반환되므로, 이는 true 값을 갖는다. 따라서, id를 검사하면 빈 리스트의 경우 id가 없으므로 false가 반환된다.
        <div style={{ border: "1px solid red" }}>
          <div>{article.subject}</div>
          <div>{article.content}</div>
          <div>{article.nickname}</div>
        </div>
      ) : (
        "☆★게시글 선택하기★☆"
      )}
      <hr />
      {articles.map(({ id, subject }) => {
        return (
          <div key={`article-${id}`} onClick={() => getArticle(id)}>
            {subject}
          </div>
        );
      })}
    </div>
  );
}

export default App;
