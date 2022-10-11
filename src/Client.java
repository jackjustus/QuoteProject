import processing.core.PApplet;

public class Client extends PApplet {

    public static void main(String[] args) {
        PApplet.main("Client");


    }

    GUI gui = new GUI(this);

    @Override
    public void settings() {
        fullScreen();
        super.settings();
    }

    @Override
    public void setup() {
        super.setup();
    }

    @Override
    public void draw() {
        super.draw();

        gui.displayupsiddownMonkey();


    }


}
