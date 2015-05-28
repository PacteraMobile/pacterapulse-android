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

import android.content.Context;


/**
 * Loader that support throwing an exception when loading in the background
 *
 */
abstract class ThrowableLoader<D> extends AsyncLoader<D>
{

	private final D data;

	private Exception exception;

	/**
	 * Create loader for context and seeded with initial data
	 *
	 * @param context context
	 * @param data initial data
	 */
	public ThrowableLoader(final Context context, final D data)
	{
		super(context);

		this.data = data;
	}

	@Override
	public D loadInBackground()
	{
		exception = null;
		try
		{
			return loadData();
		}
		catch (final Exception e)
		{
			exception = e;
			return data;
		}
	}

	/**
	 * Get exception from loader if it provides one
	 *
	 * @return exception instance from loader
	 */
	public Exception getException()
	{
		return exception;
	}

	/**
	 * Clear the stored exception and return it
	 *
	 * @return exception instance from loader
	 */
	public Exception clearException()
	{
		final Exception throwable = exception;
		exception = null;
		return throwable;
	}

	/**
	 * Load data in the background
	 *
	 * @return data
	 * @throws Exception
	 */
	public abstract D loadData() throws Exception;
}
