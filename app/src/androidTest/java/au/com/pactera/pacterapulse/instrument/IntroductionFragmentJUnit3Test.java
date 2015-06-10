package au.com.pactera.pacterapulse.instrument;

import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.test.ActivityInstrumentationTestCase2;

import au.com.pactera.pacterapulse.MainActivity;
import au.com.pactera.pacterapulse.R;

import static android.support.test.espresso.Espresso.onView;

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
		onView(ViewMatchers.withId(R.id.btnHappy))
				.perform(ViewActions.click());
	}

	@Override
	public void tearDown() throws Exception {
		super.tearDown();
	}
}
