// Buffer interface specifies methods called by Producer and Consumer.

public interface Buffer 
{
	public void deposit( int value, int id ); // place int value into Buffer
	public void withdraw( int value, int id); // get int value from Buffer
} // end interface Buffer
