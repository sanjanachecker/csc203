//import org.junit.jupiter.api.Test;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//public class SearchTests {
//
//    @Test
//    public void testAdjacent() {
//        Point p = new Point(1, 3);
//        assertTrue(p.adjacent(new Point(2, 3)));
//        assertTrue(p.adjacent(new Point(1, 4)));
//        assertFalse(p.adjacent(new Point(5, 5)));
//    }
//
//    @Test
//    public void testWorldNodeEquals() {
//        Point a = new Point(1, 1);
//        Point b = new Point(2, 2);
//        WorldNode prev = new WorldNode(new Point(1, 2), new Point(1, 3));
//        WorldNode wn1 = new WorldNode(a, b, prev);
//        WorldNode wn2 = new WorldNode(a, b, prev);
//        assertEquals(wn1, wn2);
//    }
//
//    @Test
//    public void testWorldNodeG() {
//        Point a = new Point(1, 1);
//        Point b = new Point(2, 2);
//        WorldNode prev = new WorldNode(new Point(1, 2), new Point(1, 3));
//        WorldNode wn1 = new WorldNode(a, b, prev);
//        WorldNode wn2 = new WorldNode(a, b, wn1);
//        assertEquals(2, wn2.getG());
//    }
//
//    @Test
//    public void testSingleStepNoObstacles() {
//        // Grid for testing --> 2D array
//        boolean[][] grid = {
//                { true, true, true },
//                { true, true, true },
//                { true, true, true }
//        };
//
//        PathingStrategy ps = new SingleStepPathingStrategy();
//        List<Point> path = ps.computePath(
//                new Point(0, 0), new Point(2, 2), // start, end
//                (p) -> withinBounds(p, grid) && grid[p.y][p.x], // canPassThrough
//                (p1, p2) -> p1.adjacent(p2),
//                PathingStrategy.CARDINAL_NEIGHBORS
//        );
//
//        // expected path => [(0, 1)] <=
//        assertEquals(path, Arrays.asList(new Point(0, 1)));
//    }
//
//    // Write more tests including obstacles and other edge cases.
//
//    @Test
//    public void testAStarNoObstacles() {
//        // Grid for testing --> 2D array
//        boolean[][] grid = {
//                { true, true, true },
//                { true, true, true },
//                { true, true, true }
//        };
//
//        PathingStrategy ps = new AStarPathingStrategy();
//        List<Point> path = ps.computePath(
//                new Point(0, 0), new Point(2, 2), // start, end
//                (p) -> withinBounds(p, grid) && grid[p.y][p.x], // canPassThrough
//                (p1, p2) -> p1.adjacent(p2),
//                PathingStrategy.CARDINAL_NEIGHBORS
//        );
//        List<Point> actualPath = new ArrayList<>();
//        actualPath.add(new Point(0, 1));
//        actualPath.add(new Point(1, 1));
//        actualPath.add(new Point(2, 1));
//        assertTrue(isValidPath(actualPath, 3, new Point(0, 0), new Point(2, 2)));
//        // expected path => [(0, 1)] <=
//        assertEquals(path, Arrays.asList(new Point(0, 1), new Point(1, 1), new Point(2, 1)));
//    }
//
//    @Test
//    public void testAStarWithObstacles1() {
//        // Grid for testing --> 2D array
//        boolean[][] grid = {
//                { true, true, true },
//                { true, false, true },
//                { true, true, true }
//        };
//
//        PathingStrategy ps = new AStarPathingStrategy();
//        List<Point> path = ps.computePath(
//                new Point(0, 0), new Point(2, 2), // start, end
//                (p) -> withinBounds(p, grid) && grid[p.y][p.x], // canPassThrough
//                (p1, p2) -> p1.adjacent(p2),
//                PathingStrategy.CARDINAL_NEIGHBORS
//        );
//        List<Point> actualPath = new ArrayList<>();
//        actualPath.add(new Point(0, 1));
//        actualPath.add(new Point(0, 2));
//        actualPath.add(new Point(1, 2));
//        assertTrue(isValidPath(actualPath, 3, new Point(0, 0), new Point(2, 2)));
//        // expected path => [(0, 1)] <=
//        assertEquals(path, Arrays.asList(new Point(0, 1), new Point(0, 2), new Point(1, 2)));
//    }
//
//    @Test
//    public void testAStarWithObstacles2() {
//        // Grid for testing --> 2D array
//        boolean[][] grid = {
//                { true, true, true },
//                { false, true, false },
//                { true, true, true }
//        };
//
//        PathingStrategy ps = new AStarPathingStrategy();
//        List<Point> path = ps.computePath(
//                new Point(0, 0), new Point(2, 2), // start, end
//                (p) -> withinBounds(p, grid) && grid[p.y][p.x], // canPassThrough
//                (p1, p2) -> p1.adjacent(p2),
//                PathingStrategy.CARDINAL_NEIGHBORS
//        );
//        List<Point> actualPath = new ArrayList<>();
//        actualPath.add(new Point(1, 0));
//        actualPath.add(new Point(1, 1));
//        actualPath.add(new Point(1, 2));
//        assertTrue(isValidPath(actualPath, 3, new Point(0, 0), new Point(2, 2)));
//        // expected path => [(0, 1)] <=
//        assertEquals(path, Arrays.asList(new Point(1, 0), new Point(1, 1), new Point(1, 2)));
//    }
//
//    @Test
//    public void testAStarNoPath() {
//        // Grid for testing --> 2D array
//        boolean[][] grid = {
//                { true, true, true },
//                { true, true, false },
//                { true, false, true }
//        };
//
//        PathingStrategy ps = new AStarPathingStrategy();
//        List<Point> path = ps.computePath(
//                new Point(0, 0), new Point(2, 2), // start, end
//                (p) -> withinBounds(p, grid) && grid[p.y][p.x], // canPassThrough
//                (p1, p2) -> p1.adjacent(p2),
//                PathingStrategy.CARDINAL_NEIGHBORS
//        );
//        List<Point> actualPath = new ArrayList<>();
//        // expected path => [(0, 1)] <=
//        assertEquals(path, Arrays.asList());
//        assertEquals(0, path.size());
//    }
//
//    /*
//     * Properties of a correct a-star path
//     *
//     * 1. path length
//     * 2. path starts at the start point and ends at the goal
//     * 3. path actually contains contiguous nodes
//     */
//
//    // property based testing
//    private static boolean isValidPath(List<Point> path, int expectedLength, Point expectedStart, Point expectedEnd) {
//        // check that path contains exactly `expectedLength` points
//        // check the first point in path is adjacent to expectedStart, ditto for end
//        // traverse the path and check each point is adjacent to its previous point
//        boolean length = path.size() == expectedLength;
//        boolean start = path.get(0).adjacent(expectedStart);
//        boolean end = path.get(path.size() - 1).adjacent(expectedEnd);
//        boolean adjacent = false;
//        for(int i = 1; i < path.size(); i++) {
//            adjacent = path.get(i-1).adjacent(path.get(i));
//        }
//        return (length && start && end && adjacent);
//    }
//
//    private static boolean withinBounds(Point p, boolean[][] grid) {
//        return p.y >= 0 && p.y < grid.length &&
//                p.x >= 0 && p.x < grid[0].length;
//    }
//}
