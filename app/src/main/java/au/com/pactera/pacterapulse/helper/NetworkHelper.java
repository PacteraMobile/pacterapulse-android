package au.com.pactera.pacterapulse.helper;

import android.content.Context;
import android.util.Log;

import com.github.kevinsawicki.http.HttpRequest;

import org.json.JSONArray;

import au.com.pactera.pacterapulse.model.Emotions;


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

	private static final int TIMEOUT = 10000;

	private static String getAbsoluteUrl(String relativeUrl)
	{
		return BASE_URL + API_PART_URL + relativeUrl;
	}

	/**
	 * Post emotionId to server
	 * @param emotionId
	 * @param context
	 * @throws Exception
	 */
	public static void postVote(Integer emotionId, Context context) throws Exception
	{
		String url = API_PART_VOTE_URL + "/" + UniqueID.id(context) + "/" + emotionId.toString();
		HttpRequest request = new HttpRequest(getAbsoluteUrl(url),HttpRequest.METHOD_POST);
		request(request);
	}

	private static HttpRequest request(HttpRequest request) throws NetworkException {
		request.connectTimeout(TIMEOUT);
		request.acceptJson();
		request.acceptCharset("UTF-8");
		request.useCaches(true);
		if(!request.ok()) {
			throw new NetworkException();
		} else {
			return request;
		}
	}

	/**
	 * Exception that identify there is something wrong to fetch/send data to server side.
	 */
	public static class NetworkException extends Exception {

	}

	/**
	 * Get statistic info from server
	 * @throws NetworkException
	 */
	public static Emotions getResult() throws NetworkException {
		String url = API_PART_RESULT_URL + "/" + "24hours";
		HttpRequest request = new HttpRequest(getAbsoluteUrl(url),HttpRequest.METHOD_POST);
		request(request);
		Log.d("NETWORK",request.body());

		return null;

	}
}
