package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.parceler.Parcels;

import java.util.List;

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder>{

    private Context context;
    private List<Tweet> tweets;

    //define the ViewHolder
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView tvImageview;
        public TextView tvScreenName;
        public TextView tvBody;
        public TextView tvTime;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvImageview = itemView.findViewById(R.id.tvImageView1);
            tvScreenName = itemView.findViewById(R.id.tv_screenName1);
            tvBody = itemView.findViewById(R.id.tv_body1);
            tvTime = itemView.findViewById(R.id.tv_time1);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
//
            if (position != RecyclerView.NO_POSITION){
                Tweet tweet = tweets.get(position);
                Intent intent = new Intent(context, DetailTweet.class);
                intent.putExtra(Tweet.class.getSimpleName(), Parcels.wrap(tweet));
                context.startActivity(intent);
            }
        }

//        @Override
//        public void onClick(View view){
//            int position = getAdapterPosition();
//
//            if (position != RecyclerView.NO_POSITION){
//                Tweet tweet = tweets.get(position);
//                Intent intent = new Intent(context, DetailTweet.class);
//                intent.putExtra(Tweet.class.getSimpleName(), Parcels.wrap(tweet));
//                context.startActivity(intent);
//            }
//        }
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
        holder.tvTime.setText(tweet.createdAt);
        Glide.with(context).load(tweet.user.profileImageUrl).into(holder.tvImageview);
    }


}
