package au.com.pactera.pacterapulse.fragment;


import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import au.com.pactera.pacterapulse.R;
import au.com.pactera.pacterapulse.chart.EmotionBarChartView;
import au.com.pactera.pacterapulse.helper.NetworkHelper;
import au.com.pactera.pacterapulse.model.Emotions;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ResultFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ResultFragment extends Fragment
{
	public static final int RESULT_SUCCESS = 0;
	public static final int RESULT_FAILURE = 1;
	public static final int RESULT_RESUME = 2;
	public static final int RESULT_PAUSE = 3;

	private EmotionBarChartView mBarChartView = null;

	private WeakReference<OnResultInteractionListener> mListener=null;
	private WeakReference<Activity> mWeakActivity =null;
	
	public static ResultFragment newInstance()
	{
		ResultFragment resultFragment = new ResultFragment();
		resultFragment.setRetainInstance(true);
		return resultFragment;
	}

	public ResultFragment()
	{
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState)
	{
		// Inflate the layout for this fragment
		FrameLayout fl = (FrameLayout) inflater.inflate(R.layout.fragment_result, container, false);
		mBarChartView = new EmotionBarChartView(getActivity(), 0, 0);
		fl.addView(mBarChartView);
		return fl;
	}

	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);
		try
		{
			mListener = new WeakReference<OnResultInteractionListener>((OnResultInteractionListener)activity);
			mWeakActivity = new WeakReference<Activity>(activity);
		}
		catch (ClassCastException e)
		{
			throw new ClassCastException(activity.toString()
					+ " must implement OnResultInteractionListener");
		}
	}

	@Override
	public void onResume()
	{
		super.onResume();
		// TODO: Remove the following method call from onResume, this is just a sample.
		getResultData();
		OnResultInteractionListener listener = mListener.get();
		if (listener != null)
		{
			listener.onResultInteraction(RESULT_RESUME);
		}
	}

	@Override
	public void onPause()
	{
		super.onPause();
		OnResultInteractionListener listener = mListener.get();
		if (listener != null)
		{
			listener.onResultInteraction(RESULT_PAUSE);
		}
	}

	@Override
	public void onPrepareOptionsMenu(Menu menu)
	{
		MenuItem item = menu.findItem(R.id.action_showResults);
		if(null!=item)
		{
			item.setVisible(false);
		}
		super.onPrepareOptionsMenu(menu);
	}

	private void getResultData()
	{
		NetworkHelper.getResult("24hours", new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				Log.d("Voting result", response.toString());
				// show emotions
				try {
					// TODO: The following code needs to be changed to handle all possible error.
					// It is OK at the moment we have the protocol to make sure all JSON object are exist.
					Emotions emotions = new Emotions(response.getJSONArray("emotionVotes"));
					setEmotionResult(emotions.getHappy(), emotions.getSoso(), emotions.getSad());
				} catch (JSONException e) {
					Log.w("JSON Exception", e.getMessage());
				} catch (Exception e) {
					Log.w("Exception", e.getMessage());
				}
				OnResultInteractionListener listener = mListener.get();
				Activity act = mWeakActivity.get();
				if (listener != null && act != null) {
					listener.onResultInteraction(RESULT_SUCCESS);
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
				super.onFailure(statusCode, headers, throwable, errorResponse);
				OnResultInteractionListener listener = mListener.get();
				Activity act = mWeakActivity.get();
				if (listener != null && act != null) {
					Toast.makeText(act, "Network error!", Toast.LENGTH_SHORT).show();
					listener.onResultInteraction(RESULT_FAILURE);
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				super.onFailure(statusCode, headers, responseString, throwable);
				OnResultInteractionListener listener = mListener.get();
				Activity act = mWeakActivity.get();
				if (listener != null && act != null) {
					Toast.makeText(act, "Network error!", Toast.LENGTH_SHORT).show();
					listener.onResultInteraction(RESULT_FAILURE);
				}
			}
		});
	}

	private void setEmotionResult(int happy, int neutral, int sad)
	{
		if (null != mBarChartView)
		{
			mBarChartView.setDataSet(happy, neutral, sad);
			mBarChartView.invalidate();
		}
	}

	/**
	 * This interface must be implemented by activities that contain this
	 * fragment to allow an interaction in this fragment to be communicated
	 * to the activity and potentially other fragments contained in that
	 * activity.
	 * <p/>
	 * See the Android Training lesson <a href=
	 * "http://developer.android.com/training/basics/fragments/communicating.html"
	 * >Communicating with Other Fragments</a> for more information.
	 */
	public interface OnResultInteractionListener
	{
		// TODO: Update argument type and name
		public void onResultInteraction(int id);
	}
}
