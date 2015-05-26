package au.com.pactera.pacterapulse;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.MenuItem;

import com.microsoft.aad.adal.AuthenticationCallback;
import com.microsoft.aad.adal.AuthenticationContext;
import com.microsoft.aad.adal.AuthenticationResult;
import com.microsoft.aad.adal.PromptBehavior;

import java.security.NoSuchAlgorithmException;

import javax.crypto.NoSuchPaddingException;

import au.com.pactera.pacterapulse.app.PacteraPulse;
import au.com.pactera.pacterapulse.core.SinglePaneActivity;
import au.com.pactera.pacterapulse.fragment.EmotionFragment;
import au.com.pactera.pacterapulse.fragment.IntroductionFragment;


/**
 *
 */
public class MainActivity extends SinglePaneActivity
{
	// Office 365 resources
	private static final String RESOURCE_ID = "https://graph.windows.net/";
	private static final String CLIENT_ID = "b94cba4d-b100-4c56-8ae7-db8c1f3519aa";
	private static final String REDIRECT_URL = "ppos://pactera.com.au/oauth2";
	private static final String AUTHORITY_URL = "https://login.windows.net/common";
	// Key of data which tell if the app is the first time launched saved in shared preference.
	private final String FIRST_RUN = "FIRST_RUN";
	// Login office 365
	private AuthenticationContext mAuthContext;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		boolean first = prefs.getBoolean(FIRST_RUN, true);
		if (first)
		{
			prefs.edit().putBoolean(FIRST_RUN, false).apply();
			SinglePaneActivity.start(IntroductionFragment.class, this);
			finish();
		}
		else
		{
			// login.
			callAD(this);
		}
		super.onCreate(savedInstanceState);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		case android.R.id.home:
			return false;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		if (mAuthContext != null)
		{
			mAuthContext.onActivityResult(requestCode, resultCode, data);
		}
	}

	@Override
	protected Fragment onCreatePane()
	{
		return Fragment.instantiate(this, EmotionFragment.class.getName());
	}

	private void callAD(Activity activity)
	{
		try
		{
			mAuthContext = new AuthenticationContext(activity.getApplicationContext(),
					AUTHORITY_URL, true); //true = use SharedPreferences for cache
		}
		catch (NoSuchAlgorithmException e)
		{
			Log.d(getClass().getName(), e.toString());
		}
		catch (NoSuchPaddingException e)
		{
			Log.d(getClass().getName(), e.toString());
		}

		AuthenticationCallback callback = new AuthenticationCallback<AuthenticationResult>()
		{

			@Override
			public void onSuccess(AuthenticationResult result)
			{
				Log.d("AuthResult", "suc " + result.toString());
				PacteraPulse.getInstance().setTOKEN(result.getRefreshToken());
				PacteraPulse.getInstance().setGivenName(result.getUserInfo().getGivenName());
				PacteraPulse.getInstance().setSurName(result.getUserInfo().getFamilyName());
			}

			@Override
			public void onError(Exception exc)
			{
				// TODO: Add token expired process.
				Log.d("AuthResult", "err " + exc.getLocalizedMessage());
				finish();
				doRestart();
			}

		};
		mAuthContext.acquireToken(activity, RESOURCE_ID, CLIENT_ID,
				REDIRECT_URL, PromptBehavior.Auto, callback);
	}

	/**
	 * Quit the process to make sure all activities and background threads have been destroyed and
	 * launch the introduction screen.
	 *
	 * @return true if success.
	 */
	public boolean doRestart()
	{
		try
		{
			//create the intent with the default start activity for your application
			Intent mStartActivity = new Intent(this, SinglePaneActivity.class)
					.setAction(IntroductionFragment.class.getName());

			mStartActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			//create a pending intent so the application is restarted after System.exit(0) was called.
			// We use an AlarmManager to call this intent in 100ms
			int mPendingIntentId = 223344;
			PendingIntent mPendingIntent = PendingIntent
					.getActivity(this, mPendingIntentId, mStartActivity,
							PendingIntent.FLAG_CANCEL_CURRENT);
			AlarmManager mgr = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
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
