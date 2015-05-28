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

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import au.com.pactera.pacterapulse.R;
import butterknife.ButterKnife;

/**
 * Base Fragment, all other fragments in this application should extend from this class.
 *
 * - Loader has been integrated in, so you could simply running async background task in pendingdata()
 * - ButterKnife integrated
 * - Safely launch intent
 *
 * Created by kai on 19/05/15.
 */
public abstract class BaseFragment<T> extends Fragment implements
		CocoLoader<T>
{
	/**
	 * Loader would not be initialized by fragment.
	 */
	protected static final int NEVER = -1;
	/**
	 * Loader will be initialized when fragment was created
	 */
	protected static final int ONCREATE = 0;

	protected Context context;
	protected Loader<T> loader;
	protected View v;
	private T items;
	private boolean loaderRunning;


	/**
	 * Close current UI container(activity/dialog)
	 */
	public void finish()
	{
		if (getActivity() != null)
		{
			getActivity().finish();
		}
	}

	/**
	 * run on Ui thread
	 *
	 * @param runnable
	 */
	protected void runOnUiThread(Runnable runnable)
	{
		getActivity().runOnUiThread(runnable);
	}

	/**
	 * Get exception from loader if it provides one by being a
	 * {@link ThrowableLoader}
	 *
	 * @param loader
	 * @return exception or null if none provided
	 */
	protected Exception getException(final Loader<T> loader)
	{
		if (loader instanceof ThrowableLoader)
		{
			return ((ThrowableLoader<T>) loader).clearException();
		}
		else
		{
			return null;
		}
	}

	/**
	 * Get the loader instance
	 * @return loader instance
	 */
	public ThrowableLoader<T> getLoader()
	{
		return (ThrowableLoader<T>) loader;
	}

	/**
	 * When the loader been initialized
	 */
	protected int getLoaderOn()
	{
		return BaseFragment.NEVER;
	}


	public CharSequence getTitle()
	{
		return "";
	}

	/**
	 * Is this fragment still part of an activity and usable from the UI-thread?
	 *
	 * @return true if usable on the UI-thread, false otherwise
	 */
	protected boolean isUsable()
	{
		return getActivity() != null;
	}

	/**
	 * The layout file which will be inflated for this fragment
	 * @return layout file id
	 */
	public abstract int layoutId();

	@Override
	public void onActivityCreated(final Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		if (getLoaderOn() == ONCREATE && reloadNeeded(savedInstanceState))
		{
			initLoader(getArguments());
		}
	}

	/**
	 * manually initial and start loader
	 *
	 * @param bundle
	 */
	private void initLoader(Bundle bundle)
	{
		onStartLoading();
		getLoaderManager().initLoader(this.hashCode(), bundle, this);
	}

	/**
	 *
	 * @param savedInstanceState
	 * @return
	 */
	protected boolean reloadNeeded(final Bundle savedInstanceState)
	{
		return true;
	}

	@Override
	public void onCreate(final Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		context = getActivity();
		setHasOptionsMenu(true);
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		getLoaderManager().destroyLoader(this.hashCode());
		loader = null;
	}

	@Override
	public Loader<T> onCreateLoader(final int id, final Bundle args)
	{
		onStartLoading();
		return loader = new ThrowableLoader<T>(getActivity(), items)
		{
			@Override
			public T loadData() throws Exception
			{
				return pendingData(args);
			}
		};
	}


	protected void onStartLoading()
	{
		loaderRunning = true;
	}

	protected void onStopLoading()
	{
		loaderRunning = false;
	}

	@Override
	public View onCreateView(final LayoutInflater inflater,
							 final ViewGroup container, final Bundle savedInstanceState)
	{
		v = inflater.inflate(layoutId(), null);
		ButterKnife.inject(this, v);
		try
		{
			setupUI(v, savedInstanceState);
		}
		catch (final Exception e)
		{
			Log.e("Fragment", "Fail to setup ui for" + getClass().getName(),e);
		}
		return v;
	}

	@Override
	public void onDestroyView()
	{
		super.onDestroyView();
		ButterKnife.reset(this);
		v = null;
		loader = null;
	}


	/**
	 * A handy way of findViewById
	 * @param resourceId view id
	 * @return view instance
	 */
	protected final <E extends View> E view(int resourceId)
	{
		return (E) v.findViewById(resourceId);
	}

	@Override
	public void onLoaderDone(final T items)
	{

	}

	@Override
	public void onLoaderReset(final Loader<T> loader)
	{

	}

	@Override
	public void onLoadFinished(final Loader<T> loader, final T items)
	{
		final Exception exception = getException(loader);
		onStopLoading();
		if (exception != null)
		{
			showError(exception);
			return;
		}
		onLoaderDone(items);
	}

	@Override
	public T pendingData(final Bundle arg) throws Exception
	{
		return null;
	}

	/**
	 * Reload current loader
	 */
	public void refresh()
	{
		refresh(getArguments());
	}

	/**
	 * Reload current loader with arguments
	 *
	 * @param b arguments
	 */
	protected void refresh(final Bundle b)
	{
		getLoaderManager().restartLoader(this.hashCode(), b, this);
	}

	/**
	 * Is current loader running or not
	 * @return Is current loader running or not
	 */
	protected boolean isLoaderRunning()
	{
		return loaderRunning;
	}

	/**
	 * Set up your fragment ui, which exactly like you did in {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)} with inflated view
	 *
	 * @param view
	 * @param bundle
	 * @throws Exception
	 */
	protected abstract void setupUI(View view, Bundle bundle) throws Exception;

	/**
	 * Failback of loader, meaning there is a exception been catched in {@link #pendingData(Bundle)}
	 *
	 * @param e the catch exception in loader
	 */
	@Override
	public void showError(final Exception e)
	{

	}

	/**
	 * Safely start a intent without worry activity not found
	 * @param intent the intent try to launch
	 */
	public void startActivitySafely(Intent intent)
	{
		try
		{
			super.startActivity(intent);
		}
		catch (ActivityNotFoundException e)
		{
			Toast.makeText(getActivity(), R
					.string.activity_not_found, Toast.LENGTH_SHORT).show();
		}
		catch (SecurityException e)
		{
			Toast.makeText(getActivity(), R.string.activity_not_found, Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * Safely start a intent for result without worry activity not found
	 * @param intent the intent try to launch
	 */
	public void startActivityForResultSafely(Intent intent, int requestCode)
	{
		try
		{
			super.startActivityForResult(intent, requestCode);
		}
		catch (ActivityNotFoundException e)
		{
			Toast.makeText(getActivity(), R.string.activity_not_found, Toast.LENGTH_SHORT).show();
		}
		catch (SecurityException e)
		{
			Toast.makeText(getActivity(), R.string.activity_not_found, Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * Handle onBackPressed event from activity
	 * @return is onBackPressed been properly handled by current fragment
	 */
	public boolean onBackPressed()
	{
		return false;
	}

}
