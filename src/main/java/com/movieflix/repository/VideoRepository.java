package com.movieflix.repository;

import com.movieflix.domain.Video;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VideoRepository extends MongoRepository<Video, String>, QuerydslPredicateExecutor<Video> {
    List<Video> findByCategoryId(String categoryId);
}
