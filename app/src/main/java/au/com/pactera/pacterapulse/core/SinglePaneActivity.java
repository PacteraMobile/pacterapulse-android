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

package au.com.pactera.pacterapulse.core;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.MenuItem;

import au.com.pactera.pacterapulse.R;

/**
 * You can easily show a fragment inside a activity with this class.
 * There are 2 ways of using this class.
 * - Extend SinglePaneActivity and create your own activity. override {@link #onCreatePane()} and return with the fragment instance.
 * - Launch this activity with fragment class as the action.
 *
 * Created by kai on 19/05/15.
 */
public class SinglePaneActivity extends BaseActivity
{
	private Fragment mFragment;

	/**
	 * Converts an intent into a {@link Bundle} suitable for use as fragment
	 * arguments.
	 */
	public static Bundle intentToFragmentArguments(final Intent intent)
	{
		final Bundle arguments = new Bundle();
		if (intent == null)
		{
			return arguments;
		}

		final Uri data = intent.getData();
		if (data != null)
		{
			arguments.putParcelable("_uri", data);
		}

		final Bundle extras = intent.getExtras();
		if (extras != null)
		{
			arguments.putAll(intent.getExtras());
		}

		return arguments;
	}

	/**
	 * Converts a fragment arguments bundle into an intent.
	 */
	public static Intent fragmentArgumentsToIntent(final Bundle arguments)
	{
		final Intent intent = new Intent();
		if (arguments == null)
		{
			return intent;
		}

		final Uri data = arguments.getParcelable("_uri");
		if (data != null)
		{
			intent.setData(data);
		}

		intent.putExtras(arguments);
		intent.removeExtra("_uri");
		return intent;
	}

	public static void start(final Class<? extends Fragment> fragment,
							 final Activity act, final Intent extras)
	{
		act.startActivity(new Intent(act, SinglePaneActivity.class).setAction(
				fragment.getName()).putExtras(extras));
	}

	public static void start(final Class<? extends Fragment> fragment, final Activity act)
	{
		act.startActivity(new Intent(act, SinglePaneActivity.class)
				.setAction(fragment.getName()));
	}

	public static void startForResult(final Class<? extends Fragment> fragment, final Activity act, int requestCode)
	{
		act.startActivityForResult(new Intent(act, SinglePaneActivity.class)
				.setAction(fragment.getName()), requestCode);
	}

	public static void startForResult(final Class<? extends Fragment> fragment,
									  final Activity act, final Intent extras, int requestCode)
	{
		act.startActivityForResult(new Intent(act, SinglePaneActivity.class).setAction(
				fragment.getName()).putExtras(extras), requestCode);
	}

	public static void startForResult(final Class<? extends Fragment> fragment, final Fragment targetFragment, int requestCode)
	{
		targetFragment.startActivityForResult(new Intent(targetFragment.getActivity(), SinglePaneActivity.class)
				.setAction(fragment.getName()), requestCode);
	}

	public static void startForResult(final Class<? extends Fragment> fragment,
									  final Fragment targetFragment, final Intent extras, int requestCode)
	{
		targetFragment.startActivityForResult(new Intent(targetFragment.getActivity(), SinglePaneActivity.class).setAction(
				fragment.getName()).putExtras(extras), requestCode);
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item)
	{
		switch (item.getItemId())
		{
		case android.R.id.home:
			finish();
			return true;
		}
		return false;
	}

	/**
	 * Called in <code>onCreate</code> when the fragment constituting this
	 * activity is needed. The returned fragment's arguments will be set to the
	 * intent used to invoke this activity.
	 */
	protected Fragment onCreatePane()
	{
		return Fragment.instantiate(this, getIntent().getAction());
	}

	public Fragment getFragment()
	{
		return mFragment;
	}

	@Override
	public int layoutId()
	{
		return R.layout.activity_singlepane_empty;
	}

	@Override
	protected void init(final Bundle saveBundle) throws Exception
	{

		if (saveBundle == null)
		{
			mFragment = onCreatePane();
			mFragment.setArguments(SinglePaneActivity
					.intentToFragmentArguments(getIntent()));
			getSupportFragmentManager().beginTransaction()
					.add(R.id.root_container, mFragment, "single_pane")
					.commit();
		}
		else
		{
			mFragment = getSupportFragmentManager().findFragmentByTag(
					"single_pane");
		}

		CharSequence customTitle = getIntent().getStringExtra(
				Intent.EXTRA_TITLE);
		if (customTitle == null && mFragment instanceof BaseFragment)
		{
			customTitle = ((BaseFragment<?>) mFragment).getTitle();
		}

		setTitle(!TextUtils.isEmpty(customTitle) ? customTitle : getTitle());

	}

	@Override
	public void onBackPressed()
	{
		if (mFragment != null && mFragment instanceof BaseFragment)
		{
			if (!((BaseFragment) mFragment).onBackPressed())
			{
				super.onBackPressed();
			}
		}
		else
		{
			super.onBackPressed();
		}
	}
}
