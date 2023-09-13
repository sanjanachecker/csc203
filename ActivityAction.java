public final class ActivityAction implements Action{
    private final Active entity;
    private final WorldModel world;
    private final ImageStore imageStore;

    public ActivityAction(Active entity, WorldModel world, ImageStore imageStore) {
        this.entity = entity;
        this.world = world;
        this.imageStore = imageStore;
    }
    /** Refactored Functions */

    public void executeAction(EventScheduler scheduler) {
        this.entity.executeActivity(this.world, this.imageStore, scheduler);
    }
}
