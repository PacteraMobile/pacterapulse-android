package au.com.pactera.pacterapulse;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;

import com.crashlytics.android.Crashlytics;

import au.com.pactera.pacterapulse.fragment.EmotionFragment;
import au.com.pactera.pacterapulse.fragment.IntroductionFragment;
import au.com.pactera.pacterapulse.fragment.ResultFragment;
import au.com.pactera.pacterapulse.helper.LoginHelper;
import au.com.pactera.pacterapulse.helper.NetworkHelper;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import io.fabric.sdk.android.Fabric;

/**
 *
 */
public class MainActivity extends Activity implements
		IntroductionFragment.OnIntroductionInteractionListener,
		EmotionFragment.OnEmotionInteractionListener,
		ResultFragment.OnResultInteractionListener,
		FragmentManager.OnBackStackChangedListener
{
	// Key of data which tell if the app is the first time launched saved in shared preference.
	final String FIRST_RUN = "FIRST_RUN";
	private ActionBar mActionBar = null;
	LoginHelper loginHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		Fabric.with(this, new Crashlytics());
		setContentView(R.layout.activity_main);
		// Use shared preference to identify if it is the first launch of this app after installed.
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		boolean first = prefs.getBoolean(FIRST_RUN, true);

		if (first)
		{
			prefs.edit().putBoolean(FIRST_RUN, false).apply();
		}
		if (null == savedInstanceState)
		{
			// Only show introduction screen when the first time launch the app.
			if (first)
			{
				getFragmentManager().beginTransaction()
						.setCustomAnimations(R.animator.fragment_slide_left_enter, R.animator.fragment_slide_left_exit)
						.add(R.id.container, IntroductionFragment.newInstance())
						.commit();
			}
			else
			{
				loginHelper = new LoginHelper();
				loginHelper.callAD(this);
				getFragmentManager().beginTransaction()
						.add(R.id.container, EmotionFragment.newInstance())
						.commit();
			}
		}
		mActionBar = getActionBar();
		getFragmentManager().addOnBackStackChangedListener(this);
	}

	@Override
	protected void onDestroy()
	{
		Crouton.cancelAllCroutons();
		NetworkHelper.canelAll();
		super.onDestroy();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		if(null!=loginHelper)
		{
			loginHelper.handleActivityResult(requestCode,resultCode,data);
		}
	}

	/**
	 * Handle onCreateOptionsMenu
	 *
	 * @param menu the menu of this activity
	 * @return true to show the menu otherwise the menu is invisable.
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onIntroductionInteraction(int id)
	{
		switch (id)
		{
		case IntroductionFragment.INSTRUCTION_READ:
		default:
			if(null == loginHelper)
			{
				loginHelper = new LoginHelper();
				loginHelper.callAD(this);
			}
		}
	}

	@Override
	public void onEmotionInteraction(int id)
	{
	}

	@Override
	public void onResultInteraction(int id)
	{
		switch (id)
		{
		case ResultFragment.RESULT_SUCCESS:
		case ResultFragment.RESULT_FAILURE:
			break;
		case ResultFragment.RESULT_RESUME:
			if (null != mActionBar)
			{
				mActionBar.setTitle(getResources().getString(R.string.result24H));
			}
			break;
		case ResultFragment.RESULT_PAUSE:
			if (null != mActionBar)
			{
				mActionBar.setTitle(getResources().getString(R.string.app_name));
			}
			break;
		default:
		}
	}

	@Override
	public void onBackStackChanged()
	{
		if(null != mActionBar)
		{
			if (0 == getFragmentManager().getBackStackEntryCount())
			{
				mActionBar.setDisplayHomeAsUpEnabled(false);
				mActionBar.setHomeButtonEnabled(false);
			}
			else
			{
				mActionBar.setDisplayHomeAsUpEnabled(true);
			}
		}
	}
}
