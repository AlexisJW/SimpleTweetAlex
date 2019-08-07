package com.codepath.apps.restclienttemplate;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.User;

import org.parceler.Parcels;

public class DetailTweet extends AppCompatActivity {

    ImageView tvImageView;
    TextView tv_screenName;
    TextView tv_body;
    TextView tv_time;
    TextView tv_lien;
    User user;
    Tweet tweet;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_tweet);

        tvImageView = findViewById(R.id.tvImageView1);
        tv_body = findViewById(R.id.tv_body1);
        tv_screenName = findViewById(R.id.tv_screenName1);
        tv_time = findViewById(R.id.tv_time1);
        tv_lien = findViewById(R.id.tv_lien);

        tweet = Parcels.unwrap(getIntent().getParcelableExtra(Tweet.class.getSimpleName()));

        tv_screenName.setText(tweet.user.screenName);
        tv_body.setText(tweet.body);
        //Glide.with(this).load(tweet.user.profileImageUrl).apply(new RequestOptions().transform(new CircleCrop())).into(tvImageView);
        Glide.with(this).load(tweet.user.profileImageUrl).into(tvImageView);
    }
}
