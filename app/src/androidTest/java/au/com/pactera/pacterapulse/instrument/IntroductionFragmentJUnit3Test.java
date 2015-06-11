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

package au.com.pactera.pacterapulse.instrument;

import android.support.test.espresso.matcher.RootMatchers;
import android.test.ActivityInstrumentationTestCase2;

import au.com.pactera.pacterapulse.MainActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by Test User on 9/06/2015.
 */
public class IntroductionFragmentJUnit3Test extends ActivityInstrumentationTestCase2<MainActivity>
{

	private MainActivity mActivity;

	public IntroductionFragmentJUnit3Test()
	{
		super(MainActivity.class);
	}

	@Override
	public void setUp() throws Exception {
		super.setUp();
		setActivityInitialTouchMode(true);
		mActivity = getActivity();
	}


	public void testCheckPreconditions() throws Exception {
		//onView(withId(R.id.btnAgree)).perform(click());

		onView(withText("Loading...")).inRoot(RootMatchers.isDialog()).check(matches(isDisplayed()));


		//onView(withText("Loading...")).inRoot(RootMatchers.isDialog()).check(matches(isDisplayed()));

		//Thread.sleep(2500);

		//onView(withText("Sign in")).check(matches(isDisplayed()));
	}

	/*
	 * You cannot test Login Page as Espresso does not support WEB VIEWS as of now!
	 * http://stackoverflow.com/questions/29144310/match-a-view-in-webview-from-espresso-in-android
	 */
	public void testLoginPageDisplay() throws Exception {
//		//onView(withText("Sign in")).check(matches(isDisplayed()));
	}

	@Override
	public void tearDown() throws Exception {
		super.tearDown();
	}
}
