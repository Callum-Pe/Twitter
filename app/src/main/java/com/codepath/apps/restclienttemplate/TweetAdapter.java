package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.parceler.Parcels;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.Viewholder>{

    private List<Tweet> mTweets;
    Context context;
    TwitterClient client;
    TimelineActivity timeline;
    AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

        }
    };

    public TweetAdapter(List<Tweet> tweets, TwitterClient client, TimelineActivity tl){
        mTweets = tweets;
        this.client = client;
        timeline = tl;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View tweetView = inflater.inflate(R.layout.item_tweet, parent, false);
        Viewholder viewHolder = new Viewholder(tweetView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final Viewholder holder, int position) {
        final Tweet tweet = mTweets.get(position);
        final Drawable heart = context.getResources().getDrawable(R.drawable.ic_vector_heart);
        final Drawable empty = context.getResources().getDrawable(R.drawable.heart_empty);
        final Drawable rt = context.getResources().getDrawable(R.drawable.ic_vector_retweet);
        final Drawable rtg = context.getResources().getDrawable(R.drawable.retweet_green);

        if(tweet.favorited)
            holder.bFavorite.setBackground(heart);
        else
            holder.bFavorite.setBackground(empty);
        if(tweet.retweeted)
            holder.bRetweet.setBackground(rtg);
        else
            holder.bRetweet.setBackground(rt);

        holder.tvUsername.setText(tweet.user.name);
        holder.tvBody.setText(tweet.body);
        holder.retweetCount.setText(""+tweet.retweetCount);
        holder.favoriteCounter.setText(""+tweet.favoriteCount);

        GlideApp.with(context)
                .load(tweet.user.profileImageUrl)
                .into(holder.ivProfileImage);
        holder.ivProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context,Profile.class);
                i.putExtra("user",Parcels.wrap(tweet.user));
                context.startActivity(i);
            }
        });
        holder.tvTime.setText(getRelativeTimeAgo(tweet.createdAt));
        holder.bReply.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                    timeline.onComposeAction(tweet);
            }});
        holder.bFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                client.favorite(tweet.uid,handler,tweet.favorited);
                if(tweet.favorited) {
                    holder.bFavorite.setBackground(empty);
                    holder.favoriteCounter.setText(""+(Integer.parseInt(""+holder.favoriteCounter.getText().toString())-1) );
                }
                else
                {
                    holder.bFavorite.setBackground(heart);
                    holder.favoriteCounter.setText(""+(Integer.parseInt(""+holder.favoriteCounter.getText().toString())+1) );
                }
                tweet.favorited = !tweet.favorited;
            }
        });
        holder.bRetweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                client.retweet(tweet.uid,handler,tweet.retweeted);
                if(tweet.retweeted) {
                    holder.bRetweet.setBackground(rt);
                    holder.retweetCount.setText(""+(Integer.parseInt(""+holder.retweetCount.getText().toString())-1) );
                }
                else
                {
                    holder.bRetweet.setBackground(rtg);
                    holder.retweetCount.setText(""+(Integer.parseInt(""+holder.retweetCount.getText().toString())+1) );
                }
                tweet.retweeted = !tweet.retweeted;
            }
        });
        if(tweet.pic == null)
            holder.ivPic.setVisibility(View.GONE);
        else
            GlideApp.with(context).load(tweet.pic).into(holder.ivPic);
    }

    @Override
    public int getItemCount() {
        return mTweets.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.ivProfileImage) ImageView ivProfileImage;
        @BindView(R.id.tvUserName) TextView tvUsername;
        @BindView(R.id.tvBody) TextView tvBody;
        @BindView(R.id.tvTime) TextView tvTime;
        @BindView(R.id.ivPicture) ImageView ivPic;
        @BindView(R.id.bReply) Button bReply;
        @BindView(R.id.bFavorite)  Button bFavorite;
        @BindView(R.id.bRetweet) Button bRetweet;
        @BindView(R.id.retweetCount) TextView retweetCount;
        @BindView(R.id.favoriteCounter) TextView favoriteCounter;

        public Viewholder(View itemView){
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, DetailedActivity.class);
            intent.putExtra("tweet", Parcels.wrap(mTweets.get(getAdapterPosition())));
           // intent.putExtra("client",client);
            //intent.putExtra("timeline",timeline);
            context.startActivity(intent);
        }
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
    public void clear() {
        mTweets.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Tweet> list) {
        mTweets.addAll(list);
        notifyDataSetChanged();
    }
}
