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

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import au.com.pactera.pacterapulse.MainActivity;
import au.com.pactera.pacterapulse.R;
import au.com.pactera.pacterapulse.core.BaseFragment;
import butterknife.OnClick;


public class IntroductionFragment extends BaseFragment<Void>
{

	@Override
	public int layoutId()
	{
		return R.layout.fragment_introduction;
	}

	@Override
	protected void setupUI(View view, Bundle bundle) throws Exception
	{
		if (getActivity().getActionBar() != null)
		{
			getActivity().getActionBar().hide();
		}
		getActivity().getWindow().setBackgroundDrawable(null);
	}

	@OnClick(R.id.btnAgree)
	void launch()
	{
		finish();
		startActivitySafely(new Intent(context, MainActivity.class));
	}

}
