package com.spring.sw_planet_api.domain;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
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

  public Optional<Planet> get(Long id) {
    return planetRepository.findById(id);
  }

  public Optional<Planet> getByName(String name) {
    return planetRepository.findByName(name);
  }

  public List<Planet> list(String terrain, String climate) {
    Planet planet = new Planet();
    planet.setTerrain(terrain);
    planet.setClimate(climate);

    ExampleMatcher matcher = ExampleMatcher.matchingAll()
        .withIgnoreCase()
        .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);

    Example<Planet> query = Example.of(planet, matcher);

    return planetRepository.findAll(query);
  }
}