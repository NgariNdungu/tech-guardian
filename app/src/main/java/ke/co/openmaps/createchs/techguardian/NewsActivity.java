package ke.co.openmaps.createchs.techguardian;

import android.app.LoaderManager;
import android.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<NewsItem>> {

    @BindView(R.id.list)
    ListView list;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.no_data)
    TextView noData;

    private NewsAdapter adapter;
    public final String LOG_TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        ButterKnife.bind(this);
        adapter = new NewsAdapter(this, new ArrayList<NewsItem>());
        // set adapter on listview
        list.setAdapter(adapter);
        list.setEmptyView(noData);

        getLoaderManager().initLoader(0,null, this);
    }

    @Override
    public Loader<List<NewsItem>> onCreateLoader(int id, Bundle args) {
        return new NewsLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<List<NewsItem>> loader, List<NewsItem> data) {
        adapter.clear();
        progressBar.setVisibility(View.GONE);
        if (data == null) {
            noData.setText(R.string.no_results);
            return;
        }
        adapter.addAll(data);
    }

    @Override
    public void onLoaderReset(Loader loader) {
        adapter.clear();
    }
}
