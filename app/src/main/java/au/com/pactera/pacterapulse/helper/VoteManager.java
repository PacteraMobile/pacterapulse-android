package au.com.pactera.pacterapulse.helper;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

/**
 * Created by liny1 on 14/05/2015.
 */
public class VoteManager {

    private Context context;

    public VoteManager(Context context) {
        this.context = context;
    }

    /**
     * submit the vote with the emotion value
     * @param emotion the emotion value in integer
     */
    public void submitVote(int emotion){
        //TODO: Refactor MainActivity class to move submit vote function to this mehtod
    }

    /**
     * check if the user has voted the emotion today
     * @return isVoted the boolean value
     */
    public boolean hasVotedToday(){
        boolean isVoted = true;
        return isVoted;
    }



}
