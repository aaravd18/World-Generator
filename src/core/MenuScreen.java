package core;

import edu.princeton.cs.algs4.StdDraw;

import java.awt.*;

import java.awt.Font;

public class MenuScreen {
    private static String text = "";


    // A lot of help was received form GPT-4 in this method. Mainly did some restructuring of code to match project
    public static void menu(String[] args, World world) {
        Font font = new Font("Futura", Font.BOLD, 20);
        StdDraw.setFont(font);

        double inputBoxX = 0.5, inputBoxY = 0.5; // Center of input box
        double inputBoxWidth = 0.6, inputBoxHeight = 0.1; // Width and height of input box

        StdDraw.setCanvasSize(80 * 16, 50 * 16);
        StdDraw.clear(Color.BLACK);


        // Draw the text input box

        double buttonX = 0.5, buttonY = 0.3; // Center of the button
        double buttonWidth = 0.2, buttonHeight = 0.1; // Width and height of the button
        // Draw the button
        StdDraw.setPenColor(StdDraw.BLUE);
        StdDraw.filledRectangle(buttonX, buttonY, buttonWidth / 2, buttonHeight / 2);
        // Draw the letter 'N' in the center of the button
        StdDraw.setPenColor(StdDraw.WHITE);
        font = new Font("Futura", Font.BOLD, 20);
        StdDraw.setFont(font);
        StdDraw.text(buttonX, buttonY, "New world: Press N");
        // Keep the drawing stable (Optional, remove if you want the program to close automatically)
        StdDraw.show();
        double buttonXc = 0.5, buttonYc = 0.7; // Center of the button
        double buttonWidthc = 0.2, buttonHeightc = 0.1; // Width and height of the button
        // Draw the button
        StdDraw.setPenColor(StdDraw.BLUE);
        StdDraw.filledRectangle(buttonXc, buttonYc, buttonWidthc / 2, buttonHeightc / 2);
        // Draw the letter 'N' in the center of the button
        StdDraw.setPenColor(StdDraw.WHITE);
        font = new Font("Futura", Font.BOLD, 20);
        StdDraw.setFont(font);
        StdDraw.text(buttonXc, buttonYc, "Load world: Press L");
        double buttonXm = 0.5, buttonYcm = 0.5; // Center of the button
        double buttonWidthcm = 0.2, buttonHeightcm = 0.1; // Width and height of the button


        // Draw the button
        StdDraw.setPenColor(StdDraw.BLUE);
        StdDraw.filledRectangle(buttonXm, buttonYcm, buttonWidthcm / 2, buttonHeightcm / 2);

        // Draw the letter 'N' in the center of the button
        StdDraw.setPenColor(StdDraw.WHITE);
        font = new Font("Futura", Font.BOLD, 20);
        StdDraw.setFont(font);
        StdDraw.text(buttonXm, buttonYcm, "Quit: Press Q");

        font = new Font("Futura", Font.BOLD, 40);
        StdDraw.setFont(font);
        StdDraw.text(0.5, 0.9, "World Explorer");

        // Keep the drawing stable (Optional, remove if you want the program to close automatically)
        StdDraw.show();

        // Main loop to handle text input
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                if (Character.toLowerCase(key) == 'l') {
                    world = new World();
                    world.runGame();
                }
                if (Character.toLowerCase(key) == 'q') {
                    System.exit(0);
                }
                if (Character.toLowerCase(key) == 'n') {
                    secondMenu(args, world);
                }
                if (key == '\n') {
                    System.out.println("Text submitted: " + text);
                    text = ""; // Reset text after submission
                }

                // Redraw input box with updated text
            }
            StdDraw.pause(20); // Delay to manage input speed
        }
    }

    // recieved some help from chat gpt 4
    public static boolean isInteger(String str) {
        try {
            Long.parseLong(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static void secondMenu(String[] args, World world) {
        double inputBoxX = 0.5, inputBoxY = 0.5; // Center of input box
        double inputBoxWidth = 0.6, inputBoxHeight = 0.1; // Width and height of input box

        StdDraw.setCanvasSize(80 * 16, 50 * 16);
        StdDraw.clear(Color.BLACK);


        // Draw the text input box
        drawInputBox(inputBoxX, inputBoxY, inputBoxWidth, inputBoxHeight);

        double buttonX = 0.5, buttonY = 0.3; // Center of the button
        double buttonWidth = 0.2, buttonHeight = 0.1; // Width and height of the button


        // Draw the button

        // Draw the letter 'N' in the center of the button


        // Keep the drawing stable (Optional, remove if you want the program to close automatically)
        StdDraw.show();


        // Draw the button

        // Draw the letter 'N' in the center of the button


        // Keep the drawing stable (Optional, remove if you want the program to close automatically)
        StdDraw.show();


        // Main loop to handle text input
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                if (Character.toLowerCase(key) == 'l') {
                    world = new World();
                    world.runGame();
                }
                if (Character.toLowerCase(key) == 's' && isInteger(text)) {
                    world = new World(Long.parseLong(text));
                    world.runGame();
                }
                if (key == '\n') {
                    System.out.println("Text submitted: " + text);
                    text = ""; // Reset text after submission
                } else if (key == '\b') {
                    // Handle backspace
                    if (text.length() > 0) {
                        text = text.substring(0, text.length() - 1);
                    }
                } else {
                    text += key; // Add character to text
                }

                // Redraw input box with updated text
                drawInputBox(inputBoxX, inputBoxY, inputBoxWidth, inputBoxHeight);
            }
            StdDraw.pause(20); // Delay to manage input speed
        }
    }

    private static void drawInputBox(double x, double y, double width, double height) {
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.filledRectangle(x, y, width / 2, height / 2);
        StdDraw.setPenColor(Color.BLACK);
        StdDraw.rectangle(x, y, width / 2, height / 2);

        // Draw the text inside the input box
        StdDraw.setPenColor(Color.BLACK);
        StdDraw.setFont(new Font("Arial", Font.BOLD, 16));
        StdDraw.textLeft(x - width / 2 + 0.02, y, text); // Adjust position for padding
    }
}








