package com.pactera.pacterapulseopensourceandroid.model;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by chanielyu on 29/04/2015.
 */

public class Emotions
{
	private int happy;
	private int soso;
	private int sad;

	public int getHappy()
	{
		return happy;
	}

	public int getSoso()
	{
		return soso;
	}

	public int getSad()
	{
		return sad;
	}

	public Emotions(JSONArray emotions)
	{
		JSONObject jHappy=emotions.optJSONObject(0);
		JSONObject jSoso=emotions.optJSONObject(1);
		JSONObject jSad=emotions.optJSONObject(2);
		if(null!=jHappy)
		{
			happy = jHappy.optInt("count");
		}
		if(null!=jSoso)
		{
			soso = jSoso.optInt("count");
		}
		if(null!=jSad)
		{
			sad = jSad.optInt("count");
		}
	}
}
