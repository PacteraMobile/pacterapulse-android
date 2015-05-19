package com.pactera.pacterapulseopensourceandroid.core;

import android.app.Activity;
import android.os.Bundle;

import butterknife.ButterKnife;

/**
 * Created by kai on 19/05/15.
 */
public abstract class BaseActivity extends Activity {

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layoutId());
        try {
            init(savedInstanceState);
        } catch (final Exception e) {
            finish();
            return;
        }
        ButterKnife.inject(this);
    }

    protected abstract void init(Bundle savedInstanceState) throws Exception;


    public abstract int layoutId();

}
