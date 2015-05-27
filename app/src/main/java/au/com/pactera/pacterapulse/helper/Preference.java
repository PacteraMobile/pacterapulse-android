package au.com.pactera.pacterapulse.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by kai on 26/05/15.
 */
public class Preference {

    private static final String FIRST_RUN = "FIRST_RUN";
    private static final String UID = "UID";

    public static boolean isFirstTime(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean(FIRST_RUN, true);
    }

    public static void hasFirstTime(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putBoolean(FIRST_RUN,false).apply();
    }

    public static String getUID(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(UID, null);
    }

    public static void setUID(Context context,String uid) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putString(UID,uid).apply();
    }
}
