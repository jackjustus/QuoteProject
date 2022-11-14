import processing.core.PApplet;

public class Client {


    public static void main(String[] args) {

        // Setting up processing
        PApplet.main("GUI");

        // GUI Object
        // This runs the draw() function from processing
        // Everything that is drawn on the screen comes from this class
        // Some would call it the 'front' end
        new GUI();

        // The APIManager class is the 'back' end and provided quote and author data to the GUI
        // The game gets quotes from a specified number of authors (constant in APIManager)
        // The GUI object has its own APIManager object

    }
}
