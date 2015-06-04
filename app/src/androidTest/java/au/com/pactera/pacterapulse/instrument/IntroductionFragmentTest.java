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

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import au.com.pactera.pacterapulse.MainActivity;
import au.com.pactera.pacterapulse.R;
import au.com.pactera.pacterapulse.fragment.IntroductionFragment;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by Raghu on 29/05/2015.
 * This is a basic Instrumentation Test using latest JUnit4 libraries
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class IntroductionFragmentTest
{
	@Rule
	public ActivityTestRule<MainActivity> mMainActivityRule = new ActivityTestRule<>(MainActivity.class);
	private FragmentManager fragManager;
	private FragmentTransaction fragTransaction;
	private IntroductionFragment mIntroductionFragment;

	@Before
	public void setUp()
	{
		mIntroductionFragment = new IntroductionFragment();

		//Start Fragment to be tested
		fragManager = mMainActivityRule.getActivity().getSupportFragmentManager();
		fragTransaction = fragManager.beginTransaction();
		fragTransaction.replace(R.id.root_container, mIntroductionFragment);
		fragTransaction.commit();
	}

	@Test
	public void checkPreconditions()
	{
		onView(withId(R.id.textView)).check(matches(isDisplayed()));
		onView(withId(R.id.btnAgree)).check(matches(isDisplayed()));
	}

	@Test
	public void checkTextViewStrings()
	{
		final String expectedResult = mMainActivityRule.getActivity().getString(R.string.Instruction);
		onView(withId(R.id.textView)).check(matches(withText(expectedResult)));
	}

	/*
		Functional test to check if next fragment is loaded properly or not
	*/
	@Test
	public void checkButtonClick()
	{
		onView(withId(R.id.btnAgree)).perform(click());

		//Check weather next fragment UI elements are displayed properly or not
		onView(withId(R.id.btnSad)).check(matches(isDisplayed()));
		onView(withId(R.id.btnNeutral)).check(matches(isDisplayed()));
		onView(withId(R.id.btnHappy)).check(matches(isDisplayed()));
	}
}
