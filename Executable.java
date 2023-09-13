import processing.core.PImage;

import java.util.List;

public abstract class Executable extends Animate implements Active{
    private final double actionPeriod;

    public Executable(String id, Point position, double actionPeriod, double animationPeriod, List<PImage> images) {
        super(id, position, animationPeriod, images);
        this.actionPeriod = actionPeriod;
    }

    public double getActionPeriod() {
        return this.actionPeriod;
    }

    public ActivityAction createActivityAction(WorldModel world, ImageStore imageStore) {
        return new ActivityAction(this, world, imageStore);
    }

    @Override
    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent(this, this.createActivityAction(world, imageStore), this.getActionPeriod());
        scheduler.scheduleEvent(this, this.createAnimationAction(0), this.getAnimationPeriod());
    }

}
