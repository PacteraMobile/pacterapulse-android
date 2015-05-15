package au.com.pactera.pacterapulse.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by liny1 on 14/05/2015.
 */
public class VoteManager {

    private Context context;
    private SharedPreferences sharedPreferences;
    protected static String VOTED_DATE = "VOTED_DATE";
    protected static String VOTED_VALUE = "VOTED_VALUE";
    protected static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

    public VoteManager(Context context) {
        this.context = context;
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    /**
     * submit the vote with the emotion value
     * @param emotion the emotion value in integer
     */
    public void saveVote(int emotion){
        Date now = new Date();
        sharedPreferences.edit().putString(VOTED_DATE, simpleDateFormat.format(now));
        sharedPreferences.edit().putInt(VOTED_VALUE, emotion);
        // Log.d("Date", now.toString());
        sharedPreferences.edit().apply();
    }

    /**
     * check if the user has voted the emotion today
     * @return isVoted the boolean value
     */
    public boolean hasVotedToday(){
        String votedDayString = sharedPreferences.getString(VOTED_DATE, "01/01/2000");
        try {
            Date today = simpleDateFormat.parse(simpleDateFormat.format(new Date()));
            Date votedDay = simpleDateFormat.parse(votedDayString);
            if(votedDay.compareTo(today) == 0) { // today
                return true;
            }
        } catch (ParseException e) {
            // add ...
        }
        return false;
    }



}
