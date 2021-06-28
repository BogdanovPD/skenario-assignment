package com.skenario.assignment.service;

import com.skenario.assignment.model.Building;
import reactor.core.publisher.Mono;

public interface GeoService {

    Mono<Building> checkIfAddressExists(Mono<Building> buildingMono);

}
