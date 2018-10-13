package ke.co.openmaps.createchs.techguardian;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

public class NewsLoader extends AsyncTaskLoader<List<NewsItem>> {
    private String url;
    public NewsLoader(Context context, String url) {
        super(context);
        this.url = url;
    }

    @Override
    protected void onStartLoading() {
        // make network call when loader is started
        forceLoad();
    }

    @Override
    public List<NewsItem> loadInBackground() {
        return NewsUtils.fetchNewsItems(url);
    }
}
