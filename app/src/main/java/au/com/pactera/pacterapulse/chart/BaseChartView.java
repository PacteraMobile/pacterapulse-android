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

package au.com.pactera.pacterapulse.chart;

import android.content.Context;
import android.util.AttributeSet;

import org.xclcharts.common.DensityUtil;
import org.xclcharts.renderer.XChart;
import org.xclcharts.view.ChartView;

import java.util.List;

/**
 * Created by chanielyu on 15/4/26.
 */

public class BaseChartView extends ChartView
{


	public BaseChartView(Context context)
	{
		super(context);
	}

	public BaseChartView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public BaseChartView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}

	@Override
	public List<XChart> bindChart()
	{
		return null;
	}


	// Bar chartdefault offset
	// Offset space for displaying tick, axis title....
	protected int[] getBarLnDefaultSpadding()
	{
		int[] ltrb = new int[4];
		ltrb[0] = DensityUtil.dip2px(getContext(), 40); //left	
		ltrb[1] = DensityUtil.dip2px(getContext(), 60); //top	
		ltrb[2] = DensityUtil.dip2px(getContext(), 20); //right	
		ltrb[3] = DensityUtil.dip2px(getContext(), 40); //bottom						
		return ltrb;
	}

	protected int[] getPieDefaultSpadding()
	{
		int[] ltrb = new int[4];
		ltrb[0] = DensityUtil.dip2px(getContext(), 20); //left	
		ltrb[1] = DensityUtil.dip2px(getContext(), 65); //top	
		ltrb[2] = DensityUtil.dip2px(getContext(), 20); //right		
		ltrb[3] = DensityUtil.dip2px(getContext(), 20); //bottom						
		return ltrb;
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh)
	{
		super.onSizeChanged(w, h, oldw, oldh);
	}

}
