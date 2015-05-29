package au.com.pactera.pacterapulse.instrument;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.app.FragmentTransaction;
import android.test.suitebuilder.annotation.LargeTest;

import junit.framework.Assert;

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
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by Raghu on 29/05/2015.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class IntroductionFragmentTest
{
	private IntroductionFragment mIntroductionFragment;

	@Rule
	public ActivityTestRule<MainActivity> mMainActivityRule = new ActivityTestRule<>(MainActivity.class);

	@Before
	public void setUp()
	{
		mIntroductionFragment = new IntroductionFragment();

		//Start Fragment to be tested
		FragmentTransaction fragTransaction = mMainActivityRule.getActivity().getSupportFragmentManager().beginTransaction();
		fragTransaction.replace(R.id.root_container, mIntroductionFragment);
		fragTransaction.commit();
	}

	@Test
	public void checkPreconditions()
	{

		Assert.assertNotNull(withId(R.id.textView));
		Assert.assertNotNull(withId(R.id.btnAgree));
	}

	@Test
	public void checkTextViewStrings()
	{
		final String expectedResult = mMainActivityRule.getActivity().getString(R.string.Instruction);
		onView(withId(R.id.textView)).check(matches(withText(expectedResult)));
	}

	@Test
	public void checkButtonClick()
	{
		onView(withId(R.id.btnAgree)).perform(click());
	}
}
