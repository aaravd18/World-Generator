import core.AutograderBuddy;
import core.GenerationAlgorithms;
import core.Room;
import core.World;
import edu.princeton.cs.algs4.StdDraw;
import org.junit.jupiter.api.Test;
import tileengine.TERenderer;
import tileengine.TETile;

import java.awt.*;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class WorldGenTests {
    @Test
    public void basicTest() {
        // put different seeds here to test different worlds
        TETile[][] tiles = AutograderBuddy.getWorldFromInput("n8s29081");

        TERenderer ter = new TERenderer();
        ter.initialize(tiles.length, tiles[0].length);
        ter.renderFrame(tiles);
        StdDraw.pause(90000); // pause for 5 seconds so you can see the output
    }

    @Test
    public void basicInteractivityTest() {
        // put different seeds here to test different worlds
        TETile[][] tiles = AutograderBuddy.getWorldFromInput("n123swasdwasd");

        TERenderer ter = new TERenderer();
        ter.initialize(tiles.length, tiles[0].length);
        ter.renderFrame(tiles);
        StdDraw.pause(5000); // pause for 5 seconds so you can see the output
    }

    @Test
    public void basicSaveTest() {
        TETile[][] tiles = AutograderBuddy.getWorldFromInput("n5197880843569031643s");

        TERenderer ter = new TERenderer();
        ter.initialize(tiles.length, tiles[0].length);
        ter.renderFrame(tiles);
        StdDraw.pause(5000); // pause for 5 seconds so you can see the o

        TETile[][] tiles1 = AutograderBuddy.getWorldFromInput("n5197880843569031643s");

        TERenderer ter1 = new TERenderer();
        ter.initialize(tiles1.length, tiles1[0].length);
        ter.renderFrame(tiles1);
        StdDraw.pause(5000); // pause for 5 seconds so you can see the o
        System.out.print(tiles == tiles1);
    }

    @Test
    public void autograderTest() {
        AutograderBuddy.getWorldFromInput("n1392967723524655428sddsaawws:q");
        AutograderBuddy.getWorldFromInput("laddw");
    }
}
