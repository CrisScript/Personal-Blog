package com.roadmap.personalblog.repo;

import com.roadmap.personalblog.model.Article;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.*;
import java.text.Normalizer;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class FileArticleRepository implements ArticleRepository {
    public final Path storageDir;
    public final ObjectMapper mapper;

    public FileArticleRepository(@Value("${blog.storage.path}") String storagePath) throws IOException {
        this.storageDir = Paths.get(storagePath);
        Files.createDirectories(this.storageDir);
        this.mapper = new ObjectMapper();
        this.mapper.registerModule(new JavaTimeModule());
    }

    @Override
    public List<Article> findAll() {
        try {
            if (!Files.exists(storageDir)) return List.of();
            return Files.list(storageDir)
                    .filter(p -> p.toString().endsWith(".json"))
                    .map(this::read)
                    .filter(Objects::nonNull)
                    .sorted(Comparator.comparing(Article::getPublishedAt).reversed())
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("Error listing articles", e);
        }
    }

    @Override
    public Optional<Article> findById(String id) {
        Path file = storageDir.resolve(id + ".json");
        if (!Files.exists(file)) return Optional.empty();
        return Optional.ofNullable(read(file));
    }

    @Override
    public Article save(Article article) {
        String id = (article.getId() != null && !article.getId().isBlank())
                ? article.getId()
                : slugify(article.getTitle());
        article.setId(id);
        write(storageDir.resolve(id + ".json"), article);
        return article;
    }

    @Override
    public Article update(String id, Article article) {
        article.setId(id);
        write(storageDir.resolve(id + ".json"), article);
        return article;
    }

    @Override
    public void delete(String id) {
        try {
            Files.deleteIfExists(storageDir.resolve(id + ".json"));
        } catch (IOException e) {
            throw new RuntimeException("Error deleting article " + id, e);
        }
    }

    public Article read(Path file) {
        try {
            return mapper.readValue(file.toFile(), Article.class);
        } catch (IOException e) {
            return null;
        }
    }

    public void write(Path file, Article article) {
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(file.toFile(), article);
        } catch (IOException e) {
            throw new RuntimeException("Error writing article", e);
        }
    }

    public String slugify(String input) {
        String nowhitespace = input.trim().toLowerCase(Locale.ROOT).replaceAll("\\s+", "-");
        String normalized = Normalizer.normalize(nowhitespace, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        String clean = normalized.replaceAll("[^a-z0-9-]", "");
        if (clean.isBlank()) clean = UUID.randomUUID().toString();

        String slug = clean;
        int i = 1;
        while (Files.exists(storageDir.resolve(slug + ".json"))) {
            slug = clean + "-" + i++;
        }
        return slug;
    }
}
