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
