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

package au.com.pactera.pacterapulse.fragment;

import android.os.Bundle;
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

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by Raghu on 16/06/2015.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ResultFragmentTest
{
	private FragmentManager fragManager;
	private FragmentTransaction fragTransaction;
	private ResultFragment mResultFragment;

	@Rule
	public ActivityTestRule<MainActivity> mMainActivityRule = new ActivityTestRule<>(MainActivity.class);

	@Before
	public void setUp() throws Exception
	{
		Bundle data = new Bundle();
		data.putBoolean("_SUCCESS", true);

		mResultFragment = new ResultFragment();
		mResultFragment.setArguments(data);

		//Start Fragment to be tested
		fragManager = mMainActivityRule.getActivity().getSupportFragmentManager();
		fragTransaction = fragManager.beginTransaction();
		fragTransaction.replace(R.id.root_container, mResultFragment);
		fragTransaction.commit();
	}

	@Test
	public void checkMenuOptions()
	{
		onView(withId(R.id.action_moreDetails)).perform(click());

		//TODO Check for DetailFragment Elements here.
	}

	@Test
	public void checkPreconditions()
	{
		onView(withId(R.id.actions)).check(matches(isDisplayed()));
		onView(withId(R.id.oneday)).check(matches(isDisplayed()));
		onView(withId(R.id.oneweek)).check(matches(isDisplayed()));
		onView(withId(R.id.onemonth)).check(matches(isDisplayed()));
		onView(withId(R.id.chartView)).check(matches(isDisplayed()));

		//onView(withId(R.id.progressBar)).check(!matches(isDisplayed()));
	}

	/*
     * Functional test to check if next fragment is loaded properly or not
     */
	@Test
	public void checkButtonClick24Hours()
	{
		onView(withId(R.id.oneday)).perform(click());

		onView(withId(R.id.chartView)).check(matches(isDisplayed()));
	}

	/*
     * Functional test to check if next fragment is loaded properly or not
     */
	@Test
	public void checkButtonClick1Week()
	{
		onView(withId(R.id.onemonth)).perform(click());

		onView(withId(R.id.chartView)).check(matches(isDisplayed()));
	}

	/*
     * Functional test to check if next fragment is loaded properly or not
     */
	@Test
	public void checkButtonClick1Month()
	{
		onView(withId(R.id.oneweek)).perform(click());

		onView(withId(R.id.chartView)).check(matches(isDisplayed()));
	}


}
