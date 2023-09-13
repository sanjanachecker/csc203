import processing.core.PImage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class Fairy extends Executable implements Moveable{

    public Fairy(String id, List<PImage> images, Point position, double animationPeriod, double actionPeriod) {
        super(id, position, actionPeriod, animationPeriod, images);
    }

    @Override
    public Point nextPosition(WorldModel world, Point destPos) {
        PathingStrategy fairyPos = new AStarPathingStrategy();
        Predicate<Point> pred = (p) -> (!world.isOccupied(p) && world.withinBounds(p));
        BiPredicate<Point, Point> biPred = Point::adjacent;
        List<Point> path = fairyPos.computePath(
                getPosition(),
                destPos,
                pred, biPred,
                PathingStrategy.CARDINAL_NEIGHBORS);
        if(path == null || path.size() == 0){
            return getPosition();
        }
        else {
            return path.get(0);
        }

    }

    @Override
    public boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler) {
        if (this.getPosition().adjacent(target.getPosition())) {
            world.removeEntity(scheduler, target);
            return true;
        } else {
            Point nextPos = this.nextPosition(world, target.getPosition());

            if (!this.getPosition().equals(nextPos)) {
                world.moveEntity(scheduler, this, nextPos);
            }
            return false;
        }
    }

    @Override
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Optional<Entity> fairyTarget = world.findNearest(this.getPosition(), new ArrayList<>(List.of(Stump.class)));

        if (fairyTarget.isPresent()) {
            Point tgtPos = fairyTarget.get().getPosition();

            if (this.moveTo(world, fairyTarget.get(), scheduler)) {

                Plant sapling = new Sapling(WorldModel.SAPLING_KEY + "_" + fairyTarget.get().getId(), tgtPos, imageStore.getImageList(WorldModel.SAPLING_KEY), 0);

                world.addEntity(sapling);
                sapling.scheduleActions(scheduler, world, imageStore);
            }
        }

        scheduler.scheduleEvent(this, this.createActivityAction(world, imageStore), this.getActionPeriod());
    }
}

