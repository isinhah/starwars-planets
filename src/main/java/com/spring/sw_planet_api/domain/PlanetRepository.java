package com.spring.sw_planet_api.domain;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PlanetRepository extends JpaRepository<Planet, Long> {
  Optional<Planet> findByName(String name);
}
