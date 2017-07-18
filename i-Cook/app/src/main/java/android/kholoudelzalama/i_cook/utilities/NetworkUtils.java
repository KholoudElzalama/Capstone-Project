package android.kholoudelzalama.i_cook.utilities;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by win on 09/07/2017.
 */

public class NetworkUtils {

    final static String BASE_URL =
            "https://api.edamam.com/search?";

    final static String PARAM_QUERY = "q";
    final static String PARAM_APP_ID = "app_id";
    final static String PARAM_APP_KEY = "app_key";
    final static String PARAM_FROM = "from";
    final static String PARAM_TO = "to";
    final static String PARAM_CAL = "calories";
    final static String PARAM_HEALTH = "health";

    final static String APP_ID = "597eebee";
    final static String APP_KEY = "329e52ded6224bd20ace86b4ff2486cf";
    static String cals = "gte%2520591%2C%2520lte%2520722";
    static String health = "alcohol-free";


    public static URL buildUrl(String searchQuery, String from, String to) {
        String query;
        final String calsEnc;
        Uri builtUri = null;
        if (searchQuery == null) {
            query = "";
        } else {
            query = searchQuery;
        }


        builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(PARAM_QUERY, query)
                .appendQueryParameter(PARAM_APP_ID, APP_ID)
                .appendQueryParameter(PARAM_APP_KEY, APP_KEY)
                .appendQueryParameter(PARAM_FROM, from)
                .appendQueryParameter(PARAM_TO, to)
                .appendQueryParameter(PARAM_HEALTH, health)
                .build();


        URL url = null;
        try {
            url = new URL(builtUri.toString() + "&calories=gte%20591,%20lte%20722");

            Log.d("NetworkUtils", url.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
