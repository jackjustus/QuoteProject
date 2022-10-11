import okhttp3.OkHttpClient;
import processing.core.PApplet;
import processing.core.PImage;

public class APIManager {

    PApplet p;

    String[] urls;

    OkHttpClient client = new OkHttpClient();

    public APIManager(PApplet p) {
        this.p = p;

        urls = new String[]{
                "http://api.forismatic.com/api/1.0/",
                "https://api.kanye.rest/",
                "https://kimiquotes.herokuapp.com[endpoint]",
                "https://quote-garden.herokuapp.com/api/v3/quotes",
                "https://api.themotivate365.com/stoic-quote"
        };

    }

    public String[] getRandomAPI() {


        return new String[0];
    }

    public PImage getRandomPhoto() {

        return new PImage();
    }


}
