package ke.co.openmaps.createchs.techguardian;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

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
        ButterKnife.bind(this,convertView);
        // set content on textviews
        title.setText(item.getTitle());
        section.setText(item.getSection());
        details.setText(
                generateDetails(item.getContributor(), item.getPublicationDate()));
        return convertView;
    }

    private String formatDate(String dateString) {
        String formattedDate = "";
        SimpleDateFormat outputFormat = new SimpleDateFormat("MMM dd, YYYY 'at' hh:mma",
                Locale.getDefault());
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'",
                Locale.getDefault());
        try {
            Date date = inputFormat.parse(dateString);
            formattedDate = outputFormat.format(date);
        } catch (ParseException e) {
            Log.e("NewsAdapter", "Error parsing date: " + dateString, e);
        }
        return formattedDate;
    }

    private String generateDetails(String contributor, String date) {
        StringBuilder builder = new StringBuilder();
        Resources resources = getContext().getResources();
        builder.append(resources.getString(R.string.published))
                .append(formatDate(date));
        // only add if contributor is not an empty string
        if (!TextUtils.isEmpty(contributor)) {
            builder.append(resources.getString(R.string.by))
                    .append(contributor);
        }
        return builder.toString();
    }
}
