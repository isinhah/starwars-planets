package com.spring.sw_planet_api.domain;

import static com.spring.sw_planet_api.common.PlanetConstants.PLANET;
import static com.spring.sw_planet_api.common.PlanetConstants.INVALID_PLANET;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;

@ExtendWith(MockitoExtension.class)
public class PlanetServiceTest {

  @InjectMocks
  private PlanetService planetService;

  @Mock
  private PlanetRepository planetRepository;

  @Test
  public void createPlanet_WithValidData_ReturnsPlanet() {
    when(planetRepository.save(PLANET)).thenReturn(PLANET);

    Planet sut = planetService.create(PLANET);

    assertThat(sut).isEqualTo(PLANET);
  }

  @Test
  public void createPlanet_WithInvalidData_ThrowsException() {
    when(planetRepository.save(INVALID_PLANET)).thenThrow(RuntimeException.class);

    assertThatThrownBy(() -> planetService.create(INVALID_PLANET)).isInstanceOf(RuntimeException.class);
  }

  @Test
  public void getPlanet_ByExistingId_ReturnsPlanet() {
    when(planetRepository.findById(anyLong())).thenReturn(Optional.of(PLANET));

    Optional<Planet> sut = planetService.get(1L);

    assertThat(sut).isNotEmpty();
    assertThat(sut.get()).isEqualTo(PLANET);
  }

  @Test
  public void getPlanet_ByUnexistingId_ReturnsEmpty() {
    when(planetRepository.findById(1L)).thenReturn(Optional.empty());

    Optional<Planet> sut = planetService.get(1L);

    assertThat(sut).isEmpty();
  }

  @Test
  public void getPlanet_ByExistingName_ReturnsPlanet() {
    when(planetRepository.findByName(PLANET.getName())).thenReturn(Optional.of(PLANET));

    Optional<Planet> sut = planetService.getByName(PLANET.getName());

    assertThat(sut.get()).isEqualTo(PLANET);
  }

  @Test
  public void getPlanet_ByUnexistingName_ReturnsEmpty() {
    when(planetRepository.findByName(PLANET.getName())).thenReturn(Optional.empty());

    Optional<Planet> sut = planetService.getByName(PLANET.getName());

    assertThat(sut).isEmpty();
  }

  @Test
  public void listPlanets_ReturnsAllPlanets() {
    PLANET.setTerrain("rocky");
    PLANET.setClimate("arid");

    when(planetRepository.findAll(any(Example.class))).thenReturn(Collections.singletonList(PLANET));

    List<Planet> planets = planetService.list("rocky", "arid");

    assertEquals(1, planets.size());
    assertEquals(PLANET, planets.get(0));
  }

  @Test
  public void listPlanets_ReturnsNoPlanets() {
    when(planetRepository.findAll(any(Example.class))).thenReturn(Collections.emptyList());

    List<Planet> planets = planetService.list("rocky", "arid");

    assertEquals(0, planets.size());
  }

  @Test
  public void removePlanet_WithExistingId_doesNotThrowAnyException() {
    // Verifique se o código remove não lança exceção
    assertThatCode(() -> planetService.remove(1L)).doesNotThrowAnyException();
  }

  @Test
  public void removePlanet_WithUnexistingId_ThrowsAnyException() {
    // doThrow (usa em métodos void) exceção e condição do lançamento da exceção
    doThrow(new RuntimeException()).when(planetRepository).deleteById(99L);

    assertThatThrownBy(() -> planetService.remove(99L)).isInstanceOf(RuntimeException.class);
  }
}