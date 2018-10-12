package ke.co.openmaps.createchs.techguardian;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsAdapter extends ArrayAdapter<NewsItem> {
    @BindView(R.id.article_title)
    TextView title;
    @BindView(R.id.section) TextView section;
    @BindView(R.id.publication_details)  TextView details;

    public NewsAdapter(@NonNull Context context, @NonNull ArrayList<NewsItem> newsItems) {
        super(context, 0, newsItems);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.news_list_item, parent,false);
        }
        // news item at current position
        NewsItem item = getItem(position);
        ButterKnife.bind(convertView);
        title.setText(item.getTitle());
        section.setText(item.getSection());
        details.setText(item.getPublicationDate());
        return convertView;
    }
}
