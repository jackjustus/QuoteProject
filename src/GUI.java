import processing.core.PApplet;
import processing.core.PFont;

import java.util.Arrays;

public class GUI extends PApplet {

    private final int BUTTON_TEXT_SIZE = (int) (width * .4);

    private APIManager api;

    // Home-screen button
    private Button beginButton;
    private Button randomizeAuthorsButton;
    private Button choiceButton1;
    private Button choiceButton2;
    private Button selectionButton;
    private Button returnButton;
    private Button playAgainButton;

    // String arrays for the quotes and their authors
    private String[] quotes, authors;

    // State boolean variables
    private boolean clickActive, loadNewQuotes;

    // Font used for the project
    private PFont peachDays;

    // Points is an int[] array. It counts the points for the authors. points[0] is the point count for api.getAuthorIndex(author)
    private int[] authorPoints;

    // int vars
    private int screen, round, choiceSelected;

    public GUI() {
        screen = 0;
    }

    @Override
    public void setup() {

        // API Manager Init
        api = new APIManager();
        api.generateAuthorList();

        // Creating the authorPoints array with the same size as the number of authors in the game
        authorPoints = new int[APIManager.NUM_AUTHORS_IN_GAME];

        // Arrays used to hold the quotes and authors for the rounds
        quotes = new String[2];
        authors = new String[2];

        // The round starts at -1 and increments to zero when we hit the begin button
        round = -1;

        // Giving all the buttons in the game their coordinates and size
        initButtons();

        // Boolean used in the resultsScreen()
        choiceSelected = 0;

        // This is used to test if a click is valid, and prevents unexpected behavior with clicking buttons
        clickActive = false;

        // Creating the font used in the game
        peachDays = createFont("PeachDays.ttf", 50);
    }

    @Override
    public void draw() {

        /*
         We load new quotes by setting loadNewQuotes to true,
         so that the loading screen can render then we actually get the new quotes from the api.
         The loading screen is displayed at the bottom of draw() (highest priority),
         and then and the beginning of the next frame we load the quotes.
         This is because the program freezes as we load the quotes,
         so we cant just put the loading screen up and load the quotes in the same frame, so it's split into two
         */
        if (loadNewQuotes) {

            // When we load new quotes, we increment the round
            round++;

            // We then check to see if we are finished with the game (round == 8)
            // Screen 3 is the game over screen
            if (round == 8)
                screen = 3;

            // This loads the new quotes into the author and quotes arrays
            getNewQuotesAndAuthors();

            // Resetting the boolean that triggers this
            loadNewQuotes = false;

            // Shuffling the quotes that we get
            if (Math.random() < .5) {
                String temp = quotes[0];
                quotes[0] = quotes[1];
                quotes[1] = temp;
            }
        }

        // Making the screen variable do its job
        switch (screen) {
            case 0 -> homeScreen();
            case 1 -> gameScreen();
            case 2 -> resultScreen();
            case 3 -> finalScreen();
            default -> {
                System.out.println("Invalid Screen Number: " + screen + ", Defaulting to 0");
                screen = 0;
            }
        }

        // This will be set true by one of the screen methods
        // The loading screen will be displayed
        // At the top of draw(), we will actually load the quotes, after the loading screen is displayed
        if (loadNewQuotes)
            loadingScreen();


        // This refers to the way we register clicks -- see where it is declared
        clickActive = false;

    }

    private void homeScreen() {

        background(29, 194, 139);

        // Drawing the list of authors and allowing the user to change it
        drawAuthorList((float) (width * .05), (float) (height * .6));


        // Title text settings
        textAlign(CENTER);
        textFont(peachDays);
        textSize((int) (width * .05));

        int titleTextX = width / 2;
        int titleTextY = (int) (height * .3);

        // Inking and drawing the title
        fill(0);
        text("FINDING YOUR SOULMATE FROM THE PAST", titleTextX + (int) (width * .005), titleTextY + (int) (height * .005));
        fill(255);
        text("FINDING YOUR SOULMATE FROM THE PAST", titleTextX, titleTextY);
        textSize((int) (width * .035));

        // Drawing the beginButton, allowing the user to start the game
        beginButton.drawButton();

        // Checking if the beginButton was pressed
        if (beginButton.mouseOnButton() && clickActive) {
            // Getting new quotes and changing to the game screen
            loadNewQuotes = true;
            screen = 1;
        }

    }

