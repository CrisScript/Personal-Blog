package com.roadmap.personalblog.api;

import com.roadmap.personalblog.model.Article;
import com.roadmap.personalblog.repo.ArticleRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/articles")
public class ArticleApiController {
    private final ArticleRepository repo;

    public ArticleApiController(ArticleRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Article> all() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Article> byId(@PathVariable String id) {
        return repo.findById(id).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Article> create(@RequestBody Article a) {
        Article saved = repo.save(a);
        return ResponseEntity.created(URI.create("/api/articles/" + saved.getId())).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Article> update(@PathVariable String id, @RequestBody Article a) {
        return ResponseEntity.ok(repo.update(id, a));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        repo.delete(id);
        return ResponseEntity.noContent().build();
    }
}
