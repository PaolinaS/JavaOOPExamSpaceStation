package spaceStation.models.mission;

import spaceStation.models.astronauts.Astronaut;
import spaceStation.models.planets.Planet;

import java.util.*;

public class MissionImpl implements Mission {

    public MissionImpl() {
    }

    @Override
    public void explore(Planet planet, Collection<Astronaut> astronauts) {
        for (Astronaut astronaut : astronauts) {
            while (astronaut.canBreath()) {
                if (planet.getItems().size() == 0) {
                    return;
                }

                String item = planet.getItems().iterator().next();
                planet.getItems().remove(item);
                astronaut.getBag().getItems().add(item);
                astronaut.breath();
            }
        }
    }
}
