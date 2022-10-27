import processing.core.PApplet;
import processing.core.PFont;

import java.awt.*;

public class GUI extends PApplet {

    private APIManager api;

    // Home-screen button
    private Button beginButton, choiceButton1, choiceButton2, selectionButton;

    // String arrays for the quotes and their authors
    private String[] quotes, authors;
    // See the mouseReleased() function
    private boolean clickActive, loading, isChoice1;

    private PFont peachDays;

    private int[] points;

    private int screen;

    private boolean debugMode;

    public GUI() {
        screen = 0;
    }

    public GUI(boolean debugMode) {
        screen = 0;

    }

    @Override
    public void setup() {
        this.debugMode = true;
        // Window setup
//        surface.setResizable(true);

        // API Manager Init
        api = new APIManager(this);

        points = new int[10];

        // Init quote arrays
        quotes = new String[2];
        authors = new String[2];

        // Home screen  init
        beginButton = new Button((float) (width * .3), (float) (height * .7), (float) (width * .4), (float) (height * .17), 30, "BEGIN", this);
        beginButton.setTextSize((int) (width * .04));

        // Game screen init
        choiceButton1 = new Button((float) (width * .1), (float) (height * .4), (float) (width * .35), (float) (height * .5), 30, "", this);
        choiceButton1.scaleTextSize(true);
        choiceButton2 = new Button((float) (width * .55), (float) (height * .4), (float) (width * .35), (float) (height * .5), 30, "", this);
        choiceButton2.scaleTextSize(true);

        selectionButton = new Button((float) (width * .25), (float) (height * .1), (float) (width * .5), (float) (height * .4), 30, "", this);
        selectionButton.scaleTextSize(true);

        isChoice1 = true;

        clickActive = false;

        // Init fonts
        peachDays = createFont("PeachDays.ttf", 50);

        System.out.println(width + ", " + height);
    }

    @Override
    public void draw() {
        // Leave this here
        checkDebugExit();


        switch (screen) {

            case 0:
                homeScreen();
                break;
            case 1:
                gameScreen();
                break;
            case 2:
                resultScreen();
                break;
            case 3:
                break;
            default:
                Client.printToConsole("Invalid Screen Number: " + screen + ", Defaulting to 0");
                screen = 0;
                break;
        }

        // If we are loading something, the loading screen should go on top of the current screen
        if (loading)
            loadingScreen();

        if (debugMode)
            debugDisplay();

        // This refers to the way we register clicks -- see where it is declared
        clickActive = false;
    }

    private void homeScreen() {

        //background
        background(29, 194, 139);


        // Title text settings
        textAlign(CENTER);
        textFont(peachDays);
        textSize((int) (width * .05));

        int titleTextX = width / 2;
        int titleTextY = (int) (height * .3);

        fill(0);
        text("FINDING YOUR SOULMATE FROM THE PAST", titleTextX + (int) (width * .005), titleTextY + (int) (height * .005));
        fill(255);
        text("FINDING YOUR SOULMATE FROM THE PAST", titleTextX, titleTextY);
        textSize((int) (width * .035));
//        text("better hope its not someone who committed genocide...", titleTextX, (int) (titleTextY * 1.3));


        beginButton.drawButton();

        if (loading) {
            getNewQuotes();
            loading = false;

            // Switching to the game screen
            screen = 1;
        }

        // Going to the game screen once the user has pressed the button
        if (beginButton.mouseOnButton() && clickActive) {


            // Displaying the loading screen before we
            loading = true;


        }

    }

    private void gameScreen() {
        background(29, 194, 139);

        fill(20);
        textSize((int) (width * .04));
        text("Choose which quote appeals to your soul...", width / 2, (int) (height * .2));


        //do thing

        fill(150);
        choiceButton1.setButtonText(quotes[0]);
        choiceButton1.drawButton();

        choiceButton2.setButtonText(quotes[1]);
        choiceButton2.drawButton();


        if (choiceButton1.mouseOnButton() && clickActive) {
            screen = 2;
            isChoice1 = true;
        } else if (choiceButton2.mouseOnButton() && clickActive) {
            screen = 2;
            isChoice1 = false;

        }

    }

    public void mouseReleased() {

        clickActive = true;
    }

    private void resultScreen() {
        background(210);

        int choiceIndex;

        if (isChoice1)
            choiceIndex = 0;
        else
            choiceIndex = 1;

        selectionButton.setButtonText(quotes[choiceIndex]);

        float[] buttonDimensions = selectionButton.getDimensions();
        fill(49, 214, 159);
        rect(buttonDimensions[0],(float)(buttonDimensions[1]*2.2),buttonDimensions[2],buttonDimensions[3], 30);


        selectionButton.drawButton();
        float endYButton = (buttonDimensions[1] + buttonDimensions[3]);
        fill(20);
        text("Author: " + authors[choiceIndex], width / 2, (endYButton) + (int) (height * .05));


    }

    private void getNewQuotes() {
        for (int i = 0; i < quotes.length; i++) {
            String[] data = api.getRandomQuote();
            quotes[i] = data[0];
            authors[i] = data[1];
        }
    }

    private void checkDebugExit() {
        // Allows the program to be closed by pressing escape
        if (keyPressed && keyCode == ESC)
            exit();
    }

    @Override
    public void settings() {
//        size(500,500);
        fullScreen();
    }

    private void loadingScreen() {
        background(255);

        fill(0);
        textSize(75);
        text("Loading...", width / 2, height / 2);

        textAlign(CENTER);
        textFont(peachDays);

    }

    public PApplet getPApplet() {
        return this;
    }

    private void debugDisplay() {


        fill(255);
        rect(0, 0, 100, 100);
        fill(0);
        textSize(50);
        text(screen, 50, 50);

    }
}


class Button {

    private float x, y, width, height, cornerRadius, textSize;
    private boolean scaleTextSize;
    private String text;
    private PApplet p;
    private String shape;

    public Button(float x, float y, float width, float height, float cornerRadius, String text, PApplet p) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.cornerRadius = cornerRadius;
        this.text = text;
        this.p = p;
        textSize = 36;
        scaleTextSize = false;
        shape = "rectangle";
    }

    public boolean mouseOnButton() {

        if (p.mouseX > x && p.mouseX < (x + width)) {
            return p.mouseY > y && p.mouseY < (y + height);
        }
        return false;
    }

    public void setButtonText(String text) {
        this.text = text;
    }

    public void scaleTextSize(boolean scaleTextSize) {
        this.scaleTextSize = scaleTextSize;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    public void drawButton() {

        switch (shape) {
            case "rectangle" -> {
                p.fill(85, 153, 217);
                p.stroke(255);
                p.strokeWeight((int) (width * .004));
                p.rect(x, y, width, height, cornerRadius);


                // Putting the text in the center of the button
                p.fill(90, 0, 0);
                p.textAlign(p.CENTER, p.CENTER);
                if (scaleTextSize) {
                    p.textSize(50);
                } else
                    p.textSize(textSize);

                // Inking
                p.fill(0);
                p.text(text, x + ((int) (width * .005)), y + ((int) (height * .005)), width, height);

                // Text
                p.fill(230);
                p.text(text, x, y, width, height);
            }
            case "triangle" ->
                    p.triangle((float) (width * .92), (float) (height * .2), (float) (width * .92), (float) (height * .8), (float) (width * .98), (float) (height * .5));
        }
    }

    public void setShape(String shape) {
        this.shape = shape;
    }

    public float[] getDimensions() {
        return new float[]{
                x,
                y,
                width,
                height
        };
    }


}