
public class Book
{
	int id;
	String title;
	double price;
	
	public Book(int id, String title, double price)
	{
		this.id = id;
		this.title = title;
		this.price = price;
	}

	@Override
	public String toString()
	{
		return id + " " + title + " " + price + "\n";
	}
	
	
}
