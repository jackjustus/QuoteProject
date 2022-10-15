import processing.core.PApplet;

public class Client {
    public static void main(String[] args) {

        // Setting up processing
        PApplet.main("GUI");

        // GUI Object
        // This runs the draw() function from processing so no need to call any methods from here
        GUI gui = new GUI(true);

        APIManager api = new APIManager(gui.getPApplet());

        String[] info = api.fetchQuoteGardenAPI("obama");
        System.out.println(info[0] + " By: " + info[1]);

        System.out.println(api.fetchKanyeAPI());
    }

    public static void printToConsole(String s) {
        System.out.println(s);
    }
}
