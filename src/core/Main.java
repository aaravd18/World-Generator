package core;


public class Main {
    //mehdi sucks
    public static void main(String[] args) {
        // build your own world!
        if (args.length != 1) {
            throw new IllegalArgumentException("Error: Please provide exactly one argument.");
        }
        long seed;
        try {
            seed = Long.parseLong(args[0]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Error: The input provided is not a valid seed.");
        }

        MenuScreen.menu(args, new World(seed));
    }
}
