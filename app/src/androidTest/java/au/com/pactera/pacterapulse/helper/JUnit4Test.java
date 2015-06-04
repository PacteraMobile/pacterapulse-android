package au.com.pactera.pacterapulse.helper;

import android.support.test.runner.AndroidJUnit4;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * This is a simple for JUnit 4 testing
 * <p/>
 * Created by kai on 3/06/15.
 */
@RunWith(AndroidJUnit4.class)
public class JUnit4Test
{

	@Test
	public void testSample() throws Exception
	{
		Assert.assertTrue(1 + 1 == 2);
	}
}