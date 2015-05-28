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
		Intent i = new Intent(application.getApplicationContext(), MainActivity.class);
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