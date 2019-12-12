import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class GUI 
{	
	HashMap<Integer, Book> data;
//	ArrayList<Book> order = new ArrayList<>();
	ArrayList<String> order = new ArrayList<>();
	
	JFrame frame;
	JPanel mainPanel;
	JPanel inputPanel;
	JPanel buttonsPanel;
	
	int currItem = 0, totalItems = 0;
	int textFieldLength = 40;
	int length = 640, height = 200;
	int orderQuantity = 0;
	double currTotal = 0, total = 0, discount = 0;
	
	
	JButton process;
	JButton confirm;
	JButton viewOrder;
	JButton finishOrder;
	JButton newOrder;
	JButton exit;
	
	JLabel itemsLabel;
	JLabel bookIDLabel;
	JLabel quantityLabel;
	JLabel infoLabel;
	JLabel subtotalLabel;
	
	JTextField items;
	JTextField bookID;
	JTextField quantity;
	JTextField info;
	JTextField subtotal;

	public GUI(HashMap<Integer, Book> data)
	{
		this.data = data;
		frame = new JFrame();
		
		mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		
		setupButtonsPanel();
		setupInputPanel();
		
		mainPanel.add(inputPanel, BorderLayout.WEST);
		mainPanel.add(buttonsPanel, BorderLayout.SOUTH);
		
		frame.add(mainPanel);
		
		
		//Frame settings
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Ye Olde Book Shoppe");
		frame.setSize(length, height);
		frame.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width/2 -frame.getWidth()/2, Toolkit.getDefaultToolkit().getScreenSize().height/2 -frame.getHeight()/2);
		frame.setVisible(true);
	}
	
	void setupButtonsPanel()
	{
		buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.X_AXIS));
		buttonsPanel.add(process = new JButton("Process Item #1"));
		process.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				processItem();
			}});
		buttonsPanel.add(confirm = new JButton("Confirm Item #1"));
		confirm.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				confirmItem();
			}});
		confirm.setEnabled(false);
		buttonsPanel.add(viewOrder = new JButton("View Order"));
		viewOrder.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				viewOrder();
			}});
		viewOrder.setEnabled(false);
		buttonsPanel.add(finishOrder = new JButton("Finish Order"));
		finishOrder.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				finishOrder();
			}});
		finishOrder.setEnabled(false);
		buttonsPanel.add(newOrder = new JButton("New Order"));
		newOrder.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				newOrder();
			}});
		buttonsPanel.add(exit = new JButton("Exit"));
		exit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
			}});
	}

	void setupInputPanel()
	{
		inputPanel = new JPanel();
		inputPanel.setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.EAST;
		inputPanel.add(itemsLabel = new JLabel("  Enter number of items in this order: "), constraints);
		constraints.gridx = 1;
		constraints.gridy = 0;
		inputPanel.add(items = new JTextField(textFieldLength), constraints);
		items.setDisabledTextColor(Color.black);
		constraints.gridx = 0;
		constraints.gridy = 1;
		inputPanel.add(bookIDLabel = new JLabel("  Enter book ID for item: #1: "), constraints);
		constraints.gridx = 1;
		constraints.gridy = 1;
		inputPanel.add(bookID = new JTextField(textFieldLength), constraints);
		constraints.gridx = 0;
		constraints.gridy = 2;
		inputPanel.add(quantityLabel = new JLabel("  Enter quantity for item #1: "), constraints);
		constraints.gridx = 1;
		constraints.gridy = 2;
		inputPanel.add(quantity = new JTextField(textFieldLength), constraints);
		constraints.gridx = 0;
		constraints.gridy = 3;
		inputPanel.add(infoLabel = new JLabel("  Item #1 info: "), constraints);
		constraints.gridx = 1;
		constraints.gridy = 3;
		inputPanel.add(info = new JTextField(textFieldLength), constraints);
		info.setEnabled(false);
		info.setDisabledTextColor(Color.black);
		constraints.gridx = 0;
		constraints.gridy = 4;
		inputPanel.add(subtotalLabel = new JLabel("  Order subtotal for 0 item(s): "), constraints);
		constraints.gridx = 1;
		constraints.gridy = 4;
		inputPanel.add(subtotal = new JTextField(textFieldLength), constraints);
		subtotal.setEnabled(false);
		subtotal.setDisabledTextColor(Color.black);
	}
	
	void processItem()
	{
		// Locks in the number of orders
		if (currItem == 0)
		{
			items.setEnabled(false);
			totalItems = Integer.parseInt((items.getText()));
			currItem++;
			changeLabels();
		}
		
		// Checks that there is a bookID to process
		if (!bookID.getText().isEmpty())
		{
			process.setEnabled(false);
			confirm.setEnabled(true);
			
			// Calculates the discount
			orderQuantity = Integer.parseInt((quantity.getText()));
			if (orderQuantity >= 15)
				discount = .8;
			else if (orderQuantity >= 10)
				discount = .85;
			else if (orderQuantity >= 5)
				discount = .9;
			
			// Checks if the bookID is in the inventory
			if (data.containsKey(Integer.parseInt(bookID.getText())))
			{
				Book book = data.get(Integer.parseInt(bookID.getText()));
				currTotal = Double.parseDouble(String.format("%.2f", book.price * orderQuantity * (discount == 0.0 ? 1 : discount)));
				total+= currTotal;
				info.setText(book.id + " " + book.title + " $" + book.price + " " + orderQuantity + " " + (discount != 0.0 ?  String.format("%.0f",(100 - (discount*100))) : 0 ) + "% $" + currTotal);
				infoLabel.setText("  Item #" + currItem + " info: ");
			}
			
		}
		
	}
	
	void confirmItem()
	{
		if(data.containsKey(Integer.parseInt(bookID.getText())))
		{	
			Book book = data.get(Integer.parseInt(bookID.getText()));
			subtotal.setText("$" + total);
			order.add(book.id + ", " + book.title + ", $" + book.price + ", " + orderQuantity + ", " + (discount != 0.0 ?  String.format("%.0f",(100 - (discount*100))) : 0 ) + "%, $" + currTotal);
			JOptionPane.showMessageDialog(null, "Item #" + currItem + " accepted");
			
			// Update UI
			if (currItem < totalItems)
			{				
				currItem++;
				bookIDLabel.setText("  Enter book ID for item #" + currItem + ": ");
				quantityLabel.setText("  Enter quantity for item #" + currItem + ": ");
				subtotalLabel.setText("  Order subtotal for "+ (currItem - 1) +" item(s): ");
				process.setText("Process Item #" + currItem);
				confirm.setText("Confirm Item #" + currItem);
				process.setEnabled(true);
				viewOrder.setEnabled(true);
			}
			else
			{
				itemsLabel.setText("  Number of items in this order: ");
				bookIDLabel.setText("");
				bookID.setEnabled(false);
				quantityLabel.setText("");
				quantity.setEnabled(false);
				subtotalLabel.setText("  Order subtotal for "+ (currItem) +" item(s): ");
				process.setText("Process Item");
				confirm.setText("Confirm Item");
			}
			
			// Reset form
			bookID.setText("");
			quantity.setText("");
			confirm.setEnabled(false);
			finishOrder.setEnabled(true);
			
		}
		else
		{
			JOptionPane.showMessageDialog(null, "Book ID " + bookID.getText() + " not in file");
			process.setEnabled(true);
			confirm.setEnabled(false);
		}
	}
	
	void viewOrder()
	{
		String tmp = "";
		for (int i = 0; i < order.size(); i++)
			tmp+= order.get(i).toString() + "\n";
		JOptionPane.showMessageDialog(null, tmp);
	}
	
	void finishOrder()
	{
		
		// Create invoice
		ZonedDateTime date = ZonedDateTime.now(); 
		DateTimeFormatter format = DateTimeFormatter.ofPattern("MM/dd/YY HH:mm:ss z"); 
		DateTimeFormatter format2 = DateTimeFormatter.ofPattern("DDMMYYYYHHMM"); 
		String tmp = "Date: " + date.format(format) + "\n\n" ;
		tmp+= "Number of line items: " + order.size();
		tmp+= "\n\nItem# / ID / Title / Price / Qty / Disc % / Subtotal: \n\n";
		
		for (int i = 1; i <= order.size(); i++)
			tmp+= i + ". " + order.get(i - 1).toString() + "\n"; 
		
		tmp+= "\n\n\nOrder subtotal: $" + total;
		tmp+= "\n\nTax rate: 6%";
		tmp+= "\n\nTax Amount: $" + String.format("%.2f", total*.06);
		tmp+= "\n\nOrder total: $" + String.format("%.2f", total*1.06);
		tmp+= "\n\nThanks for shopping at the Ye Olde Book Shoppe!";
		JOptionPane.showMessageDialog(null, tmp);
		
		
		// Write to file
		tmp = "";
        for (int i = 0; i < order.size(); i++ )
        {
        	tmp+= date.format(format2) + ", " + order.get(i).toString() + ", " + date.format(format) + "\n";
        }
        
		try
		{
			File file = new File("transactions.txt");
			BufferedWriter output;
			
			if (!file.exists())
				file.createNewFile();
			output = new BufferedWriter(new FileWriter(file, true));
			output.write(tmp);
			output.close();
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
        
	}
	
	void newOrder()
	{
		frame.dispose();
		new GUI(data);
	}
	
	void changeLabels()
	{
		
		bookIDLabel.setText("  Enter book ID for item #" + currItem + ": ");
		quantityLabel.setText("  Enter quantity for item #" + currItem + ": ");
		infoLabel.setText("  Item " + currItem + " info: ");
		process.setText("Process Item #" + currItem);
		confirm.setText("Confirm Item #" + currItem);
	}
}
