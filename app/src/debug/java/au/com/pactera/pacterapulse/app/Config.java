package au.com.pactera.pacterapulse.app;

import android.app.Application;
import android.os.StrictMode;

/**
 * Created by kai on 20/05/15.
 */
public class Config
{

	public static final String SERVERURL = "http://pacterapulse-sit.elasticbeanstalk.com/";

	Config(Application application)
	{
		setupStrictMode();
	}

	private void setupStrictMode()
	{
		StrictMode.ThreadPolicy.Builder threadPolicyBuilder =
				new StrictMode.ThreadPolicy.Builder()
						.detectAll()
						.penaltyLog();
		StrictMode.VmPolicy.Builder vmPolicyBuilder =
				new StrictMode.VmPolicy.Builder()
						.detectAll()
						.penaltyLog();
		StrictMode.setThreadPolicy(threadPolicyBuilder.build());
		StrictMode.setVmPolicy(vmPolicyBuilder.build());
	}
}
