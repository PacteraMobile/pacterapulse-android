package au.com.pactera.pacterapulse.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import au.com.pactera.pacterapulse.MainActivity;
import au.com.pactera.pacterapulse.R;
import au.com.pactera.pacterapulse.core.BaseFragment;
import butterknife.OnClick;


public class IntroductionFragment extends BaseFragment<Void> {

    @Override
    public int layoutId() {
        return R.layout.fragment_introduction;
    }

    @Override
    protected void setupUI(View view, Bundle bundle) throws Exception {
        if (getActivity().getActionBar()!=null) {
            getActivity().getActionBar().hide();
        }
    }

    @OnClick(R.id.btnAgree)
    void launch() {
        finish();
        startActivitySafely(new Intent(context, MainActivity.class));
    }

}
