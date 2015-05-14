package au.com.pactera.pacterapulse.helper;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;

/**
 * Created by Lubor Kolacny on 22/04/15.
 * Copyright (c) 2015 Pactera Technology. All rights reserved.
 */

public class NetworkHelper
{
	// /RequestStatuses/index
	private static final String BASE_URL = "http://pacterapulse-sit.elasticbeanstalk.com/";
	private static final String API_PART_URL = "";
	private static final String API_PART_RESULT_URL = "emotions";
	private static final String API_PART_VOTE_URL = "emotions";

	private static AsyncHttpClient sClient = new AsyncHttpClient();
	private static PersistentCookieStore sCookieStore;

	public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler)
	{
		sClient.get(getAbsoluteUrl(url), params, responseHandler);
	}

	public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler)
	{
		sClient.post(getAbsoluteUrl(url), params, responseHandler);
	}

	private static String getAbsoluteUrl(String relativeUrl)
	{
		return BASE_URL + API_PART_URL + relativeUrl;
	}


	public static void postVote(Integer emotionId, Context context, AsyncHttpResponseHandler responseHandler)
	{
		RequestParams params = new RequestParams();

		// post params
		// change to correct one
		String url = API_PART_VOTE_URL + "/" + UniqueID.id(context) + "/" + emotionId.toString();
		Log.d("Url2Vote", getAbsoluteUrl(url));
		sClient.setConnectTimeout(10000);
		sClient.post(getAbsoluteUrl(url), params, responseHandler);

	}

	public static void getResult(String resultType, AsyncHttpResponseHandler responseHandler)
	{
		// need some tests here
		// if(!resultType.equals("24hours")) return;

		// get result in Json
		String url = API_PART_RESULT_URL + "/" + resultType;
		sClient.setConnectTimeout(10000);
		sClient.get(getAbsoluteUrl(url), null, responseHandler);
	}

}
