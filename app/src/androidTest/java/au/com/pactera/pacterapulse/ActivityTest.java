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

import android.test.ActivityInstrumentationTestCase2;

import junit.framework.Assert;

/**
 * Created by chanielyu on 1/05/2015.
 */
public class ActivityTest extends ActivityInstrumentationTestCase2<MainActivity>
{
	private MainActivity mMainActivity = null;

	public ActivityTest()
	{
		super(MainActivity.class);
	}

	@Override
	protected void setUp() throws Exception
	{
		super.setUp();
		setActivityInitialTouchMode(true);
		mMainActivity = getActivity();
	}

	@Override
	protected void tearDown() throws Exception
	{
		super.tearDown();
	}

	public void testLogin() throws Exception
	{
		Assert.assertTrue(mMainActivity.getFragment() != null);
	}

	public void testIntroduction() throws Exception
	{
//		onView(withId(R.id.btnAgree))
//				.check(matches(isDisplayed())).perform(click());
	}


}
