package core;

import java.awt.*;

public class Room extends Rectangle {


    public Room(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public Point getCenter() {
        return new Point(this.x + this.width / 2, this.y - this.height / 2);
    }


}