    private void gameScreen() {

        background(29, 194, 139);

        // Title text
        fill(20);
        textSize((int) (width * .04));
        text("Choose which quote appeals to your soul...", width / 2, (int) (height * .2));

        // Drawing the buttons with the quotes inside them
        fill(150);
        choiceButton1.setButtonText(quotes[0]);
        choiceButton1.drawButton();
        choiceButton2.setButtonText(quotes[1]);
        choiceButton2.drawButton();

        // Making the quote buttons work
        if (choiceButton1.mouseOnButton() && clickActive) {
            screen = 2;
            choiceSelected = 1;

            // Incrementing that authors point count by one
            authorPoints[api.getAuthorIndex(authors[0])]++;
        } else if (choiceButton2.mouseOnButton() && clickActive) {
            screen = 2;
            choiceSelected = 2;

            // Incrementing that authors point count by one
            authorPoints[api.getAuthorIndex(authors[1])]++;
        }
    }

    public void mouseReleased() {
        // This is a processing function that is called when the mouse is released
        // It allows the buttons to be triggered
        clickActive = true;
    }

    private void resultScreen() {

        background(255);

        // Setting the quote display to the quote that was picked
        // The quote is displayed by using a dummy button
        selectionButton.setButtonText(quotes[choiceSelected - 1]);

        // This draws the smaller green rectangle below the quote that was choosen
        float[] buttonDimensions = selectionButton.getDimensions();
        fill(49, 214, 159);
        rectMode(CORNER);
        rect(
                buttonDimensions[0],
                (float) (buttonDimensions[1] * 2.2),
                buttonDimensions[2],
                buttonDimensions[3],
                30
        );
        // Due to layering the quote button, its drawn after the author rectangle
        selectionButton.drawButton();
        float endYButton = (buttonDimensions[1] + buttonDimensions[3]);
        fill(20);
        text("Author: " + authors[choiceSelected - 1], width / 2, (endYButton) + (int) (height * .05));
        returnButton.drawButton();

        // Checking to see if the returnButton was pressed
        if (returnButton.mouseOnButton() && clickActive) {
            screen = 1;
            loadNewQuotes = true;
        }


        // Drawing the graph at the x and y coordinate provided
        drawGraph(width / 2, (float) (height * .8), width / 2, (float) (height * .3));
    }


    private void finalScreen() {

        background(29, 194, 139);

        // Title text
        textSize((float) (width * .04));
        text("YOUR SOULMATE IS...", (float) (width * 0.5), (float) (height * 0.15));

        // Finding out how many points the winning author got
        int winningAuthorPointCount = 0;
        for (int i = 0; i < 4; i++)
            if (authorPoints[i] > winningAuthorPointCount)
                winningAuthorPointCount = authorPoints[i];

        // Checking if there is a tie by comparing the winning number of points with the rest of the authors
        int tieChecker = 0;
        for (int i = 0; i < 4; i++)
            if (authorPoints[i] == winningAuthorPointCount)
                tieChecker++;

        // Setting the soulmate string to the name of the winning author
        String soulmate;
        if (tieChecker > 1)
            soulmate = "A whole bunch of people you polyamorous fellow";
        else
            soulmate = api.authorList[winningAuthorPointCount - 1];

        // Drawing the soulmate below the title
        text(soulmate, (float) (width * 0.5), (float) (height * 0.24));

        // Drawing the graph for reference
        drawGraph(
                (float) (width * 0.5),
                (float) (height * 0.55),
                (float) (width * 0.6),
                (float) (height * 0.4)
        );

        // Allowing the user to play again
        playAgainButton.drawButton();
        if (playAgainButton.mouseOnButton() && clickActive) {

            // This resets the variables needed to play the game again and generates a new authorList
            screen = 0;
            round = -1;
            Arrays.fill(authorPoints, 0);
            api.generateAuthorList();
        }
    }


    private void getNewQuotesAndAuthors() {

        // Uses the APIManager to get the authors and quotes for the round
        quotes[0] = api.getQuotesForRound(round)[0];
        authors[0] = api.getAuthorsForRound(round)[0];

        quotes[1] = api.getQuotesForRound(round)[1];
        authors[1] = api.getAuthorsForRound(round)[1];

    }

    @Override
    public void settings() {
        // This is the first thing that runs, and makes the program full screen
        fullScreen();
    }

