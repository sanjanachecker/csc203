import processing.core.PImage;

import java.util.List;

public abstract class Plant extends Executable implements Transformable {
    private int health;

    public Plant(String id, Point position, double actionPeriod, double animationPeriod, List<PImage> images, int health) {
        super(id, position, actionPeriod, animationPeriod, images);
        this.health = health;
    }

    @Override
    public int getHealth() {
        return this.health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

}
