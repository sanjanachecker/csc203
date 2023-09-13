public final class AnimateAction implements Action{
    private final Animate entity;
    private final int repeatCount;

    public AnimateAction(Animate entity, int repeatCount) {
        this.entity = entity;
        this.repeatCount = repeatCount;
    }

    public void executeAction(EventScheduler scheduler) {
        this.entity.nextImage();
        if (this.repeatCount != 1) {
            scheduler.scheduleEvent(this.entity, this.entity.createAnimationAction(Math.max(this.repeatCount - 1, 0)), this.entity.getAnimationPeriod());
        }
    }
}
