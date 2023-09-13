public class WorldNode {
    private Point position;
    private WorldNode prev;
    private int g;
    private Point target;

    public WorldNode(Point position, WorldNode prev, int g, Point target){
        this.position = position;
        this.prev = prev;
        this.target = target;
        this.g = (this.prev == null) ? 0 : this.prev.g + 1;
    }

    public WorldNode getPrev() {
        return prev;
    }

    public Point getPosition(){
        return this.position;
    }

    public int getH(){
        return Math.abs(this.position.x - target.x) + Math.abs(this.position.y - target.y);
    }

    public int getG(){
        return this.g;
    }

    public int getF(){
        return getG() + getH();
    }

    @Override
    public boolean equals(Object o){
        if (o instanceof WorldNode){
            return (this.getPosition().equals(((WorldNode) o).getPosition())
                    && this.target.equals(((WorldNode) o).target));
        }
        return false;
    }

}
