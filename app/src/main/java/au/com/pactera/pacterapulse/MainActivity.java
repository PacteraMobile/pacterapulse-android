package au.com.pactera.pacterapulse;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import au.com.pactera.pacterapulse.core.SinglePaneActivity;
import au.com.pactera.pacterapulse.fragment.EmotionFragment;
import au.com.pactera.pacterapulse.fragment.IntroductionFragment;
import au.com.pactera.pacterapulse.helper.LoginHelper;


/**
 *
 */
public class MainActivity extends SinglePaneActivity {
	// Key of data which tell if the app is the first time launched saved in shared preference.
	private final String FIRST_RUN = "FIRST_RUN";
	// Login helper
	private LoginHelper loginHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		boolean first = prefs.getBoolean(FIRST_RUN, true);
		if (first) {
			prefs.edit().putBoolean(FIRST_RUN, false).apply();
			SinglePaneActivity.start(IntroductionFragment.class, this);
			finish();
		} else {
			// login.
			if(null == loginHelper)
			{
				loginHelper = new LoginHelper();
				loginHelper.callAD(this);
			}
		}
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		if(null != loginHelper)
		{
			loginHelper.handleActivityResult(requestCode,resultCode,data);
		}
	}

	@Override
	protected Fragment onCreatePane() {
		return Fragment.instantiate(this,EmotionFragment.class.getName());
	}
}
