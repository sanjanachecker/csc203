import processing.core.PImage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class Wife extends Executable implements Moveable{
    public Wife(String id, Point position, double actionPeriod, double animationPeriod, List<PImage> images) {
        super(id, position, actionPeriod, animationPeriod, images);
    }

    @Override
    public Point nextPosition(WorldModel world, Point destPos) {
        PathingStrategy wifePos = new AStarPathingStrategy();
        Predicate<Point> pred = (p) -> (!world.isOccupied(p) && world.withinBounds(p));
        BiPredicate<Point, Point> biPred = Point::adjacent;
        List<Point> path = wifePos.computePath(
                getPosition(),
                destPos,
                pred, biPred,
                PathingStrategy.CARDINAL_NEIGHBORS);
        if (path == null || path.size() == 0) {
            return getPosition();
        } else {
            return path.get(0);
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

    @Override
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Optional<Entity> wifeTarget = world.findNearest(this.getPosition(), new ArrayList<>(List.of(Troll.class)));

        if (wifeTarget.isPresent()) {
            Point tgtPos = wifeTarget.get().getPosition();

            if (this.getPosition().adjacent(tgtPos) && world.withinBounds(tgtPos)) {
                world.setBackgroundCell(tgtPos, new Background("grass", imageStore.getImageList("grass")));
            }

            this.moveTo(world, wifeTarget.get(), scheduler);

            if (world.withinBounds(this.getPosition()) && world.getBackgroundCell(this.getPosition()).toString().equals(WorldModel.PLAGUE_KEY)) {
                world.setBackgroundCell(this.getPosition(), new Background("grass", imageStore.getImageList("grass")));
            }
        }

        scheduler.scheduleEvent(this, this.createActivityAction(world, imageStore), this.getActionPeriod());
    }

}
