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

package au.com.pactera.pacterapulse.app;

import android.app.Application;
import android.os.StrictMode;

/**
 * Created by kai on 20/05/15.
 */
public class Config
{

	public static final String SERVERURL = "http://pacterapulse-dev.elasticbeanstalk.com/";

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
						.detectLeakedSqlLiteObjects()
						.detectLeakedClosableObjects()
						.penaltyLog();
		StrictMode.setThreadPolicy(threadPolicyBuilder.build());
		StrictMode.setVmPolicy(vmPolicyBuilder.build());
	}
}
