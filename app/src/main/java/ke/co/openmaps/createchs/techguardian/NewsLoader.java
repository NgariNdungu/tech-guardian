package ke.co.openmaps.createchs.techguardian;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

public class NewsLoader extends AsyncTaskLoader<List<NewsItem>> {
    public NewsLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        // make network call when loader is started
        forceLoad();
    }

    @Override
    public List<NewsItem> loadInBackground() {
        return NewsUtils.fetchNewsItems();
    }
}
