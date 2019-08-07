package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.Nullable;
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
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends AppCompatActivity {

    private final int REQUEST_CODE = 20;
    private TwitterClient client;
    private RecyclerView rvTweets;
    private TweetsAdapter adapter;
    private List<Tweet> tweets;
    Toolbar toolbar;
    Handler mHandler;

    private SwipeRefreshLayout swipeContainer;
    // Store a member variable for the listener
    private EndlessRecyclerViewScrollListener scrollListener;
    Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        //setTitle("a");

         toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);

//        getSupportActionBar().setTitle("TweetAlex");
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setLogo(R.drawable.ic_launcher_twitter_round);
//        getSupportActionBar().setDisplayUseLogoEnabled(true);
//        mHandler = new Handler();

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

        //Auto();

    }

    public void Auto(){
        swipeContainer.post(new Runnable() {
                                @Override
                                public void run() {
                                    swipeContainer.setRefreshing(true);

                                    populateHomeTmeline();
                                }
                            }
        );

        mHandler = new Handler();
        startRepeatingTask();
    }
    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {
            //updateStatus(); //this function can change value of mInterval.
            mHandler.postDelayed(mStatusChecker, 5000);
        }
    };
    void startRepeatingTask() {
        mStatusChecker.run();
    }

    void stopRepeatingTask() {
        mHandler.removeCallbacks(mStatusChecker);
    }

    //added code start here
    Runnable mAutoRefreshRunnable = new Runnable() {
        @Override
        public void run() {
            populateHomeTmeline();
            mHandler.postDelayed(mAutoRefreshRunnable, 1000);
        }
    };



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
        //Toast.makeText(this,"llslshjsdfhjhjvhjkjbkjsdhjsdj", Toast.LENGTH_SHORT).show();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.compose){
            Intent intentCompose = new Intent(this, ComposeActivity.class);
            startActivityForResult(intentCompose, REQUEST_CODE);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE && requestCode == RESULT_OK){
             //pull info out of the data Intent
            Tweet tweet = Parcels.unwrap(data.getParcelableExtra("tweet"));
            //Update the recycleView
            tweets.add(0, tweet);
            adapter.notifyItemInserted(0);
            rvTweets.smoothScrollToPosition(0);
//            Auto();
//            onResume();
//            startRepeatingTask();
        }
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
              Log.e("TwitterClient", "failed");
          }

          @Override
          public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
              Log.e("TwitterClient", "failed1");
          }
      });
    }

    public void ActionBtnFloat(View view) {
        Intent intentCompose = new Intent(this, ComposeActivity.class);
        startActivityForResult(intentCompose, REQUEST_CODE);
    }
}
