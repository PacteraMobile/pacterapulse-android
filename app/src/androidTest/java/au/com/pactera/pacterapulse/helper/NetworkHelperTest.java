package au.com.pactera.pacterapulse.helper;

import android.test.InstrumentationTestCase;

import au.com.pactera.pacterapulse.model.Emotions;

/**
 * Created by kai on 3/06/15.
 */
public class NetworkHelperTest extends InstrumentationTestCase
{

	public void testPostVote() throws Exception
	{
		assertTrue(NetworkHelper.postVote(1, getInstrumentation().getContext()) >= 0);
	}

	public void testGetResult() throws Exception
	{
		Emotions result = NetworkHelper.getResult("24hours");
		assertNotNull(result);
		assertTrue(result.getHappy() + result.getSad() + result.getSoso() >= 0);
	}

	public void testCheckNetwork() throws Exception
	{
		NetworkHelper.checkNetwork(getInstrumentation().getContext());
	}
}