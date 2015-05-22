package au.com.pactera.pacterapulse.fragment;

import android.os.Bundle;
import android.view.View;

import au.com.pactera.pacterapulse.R;
import au.com.pactera.pacterapulse.core.BaseFragment;

/**
 * Detail fragment allow users express their opinions about their emotion
 *
 * Created by kai on 22/05/15.
 */
public class DetailFragment extends BaseFragment<Void>{

    @Override
    public int layoutId() {
        return R.layout.fragment_detail;
    }

    @Override
    protected void setupUI(View view, Bundle bundle) throws Exception {

    }
}
