package au.com.pactera.pacterapulse.fragment;


import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import java.util.Objects;

import au.com.pactera.pacterapulse.R;
import au.com.pactera.pacterapulse.chart.EmotionBarChartView;
import au.com.pactera.pacterapulse.core.BaseFragment;
import au.com.pactera.pacterapulse.helper.NetworkHelper;
import au.com.pactera.pacterapulse.model.Emotions;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;


public class ResultFragment extends BaseFragment<Emotions> {

    private EmotionBarChartView mBarChartView;


    @Override
    public int layoutId() {
        return R.layout.fragment_result;
    }


    @Override
    protected void setupUI(View view, Bundle bundle) throws Exception {

        if (!getArguments().getBoolean(EmotionFragment.SUCCESS,true)) {
            Crouton.makeText(getActivity(),R.string.voted_today, Style.ALERT).show();
        }

        mBarChartView = new EmotionBarChartView(getActivity(), 0, 0);
        ((ViewGroup) view).addView(mBarChartView);
    }

    @Override
    public Emotions pendingData(Bundle arg) throws Exception {
        return NetworkHelper.getResult();
    }


    @Override
    public void onLoaderDone(Emotions items) {
        setEmotionResult(items.getHappy(), items.getSoso(), items.getSad());
    }

    @Override
    public void showError(Exception e) {
        Toast.makeText(context, "Network error!", Toast.LENGTH_SHORT).show();
    }

    private void setEmotionResult(int happy, int neutral, int sad) {
        if (null != mBarChartView) {
            mBarChartView.setDataSet(happy, neutral, sad);
            mBarChartView.invalidate();
        }
    }
}
