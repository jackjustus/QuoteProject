import processing.core.PApplet;
import processing.core.PFont;

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
        this.debugMode = debugMode;
    }

    @Override
    public void setup() {

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
        text("better hope its not someone who committed genocide...", titleTextX,(int)(titleTextY *1.3));


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
        background(100);

        fill(90, 0, 0);
        text("Choose which quote appeals to your soul...", width / 2, height / 3);


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
        background(255);


        if (isChoice1)
            selectionButton.setButtonText(quotes[0]);

        else
            selectionButton.setButtonText(quotes[1]);


        selectionButton.drawButton();


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
        text("Loading", width / 2, height / 2);

        textAlign(CENTER);
        textFont(peachDays);
        textSize(75);


    }

    public PApplet getPApplet() {
        return this;
    }

    private void debugDisplay() {

        textFont(createFont("ProcessingSansPro-Regular.ttf", 20));

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

    public void drawButton() {

        switch (shape) {
            case "rectangle" -> {
                p.fill(100);
                p.rect(x, y, width, height, cornerRadius);


                // Putting the text in the center of the button
                p.fill(90, 0, 0);
                p.textAlign(p.CENTER, p.CENTER);
                if (scaleTextSize) {
//                    p.textWidth(text);

                } else
                    p.textSize(textSize);
                p.text(text, x, y, width, height);
            }
            case "triangle" ->
                    p.triangle((float) (width * .92), (float) (height * .2), (float) (width * .92), (float) (height * .8), (float) (width * .98), (float) (height * .5));
        }
    }

    public void setShape(String shape) {
        this.shape = shape;
    }


}