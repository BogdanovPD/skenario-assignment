package com.skenario.assignment.repository;

import com.skenario.assignment.model.Building;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface BuildingRepository extends ReactiveMongoRepository<Building, String> {

    Flux<Building> findAllByIdNotNull(Pageable page);
}
