package com.skenario.assignment.configuration;

import com.skenario.assignment.repository.BuildingRepository;
import com.skenario.assignment.repository.BuildingStreamingRepository;
import com.skenario.assignment.service.BuildingHandler;
import com.skenario.assignment.service.BuildingHandlerDefault;
import com.skenario.assignment.service.GeoService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class BuildingRouter {

    @Bean
    public BuildingHandler buildingHandler(BuildingRepository buildingRepository,
                                           BuildingStreamingRepository buildingStreamingRepository,
                                           ReactiveMongoTemplate mongoTemplate,
                                           GeoService geoService) {
        return new BuildingHandlerDefault(buildingRepository, buildingStreamingRepository, mongoTemplate, geoService);
    }

    @Bean
    public RouterFunction<ServerResponse> router(BuildingHandler buildingHandler) {
        return RouterFunctions
                .route(GET("/building/all").and(accept(MediaType.APPLICATION_JSON)), buildingHandler::findAllBuildings)
                .andRoute(GET("/building/filter").and(accept(MediaType.APPLICATION_JSON)), buildingHandler::findBuildingsByFilter)
                .andRoute(GET("/building/all/stream").and(accept(MediaType.TEXT_EVENT_STREAM)), buildingHandler::findAllBuildingsStream)
                .andRoute(POST("/building").and(contentType(MediaType.APPLICATION_JSON)), buildingHandler::createBuilding)
                .andRoute(PATCH("/building/{id}").and(contentType(MediaType.APPLICATION_JSON)), buildingHandler::updateBuilding);
    }

}
