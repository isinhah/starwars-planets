package com.spring.sw_planet_api.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static com.spring.sw_planet_api.common.PlanetConstants.PLANET;
import static com.spring.sw_planet_api.common.PlanetConstants.INVALID_PLANET;

@DataJpaTest // h2 database
public class PlanetRepositoryTest {

  @Autowired
  private PlanetRepository planetRepository;

  @Autowired
  private TestEntityManager testEntityManager;

  @Test
  public void createPlanet_WithValidData_ReturnsPlanet() {
    Planet planet = planetRepository.save(PLANET);

    Planet sut = testEntityManager.find(Planet.class, planet.getId());

    // validação de cada propriedade
    assertThat(sut).isNotNull();
    assertThat(sut.getName()).isEqualTo(PLANET.getName());
    assertThat(sut.getClimate()).isEqualTo(PLANET.getClimate());
    assertThat(sut.getTerrain()).isEqualTo(PLANET.getTerrain());
  }

  @Test
  public void createPlanet_WithInvalidData_ThrowsException() {
    Planet emptyPlanet = new Planet();

    assertThatThrownBy(() -> planetRepository.save(emptyPlanet)).isInstanceOf(RuntimeException.class);
    assertThatThrownBy(() -> planetRepository.save(INVALID_PLANET)).isInstanceOf(RuntimeException.class);
  }
}