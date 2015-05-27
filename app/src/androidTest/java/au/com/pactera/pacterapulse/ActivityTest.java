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
		/*assertNotNull(mMainActivity);
		onView(withId(R.id.btnNeutral)).perform(click());*/
	}


	@MediumTest
	public void testPreconditions() throws Exception
	{
//		onView(withId(R.id.btnHappy)).perform(click());
	}
}
