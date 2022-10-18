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
    OkHttpClient client;

    String[] authorList = new String[]{
            "Kanye West",
            "Jesus Christ",
            "Marilyn Monroe",
            "Donald Trump",
            "Elvis Presley",
            "Mark Twain",
            "Jackie Chan",
            "Elton John",
            "Abraham Lincoln",
            "Princess Diana"
    };

    public APIManager(PApplet p) {
        this.p = p;

        // Init API resources
        client = new OkHttpClient();
    }

    public String[] getRandomQuote() {

        String[] data = new String[2];

        // Getting a random number for the max number of authors
        int rand = (int) (Math.random() * authorList.length + 1);

        // Returning the quote and author for the randomly selected author
        // See fetchQuoteGardenAPI() for reasoning behind returning the author when it is already specified
        switch (authorList[rand]) {

            // Kanye has his own API bc there are more quotes in his own API than in the Quote Garden API
            case "KanyeWest":
                return fetchKanyeAPI();
            default:
                return fetchQuoteGardenAPI(authorList[rand]);
        }

    }

    public String[] fetchKanyeAPI() {

        // Building the request to be sent to the API
        Request request = requestBuilder("https://api.kanye.rest/");


        try {
            // Attempting to communicate with the API
            // The response is stored in response
            Response response = fetchResponse(request);

            // Parsing the response to just the quote
            JSONObject jsonObject = parseResponse(response);
            String quote = jsonObject.get("quote") + "";

            // Returning the quote and author if there were no errors thrown
            return new String[]{quote, "Kanye West"};


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

    public String[] fetchQuoteGardenAPI(String author) {

        // Returns a string array of the following
        // [0] - quote
        // [1] - quote author


        // Building the request to be sent to the API
        Request request = requestBuilder("https://quote-garden.herokuapp.com/api/v3/quotes/random?author=" + author);


        try {
            // Attempting to communicate with the API
            // The response is stored in response
            Response response = fetchResponse(request);

            // Parsing the response to just the quoteData
            JSONObject parsedResponse = parseResponse(response);
            JSONObject quoteData = (JSONObject) ((JSONArray) parsedResponse.get("data")).get(0);

            // Getting the quote and author
            String quote = quoteData.get("quoteText") + "";
            String quoteAuthor = quoteData.get("quoteAuthor") + "";

            /* REASONING BEHIND RETURNING THE API AUTHOR INSTEAD OF THE SPECIFIED ONE
            There is a chance that despite asking the API for one author, they might give me a quote from another
            If that happens, we need to return this author because there could be a discrepancy between the two authors, and this would be the correct one
            Thanks for coming to my TED talk
            */

            // Returning the quote and author if there were no errors thrown
            String[] returnedData = new String[2];
            returnedData[0] = quote;
            returnedData[1] = quoteAuthor;
            return returnedData;


        } catch (IOException | JSONException e) {

            // If an error was thrown, the error should print in the console and identify itself
            e.printStackTrace();
            System.out.println("QuoteGarden API FETCH FAILED");

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
}