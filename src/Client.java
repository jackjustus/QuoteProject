import processing.core.PApplet;

public class Client {
    public static void main(String[] args) {

        // Setting up processing
        PApplet.main("GUI");

        // GUI Object
        // This runs the draw() function from processing so no need to call any methods from here
        GUI gui = new GUI(true);

        APIManager api = new APIManager(gui.getPApplet());

        System.out.println(api.fetchQuoteGardenAPI());

        System.out.println(api.fetchKanyeAPI());
    }

    public static void printToConsole(String s) {
        System.out.println(s);
    }
}
