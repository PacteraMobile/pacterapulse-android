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

package au.com.pactera.pacterapulse.fragment;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;

import java.util.List;

import au.com.pactera.pacterapulse.R;
import au.com.pactera.pacterapulse.app.PacteraPulse;
import au.com.pactera.pacterapulse.chart.EmotionBarChartView;
import au.com.pactera.pacterapulse.core.BaseFragment;
import au.com.pactera.pacterapulse.core.SinglePaneActivity;
import au.com.pactera.pacterapulse.helper.NetworkHelper;
import au.com.pactera.pacterapulse.helper.OfficeAuthenticationHelper;
import au.com.pactera.pacterapulse.helper.Utils;
import au.com.pactera.pacterapulse.model.Emotions;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.InjectViews;
import butterknife.OnCheckedChanged;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;


public class ResultFragment extends BaseFragment<Emotions>
{

	private static final String TYPE = "_TYPE";
	private final int reqCode = 999;
	@InjectView(R.id.progressBar)
	ProgressBar progressBar;
	@InjectView(R.id.chartView)
	EmotionBarChartView chartView;

	@InjectViews({R.id.oneweek, R.id.oneday, R.id.onemonth})
	List<RadioButton> radios;

	private String currentType = "24hours";
	private Menu detailMenu;


	@OnCheckedChanged({R.id.oneday, R.id.onemonth, R.id.oneweek})
	void onActionsChecked(RadioButton view)
	{
		if (view.isChecked() && !view.getTag().equals(currentType))
		{
			currentType = (String) view.getTag();
			Bundle b = new Bundle();
			b.putString(TYPE, currentType);
			refresh(b);
			chartView.clear();
		}
	}

	@Override
	public int layoutId()
	{
		return R.layout.fragment_result;
	}

	@Override
	protected void setupUI(View view, Bundle bundle) throws Exception
	{
		if (!getArguments().getBoolean(EmotionFragment.SUCCESS, true))
		{
			Crouton.makeText(getActivity(), R.string.voted_today, Style.ALERT).show();
		}
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
	{
		/**
		 * There are 2 possibilities to enter this screen
		 * 1. Directly from emotion fragment by click result menu item.
		 * 2. Show result after voted.
		 * for option 2 we need to load menu and show more details menu item
		 * by read the extra data passed by emotion fragment.
		 */
		if (getArguments().getBoolean(EmotionFragment.SUCCESS, false))
		{
			inflater.inflate(R.menu.menu_result, menu);
		}
		else
		{
			inflater.inflate(R.menu.menu_detail, menu);
		}
		detailMenu = menu;
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		/**
		 * If user would like to express more by select more details menu item, launch the detail page.
		 */
		switch (item.getItemId())
		{
		case R.id.action_moreDetails:
			String userName = PacteraPulse.getInstance().getGivenName() + " " + PacteraPulse.getInstance().getSurName();
			Intent intent = new Intent();
			intent.putExtra(EmotionFragment.EMOTIONS, getArguments().getInt(EmotionFragment.EMOTIONS, 0));
			intent.putExtra(EmotionFragment.USERNAME, userName);
			SinglePaneActivity.startForResult(DetailFragment.class, this, intent, reqCode);
			return true;
		case R.id.action_logout:
			final AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setTitle(R.string.confirm).setMessage(R.string.logout_confirm);
			builder.setPositiveButton(android.R.string.ok,
					new DialogInterface.OnClickListener()
					{
						@Override
						public void onClick(final DialogInterface dialog,
											final int which)
						{
							logout();
						}
					});
			builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					dialog.dismiss();
				}
			});
			builder.show();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		/**
		 * There are 2 possibilities when detail page finished.
		 * 1. User submit the result to backend, we prevent user start a detail page again by hide the menu item.
		 * 2. User finish the detail activity by back key or home button, we shall still allow user submit their details.
		 * for option 1 we need to hide the more detail menu item.
		 */
		if (reqCode == requestCode && null != detailMenu)
		{
			switch (resultCode)
			{
			case Activity.RESULT_OK:
				detailMenu.removeItem(R.id.action_moreDetails);
				MenuItem item = detailMenu.findItem(R.id.action_logout);
				if (null != item)
				{
					item.setVisible(true);
				}
				break;
			default:
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public Emotions pendingData(Bundle arg) throws Exception
	{
		String type = arg.getString(TYPE, currentType);
		return NetworkHelper.getResult(type);
	}

	@Override
	protected int getLoaderOn()
	{
		return ONCREATE;
	}

	@Override
	protected void onStartLoading()
	{
		super.onStartLoading();
		progressBar.setVisibility(View.VISIBLE);
		ButterKnife.apply(radios, new ButterKnife.Action<View>()
		{
			@Override
			public void apply(View view, int index)
			{
				view.setEnabled(false);
			}
		});
	}

	@Override
	protected void onStopLoading()
	{
		super.onStopLoading();
		progressBar.setVisibility(View.GONE);
		ButterKnife.apply(radios, new ButterKnife.Action<View>()
		{
			@Override
			public void apply(View view, int index)
			{
				view.setEnabled(true);
			}
		});
	}

	@Override
	public void onLoaderDone(Emotions items)
	{
		setEmotionResult(items.getHappy(), items.getSoso(), items.getSad());
	}

	@Override
	public void showError(Exception e)
	{
		Toast.makeText(context, R.string.invalidNetwork, Toast.LENGTH_SHORT).show();
	}

	private void setEmotionResult(int happy, int neutral, int sad)
	{
		if (null != chartView)
		{
			chartView.setDataSet(happy, neutral, sad);
			chartView.invalidate();
		}
	}

	/**
	 * Logout current account
	 */
	private void logout()
	{
		OfficeAuthenticationHelper.logout(context);
		Utils.restartApp(getActivity());
	}
}
