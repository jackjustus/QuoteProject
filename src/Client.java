import processing.core.PApplet;

public class Client {

    public static void main(String[] args) {

        // Setting up processing
        PApplet.main("GUI");

        // GUI Object
        // This runs the draw() function from processing so no need to call any methods from here
        GUI gui = new GUI();
        APIManager api = new APIManager(gui.getPApplet());


        // The game gets quotes from a specified number of authors
        // This generates those authors from a pool of authors specified in the APIManager class
        api.generateAuthorList();

//        for (int i = 0; i < APIManager.NUM_ROUNDS_IN_GAME; i++)
//            api.getAuthorsForRound(i);
    }

    public static void printToConsole(String s) {
        System.out.println(s);
    }
}
