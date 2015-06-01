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

package au.com.pactera.pacterapulse;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.microsoft.aad.adal.AuthenticationCallback;
import com.microsoft.aad.adal.AuthenticationCancelError;
import com.microsoft.aad.adal.AuthenticationContext;
import com.microsoft.aad.adal.AuthenticationResult;
import com.microsoft.aad.adal.AuthenticationSettings;

import java.security.SecureRandom;

import au.com.pactera.pacterapulse.app.PacteraPulse;
import au.com.pactera.pacterapulse.core.SinglePaneActivity;
import au.com.pactera.pacterapulse.fragment.EmotionFragment;
import au.com.pactera.pacterapulse.fragment.IntroductionFragment;
import au.com.pactera.pacterapulse.helper.NetworkHelper;
import au.com.pactera.pacterapulse.helper.OfficeAuthenticationHelper;
import au.com.pactera.pacterapulse.helper.Preference;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;


/**
 *
 */
public class MainActivity extends SinglePaneActivity implements AuthenticationCallback<AuthenticationResult>
{

	// Key of data which tell if the app is the first time launched saved in shared preference.

	private ProgressDialog progressDialog;
	private AuthenticationContext mAuthContext;
	private boolean isDestoryed;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		if (Preference.isFirstTime(this))
		{
			Preference.hasFirstTime(this);
			SinglePaneActivity.start(IntroductionFragment.class, this);
			finish();
		}
		else
		{
			// Silence login is not working well, refreshing token is always needed.
//			if (Preference.getUID(this)!=null) {
//				OfficeAuthenticationHelper.acquireTokenSilent(this,Preference.getUID(this),this);
//			} else {
			if (!NetworkHelper.checkNetwork(this)) {
				Toast.makeText(this,R.string.invalidNetwork,Toast.LENGTH_SHORT).show();
				SinglePaneActivity.start(IntroductionFragment.class, this);
				finish();
			}
			else
			{
				// Devices with API level lower than 18 must setup an encryption key.
				if (Build.VERSION.SDK_INT < 18 && AuthenticationSettings.INSTANCE.getSecretKeyData() == null) {
					AuthenticationSettings.INSTANCE.setSecretKey(generateSecretKey());
				}

				mAuthContext = OfficeAuthenticationHelper.acquireToken(this, this);
				progressDialog = ProgressDialog.show(this,
						getString(R.string.app_name), getString(R.string.app_loading), true, false);
			}
//			}
		}
		super.onCreate(savedInstanceState);
	}

	/**
	 * Randomly generates an encryption key for devices with API level lower than 18.
	 * @return The encryption key in a 32 byte long array.
	 */
	protected byte[] generateSecretKey() {
		byte[] key = new byte[32];
		new SecureRandom().nextBytes(key);
		return key;
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


	@Override
	public void onSuccess(AuthenticationResult result)
	{
		if (progressDialog != null && progressDialog.isShowing())
		{
			progressDialog.dismiss();
		}

		Log.d("AuthResult", "suc " + result.toString());
		Preference.setUID(this, result.getUserInfo().getUserId());
		PacteraPulse.getInstance().setTOKEN(result.getRefreshToken());
		PacteraPulse.getInstance().setGivenName(result.getUserInfo().getGivenName());
		PacteraPulse.getInstance().setSurName(result.getUserInfo().getFamilyName());
	}

	@Override
	public void onError(Exception exc)
	{
		if (progressDialog != null && progressDialog.isShowing())
		{
			progressDialog.dismiss();
		}
		Log.d("AuthResult", exc.getClass().getName() + "   err " + exc.getLocalizedMessage());
		if (exc instanceof AuthenticationCancelError)
		{
			SinglePaneActivity.start(IntroductionFragment.class, this);
			finish();
		}
		else
		{
			/**
			 * If activity destroyed, just do nothing
			 */
			if (!isDestoryed)
			{
				Crouton.makeText(this, R.string.login_error, Style.ALERT).show();
			}
		}

	}

	@Override
	protected void onDestroy()
	{
		isDestoryed = true;
		super.onDestroy();
	}
}
