package com.skenario.assignment.service;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public interface BuildingHandler {

    Mono<ServerResponse> createBuilding(ServerRequest request);

    Mono<ServerResponse> updateBuilding(ServerRequest request);

    Mono<ServerResponse> findAllBuildings(ServerRequest request);

    Mono<ServerResponse> findBuildingsByFilter(ServerRequest request);

    Mono<ServerResponse> findAllBuildingsStream(ServerRequest request);

}
