package spaceStation.models.astronauts;

public class Biologist extends BaseAstronaut {
    private static final int INITIAL_OXYGEN = 70;
    public Biologist(String name) {
        super(name, INITIAL_OXYGEN);
    }

    @Override
    public void breath() {
        this.setOxygen(this.getOxygen() - 5);
    }
}
