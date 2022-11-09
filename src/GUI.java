import processing.core.PApplet;
import processing.core.PFont;

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
    // See the mouseReleased() function
    private boolean clickActive, loadNewQuotes, isChoice1;

    private PFont peachDays;

    // Points is an int[] array. It counts the points for the authors. points[0] is the point count for api.getAuthorIndex(author)
    private int[] authorPoints;

    //number of bars on the results graph
    private final int NUM_BARS = APIManager.NUM_AUTHORS_IN_GAME;

    private int screen;

    //counts rounds of the game
    private int round;

    public GUI() {
        screen = 0;
    }

    @Override
    public void setup() {
        // Window setup
//        surface.setResizable(true);

        // API Manager Init
        api = new APIManager(this);
        api.generateAuthorList();

        authorPoints = new int[APIManager.NUM_AUTHORS_IN_GAME];

        // Init quote arrays
        quotes = new String[2];
        authors = new String[2];

        round = -1;

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
                (float) (height * .4),
                (float) (width * .2),
                (float) (height * .2),
                0,
                "",
                this
        );


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



        returnButton.setShape("triangle");

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

        if (loadNewQuotes) {
            round++;
            if(round==3){
                screen=3;
            }
            getNewQuotes();
            loadNewQuotes = false;
        }


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
                finalScreen();
                break;
            default:
                System.out.println("Invalid Screen Number: " + screen + ", Defaulting to 0");
                screen = 0;
                break;
        }

        // This will be set true by one of the the screen methods
        // The loading screen will be displayed
        // At the top of draw(), we will actually load the quotes, after the loading screen is displayed
        if (loadNewQuotes)
            loadingScreen();


        // This refers to the way we register clicks -- see where it is declared
        clickActive = false;

        fill(0);
        text(round, 100, 100);
    }

    private void homeScreen() {

        //background
        background(29, 194, 139);

        // Author list
        drawAuthorList((float) (width * .05), (float) (height * .6), 1);


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

        beginButton.drawButton();


        // Going to the game screen once the user has pressed the button
        if (beginButton.mouseOnButton() && clickActive) {

            // This will display the loading screen then load the quotes
            loadNewQuotes = true;

            // Going to the game screen after the quotes have been loaded
            screen = 1;


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

            // Incrementing that authors point count by one
            authorPoints[api.getAuthorIndex(authors[0])]++;
        } else if (choiceButton2.mouseOnButton() && clickActive) {
            screen = 2;
            isChoice1 = false;

            // Incrementing that authors point count by one
            authorPoints[api.getAuthorIndex(authors[1])]++;
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


        // If the author's point count is zero, we add a bar
//        authorPoints[api.getAuthorIndex(authors[choiceIndex])]++;


        selectionButton.setButtonText(quotes[choiceIndex]);

        float[] buttonDimensions = selectionButton.getDimensions();
        fill(49, 214, 159);
        rectMode(CORNER);
        rect(
                buttonDimensions[0],
                (float) (buttonDimensions[1] * 2.2),
                buttonDimensions[2],
                buttonDimensions[3],
                30);


        selectionButton.drawButton();
        float endYButton = (buttonDimensions[1] + buttonDimensions[3]);
        fill(20);
        text("Author: " + authors[choiceIndex], width / 2, (endYButton) + (int) (height * .05));


        returnButton.drawButton();
        if (returnButton.mouseOnButton() && clickActive) {
            screen = 1;
            loadNewQuotes = true;
        }


        // Drawing the graph at the x and y coordinate provided
        drawGraph(width / 2, (float) (height * .8), width / 2, (float) (height * .3));


    }


    private void finalScreen(){

        background(210);

        textSize((float)(width*.04));
        text("YOUR SOULMATE IS...", (float)(width*0.5), (float)(height*0.15));
        int authorIndex=0;
        for(int i=0;i<4;i++){
            if(authorPoints[i]>authorIndex){
                authorIndex=authorPoints[i];
            }
        }

        int tieChecker=0;
        for(int i=0;i<4;i++){
            if(authorPoints[i]==authorIndex){
                tieChecker++;
            }
        }

        String soulmate;


        if(tieChecker>1){
            soulmate="A whole bunch of people you polyamorous fellow";
        } else{
            soulmate=api.authorList[authorIndex-1];
        }

        text(soulmate,(float)(width*0.5), (float)(height*0.24));



        drawGraph(
                (float)(width*0.5),
                (float)(height*0.55),
                (float)(width*0.6),
                (float)(height*0.4)
        );

        playAgainButton.drawButton();

        if (playAgainButton.mouseOnButton() && clickActive) {

            // This will display the loading screen then load the quotes
            loadNewQuotes = true;

            // Going to the game screen after the quotes have been loaded
            screen = 0;
            round=-1;
        }



    }


    private void getNewQuotes() {


        quotes[0] = api.getQuotesForRound(round)[0];
        authors[0] = api.getAuthorsForRound(round)[0];

        quotes[1] = api.getQuotesForRound(round)[1];
        System.out.println(quotes[1]);
        authors[1] = api.getAuthorsForRound(round)[1];

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


    private void drawGraph(float graphX, float graphY, float graphWidth, float graphHeight) {

        // Drawing the graph's box
        rectMode(CENTER);
        rect(graphX, graphY, graphWidth, graphHeight, (float) (width * .02));


        rectMode(CORNER);
        for (int i = 0; i < NUM_BARS; i++) {
//            rect(
//                    (float) (graphX - graphWidth*.45 /*+ ((graphWidth*.9) * ( authorPoints[i]/getTotalPoints()))*//2),
//                    (float) ((graphY - graphHeight*.45) + i*((graphHeight*.9) / bars)  +graphHeight*0.05),
//
//                    (float) ( (graphWidth*.9) * (authorPoints[i]/getTotalPoints())),
//                    (float) ( (graphHeight*.9) / (bars + bars*graphHeight*0.001)   )
//            );

            rect(
                    (float) (graphX - graphWidth * .45),
                    (float) ((graphY - graphHeight * .45) + i * ((graphHeight * .9) / NUM_BARS) + graphHeight * .05),

                    (float) ((graphWidth * .9) * ((float) authorPoints[i] / getTotalPoints())),
                    (float) ((graphHeight * .9) / (NUM_BARS + NUM_BARS * graphHeight * 0.001))
            );


//            System.out.println("x: " + (graphX - graphWidth * .45));
//            System.out.println("y: " + ((graphY - graphHeight * .45) + i * ((graphHeight * .9) / NUM_BARS) + graphHeight * .05));
//            System.out.println("AUTHOR [" + i + "] POINT COUNT:" + authorPoints[i]);
//            System.out.println("TOTAL POINTS: " + NUM_BARS);
//            System.out.println("[" + i + "]   (" + (graphWidth * .9) + ") * (" + authorPoints[i] + " / " + getTotalPoints() + ")");
//            System.out.println("height: " + (graphHeight * .9) / (NUM_BARS + NUM_BARS * graphHeight * 0.001));
        }


        color(0);
        line(
                (float) (graphX - graphWidth * .45),
                (float) (graphY - graphHeight * .45),
                (float) (graphX - graphWidth * .45),
                (float) (graphY + graphHeight * .45));

    }

    private void drawAuthorList(float x, float y, float scale) {

        float listWidth = (float) (width * .2 * scale);
        float listHeight = (float) (width * .2 * scale);


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


        fill(200);
        rectMode(CORNER);
        rect(x, y, listWidth, listHeight, (float) (width * .02));

        fill(0);
        textSize((int) (width * .03));
        text("AUTHORS", x + listWidth / 2, y + listHeight / 8);

        // Listing the actual number of authors
        // The distance between the authors gets smaller based on the number of authors
        // This is optimized for 4 authors -- may bug out if more are added but figure I might try
        float listYIncrement = listHeight / (APIManager.NUM_AUTHORS_IN_GAME + 4);

        textSize((int) (width * .015));
        for (int i = 0; i < APIManager.NUM_AUTHORS_IN_GAME; i++) {
            text(
                    api.getAuthorList()[i],
                    x + listWidth / 2,
                    (float) ((y + listHeight / 3.5) + (listYIncrement * (i)))
            );
        }

        randomizeAuthorsButton.drawButton();


        // Randomize button functionality
        if (randomizeAuthorsButton.mouseOnButton() && clickActive)
            api.generateAuthorList();

    }

    public PApplet getPApplet() {
        return this;
    }

    private int getTotalPoints() {
        int total = 0;
        for (int num : authorPoints)
            total += num;
        return total;
    }
}


class Button {

    private float x, y, width, height, cornerRadius;
    private boolean scaleTextSize;
    private String text;
    private PApplet p;
    private String shape;
    private int rectMode, textSize;

    // Rectangle button init
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
        rectMode = p.CORNER;

    }

    public boolean mouseOnButton() {

        switch (rectMode) {
            case 0 /*CORNER*/ -> {
                if (p.mouseX > x && p.mouseX < (x + width))
                    return p.mouseY > y && p.mouseY < (y + height);
                return false;
            }
            case 3 /*CENTER*/ -> {

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

    public void scaleTextSize(boolean scaleTextSize) {
        this.scaleTextSize = scaleTextSize;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    public void drawButton() {

        switch (shape) {
            case "rectangle" -> {


                p.rectMode(rectMode);
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


                float textX = x;
                float textY = y;
                float textWidth = width;
                float textHeight = height;

                // Inking
                p.fill(0);
                p.text(text, textX + ((int) (width * .005)), textY + ((int) (height * .005)), textWidth, textHeight);

                // Text
                p.fill(230);
                p.text(text, textX, textY, textWidth, textHeight);
            }
            case "triangle" ->
                    p.triangle((float) (width * 4.5), (float) (height), (float) (width * 4.5), (float) (height * 4), (float) (width * 4.8), (float) (height * 2.5));
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
