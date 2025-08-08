package com.roadmap.personalblog.repo;

import com.roadmap.personalblog.model.Article;
import java.util.List;
import java.util.Optional;

public interface ArticleRepository {
    List<Article> findAll();
    Optional<Article> findById(String id);
    Article save(Article article);       // crear
    Article update(String id, Article article);
    void delete(String id);
}
