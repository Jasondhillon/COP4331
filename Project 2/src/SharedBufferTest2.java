// Application shows two threads manipulating a synchronized buffer.
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SharedBufferTest2
{
	public static void main( String[] args )
	{
		// create new thread pool with two threads
		ExecutorService application = Executors.newFixedThreadPool( 12 );

		// create SynchronizedBuffer to store ints
		Buffer sharedLocation = new SynchronizedBuffer();

		System.out.printf( "%-40s%s\t\t%s\n%-40s%s\n\n", "Deposit Threads", 
				"Withdrawal Threads", "Balance", "---------------", "-----------------\t\t--------------" );

		try // try to start producer and consumer
		{
			application.execute( new DepositThread( sharedLocation, 1 ) );
			application.execute( new DepositThread( sharedLocation, 2 ) );
			application.execute( new DepositThread( sharedLocation, 3 ) );
			application.execute( new DepositThread( sharedLocation, 4 ) );

			application.execute( new WithdrawThread( sharedLocation, 1 ) );
			application.execute( new WithdrawThread( sharedLocation, 2 ) );
			application.execute( new WithdrawThread( sharedLocation, 3 ) );
			application.execute( new WithdrawThread( sharedLocation, 4 ) );

			application.execute( new WithdrawThread( sharedLocation, 5 ) );
			application.execute( new WithdrawThread( sharedLocation, 6 ) );
			application.execute( new WithdrawThread( sharedLocation, 7 ) );
			application.execute( new WithdrawThread( sharedLocation, 8 ) );
		} // end try
		catch ( Exception exception )
		{
			exception.printStackTrace();
		} // end catch

//		application.shutdown();
	} 
}