package au.com.pactera.pacterapulse.helper;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.microsoft.aad.adal.AuthenticationCallback;
import com.microsoft.aad.adal.AuthenticationContext;
import com.microsoft.aad.adal.AuthenticationResult;
import com.microsoft.aad.adal.PromptBehavior;

import java.security.NoSuchAlgorithmException;

import javax.crypto.NoSuchPaddingException;

/**
 * Created by kai on 26/05/15.
 */
public class OfficeAuthenticationHelper
{

	// Office 365 resources
	private static final String RESOURCE_ID = "https://graph.windows.net/";
	private static final String CLIENT_ID = "b94cba4d-b100-4c56-8ae7-db8c1f3519aa";
	private static final String REDIRECT_URL = "ppos://pactera.com.au/oauth2";
	private static final String AUTHORITY_URL = "https://login.windows.net/common";

	private static final String TAG = "OfficeAuthHelper";

	private static AuthenticationContext mAuthContext;

	/**
	 * Acquire token from server, callback obj should be passed to get token
	 *
	 * @param activity activity context
	 * @param callback callback of token acquire
	 * @return authenticationContext instance you need to use
	 */
	public static AuthenticationContext acquireToken(Activity activity, AuthenticationCallback callback)
	{
		if (getAuthenticationContext(activity) == null)
		{
			return null;
		}
		mAuthContext.acquireToken(activity, RESOURCE_ID, CLIENT_ID,
				REDIRECT_URL, PromptBehavior.Auto, callback);
		return mAuthContext;
	}

	/**
	 * Get authentication context object for further operation
	 *
	 * @param context Context
	 * @return authenticationContext instance you need to use
	 */
	private static AuthenticationContext getAuthenticationContext(Context context)
	{
		if (mAuthContext != null)
		{
			return mAuthContext;
		}
		try
		{
			mAuthContext = new AuthenticationContext(context.getApplicationContext(),
					AUTHORITY_URL, true); //true = use SharedPreferences for cache
			return mAuthContext;
		}
		catch (NoSuchAlgorithmException e)
		{
			Log.d(TAG, e.toString());
		}
		catch (NoSuchPaddingException e)
		{
			Log.d(TAG, e.toString());
		}
		return null;
	}


	/**
	 * Logout from current context
	 *
	 * @param context Context
	 * @return logout success or not
	 */
	@SuppressWarnings("deprecation")
	public static boolean logout(Context context)
	{
		if (getAuthenticationContext(context) != null)
		{
			mAuthContext.getCache().removeAll();
			CookieSyncManager.createInstance(context.getApplicationContext());
			CookieManager cookieManager = CookieManager.getInstance();
			cookieManager.removeSessionCookie();
			cookieManager.removeAllCookie();
			CookieSyncManager.getInstance().sync();
			Preference.setUID(context, null);
			return false;
		}
		return true;
	}

	public static boolean hasToken(Context context)
	{
		if (getAuthenticationContext(context) != null)
		{
			mAuthContext.acquireTokenSilentSync(RESOURCE_ID, CLIENT_ID,
					REDIRECT_URL);
			return false;
		}
		return true;
	}

	public static AuthenticationContext acquireTokenSilent(Context context, String uid, AuthenticationCallback<AuthenticationResult> callback)
	{
		if (getAuthenticationContext(context) == null)
		{
			return null;
		}
		mAuthContext.acquireTokenSilent(RESOURCE_ID, CLIENT_ID, uid, callback);
		return mAuthContext;
	}
}
