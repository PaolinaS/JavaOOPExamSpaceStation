package spaceStation.core;


import spaceStation.models.astronauts.Astronaut;
import spaceStation.models.astronauts.Biologist;
import spaceStation.models.astronauts.Geodesist;
import spaceStation.models.astronauts.Meteorologist;
import spaceStation.models.mission.Mission;
import spaceStation.models.mission.MissionImpl;
import spaceStation.models.planets.Planet;
import spaceStation.models.planets.PlanetImpl;
import spaceStation.repositories.AstronautRepository;
import spaceStation.repositories.PlanetRepository;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class ControllerImpl implements Controller {
    private AstronautRepository astronauts;
    private PlanetRepository planets;
    private Mission mission;
    private int exploredPlanetsCount = 0;

    public ControllerImpl() {
        this.astronauts = new AstronautRepository();
        this.planets = new PlanetRepository();
        this.mission = new MissionImpl();
    }

    @Override
    public String addAstronaut(String type, String astronautName) {
        Astronaut astronaut = null;
        switch (type) {
            case "Biologist":
                astronaut = new Biologist(astronautName);
                break;
            case "Geodesist":
                astronaut = new Geodesist(astronautName);
                break;
            case "Meteorologist":
                astronaut = new Meteorologist(astronautName);
                break;
        }

        String result = "";
        if (astronaut == null) {
            throw new IllegalArgumentException("Astronaut type doesn't exists!");
        } else {
            astronauts.add(astronaut);
            result = String.format("Successfully added %s: %s!", type, astronautName);
        }
        return result;
    }

    @Override
    public String addPlanet(String planetName, String... items) {
        Planet planet = new PlanetImpl(planetName);
        for (String item : items) {
            planet.getItems().add(item);
        }
        planets.add(planet);
        return String.format("Successfully added Planet: %s!", planetName);
    }

    @Override
    public String retireAstronaut(String astronautName) {
        Astronaut astronaut = this.astronauts.findByName(astronautName);

        if (astronaut == null) {
            throw new IllegalArgumentException(String.format("Astronaut %s doesn't exists!", astronautName));
        }

        this.astronauts.remove(astronaut);
        return String.format("Astronaut %s was retired!", astronautName);
    }

    @Override
    public String explorePlanet(String planetName) {
        int deadAstronauts = 0;
        List<Astronaut> suitableAstronauts = this.astronauts.getModels()
                .stream()
                .filter(a -> a.getOxygen() > 60)
                .collect(Collectors.toList());

        if (suitableAstronauts.isEmpty()) {
            throw new IllegalArgumentException("You need at least one astronaut to explore the planet!");
        }

        Planet planet = planets.findByName(planetName);
        mission.explore(planet, suitableAstronauts);
        exploredPlanetsCount++;

        for (Astronaut astronaut : suitableAstronauts) {
            if (!astronaut.canBreath()) {
                deadAstronauts++;
            }
        }

        return String.format
                ("Planet: %s was explored! Exploration finished with %d dead astronauts!"
                        , planetName
                        , deadAstronauts);
    }

    @Override
    public String report() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%d planets were explored!", exploredPlanetsCount)).
                append(System.lineSeparator()).
                append("Astronauts info:").
                append(System.lineSeparator());
        this.astronauts.getModels().forEach(astronaut ->
                sb.append(String.format("Name: %s", astronaut.getName()))
                        .append(System.lineSeparator())
                        .append(String.format("Oxygen: %.0f", astronaut.getOxygen()))
                        .append(System.lineSeparator())
                        .append(String.format("Bag items: %s",
                                astronaut.getBag().getItems().size() == 0 ?
                                        "none" : String.join(", ", astronaut.getBag().getItems())))
                        .append(System.lineSeparator()));
        return sb.toString().trim();
    }
}
