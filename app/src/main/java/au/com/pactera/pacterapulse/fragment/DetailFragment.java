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
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import au.com.pactera.pacterapulse.R;
import au.com.pactera.pacterapulse.core.BaseFragment;
import au.com.pactera.pacterapulse.helper.NetworkHelper;
import au.com.pactera.pacterapulse.helper.OfficeAuthenticationHelper;
import au.com.pactera.pacterapulse.helper.Utils;
import butterknife.InjectView;
import butterknife.InjectViews;
import butterknife.OnClick;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

/**
 * Detail fragment allow users express their opinions about their emotion
 * <p/>
 * Created by kai on 22/05/15.
 */
public class DetailFragment extends BaseFragment<Boolean>
{
	static final String SUCCESS = "_SUCCESS";
	@InjectView(R.id.emotion_icon)
	ImageView emotionIcon;
	@InjectView(R.id.userName)
	TextView tvUserName;
	@InjectViews({R.id.seek_work_overload, R.id.seek_hear_more, R.id.seek_project_feedback})
	List<SeekBar> seekBars;
	@InjectView(R.id.btnSubmit)
	Button btnSubmit;

	private int voteID;
	private int vote;
	private ProgressDialog progressDialog;

	@Override
	protected void setupUI(View view, Bundle bundle) throws Exception
	{
		/**
		 * Set the slide bar and icon to the match user's last vote.
		 */
		String userName;
		Bundle bundleArg = getArguments();
		if (null != bundleArg)
		{
			vote = bundleArg.getInt(EmotionFragment.EMOTIONS, -1);
			userName = bundleArg.getString(EmotionFragment.USERNAME);
			voteID = bundleArg.getInt(ResultFragment.VOTEID, -1);

			if (vote == getResources().getInteger(R.integer.happy))
			{
				emotionIcon.setImageResource(R.mipmap.happy_icon);
				for (SeekBar bar : seekBars)
				{
					bar.setProgress(10);
				}
				btnSubmit.setTextColor(getResources().getColor(android.R.color.holo_red_light));
			}
			else if (vote == getResources().getInteger(R.integer.neutral))
			{
				emotionIcon.setImageResource(R.mipmap.soso_icon);
				for (SeekBar bar : seekBars)
				{
					bar.setProgress(5);
				}
				btnSubmit.setTextColor(getResources().getColor(android.R.color.holo_blue_light));
			}
			else if (vote == getResources().getInteger(R.integer.sad))
			{
				emotionIcon.setImageResource(R.mipmap.unhappy_icon);
				for (SeekBar bar : seekBars)
				{
					bar.setProgress(0);
				}
				btnSubmit.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
			}
			tvUserName.setText(getText(R.string.thanks) + userName);
		}
		checkNetwork();
	}

	private boolean checkNetwork()
	{
		boolean result = NetworkHelper.checkNetwork(getActivity());
		if (!result)
		{
			Crouton.makeText(getActivity(), R.string.invalidNetwork, Style.ALERT).show();
		}
		return result;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
	{
		inflater.inflate(R.menu.menu_detail, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		case R.id.action_logout:
			final AlertDialog.Builder builder = new AlertDialog.Builder(
					context);
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

	/**
	 * Logout current account
	 */
	private void logout()
	{
		OfficeAuthenticationHelper.logout(context);
		Utils.restartApp(getActivity());
	}

	@Override
	public int layoutId()
	{
		return R.layout.fragment_detail;
	}

	@OnClick({R.id.btnSubmit})
	void onSubmit(View submitButton)
	{
		int size = seekBars.size();
		int[] details = new int[size];
		if (checkNetwork())
		{
			for (int i=0;i<size;i++)
			{
				details[i] = seekBars.get(i).getProgress();
			}
			refresh();
		}
	}

	@Override
	protected void onStartLoading()
	{
		super.onStartLoading();
		progressDialog = ProgressDialog.show(getActivity(),
				getString(R.string.app_name), getString(R.string.app_loading), true, false);
	}

	@Override
	protected void onStopLoading()
	{
		if (null != progressDialog)
		{
			progressDialog.dismiss();
			progressDialog = null;
		}
		super.onStopLoading();
	}

	@Override
	public Boolean pendingData(Bundle arg) throws Exception
	{
		// TODO: Add the real backend API process once it is ready.
		Thread.sleep(500);
		return true;
	}

	@Override
	public void showError(Exception e)
	{
		Toast.makeText(getActivity().getBaseContext(), R.string.vote_again, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onLoaderDone(Boolean items)
	{
		super.onLoaderDone(items);
		if (items)
		{
			getActivity().setResult(Activity.RESULT_OK);
			finish();
		}
		else
		{
			Toast.makeText(getActivity().getBaseContext(), R.string.vote_again, Toast.LENGTH_SHORT).show();
		}
	}
}
