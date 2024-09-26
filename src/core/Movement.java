package core;

import tileengine.TETile;
import tileengine.Tileset;

public class Movement {
    private World world;
    private int WIDTH;
    private int HEIGHT;
    private Avatar avatar;
    private TETile[][] playground;


    public Movement(int width, int height, World world) {
        this.WIDTH = width;
        this.HEIGHT = height;
        this.world = world;
        this.avatar = world.getAvatar();
        this.playground = world.getPlayground();

    }



    public void moveLeft() {

        if (playground[avatar.x - 1][avatar.y] == Tileset.CELL) {
            playground[avatar.x - 1][avatar.y] = Tileset.AVATAR;
            playground[avatar.x][avatar.y] = Tileset.CELL;
            avatar.x = avatar.x - 1;
        }

        if (playground[avatar.x - 1][avatar.y] == Tileset.COIN) {
            playground[avatar.x - 1][avatar.y] = Tileset.AVATAR;
            playground[avatar.x][avatar.y] = Tileset.CELL;
            avatar.x = avatar.x - 1;
            world.incrementScore();
        }

    }

    public void moveRight() {
        if (playground[avatar.x + 1][avatar.y] == Tileset.CELL) {
            playground[avatar.x + 1][avatar.y] = Tileset.AVATAR;
            playground[avatar.x][avatar.y] = Tileset.CELL;
            avatar.x = avatar.x + 1;

        }
        if (playground[avatar.x + 1][avatar.y] == Tileset.COIN) {
            playground[avatar.x + 1][avatar.y] = Tileset.AVATAR;
            playground[avatar.x][avatar.y] = Tileset.CELL;
            avatar.x = avatar.x + 1;
            world.incrementScore();

        }

    }

    public void moveUp() {

        if (playground[avatar.x][avatar.y + 1] == Tileset.CELL) {
            playground[avatar.x][avatar.y + 1] = Tileset.AVATAR;
            playground[avatar.x][avatar.y] = Tileset.CELL;
            avatar.y = avatar.y + 1;

        }
        if (playground[avatar.x][avatar.y + 1] == Tileset.COIN) {
            playground[avatar.x][avatar.y + 1] = Tileset.AVATAR;
            playground[avatar.x][avatar.y] = Tileset.CELL;
            avatar.y = avatar.y + 1;
            world.incrementScore();
        }

    }

    public void moveDown() {
        if (playground[avatar.x][avatar.y - 1] == Tileset.CELL) {
            playground[avatar.x][avatar.y - 1] = Tileset.AVATAR;
            playground[avatar.x][avatar.y] = Tileset.CELL;
            avatar.y = avatar.y - 1;
        }

        if (playground[avatar.x][avatar.y - 1] == Tileset.COIN) {
            playground[avatar.x][avatar.y - 1] = Tileset.AVATAR;
            playground[avatar.x][avatar.y] = Tileset.CELL;
            avatar.y = avatar.y - 1;
            world.incrementScore();
        }
    }
}
