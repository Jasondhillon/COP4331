// Consumer's run method loops ten times reading a value from buffer.
import java.util.Random;

public class WithdrawThread implements Runnable 
{ 
	private static Random generator = new Random();
	private Buffer sharedLocation; // reference to shared object
	private int id;

	public WithdrawThread( Buffer shared, int id )
	{
		sharedLocation = shared;
		this.id = id;
	} 

	// Withdraw values fron $1 to $50
	public void run()
	{
		while(true)
		{
			int value = generator.nextInt( 51 ) + 1;;
	
			try 
			{
				Thread.sleep( generator.nextInt( 35 ) );    
				sharedLocation.withdraw(value, id);
			}
	
			catch ( InterruptedException exception ) 
			{
				exception.printStackTrace();
			}
		}

	}
}