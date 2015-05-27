package au.com.pactera.pacterapulse.helper;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.UUID;

/**
 * Created by Lubor Kolacny on 27/04/15.
 * From Google I/O example
 */
public class UniqueID
{
	private static final String PREF_UNIQUE_ID = "PREF_UNIQUE_ID";
	private static String uniqueID = null;

	public synchronized static String id(Context context)
	{
		if (uniqueID == null)
		{
			SharedPreferences sharedPrefs = context.getSharedPreferences(
					PREF_UNIQUE_ID, Context.MODE_PRIVATE);
			uniqueID = sharedPrefs.getString(PREF_UNIQUE_ID, null);
			if (uniqueID == null)
			{
				uniqueID = UUID.randomUUID().toString();
				SharedPreferences.Editor editor = sharedPrefs.edit();
				editor.putString(PREF_UNIQUE_ID, uniqueID);
				editor.commit();
			}
		}
		return uniqueID;
	}
}
