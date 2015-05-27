package au.com.pactera.pacterapulse.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import au.com.pactera.pacterapulse.R;
import au.com.pactera.pacterapulse.core.BaseFragment;
import au.com.pactera.pacterapulse.core.SinglePaneActivity;
import au.com.pactera.pacterapulse.helper.NetworkHelper;
import au.com.pactera.pacterapulse.helper.VoteManager;
import butterknife.OnClick;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;


public class EmotionFragment extends BaseFragment<Boolean>
{
	static final String SUCCESS = "_SUCCESS";
	static final String EMOTIONS = "_EMOTIONS";
	static final String USERNAME = "_USERNAME";
	private VoteManager voteManager;
	private int vote;
	private ProgressDialog progressDialog;


	@Override
	protected void setupUI(View view, Bundle bundle) throws Exception
	{
		voteManager = new VoteManager(getActivity());
		checkNetwork();
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
	{
		inflater.inflate(R.menu.menu_main, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		case android.R.id.home:
			SinglePaneActivity.start(IntroductionFragment.class, getActivity());
			finish();
			return true;
		case R.id.action_showResults:
			SinglePaneActivity.start(ResultFragment.class, getActivity());
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private boolean checkNetwork()
	{
		ConnectivityManager connMgr = (ConnectivityManager) getActivity()
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected())
		{
			return true;
		}
		else
		{
			Crouton.makeText(getActivity(), R.string.invalidNetwork, Style.ALERT).show();
			return false;
		}
	}

	@Override
	public int layoutId()
	{
		return R.layout.fragment_emotion;
	}

	@OnClick({R.id.btnSad, R.id.btnHappy, R.id.btnNeutral})
	void onVote(View button)
	{
		if (voteManager.hasVotedToday())
		{
			SinglePaneActivity.start(ResultFragment.class, getActivity(), new Intent().putExtra(SUCCESS, false));
			return;
		}
		if (checkNetwork())
		{
			try
			{
				vote = Integer.parseInt((String) button.getTag());
			}
			catch (Exception e)
			{
				vote = -1;
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
		return NetworkHelper.postVote(vote, context);
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
		voteManager.saveVote(vote);
		if (items)
		{
			Intent intent = new Intent();
			intent.putExtra(SUCCESS, items.booleanValue());
			intent.putExtra(EMOTIONS, vote);
			SinglePaneActivity.start(ResultFragment.class, getActivity(), intent);
		}
		else
		{
			Toast.makeText(getActivity().getBaseContext(), R.string.vote_again, Toast.LENGTH_SHORT).show();
		}
	}
}
