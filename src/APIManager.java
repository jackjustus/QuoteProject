import okhttp3.Request;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import processing.core.PApplet;
import processing.core.PImage;

import java.io.IOException;

public class APIManager {

    PApplet p;

    String[] urls;

    OkHttpClient client = new OkHttpClient();

    public APIManager(PApplet p) {
        this.p = p;


        urls = new String[]{
                "http://api.forismatic.com/api/1.0/", // 0
                "https://api.kanye.rest/", // 1
                "https://kimiquotes.herokuapp.com[endpoint]", // 2
                "https://quote-garden.herokuapp.com/api/v3/quotes", // 3
                "https://api.themotivate365.com/stoic-quote" // 4
        };
    }

    public String[][] getRandomAPI() {

        int rand = (int) (urls.length * Math.random() + 1);

        String[][] data = new String[2][];

        /*
        [0] - API ID
        [1] - API DATA
         */

        data[0][0] = rand + "";
        data[1] = urls;

        return data;
    }

    public PImage getRandomPhoto() {

        return new PImage();
    }

    public void fetchForismaticAPI() throws JSONException {

        Request request = new Request.Builder()
                .url("http://api.forismatic.com/api/1.0/method=getQuote&format=json&lang=en")
                .get()
//                .addHeader("authorization", "Bearer" + " " + accessToken)
//                .addHeader("cache-control", "no-cache")
//                .addHeader("postman-token", "b5fc33ce-3dad-86d7-6e2e-d67e14e8071b")
                .build();

        try {
            Response response = client.newCall(request).execute();

            JSONObject jsonObject = new JSONObject(response.body().string().trim());       // parser
            JSONArray myResponse = (JSONArray) jsonObject.get("businesses");
            for (int i = 0; i < 0; i++)
                System.out.println(myResponse.getJSONObject(i).getString("phone"));


        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

//
    }

}
