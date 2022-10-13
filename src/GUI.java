import org.json.JSONException;
import processing.core.PApplet;
import processing.core.PImage;

public class GUI extends PApplet {

    APIManager api;

    PImage image;
    int screen;

    public GUI() {

        screen = 0;

    }

    // TODO: REMOVE
    public GUI(boolean noOutput) {

    }

    @Override
    public void setup() {

        api = new APIManager(this);

        api.fetchForismaticAPI();
    }

    @Override
    public void draw() {
        // Leave this here
        checkDebugExit();

        // TODO: Remove
        displayUpsideDownMonkey();

        switch (screen) {

            case 0:
                homeScreen();
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            default:
                Client.printToConsole("Invalid Screen Number: " + screen + ", Defaulting to 0");
                screen = 0;
                break;

        }

    }

    private void homeScreen() {


    }


    public void displayUpsideDownMonkey() {
        image = loadImage("funny-cartoon-monkey-chimpanzee-hanging-upside-down-vector-illustration-2BXENAK.jpeg");

        image(this.image, 0, 0);
    }

    private void checkDebugExit() {
        // Allows the program to be closed by pressing escape
        if (keyPressed && keyCode == ESC)
            exit();
    }

    @Override
    public void settings() {
        fullScreen();
    }

    public PApplet getPApplet() {
        return this;
    }
}
