package core;

import utils.RandomUtils;

import java.awt.*;
import java.util.*;
import java.util.List;

public class GenerationAlgorithms {


    /**
     * Binary Space Partitioning Algorithm that creates random rectangular rooms in the map
     * The function splits the entire area into rectangular rooms and then deletes the rooms that are too small
     *
     * @param roomToSplit the current room to recursively split
     * @param minWidth    minimum width of a valid room
     * @param minHeight   minimum height of a valid room
     * @source Sunny Valley Studio YouTube Channel
     */
    public static List<Room> binarySpacePartition(Room roomToSplit, int minWidth, int minHeight, Random random) {


        // All rooms we may split further
        Queue<Room> currRooms = new LinkedList<Room>();

        // The final rooms
        List<Room> finalRooms = new ArrayList<Room>();
        currRooms.add(roomToSplit);

        while (!currRooms.isEmpty()) {
            Room room = currRooms.poll();

            // If the room is larger than the minimum dimensions keep it and potentially split it
            // Otherwise room is not added to finalRooms
            if (room.height >= minHeight && room.width >= minWidth) {

                // If the room is >2x minWidth and >2x minHeight, equal chance of splitting horizontally vs vertically
                // If only one type of split is possible then do it
                // Otherwise the room is of suitable size to be added to the final rooms
                if (RandomUtils.uniform(random) > 0.5) {
                    if (room.height >= minHeight * 1.9) {
                        horizontalSplit(currRooms, room, random);
                    } else if (room.width >= minWidth * 1.9) {
                        verticalSplit(currRooms, room, random);
                    } else {
                        finalRooms.add(room);
                    }
                } else {
                    if (room.width >= minWidth * 1.9) {
                        verticalSplit(currRooms, room, random);
                    } else if (room.height >= minHeight * 1.9) {
                        horizontalSplit(currRooms, room, random);
                    } else {
                        finalRooms.add(room);
                    }
                }
            }
        }
        return finalRooms;
    }

    /**
     * A room's x and y values are the coordinates of its top left corner
     * A vertical split splits the room into two smaller rooms of at least width 1
     */
    private static void verticalSplit(Queue<Room> currRooms, Room room, Random random) {
        int splitSize = RandomUtils.uniform(random, 1, room.width);
        Room new1 = new Room(room.x, room.y, splitSize, room.height);
        Room new2 = new Room(room.x + splitSize, room.y, room.width - splitSize, room.height);
        currRooms.add(new1);
        currRooms.add(new2);
    }

    private static void horizontalSplit(Queue<Room> currRooms, Room room, Random random) {
        int splitSize = RandomUtils.uniform(random, 1, room.height);
        Room new1 = new Room(room.x, room.y, room.width, splitSize);
        Room new2 = new Room(room.x, room.y - splitSize, room.width, room.height - splitSize);
        currRooms.add(new1);
        currRooms.add(new2);
    }

    public static HashSet<Point> connectRooms(List<Room> rooms, Random random) {
        List<Point> centers = new ArrayList<Point>();
        for (Room room : rooms) {
            centers.add(room.getCenter());
        }
        HashSet<Point> hallWayz = new HashSet<Point>();

        Point currentCenter = centers.get(RandomUtils.uniform(random, 0, centers.size()));

        centers.remove(currentCenter);

        while (centers.size() > 0) {
            Point closestCenter = findClosestCenter(currentCenter, centers);
            centers.remove(closestCenter);
            HashSet<Point> hallway = createHallway(currentCenter, closestCenter);
            currentCenter = closestCenter;
            hallWayz.addAll(hallway);

        }

        return hallWayz;
    }

    private static HashSet<Point> createHallway(Point currentCenter, Point closestCenter) {
        HashSet<Point> hallway = new HashSet<Point>();
        Point pos = currentCenter;

        hallway.add(new Point(pos));


        while (pos.y != closestCenter.y) {
            if (pos.y > closestCenter.y) {
                pos.translate(0, -1);
            } else {
                pos.translate(0, 1);
            }
            hallway.add(new Point(pos));
        }
        while (pos.x != closestCenter.x) {
            if (pos.x > closestCenter.x) {
                pos.translate(-1, 0);
            } else {
                pos.translate(1, 0);
            }
            hallway.add(new Point(pos));
        }
        return hallway;
    }

    private static Point findClosestCenter(Point currentCenter, List<Point> centers) {
        Point closest = new Point(0, 0);
        double minDistance = Double.POSITIVE_INFINITY;

        for (Point center : centers) {
            double currentDistance = Point.distance(closest.x, closest.y, center.x, center.y);
            if (currentDistance < minDistance) {
                minDistance = currentDistance;
                closest = center;
            }
        }
        return closest;
    }

    public static List<Room> randomRectangularRooms(Random random, int mapWidth, int mapHeight) {
        int maxRetries = 200;
        List<Room> rooms = new ArrayList<>();
        int numberOfRooms = RandomUtils.uniform(random, 12, 16);
        int minWidth = 11;
        int minHeight = 8;
        int numRetries = 0;
        int i = 0;

        while (i < numberOfRooms) {
            int roomWidth = RandomUtils.uniform(random, 6) + minWidth;
            int roomHeight = RandomUtils.uniform(random, 6) + minHeight;
            int x = RandomUtils.uniform(random, mapWidth - roomWidth);
            int y = RandomUtils.uniform(random, roomHeight, mapHeight);
            Room newRoom = new Room(x, y, roomWidth, roomHeight);

            // Check if the new room intersects any existing room - line gotten from GPT-4
            if (isWithinBounds(newRoom, mapWidth, mapHeight) && rooms.stream().noneMatch(r -> r.intersects(newRoom))) {
                rooms.add(newRoom);
            } else if (numRetries < maxRetries) {
                i--;
                numRetries++;
            }
            i++;
        }
        return rooms;
    }

    private static boolean isWithinBounds(Room room, int width, int height) {
        return room.x + room.width < width && room.y >= room.height;
    }
}
