package com.skenario.assignment.repository;

import com.skenario.assignment.model.Building;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.Tailable;
import reactor.core.publisher.Flux;

public interface BuildingStreamingRepository extends ReactiveMongoRepository<Building, String> {

    @Tailable
    Flux<Building> findAllByIdNotNull(Pageable page);
}
