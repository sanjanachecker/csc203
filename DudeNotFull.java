import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class DudeNotFull extends Dude{
    private int resourceCount;


    public DudeNotFull(String id, Point position, List<PImage> images, int resourceLimit, double actionPeriod, double animationPeriod) {
        super(id, position, images, resourceLimit, actionPeriod, animationPeriod);
    }

    public int getResourceCount() {
        return this.resourceCount;
    }

    public void setResourceCount(int resourceCount) {
        this.resourceCount = resourceCount;
    }

    @Override
    public boolean transform(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        if (this.getResourceCount() >= this.getResourceLimit()) {
            Dude dude = new DudeFull(this.getId(), this.getPosition(), this.getImages(), this.getResourceLimit(), this.getActionPeriod(), this.getAnimationPeriod());

            world.removeEntity(scheduler, this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(dude);
            dude.scheduleActions(scheduler, world, imageStore);

            return true;
        }

        return false;
    }

    @Override
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        if((world.withinBounds(this.getPosition()) &&
                world.getBackgroundCell(this.getPosition()).toString().equals(WorldModel.PLAGUE_KEY))) {
            this.transformTroll(world, scheduler, imageStore);
        }

        Optional<Entity> target = world.findNearest(this.getPosition(), new ArrayList<>(Arrays.asList(Tree.class, Sapling.class)));

        if (target.isEmpty() || !this.moveTo(world, target.get(), scheduler) || !this.transform(world, scheduler, imageStore)) {
            scheduler.scheduleEvent(this, this.createActivityAction(world, imageStore), this.getActionPeriod());
        }
    }

    @Override
    public boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler) {
        if (this.getPosition().adjacent(target.getPosition())) {
            this.setResourceCount(this.getResourceCount() + 1);
            if(target instanceof Plant) {
                ((Plant)target).setHealth(this.getHealth()-1);
            }
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
