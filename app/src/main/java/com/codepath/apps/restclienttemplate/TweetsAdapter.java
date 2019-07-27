package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;

import java.util.List;

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder>{

    private Context context;
    private List<Tweet> tweets;

    //define the ViewHolder
    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView tvImageview;
        public TextView tvScreenName;
        public TextView tvBody;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvImageview = itemView.findViewById(R.id.tvImageView);
            tvScreenName = itemView.findViewById(R.id.tv_screenName);
            tvBody = itemView.findViewById(R.id.tv_body);
        }
    }

    //passer le context et la liste des tweets.
    public TweetsAdapter(Context context, List<Tweet> tweets) {
        this.context = context;
        this.tweets = tweets;
    }

    @Override
    public int getItemCount() {
        return tweets.size();
    }

    // Clean all elements of the recycler
    public void clear() {
        tweets.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void adTweets(List<Tweet> tweetList) {
        tweets.addAll(tweetList);
        notifyDataSetChanged();
    }

    //pour chaque ligne qui va etre montre sur l'ecran.
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tweet, parent, false);
        return new ViewHolder(view);
    }
    //lier la valeur en fonction de la position de l'element
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
       Tweet tweet = tweets.get(position);
       holder.tvBody.setText(tweet.body);
       holder.tvScreenName.setText(tweet.user.screenName);
       Glide.with(context).load(tweet.user.profileImageUrl).into(holder.tvImageview);
    }

}
