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
        image = loadImage("funny-cartoon-monkey-chimpanzee-hanging-upside-down-vector-illustration-2BXENAK.jpeg");
    }

    @Override
    public void draw() {
        // Leave this here
        checkDebugExit();

        // TODO: Remove
        //displayUpsideDownMonkey();

        switch (screen) {

            case 0:
                homeScreen();
                break;
            case 1:
                gameScreen();
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

        //background
        background(255,200,200);


        //title
        textAlign(CENTER);
        textSize(50);
        fill(90,0,0);
        text("FINDING YOUR SOULMATE FROM THE PAST",width/2,height/2);
        text("better hope its not someone who committed genocide...", width/2, (int)(height*.6));


        fill(100);
        rect((float) (width*.3), (float) (height*.7), (float) (width*.4), (float) (height*.17), 30);
        fill(90,0,0);
        text("BEGIN", width/2, (int)(height*.8));

        if(mousePressed && onButton(width*.3, height*.7, width*.3 + width*.4, height*.7 + height*.17)){
            screen=1;
        }

    }


    private void gameScreen(){
        background(100);



    }

    public boolean onButton(double x1, double y1, double x2, double y2){

        if(mouseX>x1 && mouseX<x2){
            return mouseY > y1 && mouseY < y2;
        }
        return false;


    }




    public void displayUpsideDownMonkey() {


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
