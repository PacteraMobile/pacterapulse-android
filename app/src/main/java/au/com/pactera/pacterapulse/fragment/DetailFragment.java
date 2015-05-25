package au.com.pactera.pacterapulse.fragment;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import au.com.pactera.pacterapulse.R;
import au.com.pactera.pacterapulse.core.BaseFragment;
import butterknife.InjectView;
import butterknife.InjectViews;
import butterknife.OnClick;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

/**
 * Detail fragment allow users express their opinions about their emotion
 * <p/>
 * Created by kai on 22/05/15.
 */
public class DetailFragment extends BaseFragment<Void>
{
	static final String SUCCESS = "_SUCCESS";
	@InjectView(R.id.emotion_icon)
	ImageView emotionIcon;
	@InjectView(R.id.userName)
	TextView tvUserName;
	@InjectViews({R.id.seek_work_overload,R.id.seek_hear_more,R.id.seek_project_feedback})
	List<SeekBar> seekBars;
	@InjectView(R.id.btnSubmit)
	Button btnSubmit;

	@Override
	protected void setupUI(View view, Bundle bundle) throws Exception
	{
		int emotion=-1;
		String userName;
		Bundle bundleArg = getArguments();
		if(null != bundleArg)
		{
			emotion = bundleArg.getInt(EmotionFragment.EMOTIONS,-1);
			userName = bundleArg.getString(EmotionFragment.USERNAME);
			switch (emotion)
			{
			case 0:
				emotionIcon.setImageResource(R.mipmap.happy_icon);
				for(SeekBar bar : seekBars)
				{
					bar.setProgress(10);
				}
				btnSubmit.setTextColor(getResources().getColor(android.R.color.holo_red_light));
				break;
			case 1:
				emotionIcon.setImageResource(R.mipmap.soso_icon);
				for(SeekBar bar : seekBars)
				{
					bar.setProgress(5);
				}
				btnSubmit.setTextColor(getResources().getColor(android.R.color.holo_blue_light));
				break;
			case 2:
				emotionIcon.setImageResource(R.mipmap.unhappy_icon);
				for(SeekBar bar : seekBars)
				{
					bar.setProgress(0);
				}
				btnSubmit.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
				break;
			default:
				emotionIcon.setImageResource(R.mipmap.happy_icon);
			}
			tvUserName.setText(userName);
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
		return R.layout.fragment_detail;
	}

	@OnClick({R.id.btnSubmit})
	void onSubmit(View submitButton)
	{
		Crouton.makeText(getActivity(), "Submitted", Style.INFO).show();
	}

	@Override
	protected void onStartLoading()
	{
		super.onStartLoading();
	}

	@Override
	protected void onStopLoading()
	{
		super.onStopLoading();
	}

	@Override
	public Void pendingData(Bundle arg) throws Exception
	{
		return super.pendingData(arg);
	}

	@Override
	public void showError(Exception e)
	{
		super.showError(e);
	}

	@Override
	public void onLoaderDone(Void items)
	{
		super.onLoaderDone(items);
	}
}
