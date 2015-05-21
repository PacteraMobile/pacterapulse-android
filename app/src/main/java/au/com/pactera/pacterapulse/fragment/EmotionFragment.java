package au.com.pactera.pacterapulse.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import au.com.pactera.pacterapulse.R;
import au.com.pactera.pacterapulse.core.BaseFragment;
import au.com.pactera.pacterapulse.core.SinglePaneActivity;
import au.com.pactera.pacterapulse.helper.NetworkHelper;
import au.com.pactera.pacterapulse.helper.VoteManager;
import butterknife.OnClick;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;


public class EmotionFragment extends BaseFragment<Boolean> {
    static final String SUCCESS = "_SUCCESS";
    private VoteManager voteManager;
    private int vote;


    @Override
    protected void setupUI(View view, Bundle bundle) throws Exception {
        voteManager = new VoteManager(getActivity());
        checkNetwork();
    }

    private boolean checkNetwork() {
        ConnectivityManager connMgr = (ConnectivityManager) getActivity()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else {
            Crouton.makeText(getActivity(), R.string.invalidNetwork, Style.ALERT).show();
            return false;
        }
    }

    @Override
    public int layoutId() {
        return R.layout.fragment_emotion;
    }

    @OnClick({R.id.btnSad, R.id.btnHappy, R.id.btnNeutral})
    void onVote(View button) {
        if (voteManager.hasVotedToday()) {
            SinglePaneActivity.start(ResultFragment.class, getActivity(), new Intent().putExtra(SUCCESS, false));
            return;
        }
        if (checkNetwork()) {
            try {
                vote = Integer.parseInt((String) button.getTag());
            } catch (Exception e) {
                vote = -1;
            }
            refresh();
        }
    }

    @Override
    public Boolean pendingData(Bundle arg) throws Exception {
        return NetworkHelper.postVote(vote, context);
    }

    @Override
    public void showError(Exception e) {
        Toast.makeText(getActivity().getBaseContext(), R.string.vote_again, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoaderDone(Boolean items) {
        super.onLoaderDone(items);
        voteManager.saveVote(vote);
        if (items) {
            SinglePaneActivity.start(ResultFragment.class, getActivity(), new Intent().putExtra(SUCCESS, items.booleanValue()));
        } else {
            Toast.makeText(getActivity().getBaseContext(), R.string.vote_again, Toast.LENGTH_SHORT).show();
        }
    }


}
