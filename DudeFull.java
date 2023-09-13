import processing.core.PImage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DudeFull extends Dude {


    public DudeFull(String id, Point position, List<PImage> images, int resourceLimit, double actionPeriod, double animationPeriod) {
        super(id, position, images, resourceLimit, actionPeriod, animationPeriod);
    }

    @Override
    public boolean transform(WorldModel world, EventScheduler scheduler, ImageStore imageStore) { //yes
        Dude dude = new DudeNotFull(this.getId(), this.getPosition(), this.getImages(), this.getResourceLimit(), this.getActionPeriod(), this.getAnimationPeriod());

        world.removeEntity(scheduler, this);

        world.addEntity(dude);
        dude.scheduleActions(scheduler, world, imageStore);
        return false;
    }

    @Override
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        if(world.withinBounds(this.getPosition())
                && world.getBackgroundCell(this.getPosition()).toString().equals(WorldModel.PLAGUE_KEY)) {
            this.transformTroll(world, scheduler, imageStore);
        }

        Optional<Entity> fullTarget = world.findNearest(this.getPosition(), new ArrayList<>(List.of(House.class)));

        if (fullTarget.isPresent() && this.moveTo(world, fullTarget.get(), scheduler)) {
            this.transform(world, scheduler, imageStore);
        } else {
            scheduler.scheduleEvent(this, this.createActivityAction(world, imageStore), this.getActionPeriod());
        }
    }

    @Override
    public boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler) {
        if (this.getPosition().adjacent(target.getPosition())) {
            return true;
        } else {
            Point nextPos = this.nextPosition(world, target.getPosition());

            if (!this.getPosition().equals(nextPos)) {
                world.moveEntity(scheduler, this, nextPos);
            }
            return false;
        }
    }
}
