package com.codepath.apps.restclienttemplate.models;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

@Parcel
public class User {

    public String name;
    public String screenName;
    public String profileImageUrl;
    public long uid;
    //https://developer.twitter.com/en/docs/tweets/timelines/api-reference/get-statuses-home_timeline.html


    public User() {
    }

    public static User fromJson(JSONObject jsonObject) throws JSONException {
      User user = new User();
      user.name = jsonObject.getString("name");
      user.uid = jsonObject.getLong("id");
      user.screenName = jsonObject.getString("screen_name");
      user.profileImageUrl = jsonObject.getString("profile_image_url_https");

      return user;
    }
}
