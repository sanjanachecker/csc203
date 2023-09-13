import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class Troll extends Executable implements Moveable, Transformable{

    public Troll(String id, Point position, List<PImage> images, double actionPeriod, double animationPeriod){
        super(id, position, actionPeriod, animationPeriod, images);
    }

    @Override
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {

        Optional<Entity> target = world.findNearest(this.getPosition(), new ArrayList<>(Arrays.asList(DudeNotFull.class,
                DudeFull.class, Sapling.class, Tree.class)));

        boolean trans = transform(world, scheduler, imageStore);

        if (target.isEmpty() || !this.moveTo(world, target.get(), scheduler) || !trans){
            scheduler.scheduleEvent(this, this.createActivityAction(world, imageStore), this.getActionPeriod());
        }
    }

    @Override
    public Point nextPosition(WorldModel world, Point destPos) {
        PathingStrategy dudePos = new AStarPathingStrategy();
        Predicate<Point> pred = (p) -> (!world.isOccupied(p) && world.withinBounds(p) ||
                world.withinBounds(p) && world.getOccupancyCell(p) instanceof Stump);
        BiPredicate<Point, Point> biPred = Point::adjacent;
        List<Point> path = dudePos.computePath(
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
    public boolean transform(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        Optional<Entity> wife = world.findNearest(this.getPosition(), new ArrayList<>(List.of(Wife.class)));

        if (wife.isPresent()) {
            Point tgtPos = wife.get().getPosition();

            if (this.getPosition().adjacent(tgtPos)) {
                Dude dude = new DudeNotFull(this.getId(), this.getPosition(), imageStore.getImageList(WorldModel.DUDE_KEY), WorldModel.DUDE_LIMIT,this.getActionPeriod(), this.getAnimationPeriod());

                world.removeEntity(scheduler, this);
                scheduler.unscheduleAllEvents(this);

                world.addEntity(dude);
                dude.scheduleActions(scheduler, world, imageStore);

                return true;
            }

        }
        return false;
    }

    @Override
    public boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler) {
        if (this.getPosition().adjacent(target.getPosition())) {
            if(target instanceof Dude || target instanceof Plant) {
                world.removeEntity(scheduler, target);
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
