package ke.co.openmaps.createchs.techguardian;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

class NewsRecyclerAdapter extends RecyclerView.Adapter<NewsRecyclerAdapter.NewsViewHolder> {
    private ArrayList<NewsItem> mNewsItems;

    public static class NewsViewHolder extends RecyclerView.ViewHolder {
        public TextView section;
        public TextView title;
        public TextView details;
        public NewsViewHolder(View itemView) {
            super(itemView);
            section = (TextView) itemView.findViewById(R.id.section);
            title = (TextView) itemView.findViewById(R.id.article_title);
            details = (TextView) itemView.findViewById(R.id.publication_details);
        }
    }

    public NewsRecyclerAdapter(ArrayList<NewsItem> newsItems) {
        mNewsItems = newsItems;
    }

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View newsItem = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_list_item,parent,false);

        NewsViewHolder newsViewHolder = new NewsViewHolder(newsItem);
        return newsViewHolder;
    }

    @Override
    public void onBindViewHolder(NewsViewHolder holder, int position) {

        NewsItem item = mNewsItems.get(position);
        holder.section.setText(item.getSection());
        holder.title.setText(item.getTitle());
        holder.details.setText(item.getPublicationDate());
    }

/*
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }
*/

    @Override
    public int getItemCount() {
        return mNewsItems.size();
    }

}
