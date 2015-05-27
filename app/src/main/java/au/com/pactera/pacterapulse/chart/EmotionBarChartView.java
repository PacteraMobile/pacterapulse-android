package au.com.pactera.pacterapulse.chart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;

import org.xclcharts.chart.BarChart;
import org.xclcharts.chart.BarChart3D;
import org.xclcharts.chart.BarData;
import org.xclcharts.chart.StackBarChart;
import org.xclcharts.common.DensityUtil;
import org.xclcharts.common.IFormatterDoubleCallBack;
import org.xclcharts.common.IFormatterTextCallBack;
import org.xclcharts.renderer.XEnum;

import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by chanielyu on 15/4/26.
 */

public class EmotionBarChartView extends BaseChartView
{
	private int mChartStyle = 0;
	private int mOffsetWidth = 0;
	private int mOffsetHeight = 0;
	private BarChart mChart = null;
	// Label axis
	private List<String> chartLabels = new LinkedList<>();
	private List<BarData> chartData = new LinkedList<>();

	public EmotionBarChartView(Context context)
	{
		super(context);
		init();
	}

	public EmotionBarChartView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init();
	}

	public EmotionBarChartView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		init();
	}

	private void init()
	{
		mChartStyle = 0;
		mOffsetWidth = 0;
		mOffsetHeight = 0;
		chartLabels();
		chartDataSet();
		chartRender();
	}

	private void initChart(int chartStyle)
	{
		switch (chartStyle)
		{
		case 0: // Vertical bar char
			mChart = new BarChart();
//			mChart.getAxisTitle().setLeftTitle("24 Hours Results");
			break;
		case 1:    // Horizontal bar chart
			mChart = new BarChart();
			mChart.setChartDirection(XEnum.Direction.HORIZONTAL);
			break;
		case 2:    // Vertical 3D bar char
			mChart = new BarChart3D();
			break;
		case 3:    // Horizontal 3D bar chart
			mChart = new BarChart3D();
			mChart.setChartDirection(XEnum.Direction.HORIZONTAL);
			break;
		case 4:    // Vertical stack bar char
			mChart = new StackBarChart();
			((StackBarChart) mChart).setTotalLabelVisible(false);
			break;
		case 5:    // Horizontal stack bar chart
			mChart = new StackBarChart();
			mChart.setChartDirection(XEnum.Direction.HORIZONTAL);
			((StackBarChart) mChart).setTotalLabelVisible(false);
			break;
		}


	}

	private void chartRender()
	{
		try
		{
			initChart(mChartStyle);

			// Set default px value, reserve space for Axis,Axis title....
			int[] ltrb = getBarLnDefaultSpadding();
			mChart.setPadding(DensityUtil.dip2px(getContext(), 60), ltrb[1], ltrb[2], ltrb[3]);

			// Data source
			mChart.setDataSource(chartData);
			mChart.setCategories(chartLabels);

			// Data axis
			mChart.getDataAxis().setAxisMax(100);
			mChart.getDataAxis().setAxisMin(0);
			mChart.getDataAxis().setAxisSteps(20);

			// Data axis display format
			mChart.getDataAxis().setLabelFormatter(new IFormatterTextCallBack()
			{

				@Override
				public String textFormatter(String value)
				{
					// TODO Auto-generated method stub		
					Double tmp = Double.parseDouble(value);
					DecimalFormat df = new DecimalFormat("#0");
					String label = df.format(tmp).toString();
					return label + "%";
				}

			});
			// Bar tip mark format
			mChart.getBar().setItemLabelVisible(true);
			mChart.getBar().getItemLabelPaint().setColor(Color.rgb(72, 61, 139));
			mChart.getBar().getItemLabelPaint().setFakeBoldText(true);

			mChart.setItemLabelFormatter(new IFormatterDoubleCallBack()
			{
				@Override
				public String doubleFormatter(Double value)
				{
					// TODO Auto-generated method stub										
					DecimalFormat df = new DecimalFormat("#0.0");
					String label = df.format(value).toString();
					return label + "%";
				}
			});

			mChart.DeactiveListenItemClick();
		}
		catch (Exception e)
		{
			Log.d("Exception", e.getMessage());
		}
	}

	private void chartDataSet()
	{
		List<Double> dataSeriesA = new LinkedList<>();
		dataSeriesA.add(0d);
		BarData BarDataA = new BarData("Happy", dataSeriesA, Color.rgb(253, 239, 200));

		List<Double> dataSeriesB = new LinkedList<>();
		dataSeriesB.add(0d);
		BarData BarDataB = new BarData("Neutral", dataSeriesB, Color.rgb(220, 215, 255));

		List<Double> dataSeriesC = new LinkedList<>();
		dataSeriesC.add(0d);
		BarData BarDataC = new BarData("Sad", dataSeriesC, Color.rgb(198, 241, 255));

		chartData.add(BarDataA);
		chartData.add(BarDataB);
		chartData.add(BarDataC);
	}

	private void chartLabels()
	{
		chartLabels.add("Emotions");
	}

	@Override
	public void render(Canvas canvas)
	{

		mChart.setChartRange(mOffsetWidth, mOffsetHeight, this.getWidth() - mOffsetWidth, this.getHeight() - mOffsetHeight);
		try
		{
			mChart.render(canvas);
		}
		catch (Exception e)
		{
			Log.d("Exception", e.getMessage());
		}

	}

	public void setDataSet(int happy, int neutral, int sad)
	{
		double sum = (double) (happy + neutral + sad);
		if (0 != sum)
		{
			List<BarData> dataSource = mChart.getDataSource();
			dataSource.get(0).getDataSet().set(0, ((double) happy) * 100 / sum);
			dataSource.get(1).getDataSet().set(0, ((double) neutral) * 100 / sum);
			dataSource.get(2).getDataSet().set(0, ((double) sad) * 100 / sum);
		}
	}

	public void clear()
	{
		List<BarData> dataSource = mChart.getDataSource();
		dataSource.get(0).getDataSet().set(0, 0.0);
		dataSource.get(1).getDataSet().set(0, 0.0);
		dataSource.get(2).getDataSet().set(0, 0.0);
		invalidate();
	}
}
