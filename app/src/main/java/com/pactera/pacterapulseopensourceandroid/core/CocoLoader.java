package com.pactera.pacterapulseopensourceandroid.core;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;

public interface CocoLoader<T> extends LoaderManager.LoaderCallbacks<T> {

    /**
     * 后台的操作或者数据读取
     *
     * @param arg
     * @return
     * @throws Exception
     */
    T pendingData(Bundle arg) throws Exception;

    /**
     * 完成数据载入后的接口
     *
     * @param items
     */
    void onLoaderDone(final T items);

    /**
     * Show exception
     *
     * @param e
     */
    void showError(final Exception e);

}
