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

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import butterknife.ButterKnife;

/**
 * Base activity for all other activities
 * We could put some shared methods and properties into this class to simply the UI development
 *
 * Created by kai on 19/05/15.
 */
public abstract class BaseActivity extends FragmentActivity
{

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(final Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(layoutId());
		ActionBar actionBar = getActionBar();
		if (null != actionBar)
		{
			actionBar.setDisplayHomeAsUpEnabled(true);
		}
		try
		{
			init(savedInstanceState);
		}
		catch (final Exception e)
		{
			finish();
			return;
		}
		ButterKnife.inject(this);
	}

	protected abstract void init(Bundle savedInstanceState) throws Exception;


	public abstract int layoutId();

}
