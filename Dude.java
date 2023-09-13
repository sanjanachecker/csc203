import processing.core.PImage;

import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public abstract class Dude extends Executable implements Moveable, Transformable {

    private final int resourceLimit;

    public Dude(String id, Point position, List<PImage> images, int resourceLimit, double actionPeriod, double animationPeriod) {
        super(id, position, actionPeriod, animationPeriod, images);
        this.resourceLimit = resourceLimit;
    }

    public int getResourceLimit() {
        return this.resourceLimit;
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

    public void transformTroll(WorldModel world, EventScheduler scheduler, ImageStore imageStore) { //yes
        Troll troll = new Troll(WorldModel.TROLL_KEY + "_" + this.getId(), this.getPosition(), imageStore.getImageList(WorldModel.TROLL_KEY), WorldModel.TROLL_ACTION_PERIOD, WorldModel.TROLL_ANIMATION_PERIOD);
        world.removeEntity(scheduler, this);
        scheduler.unscheduleAllEvents(this);
        world.addEntity(troll);

        troll.scheduleActions(scheduler, world, imageStore);
    }

}
