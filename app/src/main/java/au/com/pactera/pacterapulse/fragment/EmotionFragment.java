package au.com.pactera.pacterapulse.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;


import au.com.pactera.pacterapulse.R;
import au.com.pactera.pacterapulse.core.BaseFragment;
import au.com.pactera.pacterapulse.core.SinglePaneActivity;
import au.com.pactera.pacterapulse.helper.NetworkHelper;
import au.com.pactera.pacterapulse.helper.VoteManager;
import butterknife.InjectView;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;


public class EmotionFragment extends BaseFragment<Boolean> implements View.OnClickListener {
    static final String SUCCESS = "_SUCCESS";
    private final int VOTE_HAPPY = 0;
    private final int VOTE_NEUTRAL = 1;
    private final int VOTE_SAD = 2;
    private final int VOTE_INVALID = -1;
    @InjectView(R.id.btnSad)
    ImageButton btnSad;
    @InjectView(R.id.btnNeutral)
    ImageButton btnNeutral;
    @InjectView(R.id.btnHappy)
    ImageButton btnHappy;

    private VoteManager voteManager;
    private int vote;


    @Override
    protected void setupUI(View view, Bundle bundle) throws Exception {
        voteManager = new VoteManager(getActivity());
        checkNetwork();
        btnHappy.setOnClickListener(this);
        btnNeutral.setOnClickListener(this);
        btnSad.setOnClickListener(this);
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


    private void onButtonPressed(int id) {
        // fetch data
        switch (id) {
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
        if (checkNetwork())
            refresh(getArguments());
    }



    @Override
    public Boolean pendingData(Bundle arg) throws Exception {
        if (voteManager.hasVotedToday())
            return false;
        NetworkHelper.postVote(vote, context);
        return true;
    }

    @Override
    public void showError(Exception e) {
        Toast.makeText(getActivity().getBaseContext(), "Network error, please vote again!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoaderDone(Boolean items) {
        super.onLoaderDone(items);
        voteManager.saveVote(vote);
        SinglePaneActivity.start(ResultFragment.class, getActivity(), new Intent().putExtra(SUCCESS, items.booleanValue()));
    }

    @Override
    public void onClick(View v) {
        onButtonPressed(v.getId());
    }

}
