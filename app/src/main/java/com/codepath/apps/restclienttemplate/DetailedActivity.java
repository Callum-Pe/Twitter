package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.parceler.Parcels;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class DetailedActivity extends AppCompatActivity {
    @BindView(R.id.ivProfileImage) ImageView ivProfileImage;
    @BindView(R.id.tvUserName) TextView tvUsername;
    @BindView(R.id.tvBody) TextView tvBody;
    @BindView(R.id.tvTime) TextView tvTime;
    @BindView(R.id.bRetweet) Button bRetweet;
    @BindView(R.id.ivPicture) ImageView ivPic;
    @BindView(R.id.bFavorite)  Button bFavorite;
    @BindView(R.id.bReply) Button bReply;
    public Tweet tweet;
    AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);
        ButterKnife.bind(this);

        Intent i = getIntent();
        //final TimelineActivity timeline = (TimelineActivity) i.getExtras().getSerializable("timeline");
        //final TwitterClient client = (TwitterClient)i.getExtras().getSerializable("client");
        tweet = Parcels.unwrap(i.getParcelableExtra("tweet"));

        tvUsername.setText(tweet.user.name);
        tvBody.setText(tweet.body);

        GlideApp.with(this)
                .load(tweet.user.profileImageUrl)
                .into(ivProfileImage);
        tvTime.setText(getRelativeTimeAgo(tweet.createdAt));
        /*bReply.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                timeline.onComposeAction(tweet);
            }});
        bFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                client.favorite(tweet.uid,handler,tweet.favorited);
                tweet.favorited = !tweet.favorited;
            }
        });
        bRetweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                client.retweet(tweet.uid,handler);
            }
        });*/
        GlideApp.with(this).load(tweet.pic).into(ivPic);
    }
    public String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }
}
