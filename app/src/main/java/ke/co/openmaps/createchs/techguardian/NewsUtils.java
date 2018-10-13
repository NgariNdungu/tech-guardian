package ke.co.openmaps.createchs.techguardian;

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

    /**
     * Construct string from inputstream
     * @param response  the inputstream to decode
     *
     * @return String
     */
    private static String decodeStream(InputStream response) throws IOException {
        String jsonResponse = null;
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
        } finally {
            response.close();
        }

        jsonResponse = stringBuilder.toString();
        return jsonResponse;
    }

    public static List<NewsItem> fetchNewsItems(String requestUrl) {
        String jsonResponse = null;
        HttpURLConnection connection = null;
        InputStream inputStream;
        try {
            URL url = new URL(requestUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = connection.getInputStream();
                jsonResponse = decodeStream(inputStream);
            } else {
                Log.e(TAG, "The server returned an error: " + connection.getResponseCode());
            }

        } catch (MalformedURLException e) {
            Log.e(TAG, "There was an error generating the url",e);
        } catch (IOException e) {
            Log.e(TAG, "Error reading from network", e);
        } finally {
          if (connection != null) {
              connection.disconnect();
          }
        }

        return parseJson(jsonResponse);
    }

    /**
     * Parse received json response and generate NewsItems
     *
     * @param jsonString the json response to parse
     * @return List<NewsItem>
     */
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
            Log.e(TAG, "Error parsing response",e);
            // Ensure that method always returns
            return null;
        }
        return newsItems;
    }
}

