package com.skenario.assignment.service;

import com.skenario.assignment.dto.ResponseDto;
import com.skenario.assignment.model.Building;
import com.skenario.assignment.repository.BuildingRepository;
import com.skenario.assignment.repository.BuildingStreamingRepository;
import lombok.RequiredArgsConstructor;
import org.reactivestreams.Publisher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class BuildingHandlerDefault implements BuildingHandler {

    private final BuildingRepository buildingRepository;
    private final BuildingStreamingRepository buildingStreamingRepository;
    private final ReactiveMongoTemplate mongoTemplate;
    private final GeoService geoService;

    @Override
    public Mono<ServerResponse> createBuilding(ServerRequest request) {
        return request.bodyToMono(Building.class)
                .transform(geoService::checkIfAddressExists)
                .flatMap(buildingRepository::insert)
                .transform(this::buildOkResponse)
                .onErrorResume(this::buildBadRequest);
    }

    @Override
    public Mono<ServerResponse> updateBuilding(ServerRequest request) {
        return request.bodyToMono(Building.class)
                .flatMap(reqB -> buildingRepository.findAllById(
                        Collections.singletonList(request.pathVariable("id")))
                        .next()
                        .switchIfEmpty(Mono.error(new Exception("Building is not found")))
                        .flatMap(bToUpdate -> { //let's assume that only these two fields are updatable to avoid 'address recheck logic'
                            if (reqB.getName() != null) {
                                bToUpdate.setName(reqB.getName());
                            }
                            if (reqB.getDescription() != null) {
                                bToUpdate.setDescription(reqB.getDescription());
                            }
                            return buildingRepository.save(bToUpdate);
                        }))
                .transform(this::buildOkResponse)
                .onErrorResume(this::buildBadRequest);
    }

    @Override
    public Mono<ServerResponse> findAllBuildings(ServerRequest request) {
        Pageable page = handlePagination(request);
        ResponseDto.ResponseDtoBuilder<Building> builder = ResponseDto
                .builder();
        Flux<Building> buildings = buildingRepository.findAllByIdNotNull(page);
        return buildOkResponse(buildings);
    }

    @Override
    public Mono<ServerResponse> findBuildingsByFilter(ServerRequest request) {
        Optional<String> queryOpt = request.queryParam("query");
        if (queryOpt.isEmpty()) {
            return findAllBuildings(request);
        }
        Pageable page = handlePagination(request);
        String queryStr = queryOpt.get();
        BasicQuery query = new BasicQuery(queryStr
                .replaceAll("\\(", "{")
                .replaceAll("\\)", "}"));
        query.with(page);
        Flux<Building> buildings = mongoTemplate.find(query, Building.class);
        return buildOkResponse(buildings);
    }

    @Override
    public Mono<ServerResponse> findAllBuildingsStream(ServerRequest request) {
        Pageable page = handlePagination(request);
        Flux<ServerSentEvent<Building>> buildings = buildingStreamingRepository.findAllByIdNotNull(page)
                .map(data ->
                        ServerSentEvent.<Building>builder()
                                .id(data.getId())
                                .data(data)
                                .event("building-found-event")
                                .build());
        return ServerResponse.ok()
                .contentType(MediaType.TEXT_EVENT_STREAM)
                .body(buildings, ResponseDto.class);
    }

    private Pageable handlePagination(ServerRequest request) {
        int size = Integer.parseInt(request.queryParam("size").orElse("10"));
        int pageNum = Integer.parseInt(request.queryParam("page").orElse("0"));
        List<Sort.Order> orders = new ArrayList<>();
        List<String> sortParams = request.queryParams().get("sort");
        if (sortParams != null) {
            sortParams.forEach(sort -> {
                        String[] sortData = sort.trim().split(",");
                        if (sortData.length == 2) {
                            String order = sortData[1].toLowerCase();
                            if ("desc".equals(order)) {
                                orders.add(Sort.Order.desc(sortData[0]));
                            } else {
                                orders.add(Sort.Order.asc(sortData[0]));
                            }
                        }
                        if (sortData.length == 1) {
                            orders.add(Sort.Order.asc(sortData[0]));
                        }
                    }
            );
        }
        PageRequest page = PageRequest.of(pageNum, size);
        if (!orders.isEmpty()) {
            page = page.withSort(Sort.by(orders));
        }
        return page;
    }

    private Mono<ServerResponse> buildBadRequest(Throwable e) {
        return ServerResponse
                .badRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(ResponseDto
                        .<String>builder()
                        .data(List.of(e.getMessage()))
                        .code(HttpStatus.BAD_REQUEST.value())
                        .build()), ResponseDto.class);
    }

    private <T> Mono<ServerResponse> buildOkResponse(T data) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(data, Building.class);
    }

}
