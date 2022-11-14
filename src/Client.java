import processing.core.PApplet;

public class Client {

    static boolean startNewGame;
    public static void main(String[] args) {

        // Setting up processing
        PApplet.main("GUI");

        newGame();


    }

    public static void newGame() {
        // GUI Object
        // This runs the draw() function from processing so no need to call any methods from here
        GUI gui = new GUI();
        APIManager api = new APIManager(gui.getPApplet());


        // The game gets quotes from a specified number of authors
        // This generates those authors from a pool of authors specified in the APIManager class
        api.generateAuthorList();
    }

    public static void printToConsole(String s) {
        System.out.println(s);
    }
}
