package core;

import edu.princeton.cs.algs4.StdDraw;
import tileengine.TERenderer;
import tileengine.TETile;
import tileengine.Tileset;
import utils.FileUtils;
import utils.RandomUtils;

import java.awt.*;
import java.io.*;
import java.util.Base64;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class World {

    private TETile[][] playground;

    private boolean darkMode;
    private boolean isMousePressed = false;

    private SearchAlgorithms search;
    private boolean isGameOver;
    private final TERenderer OUR_RENDERER = new TERenderer();
    private char lastKey = ' ';

    private List<Room> rooms;
    private int WIDTH;
    private int HEIGHT;

    private Random RANDOM;

    private Avatar avatar;
    private Movement movement;

    private int score;


    public World(Long seed) {
        RANDOM = new Random(seed);
        WIDTH = 80;
        HEIGHT = 50;
        playground = new TETile[WIDTH][HEIGHT];
        rooms = GenerationAlgorithms.randomRectangularRooms(RANDOM, WIDTH, HEIGHT);
        HashSet<Point> hallways = GenerationAlgorithms.connectRooms(rooms, RANDOM);
        fillWithNothing(playground);
        loadCells(playground, rooms, hallways);

        Point spawnPoint = getRandomSpawn();
        avatar = new Avatar(spawnPoint.x, spawnPoint.y);
        movement = new Movement(WIDTH, HEIGHT, this);
        generateCoins(seed);
        search = new SearchAlgorithms(playground);
        darkMode = false;
        this.score = 0;
    }

    public World() {
        loadWorld();
        RANDOM = deserializeRandomVariable();
        movement = new Movement(WIDTH, HEIGHT, this);
        search = new SearchAlgorithms(playground);
    }

    private void generateCoins(Long seed) {
        RANDOM = new Random(seed);
        int coordx = RandomUtils.uniform(RANDOM, 0, playground.length);
        int coordy = RandomUtils.uniform(RANDOM, 0, playground[0].length);
        int i = 0;
        while (i < 10) {
            if (playground[coordx][coordy] == Tileset.CELL) {
                playground[coordx][coordy] = Tileset.COIN;
                i++;
            }
            coordx = RandomUtils.uniform(RANDOM, 0, playground.length);
            coordy = RandomUtils.uniform(RANDOM, 0, playground[0].length);
        }
    }

    public static void fillWithNothing(TETile[][] tiles) {
        int height = tiles[0].length;
        int width = tiles.length;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                tiles[x][y] = Tileset.NOTHING;
            }
        }
    }

    public Point getRandomSpawn() {
        return rooms.get(RandomUtils.uniform(RANDOM, 0, rooms.size())).getCenter();
    }

    public void loadCells(TETile[][] tiles, List<Room> localRooms, HashSet<Point> points) {

        // Load rooms onto map
        for (Room room : localRooms) {
            int marginWidth = 1;
            int marginHeight = 1;
            for (int i = marginWidth; i < room.width - marginWidth; i++) {
                for (int j = marginHeight; j < room.height - marginHeight; j++) {
                    if (room.y - j >= HEIGHT - 2 || room.y - j <= 2) {
                        continue;
                    }
                    tiles[room.x + i][room.y - j] = Tileset.CELL;
                }
            }
        }
        // Load hallways onto map
        for (Point point : points) {
            tiles[point.x][point.y] = Tileset.CELL;
        }
        for (int x = 1; x < tiles.length; x += 1) {
            for (int y = 1; y < tiles[0].length; y += 1) {
                if (tiles[x][y] == Tileset.CELL) {
                    if (tiles[x - 1][y] == Tileset.NOTHING) {
                        tiles[x - 1][y] = Tileset.WALL;
                    }
                    if (tiles[x + 1][y] == Tileset.NOTHING) {
                        tiles[x + 1][y] = Tileset.WALL;
                    }
                    if (tiles[x][y + 1] == Tileset.NOTHING) {
                        tiles[x][y + 1] = Tileset.WALL;
                    }
                    if (tiles[x][y - 1] == Tileset.NOTHING) {
                        tiles[x][y - 1] = Tileset.WALL;
                    }
                    if (tiles[x + 1][y + 1] == Tileset.NOTHING) {
                        tiles[x + 1][y + 1] = Tileset.WALL;
                    }
                    if (tiles[x - 1][y - 1] == Tileset.NOTHING) {
                        tiles[x - 1][y - 1] = Tileset.WALL;
                    }
                    if (tiles[x - 1][y + 1] == Tileset.NOTHING) {
                        tiles[x - 1][y + 1] = Tileset.WALL;
                    }
                    if (tiles[x + 1][y - 1] == Tileset.NOTHING) {
                        tiles[x + 1][y - 1] = Tileset.WALL;
                    }
                }
            }

        }
    }

    public void runGame() {
        OUR_RENDERER.initialize(WIDTH, HEIGHT);
        while (!gameOver()) {
            updateBoard();
            renderWorld();
            StdDraw.show();
        }
        saveWorld();
        System.exit(0);
    }


    public void updateBoard() {
        if (StdDraw.hasNextKeyTyped()) {
            char typedKey = StdDraw.nextKeyTyped();
            typedKey = Character.toLowerCase(typedKey);
            if (typedKey == 'a') {
                movement.moveLeft();
            } else if (typedKey == 's') {
                movement.moveDown();
            } else if (typedKey == 'd') {
                movement.moveRight();
            } else if (typedKey == 'w') {
                movement.moveUp();
            } else if (typedKey == 'b') {
                darkMode = !darkMode;
            } else if (typedKey == 'q' && lastKey == ':') {
                isGameOver = true;
            }
            lastKey = typedKey;
        }
        playground[avatar.x][avatar.y] = Tileset.AVATAR;

        // Check if the mouse is pressed
        if (!isMousePressed && StdDraw.isMousePressed()) {
            isMousePressed = true;  // Update the flag to detect new clicks
            int mouseX = (int) StdDraw.mouseX();
            int mouseY = (int) StdDraw.mouseY();

            List<Point> path = search.findPath(new Point(avatar.x, avatar.y), new Point(mouseX, mouseY));
            followPath(path);
            clearKeyPressQueue();
        } else if (!StdDraw.isMousePressed()) {
            isMousePressed = false;  // Reset flag when mouse is released
        }
    }

    private static void clearKeyPressQueue() {
        while (StdDraw.hasNextKeyTyped()) {
            StdDraw.nextKeyTyped();  // Remove all pending keys from the queue
        }
    }

    public void followPath(List<Point> path) {
        for (Point point : path) {
            int nextX = point.x;
            int nextY = point.y;

            if (playground[nextX][nextY] == Tileset.COIN) {
                incrementScore();
            }
            playground[avatar.x][avatar.y] = Tileset.CELL;
            playground[nextX][nextY] = Tileset.AVATAR;
            avatar.x = nextX;
            avatar.y = nextY;
            renderWorld();
            StdDraw.show();
            StdDraw.pause(50);
        }
    }

    public void renderWorld() {
        if (darkMode) {
            StdDraw.clear(Color.black);
            OUR_RENDERER.drawTilesDark(playground, search.findVisiblePoints(new Point(avatar.x, avatar.y)));
        } else {
            OUR_RENDERER.drawTiles(playground);
        }
        renderHUD();
    }

    private void renderHUD() {
        int mouseX = (int) StdDraw.mouseX();
        int mouseY = (int) StdDraw.mouseY();
        // Calculate which cell the mouse is currently over
        StdDraw.setPenColor(StdDraw.WHITE);
        if (0 <= mouseX && mouseX < WIDTH && 0 <= mouseY && mouseY < HEIGHT) {
            StdDraw.text(5, 1, "Score " + this.score + " / 10");

            StdDraw.text(15, 1, "  Tile: " + playground[mouseX][mouseY].description());
        }
    }

    public void saveWorld() {
        serializeRandomVariable();
        StringBuilder contents = new StringBuilder(WIDTH + " " + HEIGHT + " " + darkMode + " " + score + "\n");
        for (int i = HEIGHT - 1; i >= 0; i--) {
            for (int j = 0; j < WIDTH; j++) {
                if (playground[j][i] == Tileset.CELL) {
                    contents.append("1");
                } else if (playground[j][i] == Tileset.WALL) {
                    contents.append("2");
                } else if (playground[j][i] == Tileset.AVATAR) {
                    contents.append("@");

                } else if (playground[j][i] == Tileset.COIN) {
                    contents.append("$");
                } else {
                    contents.append("0");
                }
            }
            contents.append("\n");
        }
        FileUtils.writeFile("save.txt", contents.toString());
    }

    // Inspiration from Lab 10
    public void loadWorld() {
        String[] contents = FileUtils.readFile("save.txt").split("\n");
        String[] dimensions = contents[0].split(" ");
        WIDTH = Integer.parseInt(dimensions[0]);
        HEIGHT = Integer.parseInt(dimensions[1]);
        darkMode = Boolean.parseBoolean(dimensions[2]);
        score = Integer.parseInt(dimensions[3]);

        TETile[][] board = new TETile[WIDTH][HEIGHT];
        fillWithNothing(board);
        for (int i = 1; i < HEIGHT + 1; i++) {
            for (int j = 0; j < WIDTH; j++) {
                char cell = contents[i].charAt(j);
                if (cell == '1') {
                    board[j][HEIGHT - i] = Tileset.CELL;
                } else if (cell == '2') {
                    board[j][HEIGHT - i] = Tileset.WALL;
                } else if (cell == '@') {
                    board[j][HEIGHT - i] = Tileset.AVATAR;
                    this.avatar = new Avatar(j, HEIGHT - i);
                } else if (cell == '$') {
                    board[j][HEIGHT - i] = Tileset.COIN;
                } else {
                    board[j][HEIGHT - i] = Tileset.NOTHING;
                }
            }
        }
        playground = board;
    }

    // Serializing random variable state function, help from GPT-4
    public void serializeRandomVariable() {
        // Serialize the Random object to a byte array
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(RANDOM);
            oos.flush();
            byte[] randomBytes = baos.toByteArray();

            // Encode the byte array to Base64 string
            String encoded = Base64.getEncoder().encodeToString(randomBytes);

            // Write the Base64 string to a text file
            try (FileWriter writer = new FileWriter("randomState.txt")) {
                writer.write(encoded);
            } catch (IOException e) {
                System.out.println("Error writing to file: " + e.getMessage());
            }
        } catch (IOException e) {
            System.out.println("Error serializing Random object: " + e.getMessage());
        }
    }

    // Deserializing random variable state function, help from GPT-4
    public Random deserializeRandomVariable() {
        try (FileReader reader = new FileReader("randomState.txt")) {
            char[] chars = new char[1024];
            int read = reader.read(chars);
            String encoded = new String(chars, 0, read);

            // Decode the Base64 string to a byte array
            byte[] bytes = Base64.getDecoder().decode(encoded);

            // Deserialize the byte array to a Random object
            try (ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
                 ObjectInputStream ois = new ObjectInputStream(bais)) {
                return (Random) ois.readObject();
            } catch (ClassNotFoundException e) {
                System.out.println("Class not found during deserialization: " + e.getMessage());
            }
        } catch (IOException e) {
            System.out.println("Error reading from file: " + e.getMessage());
        }
        return null;
    }

    public boolean gameOver() {
        return isGameOver;
    }

    public int getWIDTH() {
        return WIDTH;
    }

    public int getHEIGHT() {
        return HEIGHT;
    }

    public Avatar getAvatar() {
        return avatar;
    }

    public TETile[][] getPlayground() {
        return playground;
    }

    public void incrementScore() {
        score++;
    }
}


