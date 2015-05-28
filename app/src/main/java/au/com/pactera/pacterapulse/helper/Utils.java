package au.com.pactera.pacterapulse.helper;

import android.app.Activity;
import android.content.Intent;

import au.com.pactera.pacterapulse.core.SinglePaneActivity;
import au.com.pactera.pacterapulse.fragment.IntroductionFragment;

/**
 * Utils class
 *
 * Created by kai on 26/05/15.
 */
public class Utils {

    /**
     /**
     * Quit the process to make sure all activities and background threads have been destroyed and
     * launch the introduction screen.
     *
     * @return true if success.
     */
    public static boolean restartApp(Activity context)
    {
        try
        {
            //create the intent with the default start activity for your application
            Intent mStartActivity = new Intent(context, SinglePaneActivity.class)
                    .setAction(IntroductionFragment.class.getName());
            mStartActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            mStartActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(mStartActivity);
        }
        catch (Exception ex)
        {
            return false;
        }
        return true;
    }
}
