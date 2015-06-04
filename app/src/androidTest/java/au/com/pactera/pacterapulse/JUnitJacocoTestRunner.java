package au.com.pactera.pacterapulse;

import android.os.Bundle;
import android.support.test.runner.AndroidJUnitRunner;

import java.lang.reflect.Method;

/**
 * Workaround from https://code.google.com/p/android/issues/detail?id=170607
 * Created by kai on 3/06/15.
 */
public class JUnitJacocoTestRunner extends AndroidJUnitRunner
{
	static
	{
		System.setProperty("jacoco-agent.destfile", "/data/data/" + BuildConfig.APPLICATION_ID + "/coverage.ec");
	}

	@Override
	public void finish(int resultCode, Bundle results)
	{
		try
		{
			Class rt = Class.forName("org.jacoco.agent.rt.RT");
			Method getAgent = rt.getMethod("getAgent");
			Method dump = getAgent.getReturnType().getMethod("dump", boolean.class);
			Object agent = getAgent.invoke(null);
			dump.invoke(agent, false);
		}
		catch (Throwable e)
		{
			e.printStackTrace();
		}

		super.finish(resultCode, results);
	}
}
