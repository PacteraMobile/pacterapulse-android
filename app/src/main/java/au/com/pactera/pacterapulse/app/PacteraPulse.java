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

/**
 * Application context
 *
 * Created by kai on 20/05/15.
 */
public class PacteraPulse extends Application
{

	private static PacteraPulse instance;

	private String TOKEN = "";
	private String givenName = "";
	private String surName = "";

	/**
	 * Get application context instance
	 *
	 * @return context instance
	 */
	public static PacteraPulse getInstance()
	{
		return instance;
	}

	public String getTOKEN()
	{
		return TOKEN;
	}

	public void setTOKEN(String token)
	{
		TOKEN = token;
	}

	public String getGivenName()
	{
		return givenName;
	}

	public void setGivenName(String givenName)
	{
		this.givenName = givenName;
	}

	public String getSurName()
	{
		return surName;
	}

	public void setSurName(String surName)
	{
		this.surName = surName;
	}

	@Override
	public void onCreate()
	{
		super.onCreate();
		new Config(this);
		instance = this;
	}
}
