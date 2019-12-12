// SynchronizedBuffer synchronizes access to a single shared integer.
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;

public class SynchronizedBuffer implements Buffer
{
	// Lock to control synchronization with this buffer   
	private Lock accessLock = new ReentrantLock();

	// conditions to control reading and writing
	private Condition canWithdraw = accessLock.newCondition();

	private int account = 0; // shared by producer and consumer threads

	// setter method for producer to write into buffer
	public void deposit( int value, int id )
	{
		accessLock.lock(); // lock the account
		
		account += value;
		System.out.println("Thread D" + id + " deposits $" + value + "\t\t\t\t\t\t\tBalance is $" + account);
		
		// signal threads waiting to withdraw from account
		canWithdraw.signalAll(); 
		accessLock.unlock(); // unlock the account
	} 

	// getter method for consumer to retrieve value from buffer
	public void withdraw( int value, int id )
	{
		accessLock.lock(); // lock the account
		try
		{
			// while locked, place thread in waiting state
			while ( account < value ) 
			{
				
				System.out.println( "\t\t\t\t\tThread W" + id + " withdraws $"+ value + "\t\tWithdrawal Blocked, Insufficient Funds" );
				canWithdraw.await(); // wait until the acount has more money
			} 
			
			account -= value;
			System.out.println("\t\t\t\t\tThread W" + id + " withdraws $" + value + "\t\tBalance is $" + account);

		} 

		// if waiting thread interrupted, print stack trace
		catch ( InterruptedException exception ) 
		{
			exception.printStackTrace();
		}
		finally
		{
			accessLock.unlock(); // unlock this object
		} 

	} 

//	   public void displayState( String operation, String  )
//	   {
//	      System.out.printf( "%-40s%d\t\t\t\t%b\n", operation, buffer, 
//	         account );
//	   } 
}


