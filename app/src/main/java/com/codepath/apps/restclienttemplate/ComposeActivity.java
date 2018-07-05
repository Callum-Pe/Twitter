package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;

public class ComposeActivity extends AppCompatActivity {

    EditText etStatus;
    TextView mTextView;
    String at;
    Long reply_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
        etStatus = findViewById(R.id.etStatus);
        mTextView = findViewById(R.id.mtextView);
        reply_id = getIntent().getLongExtra("reply_id",-1);
        at = getIntent().getStringExtra("at");
        if(reply_id != -1)
            etStatus.setText("@"+at);
        etStatus.addTextChangedListener(mTextEditorWatcher);


    }

    public void onClick(View view)
    {

        TwitterClient client = TwitterApp.getRestClient(this);
        String x = etStatus.getText().toString();
        if(reply_id == -1){
            client.sendTweet(etStatus.getText().toString(),new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                    try {
                        Intent data = new Intent();
                        String res = new String(responseBody);
                        JSONObject o = new JSONObject(res);
                        Tweet tweet =  Tweet.fromJSON(o);
                        data.putExtra("tweet", Parcels.wrap(tweet));
                        setResult(RESULT_OK, data); // set result code and bundle data for response
                        finish(); // closes the activity, pass data to parent
                    } catch (JSONException e) {
                        Log.d("Compose", "failed to parse json");
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                }
            });
        }
        else{
            client.replyToTweet(etStatus.getText().toString(),reply_id,new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                    try {
                        Intent data = new Intent();
                        String res = new String(responseBody);
                        JSONObject o = new JSONObject(res);
                        Tweet tweet =  Tweet.fromJSON(o);
                        data.putExtra("tweet", Parcels.wrap(tweet));
                        setResult(RESULT_OK, data); // set result code and bundle data for response
                        finish(); // closes the activity, pass data to parent
                    } catch (JSONException e) {
                        Log.d("Compose", "failed to parse json");
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                }
            });

        }

    }
    private final TextWatcher mTextEditorWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //This sets a textview to the current length
            mTextView.setText(String.valueOf(s.length()));
        }

        public void afterTextChanged(Editable s) {
        }
    };
}
