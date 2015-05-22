package au.com.pactera.pacterapulse;

import android.app.Activity;
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
public class MainActivity extends SinglePaneActivity {
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
		if (first) {
			prefs.edit().putBoolean(FIRST_RUN, false).apply();
			SinglePaneActivity.start(IntroductionFragment.class, this);
			finish();
		} else {
			// login.
			callAD(this);
		}
		super.onCreate(savedInstanceState);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch(item.getItemId())
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
	protected Fragment onCreatePane() {
		return Fragment.instantiate(this,EmotionFragment.class.getName());
	}

	private void callAD(Activity activity)
	{
		try
		{
			mAuthContext = new AuthenticationContext(this.getApplicationContext(),
					AUTHORITY_URL, true); //true = use SharedPreferences for cache
		}
		catch (NoSuchAlgorithmException e)
		{
			Log.d(getClass().getName(),e.toString());
		}
		catch (NoSuchPaddingException e)
		{
			Log.d(getClass().getName(),e.toString());
		}

		AuthenticationCallback callback = new AuthenticationCallback<AuthenticationResult>()
		{

			@Override
			public void onSuccess(AuthenticationResult result)
			{
				Log.d("AuthResult", "" + result.toString());
				PacteraPulse.getInstance().setTOKEN(result.getRefreshToken());
				PacteraPulse.getInstance().setGivenName(result.getUserInfo().getGivenName());
				PacteraPulse.getInstance().setSurName(result.getUserInfo().getFamilyName());
			}

			@Override
			public void onError(Exception exc)
			{
				// TODO: Add token expired process.
				Log.d("AuthResult", "" + exc.getLocalizedMessage());
				SinglePaneActivity.start(IntroductionFragment.class, MainActivity.this);
				MainActivity.this.finish();
			}

		};
		mAuthContext.acquireToken(activity, RESOURCE_ID, CLIENT_ID,
				REDIRECT_URL, PromptBehavior.Auto, callback);
	}
}
