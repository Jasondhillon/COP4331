// Producer's run method stores the values 1 to 10 in buffer.
import java.util.Random;

public class DepositThread implements Runnable 
{
	private static Random generator = new Random();
	private Buffer sharedLocation; // reference to shared object
	private int id;

	public DepositThread( Buffer shared, int id )
	{
		sharedLocation = shared;
		this.id = id;
	} 

	// Deposit values fron $1 to $250
	public void run()
	{
		while(true)
		{
			int value = generator.nextInt( 251 ) + 1;
	
			try 
			{
				Thread.sleep( generator.nextInt( 90 ) );
				sharedLocation.deposit( value, id );
			} 
	
			catch ( InterruptedException exception ) 
			{
				exception.printStackTrace();
			}
		}

	}
}