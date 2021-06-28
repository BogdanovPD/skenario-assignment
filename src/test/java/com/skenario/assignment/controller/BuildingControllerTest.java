package com.skenario.assignment.controller;

import com.skenario.assignment.configuration.BuildingRouter;
import com.skenario.assignment.model.Building;
import com.skenario.assignment.repository.BuildingRepository;
import com.skenario.assignment.repository.BuildingStreamingRepository;
import com.skenario.assignment.service.GeoService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@ExtendWith({ SpringExtension.class, MockitoExtension.class })
@ContextConfiguration(classes = {BuildingRouter.class})
@WebFluxTest
public class BuildingControllerTest {

    @MockBean
    private BuildingRepository buildingRepository;
    @MockBean
    private BuildingStreamingRepository buildingStreamingRepository;
    @MockBean
    private ReactiveMongoTemplate mongoTemplate;
    @MockBean
    private GeoService geoService;

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void testCreateBuilding() {
        String id = "1";
        String city = "SPb";
        String street = "Test st.";

        Building building = Building.builder().id(id).city(city).street(street).build();
        Mono<Building> buildingMono = Mono.just(building);

        doReturn(buildingMono).when(geoService).checkIfAddressExists(any());
        doReturn(buildingMono).when(buildingRepository).insert(building);

        webTestClient.post()
                .uri("/building")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(buildingMono, Building.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Building.class)
        .value(resp -> {
            assertThat(resp.getId(), is(id));
            assertThat(resp.getCity(), is(city));
            assertThat(resp.getStreet(), is(street));
        });
    }
}