    private void loadingScreen() {

        background(255);

        // Loading Text
        fill(0);
        textSize(75);
        textAlign(CENTER, CENTER);
        textFont(peachDays);
        text("Loading...", width / 2, height / 2);
    }


    private void drawGraph(float graphX, float graphY, float graphWidth, float graphHeight) {

        // Drawing the graph's box
        rectMode(CENTER);
        fill(85, 153, 217);
        stroke(0);
        rect(graphX, graphY, graphWidth, graphHeight, (float) (width * .02));


        // Calculating the highest point count among the authors
        float highestPointCount = 0;
        for (int num : authorPoints)
            if (num > highestPointCount)
                highestPointCount = num;


        rectMode(CORNER);
        //number of bars on the results graph
        int NUM_BARS = APIManager.NUM_AUTHORS_IN_GAME;

        // Drawing each of the bars on the graph
        for (int i = 0; i < NUM_BARS; i++) {

            // Using simple but convoluted math to determine where the bars should be drawn
            float barX = (float) (graphX - graphWidth * .45);
            float barY = (float) ((graphY - graphHeight * .45) + i * ((graphHeight * .9) / NUM_BARS) + graphHeight * .05);
            float barWidth = (float) ((graphWidth * .9) * ((float) authorPoints[i] / highestPointCount));
            float barHeight = (float) ((graphHeight * .9) / (NUM_BARS + NUM_BARS * graphHeight * 0.001));

            // Drawing the actual bar of the graph
            fill(0);
            stroke(255);
            rect(
                    barX,
                    barY,
                    barWidth,
                    barHeight
            );

            // Labeling the bar with the author's name
            String author = api.authorList[i];
            fill(255);
            textSize((float) (graphWidth * .02));
            textAlign(LEFT, CENTER);
            text(author + ": " + authorPoints[i], barX + graphWidth / 16 + barWidth / 8, barY + barHeight / 2);
        }

        // Drawing the line (Stylizing the graph)
        color(0);
        line(
                (float) (graphX - graphWidth * .45),
                (float) (graphY - graphHeight * .45),
                (float) (graphX - graphWidth * .45),
                (float) (graphY + graphHeight * .45));

    }

    private void drawAuthorList(float x, float y) {
        // This is used on the home-screen to inform the player of which authors they have in the game

        float listWidth = (float) (width * .2);
        float listHeight = (float) (width * .2);

        // init randomizeButton
        if (randomizeAuthorsButton == null) {
            randomizeAuthorsButton = new Button(
                    x + listWidth / 2,
                    (y + listHeight) - listHeight / 6,
                    listWidth / 2,
                    listHeight / 8,
                    ((float) (width * .01)),
                    "Randomize",
                    this
            );
            randomizeAuthorsButton.setRectMode(CENTER);
            randomizeAuthorsButton.setTextSize((int) (width * .015));
        }

        // Drawing the rectangle for the list
        fill(200);
        rectMode(CORNER);
        rect(x, y, listWidth, listHeight, (float) (width * .02));

        // Drawing the title
        fill(0);
        textSize((int) (width * .03));
        text("AUTHORS", x + listWidth / 2, y + listHeight / 8);

        // Listing the actual number of authors
        // The distance between the authors gets smaller based on the number of authors
        // This is optimized for 4 authors -- may bug out if more are added but figure I might try a little bit
        float listYIncrement = listHeight / (APIManager.NUM_AUTHORS_IN_GAME + 4);

        // Drawing the authors names
        textSize((int) (width * .015));
        for (int i = 0; i < APIManager.NUM_AUTHORS_IN_GAME; i++) {
            text(
                    api.getAuthorList()[i],
                    x + listWidth / 2,
                    (float) ((y + listHeight / 3.5) + (listYIncrement * (i)))
            );
        }

        // Drawing the randomize button
        randomizeAuthorsButton.drawButton();


        // Randomize button functionality
        if (randomizeAuthorsButton.mouseOnButton() && clickActive)
            api.generateAuthorList();

    }

