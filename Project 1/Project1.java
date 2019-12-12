import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

public class Project1
{

	public static void main(String[] args) throws FileNotFoundException, ClassNotFoundException, IOException 
	{
		// Reads data and starts GUI
		new GUI(readData(new File("inventory.txt")));
	}
	
	private static HashMap<Integer, Book> readData(File file) throws FileNotFoundException, IOException
	{
		int id;
		String title;
		double price;
		HashMap<Integer, Book> data = new HashMap<Integer, Book>();

		Scanner in = new Scanner(file);
		in.useDelimiter(",|\\n");
		while (in.hasNextLine())
		{
			id = Integer.parseInt(in.next());
			title = in.next();
			price = Double.parseDouble(in.next());
			data.put(id, new Book(id, title, price));
		}
		
		in.close();
		return data;
		
	}

}
