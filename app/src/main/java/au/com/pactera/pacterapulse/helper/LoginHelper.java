package au.com.pactera.pacterapulse.helper;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.microsoft.aad.adal.AuthenticationCallback;
import com.microsoft.aad.adal.AuthenticationContext;
import com.microsoft.aad.adal.AuthenticationResult;
import com.microsoft.aad.adal.PromptBehavior;

import java.security.NoSuchAlgorithmException;

import javax.crypto.NoSuchPaddingException;

import au.com.pactera.pacterapulse.app.PacteraPulse;

/**
 * Created by chanielyu on 20/05/2015.
 */
public class LoginHelper
{
	private AuthenticationContext mAuthContext;

	private static final String RESOURCE_ID = "https://graph.windows.net/";
	private static final String CLIENT_ID = "b94cba4d-b100-4c56-8ae7-db8c1f3519aa";
	private static final String REDIRECT_URL = "ppos://pactera.com.au/oauth2";
	private static final String AUTHORITY_URL = "https://login.windows.net/common";

	public void callAD(Activity activity)
	{
		try
		{
			mAuthContext = new AuthenticationContext(activity.getApplicationContext(), AUTHORITY_URL,
					true); //true = use SharedPreferences for cache
		}
		catch (NoSuchAlgorithmException e)
		{
		}
		catch (NoSuchPaddingException e)
		{
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
			}

		};
		mAuthContext.acquireToken(activity, RESOURCE_ID, CLIENT_ID,
				REDIRECT_URL, PromptBehavior.Auto, callback);
	}

	public void handleActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (mAuthContext != null)
		{
			mAuthContext.onActivityResult(requestCode, resultCode, data);
		}
	}
}
