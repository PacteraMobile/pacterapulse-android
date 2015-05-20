package au.com.pactera.pacterapulse.core;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;

public interface CocoLoader<T> extends LoaderManager.LoaderCallbacks<T> {

    /**
     * Called on a worker thread to perform the actual load. meaning this will not block your UI thread
     * You can do whatever you want here, if any exception was thrown, showError will be triggered or on LoaderDone will be executed instead
     *
     * @param arg
     * @return
     * @throws Exception
     */
    T pendingData(Bundle arg) throws Exception;

    /**
     * The callback of loader, result will be passed into as a parameter.
     *
     * @param items
     */
    void onLoaderDone(final T items);

    /**
     * The failback of loader, a exception which catched in pendingData will be passed
     *
     * @param e
     */
    void showError(final Exception e);

}