    private void initButtons() {

        // Home screen init
        beginButton = new Button(
                (float) (width * .3),
                (float) (height * .7),
                (float) (width * .4),
                (float) (height * .17),
                30,
                "BEGIN",
                this);
        beginButton.setTextSize((int) (width * .04));

        // Game screen init
        choiceButton1 = new Button(
                (float) (width * .1),
                (float) (height * .4),
                (float) (width * .35),
                (float) (height * .5),
                30,
                "",
                this);
        choiceButton1.setTextSize(BUTTON_TEXT_SIZE);

        choiceButton2 = new Button(
                (float) (width * .55),
                (float) (height * .4),
                (float) (width * .35),
                (float) (height * .5),
                30,
                "",
                this);
        choiceButton2.setTextSize(BUTTON_TEXT_SIZE);

        // Results screen init
        selectionButton = new Button(
                (float) (width * .25),
                (float) (height * .1),
                (float) (width * .5),
                (float) (height * .4),
                30,
                "",
                this);
        selectionButton.setTextSize(50);

        returnButton = new Button(
                (float) (width * .8),
                (float) (height * .2),
                (float) (width * .2),
                (float) (height * .6),
                0,
                "",
                this
        );

        // Final screen init
        playAgainButton = new Button(
                (float) (width * .35),
                (float) (height * .8),
                (float) (width * .3),
                (float) (height * .17),
                30,
                "PLAY AGAIN",
                this
        );
        playAgainButton.setTextSize(BUTTON_TEXT_SIZE);

        // Weird triangle button init
        returnButton.setShape("triangle");
    }
}


class Button {

    private final float x, y, width, height, cornerRadius;
    private String text, shape;
    private final PApplet p;
    private int rectMode, textSize;

    public Button(float x, float y, float width, float height, float cornerRadius, String text, PApplet p) {

        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.cornerRadius = cornerRadius;
        this.text = text;
        this.p = p;
        textSize = 36;
        shape = "rectangle";
        rectMode = p.CORNER;

    }

    public boolean mouseOnButton() {

        // Detecting if the mouse is over the button is different depending on the rectMode o the button
        // (If the position is based on the center or corner of the button
        switch (rectMode) {
            case 0 /*CORNER*/ -> {
                // Straightforward checking for corner mode
                if (p.mouseX > x && p.mouseX < (x + width))
                    return p.mouseY > y && p.mouseY < (y + height);
                return false;
            }
            case 3 /*CENTER*/ -> {

                // Getting the coordinates for the corner of the rect, then checking if it hits
                float x1 = x - width / 2;
                float y1 = y - height / 2;
                float x2 = x + width / 2;
                float y2 = y + height / 2;

                if (p.mouseX > x1 && p.mouseX < x2)
                    return p.mouseY > y1 && p.mouseY < y2;
                return false;
            }
            default -> {
                System.out.println("RECT MODE INVALID IN Button.mouseOnButton()");
                p.exit();
                return false;
            }
        }
    }

    public void setButtonText(String text) {
        this.text = text;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    public void drawButton() {

        switch (shape) {
            case "rectangle" -> {

                // Styling
                p.rectMode(rectMode);
                p.fill(85, 153, 217);
                p.stroke(255);
                p.strokeWeight((int) (width * .004));
                p.rect(x, y, width, height, cornerRadius);


                // Putting the text in the center of the button
                p.fill(90, 0, 0);
                p.textAlign(p.CENTER, p.CENTER);
                p.textSize(textSize);

                // Vars to change the things
                float textBuffer = (float) (width * .01);
                float textX = x + textBuffer;
                float textY = y + textBuffer;
                float textWidth = width - textBuffer;
                float textHeight = height - textBuffer;

                // Inking
                p.fill(0);
                p.text(text, textX + ((int) (width * .005)), textY + ((int) (height * .005)), textWidth, textHeight);

                // Text
                p.fill(230);
                p.text(text, textX, textY, textWidth, textHeight);
            }
            case "triangle" -> {

                // The triangle has caused us so much unnecessary trouble, so I just decided to throw this together
                p.pushMatrix();

                p.fill(85, 153, 217);
                p.stroke(0);
                p.strokeWeight((int) (width * .01));

                // This draws the actual triangle by putting three vertices in the wrong spot for some reason
                // Then it connects them and fills it in
                p.beginShape();
                p.vertex((float) (p.width * .8),
                        (float) (p.height * .2));
                p.vertex((float) (p.width * .9),
                        (float) (p.height * .5));
                p.vertex((float) (p.width * .8),
                        (float) (p.height * .8));
                p.endShape(p.CLOSE);

                p.popMatrix();
            }
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

    public void setRectMode(int rectMode) {
        this.rectMode = rectMode;
    }
}
