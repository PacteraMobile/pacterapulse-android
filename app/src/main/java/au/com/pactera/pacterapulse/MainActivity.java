package au.com.pactera.pacterapulse;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import au.com.pactera.pacterapulse.core.SinglePaneActivity;
import au.com.pactera.pacterapulse.fragment.EmotionFragment;
import au.com.pactera.pacterapulse.fragment.IntroductionFragment;


/**
 *
 */
public class MainActivity extends SinglePaneActivity {
	// Key of data which tell if the app is the first time launched saved in shared preference.
	private final String FIRST_RUN = "FIRST_RUN";

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
		}
		super.onCreate(savedInstanceState);
	}

	@Override
	protected Fragment onCreatePane() {
		return Fragment.instantiate(this,EmotionFragment.class.getName());
	}
}
