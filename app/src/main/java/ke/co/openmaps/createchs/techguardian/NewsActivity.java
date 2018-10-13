package ke.co.openmaps.createchs.techguardian;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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
        // open news item in browser when clicked on
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String url = adapter.getItem(position).getUrl();
                Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                if (webIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(webIntent);
                }
            }
        });

        // load news stories if device is connected to the internet
        if (isOnline()) {
            getLoaderManager().initLoader(0, null, this);
        } else {
            progressBar.setVisibility(View.GONE);
            noData.setText(R.string.not_connected);
        }
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

    /**
     * Method checks if device is connected to the internet
     * from https://developer.android.com/training/basics/network-ops/managing#check-connection
     */
    private boolean isOnline() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }
}
