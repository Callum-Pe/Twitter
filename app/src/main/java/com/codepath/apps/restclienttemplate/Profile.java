package com.codepath.apps.restclienttemplate;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.codepath.apps.restclienttemplate.models.User;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class Profile extends AppCompatActivity {

    @BindView(R.id.profile) ImageView profile;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.followers)
    ListView lvFollowers;
    @BindView(R.id.following) ListView lvFollowing;
    User user;
    ArrayList<User> followers = new ArrayList<User>();
    ArrayList<User> following = new ArrayList<User>();
    ArrayList<String> follower_names = new ArrayList<String>();
    ArrayList<String> following_names = new ArrayList<String>();

    ArrayAdapter<String> itemsAdapter1;
    ArrayAdapter<String> itemsAdapter2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        itemsAdapter1 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, follower_names);
        itemsAdapter2 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, following_names);

        lvFollowers.setAdapter(itemsAdapter1);
        lvFollowing.setAdapter(itemsAdapter2);

        TextView textView = new TextView(this);
        textView.setText("Followers");
        lvFollowers.addHeaderView(textView);

        TextView textView1 = new TextView(this);
        textView.setText("Following");
        lvFollowing.addHeaderView(textView);

        user = Parcels.unwrap(getIntent().getParcelableExtra("user"));
        GlideApp.with(this)
                .load(user.profileImageUrl)
                .into(profile);
        name.setText(user.screenName);
        TwitterClient client = new TwitterClient(this);

        client.following(user.screenName, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String rep = new String(responseBody);
                try {
                    JSONObject o = new JSONObject(rep);
                    JSONArray response = o.getJSONArray("users");
                    for(int i = 0; i < response.length(); i++)
                    {
                        User use = null;
                        try {
                            use = User.fromJSON(response.getJSONObject(i));
                            following_names.add(use.screenName);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        itemsAdapter2.notifyDataSetChanged();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
        client.followers(user.screenName, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String rep = new String(responseBody);
                try {
                    JSONObject o = new JSONObject(rep);
                    JSONArray response = o.getJSONArray("users");
                    for(int i = 0; i < response.length(); i++)
                    {
                        User use = null;
                        try {
                            use = User.fromJSON(response.getJSONObject(i));
                            follower_names.add(use.screenName);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        itemsAdapter1.notifyDataSetChanged();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }
}
