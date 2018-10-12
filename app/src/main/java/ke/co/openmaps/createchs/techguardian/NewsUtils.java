package ke.co.openmaps.createchs.techguardian;

import android.net.Uri;
import android.util.Log;
import android.widget.ListView;

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
    // fields to retrieve from api response
    private String title = "webTitle";
    private String section = "sectionName";
    private String date = "webPublicationDate";
    private String url = "webUrl";
    private String tags = "tags";
    private String results = "results";

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
//        URI baseUri = URI.create(endpointUrl);
        Uri baseUri = Uri.parse(endpointUrl);
        Uri.Builder builder = baseUri.buildUpon();
        builder.appendQueryParameter("api-key", "test")
                .appendQueryParameter("show-tags", "contributor")
                .appendQueryParameter("format", "json")
                .appendQueryParameter("section", "technology");
        return builder.build().toString();
    }

    public static List<NewsItem> fetchNewsItems() {
        String requestUrl = getUrl();
        HttpURLConnection connection;
        InputStream inputStream;
        try {
            URL url = new URL(requestUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            inputStream = connection.getInputStream();
            decodeStream(inputStream);
        } catch (MalformedURLException e) {
            Log.e("NewsUtils", "There was an error generating the url",e);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // TODO:
        return new ArrayList<NewsItem>();
    }

    private List<NewsItem> parseJson(String jsonString) {
        List<NewsItem> newsItems = new ArrayList<>();
        if (jsonString == null) {
            return null;
        }
        JSONObject rootResponse;
        JSONArray news;
        try {
            rootResponse = new JSONObject(jsonString);
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

