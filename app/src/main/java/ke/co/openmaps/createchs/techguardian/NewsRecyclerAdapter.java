package ke.co.openmaps.createchs.techguardian;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

class NewsRecyclerAdapter extends RecyclerView.Adapter<NewsRecyclerAdapter.NewsViewHolder> {
    private ArrayList<NewsItem> mNewsItems;
    private Context context;

    public static class NewsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView section;
        public TextView title;
        TextView details;
        String url;
        Context context;
        NewsViewHolder(View itemView, Context context) {
            super(itemView);
            itemView.setOnClickListener(this);
            this.context = context;
            section = (TextView) itemView.findViewById(R.id.section);
            title = (TextView) itemView.findViewById(R.id.article_title);
            details = (TextView) itemView.findViewById(R.id.publication_details);

        }

        @Override
        public void onClick(View v) {
            // get what was clicked on
            // get the url to the news item
            // open in a browser

            Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            if (webIntent.resolveActivity(context.getPackageManager()) != null) {
                context.startActivity(webIntent);
            }
        }
    }

    NewsRecyclerAdapter(ArrayList<NewsItem> newsItems, Context context) {
        mNewsItems = newsItems;
        this.context = context;
    }

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View newsItem = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_list_item,parent,false);

        return new NewsViewHolder(newsItem, context);
    }

    @Override
    public void onBindViewHolder(NewsViewHolder holder, int position) {
        NewsItem item = mNewsItems.get(position);
        holder.url = item.getUrl();
        holder.section.setText(item.getSection());
        holder.title.setText(item.getTitle());
        holder.details.setText(item.getPublicationDate());
    }

    @Override
    public int getItemCount() {
        return mNewsItems.size();
    }

}
