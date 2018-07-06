package com.codepath.apps.restclienttemplate.models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

@Parcel
public class Tweet {

    public String body;
    public Long uid = new Long(-1);
    public String createdAt;
    public User user;
    public String pic = null;
    public boolean favorited;
    public int favoriteCount;
    public int retweetCount;
    public Boolean retweeted;

    public Tweet(){user = new User(); }

    public static Tweet fromJSON(JSONObject jsonObject) throws JSONException{
        Tweet tweet = new Tweet();

        tweet.body = jsonObject.getString("text");
        tweet.uid = jsonObject.getLong("id");
        tweet.createdAt = jsonObject.getString("created_at");
        tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));
        tweet.favorited = jsonObject.getBoolean("favorited");
        tweet.favoriteCount = jsonObject.getInt("favorite_count");
        tweet.retweetCount = jsonObject.getInt("retweet_count");
        tweet.retweeted = jsonObject.getBoolean("retweeted");
        JSONObject entities = jsonObject.getJSONObject("entities");
        try {
            JSONArray media = entities.getJSONArray("media");
            if (media.length() != 0)
                tweet.pic = media.getJSONObject(0).getString("media_url");
        }
        catch(Exception e)
        {
            Log.d("COMPOSE ACTIVITY", "unable to parse media array from entities");
        }
        return tweet;
    }
}
