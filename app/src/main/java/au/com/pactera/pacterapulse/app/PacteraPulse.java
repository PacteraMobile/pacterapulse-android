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
