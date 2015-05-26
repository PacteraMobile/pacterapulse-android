package au.com.pactera.pacterapulse.fragment;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;

import java.util.List;

import au.com.pactera.pacterapulse.R;
import au.com.pactera.pacterapulse.app.PacteraPulse;
import au.com.pactera.pacterapulse.chart.EmotionBarChartView;
import au.com.pactera.pacterapulse.core.BaseFragment;
import au.com.pactera.pacterapulse.core.SinglePaneActivity;
import au.com.pactera.pacterapulse.helper.NetworkHelper;
import au.com.pactera.pacterapulse.model.Emotions;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.InjectViews;
import butterknife.OnCheckedChanged;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;


public class ResultFragment extends BaseFragment<Emotions> {

    private static final String TYPE = "_TYPE";
    private final int reqCode = 999;
    @InjectView(R.id.progressBar)
    ProgressBar progressBar;
    @InjectView(R.id.chartView)
    EmotionBarChartView chartView;

    @InjectViews({R.id.oneweek, R.id.oneday, R.id.onemonth})
    List<RadioButton> radios;

    private String currentType = "24hours";
    private Menu detailMenu;


    @OnCheckedChanged({R.id.oneday, R.id.onemonth, R.id.oneweek})
    void onActionsChecked(RadioButton view) {
        if (view.isChecked() && !view.getTag().equals(currentType)) {
            currentType = (String) view.getTag();
            Bundle b = new Bundle();
            b.putString(TYPE, currentType);
            refresh(b);
            chartView.clear();
        }
    }

    @Override
    public int layoutId() {
        return R.layout.fragment_result;
    }

    @Override
    protected void setupUI(View view, Bundle bundle) throws Exception {
        if (!getArguments().getBoolean(EmotionFragment.SUCCESS, true)) {
            Crouton.makeText(getActivity(), R.string.voted_today, Style.ALERT).show();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        if(getArguments().getBoolean(EmotionFragment.SUCCESS, false))
        {
            inflater.inflate(R.menu.menu_main, menu);
            menu.findItem(R.id.action_showResults).setVisible(false);
            menu.findItem(R.id.action_moreDetails).setVisible(true);
            detailMenu = menu;
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
        case R.id.action_moreDetails:
            String userName=PacteraPulse.getInstance().getGivenName()+" "+ PacteraPulse.getInstance().getSurName();
            Intent intent = new Intent();
            intent.putExtra(EmotionFragment.EMOTIONS, getArguments().getInt(EmotionFragment.EMOTIONS,0));
            intent.putExtra(EmotionFragment.USERNAME, userName);
            SinglePaneActivity.startForResult(DetailFragment.class, this, intent, reqCode);
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(reqCode == requestCode && null != detailMenu)
        {
            switch (resultCode)
            {
            case Activity.RESULT_OK:
                detailMenu.findItem(R.id.action_moreDetails).setVisible(false);
                break;
            default:
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public Emotions pendingData(Bundle arg) throws Exception {
        String type = arg.getString(TYPE, currentType);
        return NetworkHelper.getResult(type);
    }

    @Override
    protected int getLoaderOn() {
        return ONCREATE;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        progressBar.setVisibility(View.VISIBLE);
        ButterKnife.apply(radios, new ButterKnife.Action<View>() {
            @Override
            public void apply(View view, int index) {
                view.setEnabled(false);
            }
        });
    }

    @Override
    protected void onStopLoading() {
        super.onStopLoading();
        progressBar.setVisibility(View.GONE);
        ButterKnife.apply(radios, new ButterKnife.Action<View>()
        {
            @Override
            public void apply(View view, int index)
            {
                view.setEnabled(true);
            }
        });
    }

    @Override
    public void onLoaderDone(Emotions items)
    {
        setEmotionResult(items.getHappy(), items.getSoso(), items.getSad());
    }

    @Override
    public void showError(Exception e) {
        Toast.makeText(context, R.string.invalidNetwork, Toast.LENGTH_SHORT).show();
    }

    private void setEmotionResult(int happy, int neutral, int sad) {
        if (null != chartView) {
            chartView.setDataSet(happy, neutral, sad);
            chartView.invalidate();
        }
    }

}
