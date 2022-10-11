import processing.core.PApplet;
import processing.core.PImage;

public class GUI {

    PApplet p;

    PImage image;

    public GUI(PApplet p) {


        this.p = p;
    }

    public void displayupsiddownMonkey() {
        image = p.loadImage("funny-cartoon-monkey-chimpanzee-hanging-upside-down-vector-illustration-2BXENAK.jpeg");

        p.image(this.image, 0, 0);
    }
}
