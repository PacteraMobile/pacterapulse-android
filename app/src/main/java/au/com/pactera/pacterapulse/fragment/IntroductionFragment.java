package au.com.pactera.pacterapulse.fragment;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import au.com.pactera.pacterapulse.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link IntroductionFragment.OnIntroductionInteractionListener} interface
 * to handle interaction events.
 * Use the {@link IntroductionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IntroductionFragment extends Fragment implements View.OnClickListener
{
	public static final int INSTRUCTION_READ = 0;
	// The fragment tag parameter
	private static final String ARG_TAG = "INTRODUCTION_TAG";
	// Tag from Activity.
	private String ArgTag;



	private OnIntroductionInteractionListener mListener;

	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @return A new instance of fragment IntroductionFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static IntroductionFragment newInstance(String argTag)
	{
		IntroductionFragment fragment = new IntroductionFragment();
		Bundle args = new Bundle();
		args.putString(ARG_TAG,argTag);
		fragment.setArguments(args);
		return fragment;
	}

	public IntroductionFragment()
	{
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		if (getArguments() != null)
		{
			ArgTag = getArguments().getString(ARG_TAG);
		}
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState)
	{
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_introduction, container, false);
	}

	private void onButtonPressed(int id)
	{
		switch (id)
		{
		case R.id.btnAgree:
		default:
			if (getFragmentManager().getBackStackEntryCount() > 0)
			{
				getFragmentManager().popBackStack();
			}
			else
			{
				introToEmotion();
			}
			/*if (mListener != null)
			{
				mListener.onIntroductionInteraction(INSTRUCTION_READ);
			}*/
		}
	}

	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);
		try
		{
			mListener = (OnIntroductionInteractionListener) activity;
		}
		catch (ClassCastException e)
		{
			throw new ClassCastException(activity.toString()
					+ " must implement OnIntroductionInteractionListener");
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		ActionBar actionBar = getActivity().getActionBar();
		super.onActivityCreated(savedInstanceState);
		getActivity().findViewById(R.id.btnAgree).setOnClickListener(this);
		if(null!=actionBar)
		{
			actionBar.hide();
		}
	}

	@Override
	public void onDetach()
	{
		super.onDetach();
		mListener = null;
	}

	/*@Override
	public void onPrepareOptionsMenu(Menu menu)
	{
		menu.removeItem(R.id.action_settings);
		menu.removeItem(R.id.action_showResults);
		super.onPrepareOptionsMenu(menu);
	}*/

	@Override
	public void onClick(View v)
	{
		onButtonPressed(v.getId());
	}

	/**
	 * Replace introduction fragment to emotion fragment and also save it into back stack.
	 *
	 * @return commitment ID.
	 */
	private int introToEmotion()
	{
		return getFragmentManager().beginTransaction()
				.setCustomAnimations(R.animator.fragment_slide_left_enter, R.animator.fragment_slide_left_exit)
				.replace(R.id.container, EmotionFragment.newInstance(0, 0, 0))
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
	public interface OnIntroductionInteractionListener
	{
		public void onIntroductionInteraction(int id);
	}

}
