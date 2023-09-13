import processing.core.PImage;

import java.util.List;

public abstract class Entity {
    private Point position;
    private final List<PImage> images;
    private int imageIndex;
    private final String id;

    public Entity(String id, Point position, List<PImage> images) {
        this.position = position;
        this.images = images;
        this.id = id;
        this.imageIndex = 0;
    }

    public int getHealth() {
        return 0;
    }

    public Point getPosition() {
        return this.position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public String getId() {
        return this.id;
    }

    public List<PImage> getImages() {
        return this.images;
    }

    public int getImageIndex() {
        return imageIndex;
    }

    public void setImageIndex(int imageIndex) {
        this.imageIndex = imageIndex;
    }

    public PImage getCurrentImage() {
        return this.images.get(this.imageIndex % this.images.size());
    }

    public String log(){
        return this.id.isEmpty() ? null :
                String.format("%s %d %d %d", this.id, this.position.x, this.position.y, this.imageIndex);
    }

}
