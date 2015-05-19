package au.com.pactera.pacterapulse.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

import au.com.pactera.pacterapulse.R;
import au.com.pactera.pacterapulse.helper.NetworkHelper;
import au.com.pactera.pacterapulse.helper.VoteManager;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EmotionFragment.OnEmotionInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EmotionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EmotionFragment extends Fragment implements View.OnClickListener
{
	private final int VOTE_HAPPY = 0;
	private final int VOTE_NEUTRAL = 1;
	private final int VOTE_SAD = 2;
	private final int VOTE_INVALID = -1;

	private VoteManager voteManager;
	private OnEmotionInteractionListener mListener;

	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @return A new instance of fragment EmotionFragment.
	 */
	public static EmotionFragment newInstance()
	{
		EmotionFragment fragment = new EmotionFragment();
		fragment.setRetainInstance(true);
		return fragment;
	}

	public EmotionFragment()
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
		return inflater.inflate(R.layout.fragment_emotion, container, false);
	}

	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);
		try
		{
			mListener = (OnEmotionInteractionListener) activity;
		}
		catch (ClassCastException e)
		{
			throw new ClassCastException(activity.toString()
					+ " must implement OnEmotionInteractionListener");
		}
		voteManager = new VoteManager(getActivity());
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		// Set on click listener when activity is created.
		getActivity().findViewById(R.id.btnHappy).setOnClickListener(this);
		getActivity().findViewById(R.id.btnNeutral).setOnClickListener(this);
		getActivity().findViewById(R.id.btnSad).setOnClickListener(this);
//		getActivity().getActionBar().show();
	}

	@Override
	public void onDetach()
	{
		super.onDetach();
		mListener = null;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		switch (id)
		{
		case android.R.id.home:
			getFragmentManager().popBackStack();
			return true;
		case R.id.action_settings:
			emotionToIntro();
			return true;
		case R.id.action_showResults:
			emotionToResult();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void onButtonPressed(int id)
	{
		int vote;
		ConnectivityManager connMgr = (ConnectivityManager) getActivity()
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

		if (networkInfo != null && networkInfo.isConnected())
		{
			// fetch data
			switch (id)
			{
			case R.id.btnHappy:
				vote = VOTE_HAPPY;
				break;
			case R.id.btnNeutral:
				vote = VOTE_NEUTRAL;
				break;
			case R.id.btnSad:
				vote = VOTE_SAD;
				break;
			default:
				vote = VOTE_INVALID;
				Log.w("Warning", "Should never reach here!");
			}

			if (mListener != null)
			{
				mListener.onEmotionInteraction(vote);
			}
			if (voteManager.hasVotedToday())
			{
				Crouton.makeText(getActivity(), "Thanks. You have voted today.", Style.ALERT).show();
				emotionToResult();
				return;
			}
			NetworkHelper.postVote(vote, getActivity(), new PacteraPulseJsonHttpResponseHandler(id));
		}
		else
		{
			// Display error
			Toast.makeText(getActivity(), getResources().getText(R.string.invalidNetwork), Toast.LENGTH_SHORT).show();
		}

	}

	@Override
	public void onClick(View v)
	{
		onButtonPressed(v.getId());
	}

	/**
	 * Replace emotion fragment to result fragment and also save it into back stack.
	 *
	 * @return commitment ID.
	 */
	private int emotionToResult()
	{
		/**
		 *  To avoid an commitment after onSaveInstanceState() exception we need to use commitAllowingStateLoss.
		 */
		return getFragmentManager().beginTransaction()
				.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
				.replace(R.id.container, ResultFragment.newInstance())
				.addToBackStack(null)
				.commitAllowingStateLoss();
	}

	/**
	 * Replace emotion fragment to introduction fragment and also save it into back stack.
	 *
	 * @return commitment ID.
	 */
	private int emotionToIntro()
	{
		return getFragmentManager().beginTransaction()
				.setCustomAnimations(R.animator.fragment_slide_left_enter, R.animator.fragment_slide_left_exit)
				.replace(R.id.container, IntroductionFragment.newInstance())
				.addToBackStack(null)
				.commit();
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
	public interface OnEmotionInteractionListener
	{
		public void onEmotionInteraction(int id);
	}

	class PacteraPulseJsonHttpResponseHandler extends JsonHttpResponseHandler
	{
		private ProgressDialog progressdlg;
		private int lastVotedEmotion;

		public PacteraPulseJsonHttpResponseHandler()
		{
		}

		public PacteraPulseJsonHttpResponseHandler(int votedEmotion)
		{
			lastVotedEmotion = votedEmotion;
		}

		public PacteraPulseJsonHttpResponseHandler(String encoding)
		{
			super(encoding);
		}

		@Override
		public void onStart()
		{
			super.onStart();
			showProgressDlg();
		}

		@Override
		public void onFinish()
		{
			DismissProgressDlg();
			super.onFinish();
		}

		@Override
		public void onCancel()
		{
			DismissProgressDlg();
			super.onCancel();
			Log.d(getTag(),"ResponseHandler onCancel");
		}

		@Override
		public void onSuccess(int statusCode, Header[] headers, JSONObject response)
		{
			super.onSuccess(statusCode, headers, response);
			Log.d("Voting result", response.toString());
			// save the vote
			voteManager.saveVote(lastVotedEmotion);
			// move to the result fragment
			emotionToResult();
		}

		@Override
		public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse)
		{
			super.onFailure(statusCode, headers, throwable, errorResponse);
			Toast.makeText(getActivity().getBaseContext(), "Network error, please vote again!", Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable)
		{
			super.onFailure(statusCode, headers, responseString, throwable);
			Toast.makeText(getActivity().getBaseContext(), "Network error, please vote again!", Toast.LENGTH_SHORT).show();
		}

		private void showProgressDlg()
		{
			progressdlg = ProgressDialog.show(getActivity(),getString(R.string.app_name),getString(R.string.gettingData),true, false);
		}

		private void DismissProgressDlg()
		{
			if (null != progressdlg)
			{
				progressdlg.dismiss();
				progressdlg = null;
			}
		}
	}
}
