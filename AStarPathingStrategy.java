import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class AStarPathingStrategy implements PathingStrategy{

    @Override
    public List<Point> computePath(Point start, Point end, Predicate<Point> canPassThrough, BiPredicate<Point, Point> withinReach, Function<Point, Stream<Point>> potentialNeighbors) {

        PriorityQueue<WorldNode> openList = new PriorityQueue<>(Comparator.comparingInt(WorldNode::getF));
        Set<Point> closedList = new HashSet<>();
        WorldNode first = new WorldNode(start, null, 0, end);
        openList.add(first);

        List<Point> path = new ArrayList<>();
        WorldNode theOne = null;
        while (!openList.isEmpty()){
            WorldNode curr = openList.remove();
            if (withinReach.test(curr.getPosition(), end)){
                theOne = curr;
                break;
            }
            // first, get neighbors and filter them
            Stream<WorldNode> neigh = potentialNeighbors.apply(curr.getPosition())
                    .filter(canPassThrough)                 // make sure neighbor is passable
                    .filter(p -> !closedList.contains(p)) // make sure neighbor is not already visited
                    .map(p -> new WorldNode(p, curr, curr.getG() + 1, end));  // map to WorldNode
            // next, go thru each neighbor and add to openList
            neigh.forEach(neighbor -> {
                if (!openList.contains(neighbor)){
                    openList.add(neighbor);        // add to openlist if not alr in
                } else {
                    if(openList.removeIf(wn -> wn.getPosition().equals(neighbor.getPosition())
                            && wn.getG() > neighbor.getG())){  // we want to remove the node w same pos from
                        // list if the g value is worse then neighnode's g val
                        openList.add(neighbor);
                    }
                }
            });
            // add the current pos to closedList
            closedList.add(curr.getPosition());
        }
        // make path of points
        if (theOne != null){
            while (theOne.getPrev() != null){
                path.add(0, theOne.getPosition());
                theOne = theOne.getPrev();
            }
            return path;
        }

        // if no path was found
        return null;
    }
}
