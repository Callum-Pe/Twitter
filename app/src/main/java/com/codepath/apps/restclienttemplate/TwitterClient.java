package com.codepath.apps.restclienttemplate;

import android.content.Context;

import com.codepath.oauth.OAuthBaseClient;
import com.github.scribejava.apis.TwitterApi;
import com.github.scribejava.core.builder.api.BaseApi;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/scribejava/scribejava/tree/master/scribejava-apis/src/main/java/com/github/scribejava/apis
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */

public class TwitterClient extends OAuthBaseClient{
	public static final BaseApi REST_API_INSTANCE = TwitterApi.instance(); // Change this
	public static final String REST_URL = "https://api.twitter.com/1.1"; // Change this, base API URL
	public static final String FALLBACK_URL = "https://codepath.github.io/android-rest-client-template/success.html";
	public static final String REST_CALLBACK_URL_TEMPLATE = "intent://%s#Intent;action=android.intent.action.VIEW;scheme=%s;package=%s;S.browser_fallback_url=%s;end";

	// Constructor for client. Access api keys in secret folder which is not pushed to git.
	public TwitterClient(Context context) {
		super(context, REST_API_INSTANCE,
				REST_URL,
				context.getString(R.string.TWITTER_CLIENT),
				context.getString(R.string.TWITTER_SECRET),
				String.format(REST_CALLBACK_URL_TEMPLATE, context.getString(R.string.intent_host),
						context.getString(R.string.intent_scheme), context.getPackageName(), FALLBACK_URL));
	}
	public void getHomeTimeline(AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/home_timeline.json");
		RequestParams params = new RequestParams();
		params.put("since_id", 1);
		client.get(apiUrl, params, handler);
	}
	// Tweets or replies. original would be tweet replying to. If a regular tweet any value can be used
	public void sendTweet(String message, Long original,AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/update.json");
		RequestParams params = new RequestParams();
		params.put("in_reply_to_status_id", original);
		params.put("status", message);
		client.post(apiUrl, params, handler);
	}
    public void favorite(Long id, AsyncHttpResponseHandler handler, boolean unfavorite){
		String apiUrl = getApiUrl(unfavorite?"favorites/destroy.json":"favorites/create.json");
		RequestParams params = new RequestParams();
		params.put("id",id);
		client.post(apiUrl, params, handler);
	}
	public void retweet(Long id, AsyncHttpResponseHandler handler, boolean unretweet){
		String apiUrl = getApiUrl(String.format(!unretweet?"statuses/retweet/%s.json":"statuses/unretweet/%s.json",id));
		RequestParams params = new RequestParams();
		params.put("id",id);
		client.post(apiUrl, params, handler);
	}
	public void followers(String screen_name, AsyncHttpResponseHandler handler){
		String apiUrl = getApiUrl("followers/list.json");
		RequestParams params = new RequestParams();
		params.put("screen_name",screen_name);
		client.get(apiUrl, params, handler);
	}
	public void following(String screen_name, AsyncHttpResponseHandler handler){
		String apiUrl = getApiUrl("friends/list.json");
		RequestParams params = new RequestParams();
		params.put("screen_name",screen_name);
		client.get(apiUrl, params, handler);
	}
}
