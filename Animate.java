import processing.core.PImage;

import java.util.List;
import java.util.Random;

public abstract class Animate extends Entity {
    private final double animationPeriod;

    public Animate(String id, Point position, double animationPeriod, List<PImage> images) {
        super(id, position, images);
        this.animationPeriod = animationPeriod;
    }

    public double getAnimationPeriod() {
        return animationPeriod;
    }

    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent(this, this.createAnimationAction(0), this.getAnimationPeriod());
    }

    public AnimateAction createAnimationAction(int repeatCount) {
        return new AnimateAction(this, repeatCount);
    }

    public static int getIntFromRange(int max, int min) {
        Random rand = new Random();
        return min + rand.nextInt(max-min);
    }

    public static double getNumFromRange(double max, double min) {
        Random rand = new Random();
        return min + rand.nextDouble() * (max - min);
    }

    public void nextImage() {
        this.setImageIndex(this.getImageIndex() + 1);
    }
}
