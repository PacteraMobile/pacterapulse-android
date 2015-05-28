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
	 * @param activity
	 * @param callback
	 * @return
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
	 * @param context
	 * @return
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
	 * @param context
	 * @return
	 */
	public static boolean logout(Context context)
	{
		CookieManager cookieManager = CookieManager.getInstance();
		cookieManager.removeAllCookie();
		CookieSyncManager.getInstance().sync();
		if (getAuthenticationContext(context) != null)
		{
			mAuthContext.getCache().removeAll();
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
