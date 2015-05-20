package au.com.pactera.pacterapulse.core;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import au.com.pactera.pacterapulse.R;

import butterknife.ButterKnife;

/**
 * Created by kai on 19/05/15.
 */
public abstract class BaseFragment<T> extends Fragment implements
        CocoLoader<T> {

    protected static final int NEVER = -1;
    protected static final int ONCREATE = 0;
    protected Context context;
    protected Loader<T> loader;
    protected View v;
    private T items;
    private boolean loaderRunning;


    /**
     * Close current UI container(activity/dialog)
     */
    public void finish() {
        {
            getActivity().finish();
        }
    }

    /**
     * run on Ui thread
     *
     * @param runnable
     */
    protected void runOnUiThread(Runnable runnable) {
        getActivity().runOnUiThread(runnable);
    }

    /**
     * Get exception from loader if it provides one by being a
     * {@link ThrowableLoader}
     *
     * @param loader
     * @return exception or null if none provided
     */
    protected Exception getException(final Loader<T> loader) {
        if (loader instanceof ThrowableLoader) {
            return ((ThrowableLoader<T>) loader).clearException();
        } else {
            return null;
        }
    }

    public ThrowableLoader<T> getLoader() {
        return (ThrowableLoader<T>) loader;
    }

    /**
     * When the loader been initialized
     */
    protected int getLoaderOn() {
        return BaseFragment.NEVER;
    }

    public CharSequence getTitle() {
        return "";
    }

    /**
     * Is this fragment still part of an activity and usable from the UI-thread?
     *
     * @return true if usable on the UI-thread, false otherwise
     */
    protected boolean isUsable() {
        return getActivity() != null;
    }

    public abstract int layoutId();

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getLoaderOn() == ONCREATE && reloadNeeded(savedInstanceState)) {
            onStartLoading();
            getLoaderManager().initLoader(this.hashCode(), getArguments(), this);
        }
    }

    protected boolean reloadNeeded(final Bundle savedInstanceState) {
        return true;
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        setHasOptionsMenu(true);
    }


    @Override
    public Loader<T> onCreateLoader(final int id, final Bundle args) {
        onStartLoading();
        return loader = new ThrowableLoader<T>(getActivity(), items) {
            @Override
            public T loadData() throws Exception {
                return pendingData(args);
            }
        };
    }


    protected void onStartLoading() {
        loaderRunning = true;
    }

    protected void onStopLoading() {
        loaderRunning = false;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater,
                             final ViewGroup container, final Bundle savedInstanceState) {
        v = inflater.inflate(layoutId(), null);
        ButterKnife.inject(this, v);
        try {
            setupUI(v, savedInstanceState);
        } catch (final Exception e) {

        }
        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
        v = null;
        loader = null;
    }


    protected final <E extends View> E view(int resourceId) {
        return (E) v.findViewById(resourceId);
    }

    /**
     * @param items
     */
    @Override
    public void onLoaderDone(final T items) {

    }

    @Override
    public void onLoaderReset(final Loader<T> loader) {

    }

    @Override
    public void onLoadFinished(final Loader<T> loader, final T items) {
        final Exception exception = getException(loader);
        onStopLoading();
        if (exception != null) {
            showError(exception);
            return;
        }
        onLoaderDone(items);
    }

    @Override
    public T pendingData(final Bundle arg) throws Exception {
        return null;
    }

    /**
     * Reload current loader
     */
    public void refresh() {
        refresh(getArguments());
    }

    /**
     * 带参数的刷新
     *
     * @param b
     */
    protected void refresh(final Bundle b) {
        onStartLoading();
        getLoaderManager().restartLoader(this.hashCode(), b, this);
    }

    protected boolean isLoaderRunning() {
        return loaderRunning;
    }

    protected abstract void setupUI(View view, Bundle bundle) throws Exception;

    /**
     * Show exception
     *
     * @param e
     */
    @Override
    public void showError(final Exception e) {

    }


    public void startActivitySafely(Intent intent) {
        try {
            super.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getActivity(), R
                    .string.activity_not_found, Toast.LENGTH_SHORT).show();
        } catch (SecurityException e) {
            Toast.makeText(getActivity(), R.string.activity_not_found, Toast.LENGTH_SHORT).show();
        }
    }

    public void startActivityForResultSafely(Intent intent, int requestCode) {
        try {
            super.startActivityForResult(intent, requestCode);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getActivity(), R.string.activity_not_found, Toast.LENGTH_SHORT).show();
        } catch (SecurityException e) {
            Toast.makeText(getActivity(), R.string.activity_not_found, Toast.LENGTH_SHORT).show();
        }
    }

    public boolean onBackPressed() {
        return false;
    }

}
