/*
 * Copyright (c) 2015 Pactera. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * THIS CODE IS PROVIDED AS IS BASIS, WITHOUT WARRANTIES OR CONDITIONS
 * OF ANY KIND, EITHER EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION
 * ANY IMPLIED WARRANTIES OR CONDITIONS OF TITLE, FITNESS FOR A
 * PARTICULAR PURPOSE, MERCHANTABILITY OR NON-INFRINGEMENT.
 *
 * See the Apache License, Version 2.0 for the specific language
 * governing permissions and limitations under the License.
 */

package au.com.pactera.pacterapulse.core;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;

interface CocoLoader<T> extends LoaderManager.LoaderCallbacks<T>
{

	/**
	 * Called on a worker thread to perform the actual load. meaning this will not block your UI thread
	 * You can do whatever you want here, if any exception was thrown, showError will be triggered or on LoaderDone will be executed instead
	 *
	 * @param arg parameters
	 * @return data
	 * @throws Exception
	 */
	T pendingData(Bundle arg) throws Exception;

	/**
	 * The callback of loader, result will be passed into as a parameter.
	 *
	 * @param items callback result
	 */
	void onLoaderDone(final T items);

	/**
	 * The failback of loader, a exception which catched in pendingData will be passed
	 *
	 * @param e exception
	 */
	void showError(final Exception e);

}
