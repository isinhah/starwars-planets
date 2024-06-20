package com.spring.sw_planet_api.web;

import org.springframework.web.bind.annotation.RestController;

import com.spring.sw_planet_api.domain.Planet;
import com.spring.sw_planet_api.domain.PlanetService;

import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/planets")
public class PlanetController {

  @Autowired
  private PlanetService planetService;

  @PostMapping
  public ResponseEntity<Planet> create(@RequestBody Planet planet) {
    Planet createdPlanet = planetService.create(planet);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdPlanet);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Planet> get(@PathVariable("id") Long id) {
    return planetService.get(id).map(planet -> ResponseEntity.ok(planet))
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @GetMapping("/name/{name}")
  public ResponseEntity<Planet> getByName(@PathVariable("name") String name) {
    return planetService.getByName(name).map(planet -> ResponseEntity.ok(planet))
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @GetMapping
  public ResponseEntity<List<Planet>> list(@RequestParam(required = false) String terrain,
      @RequestParam(required = false) String climate) {
    List<Planet> planets = planetService.list(terrain, climate);
    return ResponseEntity.ok(planets);
  }
}