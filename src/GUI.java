import processing.core.PApplet;

public class GUI extends PApplet {

    APIManager api;

    int screen;


    // Home-screen button
    Button beginButton;

    Button choiceButton1;
    Button choiceButton2;

    boolean isChoice1;

    String[] quotes;
    String[] authors;

    boolean mouseReleased;
    boolean buttonFunctionalityActive;

    public GUI() {

        screen = 0;

    }

    // TODO: REMOVE
    public GUI(boolean noOutput) {

    }

    @Override
    public void setup() {

        api = new APIManager(this);

        // Init quote arrays
        quotes = new String[2];
        authors = new String[2];

        // Home screen  init
        beginButton = new Button((float) (width * .3), (float) (height * .7), (float) (width * .4), (float) (height * .17), 30, "BEGIN", this);

        // Game screen init
        choiceButton1 = new Button((float) (width * .1), (float) (height * .4), (float) (width * .35), (float) (height * .5), 30, "", this);
        choiceButton2 = new Button((float) (width * .55), (float) (height * .4), (float) (width * .35), (float) (height * .5), 30, "", this);
        isChoice1 = true;
        mouseReleased = false;


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
                resultScreen();
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

        buttonFunctionalityActive = true;

        //background
        background(255, 200, 200);


        //title
        textAlign(CENTER);
        textSize(50);
        fill(90, 0, 0);
        text("FINDING YOUR SOULMATE FROM THE PAST", width / 2, height / 2);
        text("better hope its not someone who committed genocide...", width / 2, (int) (height * .6));


        beginButton.drawButton();


        // Going to the game screen once the user has pressed the button
        if (beginButton.mouseOnButton(buttonFunctionalityActive) && mousePressed) {


            // Generating new quotes for the game screen
            getNewQuotes();

            // Switching to the game screen
            screen = 1;
        }

    }


    private void gameScreen() {
        buttonFunctionalityActive = false;
        background(100);

        fill(90, 0, 0);
        text("Choose which quote appeals to your soul...", width / 2, height / 3);


        //do thing

        fill(150);
        choiceButton1.setButtonText(quotes[0]);
        choiceButton1.drawButton();

        choiceButton2.setButtonText(quotes[1]);
        choiceButton2.drawButton();

        if (mouseReleased) {
            buttonFunctionalityActive = true;
        }

        if (choiceButton1.mouseOnButton(buttonFunctionalityActive && mousePressed)) {

            screen = 2;
            isChoice1 = true;
        } else if (choiceButton2.mouseOnButton(buttonFunctionalityActive) && mousePressed) {
            screen = 2;
            isChoice1 = false;

        }

    }

    public void mouseReleased() {
        mouseReleased = true;
    }

    private void resultScreen() {
        background(255);


        //opperating under the assumption that the user will choose one of hitlers quotes
        text("HA HITLER", width / 2, height / 2);


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
        fullScreen();
    }

    public PApplet getPApplet() {
        return this;
    }
}


class Button {

    private float x, y, width, height, cornerRadius;
    private String text;
    private PApplet p;

    public Button(float x, float y, float width, float height, float cornerRadius, String text, PApplet p) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.cornerRadius = cornerRadius;
        this.text = text;
        this.p = p;
    }


    public boolean mouseOnButton(boolean exists) {
        if (!exists) {
            return false;
        }
        if (p.mouseX > x && p.mouseX < (x + width)) {
            return p.mouseY > y && p.mouseY < (y + height);
        }
        return false;
    }

    public void setButtonText(String text) {
        this.text = text;
    }


    public void drawButton() {


        p.fill(100);
        p.rect(x, y, width, height, cornerRadius);


        // Putting the text in the center of the button
        p.fill(90, 0, 0);
//        p.textAlign(p.CENTER, p.CENTER);
        p.textAlign(p.CENTER, p.CENTER);
        p.text(text, x, y, width, height);

    }


}