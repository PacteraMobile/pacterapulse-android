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

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
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
    public static boolean restartApp(Context context)
    {
        try
        {
            //create the intent with the default start activity for your application
            Intent mStartActivity = new Intent(context, SinglePaneActivity.class)
                    .setAction(IntroductionFragment.class.getName());

            mStartActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            //create a pending intent so the application is restarted after System.exit(0) was called.
            // We use an AlarmManager to call this intent in 100ms
            int mPendingIntentId = 223344;
            PendingIntent mPendingIntent = PendingIntent
                    .getActivity(context, mPendingIntentId, mStartActivity,
                            PendingIntent.FLAG_CANCEL_CURRENT);
            AlarmManager mgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
            //kill the application
            System.exit(0);
        }
        catch (Exception ex)
        {
            return false;
        }
        return true;
    }
}
