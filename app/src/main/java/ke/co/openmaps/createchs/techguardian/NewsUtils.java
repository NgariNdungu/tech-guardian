package ke.co.openmaps.createchs.techguardian;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class NewsUtils {

    private static final String TAG = "NewsUtils";
    public NewsUtils() {
    }

    /*
    * Method to decode the response into a json string
    * */
    private static String decodeStream(InputStream response) {
        String jsonResponse = "";
        if (response == null) {
            return jsonResponse;
        }
        StringBuilder stringBuilder = new StringBuilder();
        InputStreamReader streamReader = new InputStreamReader(response, Charset.forName("UTF-8"));
        BufferedReader bufferedReader = new BufferedReader(streamReader);
        try {
            String line = bufferedReader.readLine();
            while (line != null) {
                stringBuilder.append(line);
                line = bufferedReader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        jsonResponse = stringBuilder.toString();
        return jsonResponse;
    }

    private static String getUrl() {
        // Guardian content endpoint
        String endpointUrl = "https://content.guardianapis.com/search";
        String apiKey = "8899a4d1-de3b-41c1-b171-882b35592b06";
        String url = "";
        Uri baseUri = Uri.parse(endpointUrl);
        Uri.Builder builder = baseUri.buildUpon();
        builder.appendQueryParameter("api-key", apiKey)
                .appendQueryParameter("show-tags", "contributor")
                .appendQueryParameter("format", "json")
                .appendQueryParameter("section", "technology");
        url = builder.build().toString();
        Log.i(TAG, "Request url:" + url);
        return url;
    }

    public static List<NewsItem> fetchNewsItems() {
        String requestUrl = getUrl();
        String jsonResponse = null;
        HttpURLConnection connection;
        InputStream inputStream;
        try {
            URL url = new URL(requestUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = connection.getInputStream();
                jsonResponse = decodeStream(inputStream);
            }

        } catch (MalformedURLException e) {
            Log.e("NewsUtils", "There was an error generating the url",e);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // TODO:
        return parseJson(jsonResponse);
    }

    private static List<NewsItem> parseJson(String jsonString) {
        List<NewsItem> newsItems = new ArrayList<>();
        if (jsonString == null) {
            return null;
        }

        // fields to retrieve from api response
        String title = "webTitle";
        String section = "sectionName";
        String date = "webPublicationDate";
        String url = "webUrl";
        String tags = "tags";
        String results = "results";

        JSONObject rootResponse;
        JSONArray news;
        try {
            rootResponse = new JSONObject(jsonString).getJSONObject("response");
            news = rootResponse.getJSONArray(results);
            for (int i = 0; i < news.length(); i++) {
                JSONObject newsItem = news.getJSONObject(i);
                // contributor available in tags
                JSONObject contributor = newsItem.getJSONArray(tags).getJSONObject(0);
                newsItems.add(new NewsItem(newsItem.getString(title),
                        newsItem.getString(section),
                        newsItem.getString(url),
                        newsItem.optString(date),
                        contributor.optString(title)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return newsItems;
    }
}

