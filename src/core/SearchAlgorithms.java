package core;

import tileengine.TETile;
import tileengine.Tileset;

import java.awt.*;
import java.util.*;
import java.util.List;

public class SearchAlgorithms {
    private TETile[][] grid;
    private int width;
    private int height;

    public SearchAlgorithms(TETile[][] grid) {
        this.grid = grid;
        this.width = grid.length;
        this.height = grid[0].length;
    }

    private static class Node {
        Point point;
        Node parent;
        double gCost;
        double hCost;
        double fCost;

        public Node(Point point, Node parent, double gCost, double hCost) {
            this.point = point;
            this.parent = parent;
            this.gCost = gCost;
            this.hCost = hCost;
            this.fCost = gCost + hCost;
        }
    }

    private static class PointDistance {
        Point point;
        int distance;

        public PointDistance(Point point, int distance) {
            this.point = point;
            this.distance = distance;
        }
    }

    /**
     * A* search algorithm that finds a path from start to end.
     * Algorithm partially written by GPT-4
     */

    public List<Point> findPath(Point start, Point end) {
        PriorityQueue<Node> openList = new PriorityQueue<>(Comparator.comparingDouble(n -> n.fCost));
        Set<Point> visited = new HashSet<>();

        Node startNode = new Node(start, null, 0, dist(start, end));
        openList.add(startNode);
        visited.add(startNode.point);

        while (!openList.isEmpty()) {
            Node currentNode = openList.poll();
            if (currentNode.point.equals(end)) {
                return constructPath(currentNode);
            }

            for (Point neighbour : getNeighbours(currentNode.point)) {
                if (grid[neighbour.x][neighbour.y] == Tileset.WALL || visited.contains(neighbour)) {
                    continue;
                }

                Node neighbourNode = new Node(neighbour, currentNode, currentNode.gCost + 1, dist(neighbour, end));
                openList.add(neighbourNode);
                visited.add(neighbour);
            }
        }

        return Collections.emptyList(); // No path found
    }

    private List<Point> constructPath(Node node) {
        List<Point> path = new ArrayList<>();
        while (node != null) {
            path.add(0, node.point);
            node = node.parent;
        }
        return path;
    }

    private List<Point> getNeighbours(Point p) {
        List<Point> neighbors = new ArrayList<>();
        int[] dx = {-1, 1, 0, 0};
        int[] dy = {0, 0, -1, 1};

        for (int i = 0; i < 4; i++) {
            int newX = p.x + dx[i];
            int newY = p.y + dy[i];
            if (newX >= 0 && newX < width && newY >= 0 && newY < height) {
                neighbors.add(new Point(newX, newY));
            }
        }

        return neighbors;
    }

    private int dist(Point a, Point b) {

        // Manhattan distance as a heuristic
        return Math.abs(a.x - b.x) + Math.abs(a.y - b.y);
    }

    /**
     * BFS search algorithm used to display visible tiles within a certain radius.
     * Algorithm partially written by GPT-4
     */

    public List<Point> findVisiblePoints(Point start) {
        List<Point> accessiblePoints = new ArrayList<>();
        Queue<PointDistance> queue = new LinkedList<>();
        Set<Point> visited = new HashSet<>();

        accessiblePoints.add(start);
        queue.offer(new PointDistance(start, 0));
        visited.add(start);

        while (!queue.isEmpty()) {
            PointDistance current = queue.poll();

            // Add current point to accessible points if it's within the radius and not the starting point
            if (current.distance > 0 && current.distance <= 6) {
                accessiblePoints.add(current.point);
            }

            // Explore neighbors if within radius
            if (current.distance < 6 && grid[current.point.x][current.point.y] != Tileset.WALL) {
                for (Point neighbor : getNeighbours(current.point)) {
                    if (!visited.contains(neighbor)) {
                        visited.add(neighbor);
                        queue.offer(new PointDistance(neighbor, current.distance + 1));
                    }
                }
            }
        }

        return accessiblePoints;
    }
}
