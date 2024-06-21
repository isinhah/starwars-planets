package com.spring.sw_planet_api.common;

import com.spring.sw_planet_api.domain.Planet;

import java.util.ArrayList;
import java.util.List;

public class PlanetConstants {
  public static final Planet PLANET = new Planet("name", "climate", "terrain");

  public static final Planet INVALID_PLANET = new Planet("", "", "");

  public static final Planet TATOOINE = new Planet(1L, "Tatooine", "arid", "desert");
  public static final Planet HOTH = new Planet(2L,"Hoth", "frozen", "tundra, ice caves, mountain ranges");
  public static final Planet ENDOR = new Planet(3L, "Endor", "temperate", "forests, mountains, lakes");

  public static final List<Planet> PLANETS = new ArrayList<>() {
    {
      add(TATOOINE);
      add(HOTH);
      add(ENDOR);
    }
  };
}
