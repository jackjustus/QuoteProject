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

    public String getRandomAPI() {

        int rand = (int) (urls.length * Math.random() + 1);

        String data = "";

        switch (rand) {
            case 0 -> data = fetchForismaticAPI();
            case 1 -> data = fetchKanyeAPI();
        }

        return data;
    }


    // TODO: Remove
    @Deprecated
    public String fetchForismaticAPI() {

        Request request = new Request.Builder()
                .url("http://api.forismatic.com/api/1.0/method=getQuote&format=xml&lang=en")
                .get()
//                .addHeader("authorization", "Bearer" + " " + accessToken)
//                .addHeader("cache-control", "no-cache")
//                .addHeader("postman-token", "b5fc33ce-3dad-86d7-6e2e-d67e14e8071b")
                .build();

        try {
            Response response = client.newCall(request).execute();

            JSONObject jsonObject = new JSONObject(response.body().string().trim());       // parser
            JSONArray myResponse = (JSONArray) jsonObject.get("businesses");
            String quote = (myResponse.getJSONObject(0).getString("phone"));

            return quote;
        } catch (IOException | JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println("FORISMATIC API FETCH FAILED");

            // If the API Fails, the program should stop so we can troubleshoot the issue
            System.exit(1);

            // This line will never run
            return null;
        }
    }


    public String fetchKanyeAPI() {

        // Building the request to be sent to the API
        Request request = requestBuilder("https://api.kanye.rest/");


        try {
            // Attempting to communicate with the API
            // The response is stored in response
            Response response = fetchResponse(request);

            // Parsing the response to just the quote
            JSONObject jsonObject = parseResponse(response);
            String quote = jsonObject.get("quote") + "";

            // Returning the quote if there were no errors thrown
            return quote;
        } catch (IOException | JSONException e) {

            // If an error was thrown, the error should print in the console and identify itself
            e.printStackTrace();
            System.out.println("KANYE API FETCH FAILED");

            // It should then stop the program
            System.exit(1);

            // This line will never run - only needed to compile
            return null;
        }
    }

    public String fetchQuoteGardenAPI() {

        String author = "jesus";

        // Building the request to be sent to the API
        Request request = requestBuilder("https://quote-garden.herokuapp.com/api/v3/quotes/random?author=" + author);


        try {
            // Attempting to communicate with the API
            // The response is stored in response
            Response response = fetchResponse(request);

            // Parsing the response to just the quote
            JSONObject parsedResponse = parseResponse(response);
            JSONObject quoteData = (JSONObject) parsedResponse.get("data");
            String quote = quoteData.get("quoteText") + "";

            // Returning the quote if there were no errors thrown
            return quote;
        } catch (IOException | JSONException e) {

            // If an error was thrown, the error should print in the console and identify itself
            e.printStackTrace();
            System.out.println("KANYE API FETCH FAILED");

            // It should then stop the program
            System.exit(1);

            // This line will never run - only needed to compile
            return null;
        }


    }

    private Request requestBuilder(String url) {
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        return request;
    }

    private Request requestBuilder(String url, String header1, String value) {
        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader(header1, value)
                .build();
        return request;
    }

    private Response fetchResponse(Request request) throws IOException {
        return client.newCall(request).execute();
    }

    private JSONObject parseResponse(Response response) throws IOException, JSONException {
        return new JSONObject(response.body().string().trim());
    }

}


