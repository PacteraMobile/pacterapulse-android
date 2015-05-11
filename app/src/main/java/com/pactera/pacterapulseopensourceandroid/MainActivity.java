package com.pactera.pacterapulseopensourceandroid;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.pactera.pacterapulseopensourceandroid.fragment.EmotionFragment;
import com.pactera.pacterapulseopensourceandroid.fragment.IntroductionFragment;
import com.pactera.pacterapulseopensourceandroid.fragment.ResultFragment;
import com.pactera.pacterapulseopensourceandroid.helper.NetworkHelper;

import io.fabric.sdk.android.Fabric;
import org.apache.http.Header;
import org.json.JSONObject;

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
	// Initial data for emotion for extension.
	private Integer iHappy = 0;
	private Integer iNeutral = 0;
	private Integer iSad = 0;

	private static ProgressDialog progressdlg = null;

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
			// TODO: Open the comment to enable the feature which only show introduction screen when the first time launch the app.
			if (first)
			{
				getFragmentManager().beginTransaction()
						.setCustomAnimations(R.animator.fragment_slide_left_enter, R.animator.fragment_slide_left_exit)
						.add(R.id.container, IntroductionFragment.newInstance(null))
						.commit();
			}
			else
			{
				getFragmentManager().beginTransaction()
						.add(R.id.container, EmotionFragment.newInstance(iHappy, iNeutral, iSad), EmotionFragment.class.getName())
						.commit();
			}
		}
		mActionBar = getActionBar();
		getFragmentManager().addOnBackStackChangedListener(this);
		if (null == progressdlg)
		{
			progressdlg = new ProgressDialog(this);
			progressdlg.setTitle(R.string.app_name);
			progressdlg.setMessage(getString(R.string.gettingData));
			progressdlg.setIndeterminate(true);
			/**
			 *  Set the static progress dialog to un-cancelable to prevent activity be destroyed
			 *  during a asynchronous process.
			 */
			progressdlg.setCancelable(false);
		}
	}

	@Override
	protected void onDestroy()
	{
		progressdlg.dismiss();
		progressdlg = null;
		super.onDestroy();
	}

	/**
	 * Handle onCreateOptionsMenu
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
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		switch (id)
		{
		case android.R.id.home:
			getFragmentManager().popBackStack();
			break;
		case R.id.action_settings:
			emotionToIntro();
			return true;
		case R.id.action_showResults:
			emotionToResult();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	/**
	 * Replace introduction fragment to emotion fragment and also save it into back stack.
	 * @return commitment ID.
	 */
	private int introToEmotion()
	{
		return getFragmentManager().beginTransaction()
				.setCustomAnimations(R.animator.fragment_slide_left_enter, R.animator.fragment_slide_left_exit)
				.replace(R.id.container, EmotionFragment.newInstance(iHappy, iNeutral, iSad))
				.commit();
	}

	/**
	 * Replace emotion fragment to result fragment and also save it into back stack.
	 * @return commitment ID.
	 */
	private int emotionToResult()
	{
		if (null != mActionBar)
		{
			mActionBar.setDisplayHomeAsUpEnabled(true);
		}
		/**
		 *  To avoid an commitment after onSaveInstanceState() exception we need to use commitAllowingStateLoss.
		 */
		return getFragmentManager().beginTransaction()
				.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
				.replace(R.id.container, ResultFragment.newInstance())
				.addToBackStack(null)
				.commitAllowingStateLoss();
	}

	/**
	 * Replace emotion fragment to introduction fragment and also save it into back stack.
	 * @return commitment ID.
	 */
	private int emotionToIntro()
	{
		if (null != mActionBar)
		{
			mActionBar.setDisplayHomeAsUpEnabled(true);
		}
		return getFragmentManager().beginTransaction()
				.setCustomAnimations(R.animator.fragment_slide_left_enter, R.animator.fragment_slide_left_exit)
				.replace(R.id.container, IntroductionFragment.newInstance(null))
				.addToBackStack(null)
				.commit();
	}

	@Override
	public void onIntroductionInteraction(int id)
	{
		switch (id)
		{
		case IntroductionFragment.INSTRUCTION_READ:
		default:
			if (getFragmentManager().getBackStackEntryCount() > 0)
			{
				getFragmentManager().popBackStack();
			}
			else
			{
				introToEmotion();
			}
		}

	}

	@Override
	public void onEmotionInteraction(int id)
	{
		NetworkHelper.postVote(id, this, new JsonHttpResponseHandler()
		{
			@Override
			public void onStart()
			{
				super.onStart();
				if (null != progressdlg)
				{
					progressdlg.show();
				}
			}

			@Override
			public void onFinish()
			{
				if (null != progressdlg)
				{
					progressdlg.dismiss();
				}
				super.onFinish();
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response)
			{
				super.onSuccess(statusCode, headers, response);
				Log.d("Voting result", response.toString());
				// move to the result fragment
				emotionToResult();
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse)
			{
				super.onFailure(statusCode, headers, throwable, errorResponse);
				Toast.makeText(getBaseContext(), "Network error, please vote again!", Toast.LENGTH_SHORT).show();
				if (null != progressdlg)
				{
					progressdlg.dismiss();
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable)
			{
				super.onFailure(statusCode, headers, responseString, throwable);
				Toast.makeText(getBaseContext(), "Network error, please vote again!", Toast.LENGTH_SHORT).show();
				if (null != progressdlg)
				{
					progressdlg.dismiss();
				}
			}
		});
	}

	@Override
	public void onResultInteraction(int id)
	{
		switch(id)
		{
		case ResultFragment.RESULT_SUCCESS:
		case ResultFragment.RESULT_FAILURE:
			if (null != progressdlg)
			{
				progressdlg.dismiss();
			}
			break;
		case ResultFragment.RESULT_RESUME:
			if (null != mActionBar)
			{
				mActionBar.setTitle(getResources().getString(R.string.result24H));
			}
			if (null != progressdlg)
			{
				if(!progressdlg.isShowing())
				{
					progressdlg.show();
				}
			}
			break;
		case ResultFragment.RESULT_PAUSE:
			if (null != mActionBar)
			{
				mActionBar.setTitle(getResources().getString(R.string.app_name));
			}
			break;
		default:
			if (null != progressdlg)
			{
				progressdlg.dismiss();
			}
		}
	}

	@Override
	public void onBackStackChanged()
	{
		if (0 == getFragmentManager().getBackStackEntryCount())
		{
			mActionBar.setDisplayHomeAsUpEnabled(false);
			mActionBar.setHomeButtonEnabled(false);
		}
	}
}
