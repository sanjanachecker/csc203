import processing.core.PImage;

import java.util.List;

public class Sapling extends Plant {
    private final int healthLimit;
    private static final double TREE_ANIMATION_MAX = 0.600;
    private static final double TREE_ANIMATION_MIN = 0.050;
    private static final double TREE_ACTION_MAX = 1.400;
    private static final double TREE_ACTION_MIN = 1.000;
    private static final int TREE_HEALTH_MAX = 3;
    private static final int TREE_HEALTH_MIN = 1;

    public Sapling(String id, Point position, List<PImage> images, int health) {
        super(id, position, WorldModel.SAPLING_ACTION_ANIMATION_PERIOD, WorldModel.SAPLING_ACTION_ANIMATION_PERIOD, images, health);
        this.healthLimit = WorldModel.SAPLING_HEALTH_LIMIT;
    }

    @Override
    public boolean transform(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        if (this.getHealth() <= 0) {
            Entity stump = new Stump(WorldModel.STUMP_KEY + "_" + this.getId(), this.getPosition(), imageStore.getImageList(WorldModel.STUMP_KEY));

            world.removeEntity(scheduler, this);

            world.addEntity(stump);

            return true;
        } else if (this.getHealth() >= this.healthLimit) {
            Plant tree = new Tree(WorldModel.TREE_KEY + "_" + this.getId(), this.getPosition(), imageStore.getImageList(WorldModel.TREE_KEY), getIntFromRange(TREE_HEALTH_MAX, TREE_HEALTH_MIN), getNumFromRange(TREE_ACTION_MAX, TREE_ACTION_MIN), getNumFromRange(TREE_ANIMATION_MAX, TREE_ANIMATION_MIN));

            world.removeEntity(scheduler, this);

            world.addEntity(tree);
            tree.scheduleActions(scheduler, world, imageStore);

            return true;
        }

        return false;
    }

    @Override
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        this.setHealth(this.getHealth()+1);
        if (!this.transform(world, scheduler, imageStore)) {
            scheduler.scheduleEvent(this, this.createActivityAction(world, imageStore), this.getActionPeriod());
        }
    }
}
