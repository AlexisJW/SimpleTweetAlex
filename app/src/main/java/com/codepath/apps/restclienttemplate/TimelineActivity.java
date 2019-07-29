package com.codepath.apps.restclienttemplate;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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

    private SwipeRefreshLayout swipeContainer;
    // Store a member variable for the listener
    private EndlessRecyclerViewScrollListener scrollListener;
    Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        //setTitle("a");

        Toolbar toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);


        client = TwitterApp.getRestClient(this);
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


        //find the recycleView
        rvTweets = findViewById(R.id.rv_tweets);

        //Initialiser la liste des tweets et adapter a partir de les donnees
        tweets = new ArrayList<>();
        adapter = new TweetsAdapter(this,tweets);

        //RecycleView setup: layout manager et setting adapter
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvTweets.setLayoutManager(linearLayoutManager);
        // Retain an instance so that you can call `resetState()` for fresh searches
        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                //loadNextDataFromApi();
                rvTweets.setAdapter(adapter);

                populateHomeTmeline();

                swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        Log.d("TwitterRefresh", "context s being refreshed");
                        populateHomeTmeline();
                    }
                });
            }
        };
        rvTweets.setAdapter(adapter);

        populateHomeTmeline();

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d("TwitterRefresh", "context s being refreshed");
                populateHomeTmeline();
            }
        });
    }


    private void populateHomeTmeline() {
      client.getHomeTimeline(new JsonHttpResponseHandler(){
          @Override
          public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
             Log.d("TwitterClient success1", response.toString());
             List<Tweet> tweetsToAdd = new ArrayList<>();
             //parcourir la liste des tweets qui vient de ce Json
              for (int i = 0; i < response.length(); i++){
                  try {
                      //convert each Json into a tweet object
                      JSONObject jsonTweetObject = response.getJSONObject(i);
                      Tweet tweet = Tweet.fromJson(jsonTweetObject);
                      //add the tweet
                      tweetsToAdd.add(tweet);
                  } catch (JSONException e) {
                      e.printStackTrace();
                  }
              }
              //clear exiting data
              adapter.clear();
              //show the data we just received
              adapter.adTweets(tweetsToAdd);
              // Now we call setRefreshing(false) to signal refresh has finished
              swipeContainer.setRefreshing(false);
              scrollListener.resetState();

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
