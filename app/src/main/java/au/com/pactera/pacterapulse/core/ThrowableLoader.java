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
