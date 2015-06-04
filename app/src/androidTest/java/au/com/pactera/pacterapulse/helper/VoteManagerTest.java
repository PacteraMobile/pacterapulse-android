package au.com.pactera.pacterapulse.helper;

import android.test.InstrumentationTestCase;

/**
 * Created by kai on 2/06/15.
 */
public class VoteManagerTest extends InstrumentationTestCase
{

	private VoteManager vm;

	@Override
	protected void setUp() throws Exception
	{
		super.setUp();
		vm = new VoteManager(getInstrumentation().getContext());
	}

	public void testSaveVote() throws Exception
	{
		vm.saveVote(1);
		vm.hasVotedToday();
	}

}