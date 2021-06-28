package com.skenario.assignment.service;

import com.skenario.assignment.model.Building;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class GeoApifyService implements GeoService {

    private final WebClient geoApiClient;
    @Value("${api-key}")
    private String apiKey;

    @Override
    public Mono<Building> checkIfAddressExists(Mono<Building> data) {
        return data.flatMap(building ->
                geoApiClient.get()
                        .uri(UriComponentsBuilder.fromUriString("/search")
                                .queryParam("housenumber", building.getNumber())
                                .queryParam("street", building.getStreet())
                                .queryParam("city", building.getCity())
                                .queryParam("country", building.getCountry())
                                .queryParam("apiKey", apiKey)
                                .toUriString())
                        .retrieve()
                        .bodyToMono(String.class)
                        .flatMap(resp -> {
                            if (resp.contains("\"match_type\":\"full_match\"")) {
                                return Mono.just(building);
                            }
                            return Mono.error(new Exception("Cannot find geoinformation"));
                        }))
                .switchIfEmpty(Mono.error(new Exception("Retrieved data is empty")));
    }
}
