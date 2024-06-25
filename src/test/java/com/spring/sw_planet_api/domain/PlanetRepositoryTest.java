package com.spring.sw_planet_api.domain;

import static com.spring.sw_planet_api.common.PlanetConstants.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Example;
import org.springframework.test.context.jdbc.Sql;


import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@DataJpaTest
public class PlanetRepositoryTest {
  @Autowired
  private PlanetRepository planetRepository;

  @Autowired
  private TestEntityManager testEntityManager;

  @AfterEach
  public void afterEach() {
    PLANET.setId(null);
  }

  @Test
  public void createPlanet_WithValidData_ReturnsPlanet() {
    Planet planet = planetRepository.save(PLANET);

    Planet sut = testEntityManager.find(Planet.class, planet.getId());

    assertThat(sut).isNotNull();
    assertThat(sut.getName()).isEqualTo(PLANET.getName());
    assertThat(sut.getClimate()).isEqualTo(PLANET.getClimate());
    assertThat(sut.getTerrain()).isEqualTo(PLANET.getTerrain());
  }

  @ParameterizedTest
  @MethodSource("providesInvalidPlanets")
  public void createPlanet_WithInvalidData_ThrowsException(Planet planet) {
    assertThatThrownBy(() -> planetRepository.save(planet)).isInstanceOf(RuntimeException.class);
  }

  private static Stream<Arguments> providesInvalidPlanets() {
    return Stream.of(
            arguments(new Planet("name", null, "terrain")),
            arguments(new Planet("name", "terrain", null)),
            arguments(new Planet(null, "climate", "terrain")),
            arguments(new Planet(null, null, null)),
            arguments(new Planet("name", null, null)),
            arguments(new Planet(null, "climate", null)),
            arguments(new Planet(null, null, "terrain")),
            arguments(new Planet("", "", "")),
            arguments(new Planet("name", "climate", "")),
            arguments(new Planet("name", "", "terrain")),
            arguments(new Planet("", "climate", "terrain")),
            arguments(new Planet("name", "", "")),
            arguments(new Planet("", "climate", "")),
            arguments(new Planet("", "", "terrain"))
    );
  }

  @Test
  public void createPlanet_WithExistingName_ThrowsException() {
    Planet planet = testEntityManager.persistFlushFind(PLANET);
    testEntityManager.detach(planet);
    planet.setId(null);

    assertThatThrownBy(() -> planetRepository.save(planet)).isInstanceOf(RuntimeException.class);
  }

  @Test
  public void getPlanet_ByExistingId_ReturnsPlanet() {
    Planet planet = testEntityManager.persistFlushFind(PLANET);

    Optional<Planet> planetOpt = planetRepository.findById(planet.getId());

    assertThat(planetOpt).isNotEmpty();
    assertThat(planetOpt.get()).isEqualTo(planet);
  }

  @Test
  public void getPlanet_ByUnexistingId_ReturnsEmpty() {
    Optional<Planet> planetOpt = planetRepository.findById(1L);

    assertThat(planetOpt).isEmpty();
  }

  @Test
  public void getPlanet_ByExistingName_ReturnsPlanet() {
    Planet planet = testEntityManager.persistFlushFind(PLANET);

    Optional<Planet> planetOpt = planetRepository.findByName(planet.getName());

    assertThat(planetOpt).isNotEmpty();
    assertThat(planetOpt.get()).isEqualTo(planet);
  }

  @Test
  public void getPlanet_ByUnexistingName_ReturnsNotFound() {
    Optional<Planet> planetOpt = planetRepository.findByName("name");

    assertThat(planetOpt).isEmpty();
  }

  @Sql(scripts = "/import_planets.sql")
  @Test
  public void listPlanets_ReturnsFilteredPlanets() {
    Example<Planet> queryWithoutFilters = QueryBuilder.makeQuery(new Planet());
    Example<Planet> queryWithFilters = QueryBuilder.makeQuery(new Planet(TATOOINE.getClimate(), TATOOINE.getTerrain()));

    List<Planet> responseWithoutFilters = planetRepository.findAll(queryWithoutFilters);
    List<Planet> responseWithFilters = planetRepository.findAll(queryWithFilters);

    assertThat(responseWithoutFilters).isNotEmpty();
    assertThat(responseWithoutFilters).hasSize(3);
    assertThat(responseWithFilters).isNotEmpty();
    assertThat(responseWithFilters).hasSize(1);
    assertThat(responseWithFilters.get(0)).isEqualTo(TATOOINE);
  }

  @Test
  public void listPlanets_ReturnsNoPlanets() {
    Example<Planet> query = QueryBuilder.makeQuery(new Planet());

    List<Planet> response = planetRepository.findAll(query);

    assertThat(response).isEmpty();
  }

  @Test
  public void removePlanet_WithExistingId_RemovesPlanetFromDatabase() {
    Planet planet = testEntityManager.persistFlushFind(PLANET);

    planetRepository.deleteById(planet.getId());

    Planet removedPlanet = testEntityManager.find(Planet.class, planet.getId());
    assertThat(removedPlanet).isNull();
  }

  @Test
  @Sql(scripts = "/import_planets.sql")
  public void removePlanet_WithUnexistingId_ThrowsException() {
    planetRepository.deleteById(4L);
    assertThat(testEntityManager.find(Planet.class, 1L)).isInstanceOf(Planet.class);
    assertThat(testEntityManager.find(Planet.class, 2L)).isInstanceOf(Planet.class);
    assertThat(testEntityManager.find(Planet.class, 3L)).isInstanceOf(Planet.class);
  }
}
