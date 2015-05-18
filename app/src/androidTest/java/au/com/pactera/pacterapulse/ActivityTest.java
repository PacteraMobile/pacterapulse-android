package au.com.pactera.pacterapulse;

import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.test.suitebuilder.annotation.MediumTest;
import android.test.suitebuilder.annotation.SmallTest;
import android.widget.ImageButton;

/**
 * Created by chanielyu on 1/05/2015.
 */
public class ActivityTest extends ActivityInstrumentationTestCase2<MainActivity>
{
	private MainActivity mMainActivity = null;
	private ImageButton btnHappy = null;
	private ImageButton btnNeutral = null;

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
		btnHappy = (ImageButton) mMainActivity.findViewById(R.id.btnHappy);
		btnNeutral = (ImageButton) mMainActivity.findViewById(R.id.btnNeutral);
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
		assertNotNull(btnHappy);
		assertNotNull(btnNeutral);
		getInstrumentation().waitForIdleSync();
		TouchUtils.clickView(this, btnNeutral);
		getInstrumentation().waitForIdleSync();
		getInstrumentation().invokeContextMenuAction(mMainActivity, android.R.id.home, 1);
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
		getInstrumentation().waitForIdleSync();
		TouchUtils.clickView(this, btnHappy);
		/*getInstrumentation().invokeMenuActionSync(mMainActivity, R.id.action_showResults, 0);
		getInstrumentation().waitForIdleSync();
		getInstrumentation().invokeMenuActionSync(mMainActivity, android.R.id.home, 0);
		getInstrumentation().waitForIdleSync();*/
	}
}
