public interface Transformable extends Active {
    boolean transform(WorldModel world, EventScheduler scheduler, ImageStore imageStore);
}
