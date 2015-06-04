/*
 * Copyright (c) 2015 Pactera. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * THIS CODE IS PROVIDED AS IS BASIS, WITHOUT WARRANTIES OR CONDITIONS
 * OF ANY KIND, EITHER EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION
 * ANY IMPLIED WARRANTIES OR CONDITIONS OF TITLE, FITNESS FOR A
 * PARTICULAR PURPOSE, MERCHANTABILITY OR NON-INFRINGEMENT.
 *
 * See the Apache License, Version 2.0 for the specific language
 * governing permissions and limitations under the License.
 */

package au.com.pactera.pacterapulse.helper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.github.kevinsawicki.http.HttpRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import au.com.pactera.pacterapulse.app.Config;
import au.com.pactera.pacterapulse.model.Emotions;


public class NetworkHelper
{
	private static final String BASE_URL = Config.SERVERURL;
	private static final String API_PART_URL = "";
	private static final String API_PART_RESULT_URL = "emotions";
	private static final String API_PART_VOTE_URL = "emotions";

	private static final int TIMEOUT = 10000;

	private static String getAbsoluteUrl(String relativeUrl)
	{
		return BASE_URL + API_PART_URL + relativeUrl;
	}

	/**
	 *
	 * @param voteId
	 * @param body
	 * @return
	 * @throws Exception
	 */
	public static boolean putDetails(Integer voteId, JSONArray body) throws Exception
	{
		String url = API_PART_VOTE_URL + "/" + voteId;
		HttpRequest request = new HttpRequest(getAbsoluteUrl(url), HttpRequest.METHOD_PUT);
		request.header(HttpRequest.HEADER_CONTENT_TYPE, HttpRequest.CONTENT_TYPE_JSON);
		request.connectTimeout(TIMEOUT);
		request.readTimeout(TIMEOUT);
		request.acceptJson();
		request.acceptCharset("UTF-8");
		request.useCaches(true);
		request.send(body.toString());
		if (!request.ok())
		{
			throw new NetworkException();
		}
		else
		{
			JSONObject json = new JSONObject(request.body());
			return json.optString("status").equals("OK");
		}
	}

	/**
	 * Post emotionId to server
	 *
	 * @param emotionId emotion
	 * @param context Context
	 * @throws Exception
	 */
	public static int postVote(Integer emotionId, Context context) throws Exception
	{
		String url = API_PART_VOTE_URL + "/" + UniqueID.id(context) + "/" + emotionId.toString();
		HttpRequest request = new HttpRequest(getAbsoluteUrl(url), HttpRequest.METHOD_POST);
		request(request);
		JSONObject json = new JSONObject(request.body());
		return json.getJSONObject("emotionVote").optInt("voteId");
	}

	/**
	 * This is a perfect place that you could modify the header/content of every http request
	 */
	private static HttpRequest request(HttpRequest request) throws NetworkException
	{
		Log.d("Network", request.getConnection().getURL().toString());
		request.connectTimeout(TIMEOUT);
		request.readTimeout(TIMEOUT);
		request.acceptJson();
		request.acceptCharset("UTF-8");
		request.useCaches(true);
		if (!request.ok())
		{
			throw new NetworkException();
		}
		else
		{
			return request;
		}
	}

	/**
	 * Get statistic info from server
	 *
	 * @throws NetworkException
	 */
	public static Emotions getResult(String type) throws NetworkException, JSONException
	{
		String url = API_PART_RESULT_URL + "/" + type;
		HttpRequest request = new HttpRequest(getAbsoluteUrl(url), HttpRequest.METHOD_GET);
		request(request);
		JSONObject json = new JSONObject(request.body());
		return new Emotions(json.getJSONArray("emotionVotes"));
	}

	/**
	 * Indicates whether network connectivity exists and it is possible to establish
	 * connections and pass data.
	 * @param context Context
	 * @return whether network connectivity exists or not
	 */
	public static boolean checkNetwork(Context context)
	{
		ConnectivityManager connMgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		return networkInfo != null && networkInfo.isConnected();
	}

	/**
	 * Exception that identify there is something wrong to fetch/send data to server side.
	 */
	public static class NetworkException extends Exception
	{

	}
}
