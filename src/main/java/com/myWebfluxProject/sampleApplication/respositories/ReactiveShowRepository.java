package com.myWebfluxProject.sampleApplication.respositories;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.myWebfluxProject.sampleApplication.domain.Show;

import reactor.core.publisher.Flux;

@Repository
public interface ReactiveShowRepository extends ReactiveMongoRepository<Show, String> {
	@Query("{ 'title': ?0 }")
	Flux<Show> findByTitle(String title);
}
