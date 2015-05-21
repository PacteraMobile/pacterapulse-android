package au.com.pactera.pacterapulse.core;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import butterknife.ButterKnife;

/**
 * Created by kai on 19/05/15.
 */
public abstract class BaseActivity extends FragmentActivity
{

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(final Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(layoutId());
		ActionBar actionBar = getActionBar();
		if (null != actionBar)
		{
			actionBar.setDisplayHomeAsUpEnabled(true);
		}
		try
		{
			init(savedInstanceState);
		}
		catch (final Exception e)
		{
			finish();
			return;
		}
		ButterKnife.inject(this);
	}

	protected abstract void init(Bundle savedInstanceState) throws Exception;


	public abstract int layoutId();

}
