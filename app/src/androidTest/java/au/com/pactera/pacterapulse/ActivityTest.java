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
import android.test.suitebuilder.annotation.MediumTest;
import android.test.suitebuilder.annotation.SmallTest;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

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
		//Disable Key Events from this TestPackage to the Activity under Test
		//If you need to test Key Events, then turn this to false
		setActivityInitialTouchMode(true);
		// Starts the activity under test using the default Intent with:
		// action = {@link Intent#ACTION_MAIN}
		// flags = {@link Intent#FLAG_ACTIVITY_NEW_TASK}
		// All other fields are null or empty.
		mMainActivity = getActivity();
	}

	@Override
	protected void tearDown() throws Exception
	{
		/*mMainActivity.finish();
		setActivity(null);*/
		super.tearDown();
	}

	@SmallTest
	public void testActivity() throws Exception
	{
		assertNotNull(mMainActivity);
		onView(withId(R.id.btnNeutral)).perform(click());

//		getInstrumentation().waitForIdleSync();
//		getInstrumentation().invokeContextMenuAction(mMainActivity, android.R.id.home, 1);
//		TouchUtils.clickView(this,mMainActivity.getActionBar().getCustomView().findViewById(android.R.id.home));
//		sendKeys(KeyEvent.KEYCODE_BACK);
		/*getInstrumentation().waitForIdleSync();
		getInstrumentation().invokeMenuActionSync(mMainActivity, R.id.action_showResults, 0);
		getInstrumentation().invokeContextMenuAction(mMainActivity, android.R.id.home, 0);
		getInstrumentation().waitForIdleSync();*/
	}


	@MediumTest
	public void testPreconditions() throws Exception
	{
		onView(withId(R.id.btnHappy)).perform(click());
		/*getInstrumentation().invokeMenuActionSync(mMainActivity, R.id.action_showResults, 0);
		getInstrumentation().waitForIdleSync();
		getInstrumentation().invokeMenuActionSync(mMainActivity, android.R.id.home, 0);
		getInstrumentation().waitForIdleSync();*/
	}
}
