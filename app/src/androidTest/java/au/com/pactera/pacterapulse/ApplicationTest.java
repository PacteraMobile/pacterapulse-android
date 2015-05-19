package au.com.pactera.pacterapulse;

import android.app.Application;
import android.content.Intent;
import android.test.ApplicationTestCase;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application>
{
	private Application application = null;
	public ApplicationTest()
	{
		super(Application.class);
	}

	@Override
	protected void setUp() throws Exception
	{
		super.setUp();
		createApplication();
		application = getApplication();
		Intent i = new Intent(application.getApplicationContext(),MainActivity.class);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		application.startActivity(i);
	}

	@Override
	protected void tearDown() throws Exception
	{
		super.tearDown();
	}

	public void testApplication() throws Exception
	{
		assertNotNull(application);
		testAndroidTestCaseSetupProperly();
	}
}