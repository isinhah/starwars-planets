package com.spring.sw_planet_api.domain;

import org.springframework.stereotype.Service;

@Service
public class PlanetService {

  private PlanetRepository planetRepository;

  public PlanetService(PlanetRepository planetRepository) {
    this.planetRepository = planetRepository;
  }

  public Planet create(Planet planet) {
    return planetRepository.save(planet);
  }
}
