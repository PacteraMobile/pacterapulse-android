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
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by liny1 on 14/05/2015.
 */
public class VoteManager
{

	protected static String VOTED_DATE = "VOTED_DATE";
	protected static String VOTED_VALUE = "VOTED_VALUE";
	protected static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
	private Context context;
	private SharedPreferences sharedPreferences;

	public VoteManager(Context context)
	{
		this.context = context;
	}

	/**
	 * submit the vote with the emotion value
	 *
	 * @param emotion the emotion value in integer
	 */
	public void saveVote(int emotion)
	{
		Date now = new Date();
		this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		sharedPreferences.edit().putString(VOTED_DATE, Preference.getUID(context) + simpleDateFormat.format(now)).apply();
		sharedPreferences.edit().putInt(VOTED_VALUE, emotion).apply();
		Log.d("info", "saved");
	}

	/**
	 * check if the user has voted the emotion today
	 *
	 * @return isVoted the boolean value
	 */
	public boolean hasVotedToday()
	{
		this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		String votedDayString = sharedPreferences.getString(VOTED_DATE, "01/01/2000");
		return !TextUtils.isEmpty(votedDayString) && votedDayString.equals(Preference.getUID(context) + simpleDateFormat.format(new Date()));
	}
}
