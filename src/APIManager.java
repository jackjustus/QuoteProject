


//TODO fix the authors stuff something with indicies i think


import okhttp3.Request;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import processing.core.PApplet;

import java.io.IOException;
import java.util.Arrays;

public class APIManager {

    PApplet p;
    OkHttpClient client;

    private final int MAX_QUOTE_LENGTH = 150;
    public static int NUM_AUTHORS_IN_GAME = 4;
    public static int NUM_ROUNDS_IN_GAME = NUM_AUTHORS_IN_GAME * 2;

    public String[] authorPool = new String[]{
            "Simon Cowell",
            "Marilyn Monroe",
            "Donald Trump",
            "Elvis Presley",
            "Mark Twain",
            "Joe Biden",
            "Elton John",
            "Abraham Lincoln",
            "Princess Diana",
            "Leonardo da Vinci",
            "Winston Churchill",
            "Charles Darwin"
    };

    public String[] authorList;

    public APIManager(PApplet p) {
        this.p = p;

        // Init API resources
        client = new OkHttpClient();

    }

    public String[] getQuotesForRound(int round) {

        String returnedQuotes[] = new String[2];

        returnedQuotes[0] = fetchQuoteGardenAPI(getAuthorsForRound(round)[0])[0];
        returnedQuotes[1] = fetchQuoteGardenAPI(getAuthorsForRound(round)[1])[0];
        return returnedQuotes;
    }

    public String[] getAuthorsForRound(int round) {

        // Return two authors
        // [0] - The first author for the round (the left button)
        // [1] - The second author for the round (the right button)
        String[] returnedAuthors = new String[2];

        // Setting the first author for the round to the round number and math things
        returnedAuthors[0] = authorList[round % NUM_AUTHORS_IN_GAME];


        // For some ungodly reason there is an exception to the math at the very specific number three
        // Thus this if statement exists for this edge case born from the depths of hell
        if (round == 3) {
            returnedAuthors[1] = authorList[0];
        } else {
            returnedAuthors[1] = authorList[Math.abs(NUM_AUTHORS_IN_GAME - Math.abs((NUM_AUTHORS_IN_GAME - 1) - round))];
        }


        System.out.println(returnedAuthors[0] + " AND " + returnedAuthors[1]);
        return returnedAuthors;

    }

    public String[] getRandomQuote() {
        String[] data = new String[2];

        // Getting a random number for the max number of authors
        int rand = (int) (Math.random() * authorList.length);

        // This loops infinitely until the quote returned is within the maxQuoteLength and the quote is returned
        while (true)
            switch (authorList[rand]) {


                // Kanye has his own API bc there are more quotes in his own API than in the Quote Garden API
                // Kanye is no longer in the list of authors however the functionality is still here bc it took a good amount of time to make his separate API
                case "Kanye West":
                    return fetchKanyeAPI();

                default:
                    String[] quote = fetchQuoteGardenAPI(authorList[rand]);
                    if (quote[0].length() <= MAX_QUOTE_LENGTH)
                        return quote;
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
        Request request = requestBuilder("https://quote-garden.herokuapp.com/api/v3/quotes/random?limit=10&author=" + author);


        try {
            // Attempting to communicate with the API
            // The response is stored in response
            Response response = fetchResponse(request);

            // Parsing the response to just the quoteData
            JSONObject parsedResponse = parseResponse(response);
            JSONObject quoteData = (JSONObject) ((JSONArray) parsedResponse.get("data")).get(0);

            // Getting the quote and author
            String quote = quoteData.get("quoteText") + "";
//            String quoteAuthor = quoteData.get("quoteAuthor") + "";

            // Making sure the quote is a valid length
            while (quote.length() > MAX_QUOTE_LENGTH) {

                // Attempting to communicate with the API
                // The response is stored in response
                response = fetchResponse(request);

                // Parsing the response to just the quoteData
                parsedResponse = parseResponse(response);
                quoteData = (JSONObject) ((JSONArray) parsedResponse.get("data")).get(0);

                // Getting the quote and author
                quote = quoteData.get("quoteText") + "";
//                quoteAuthor = quoteData.get("quoteAuthor") + "";

            }


            // Returning the quote and author if there were no errors thrown
            String[] returnedData = new String[3];
            returnedData[0] = quote;
            returnedData[1] = author;
            returnedData[2] = getAuthorIndex(author) + "";

            /* returnedData
            [0] - The Quote
            [1] - The Author
            [2] - The author's index in the list of authors (String format for simplicity)
             */
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

    public void generateAuthorList() {

        // authorPool holds all of the potential authors - authorList holds the authors that will be in the game
        authorList = new String[NUM_AUTHORS_IN_GAME];
        Arrays.fill(authorList, "");


        for (int i = 0; i < NUM_AUTHORS_IN_GAME; i++) {

            int randomAuthorIndex = (int) (Math.random() * authorPool.length);

            boolean indexValid = true;

            // Making sure the author isn't a duplicate (and there are other authors in the list)
            if (!authorList[0].equals(""))
                for (String authorsInList : authorList)
                    if (authorsInList.equals(authorPool[randomAuthorIndex])) {
                        indexValid = false;
                        break;
                    }

            // If there are no matching authors already in the author list then the author is added to the pool
            // If there are, the for loop will run again
            if (indexValid)
                authorList[i] = authorPool[randomAuthorIndex];
            else
                i--;
        }

    }

    public String[] getAuthorList() {
        return authorList;
    }

    public int getAuthorIndex(String author) {
        for (int i = 0; i < authorList.length; i++)
            if (author.equals(authorList[i]))
                return i;
        System.out.println("Author not found -- See APIManager's getAuthorIndex()");
        return -1;
    }

    private Request requestBuilder(String url) {
        Request request = new Request.Builder()
                .url(url)
                .get()
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