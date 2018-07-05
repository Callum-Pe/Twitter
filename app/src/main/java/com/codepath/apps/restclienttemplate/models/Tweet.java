package com.codepath.apps.restclienttemplate.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

@Parcel
public class Tweet {

    public String body;
    public Long uid;
    public String createdAt;
    public User user;
    public String pic = null;

    public String getBody() {
        return body;
    }

    public Long getUid() {
        return uid;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public User getUser() {
        return user;
    }



    public Tweet(){ }

    public static Tweet fromJSON(JSONObject jsonObject) throws JSONException{
        Tweet tweet = new Tweet();

        tweet.body = jsonObject.getString("text");
        tweet.uid = jsonObject.getLong("id");
        tweet.createdAt = jsonObject.getString("created_at");
        tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));
        JSONObject entities = jsonObject.getJSONObject("entities");
        JSONArray media = entities.getJSONArray("media");
        if(media.length() != 0)
            tweet.pic = media.getJSONObject(0).getString("media_url");

        return tweet;
    }
}
