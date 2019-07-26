package com.codepath.apps.restclienttemplate;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends AppCompatActivity {

    private TwitterClient client;
    private RecyclerView rvTweets;
    private TweetsAdapter adapter;
    private List<Tweet> tweets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        client = TwitterApp.getRestClient(this);

        //find the recycleView
        rvTweets = findViewById(R.id.rv_tweets);

        //Initialiser la liste des tweets et adapter a partir de les donnees
        tweets = new ArrayList<>();
        adapter = new TweetsAdapter(this,tweets);

        //RecycleView setup: layout manager et setting adapter
        rvTweets.setLayoutManager(new LinearLayoutManager(this));
        rvTweets.setAdapter(adapter);

        populateHomeTmeline();
    }

    private void populateHomeTmeline() {
      client.getHomeTimeline(new JsonHttpResponseHandler(){
          @Override
          public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
             Log.d("TwitterClient success1", response.toString());
             //parcourir la liste des tweets qui vient de ce Json
              for (int i = 0; i < response.length(); i++){
                  try {
                      //convert each Json into a tweet object
                      JSONObject jsonTweetObject = response.getJSONObject(i);
                      Tweet tweet = Tweet.fromJson(jsonTweetObject);
                      //add the tweet
                      tweets.add(tweet);
                      //notify adapter
                      adapter.notifyItemInserted(tweets.size() - 1);
                  } catch (JSONException e) {
                      e.printStackTrace();
                  }
              }
          }

          @Override
          public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
              Log.e("TwitterClient err1", errorResponse.toString());
          }

          @Override
          public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
              Log.e("TwitterClient", responseString);
          }
      });
    }

}
