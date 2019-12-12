import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.mysql.cj.jdbc.MysqlDataSource;

public class GUI 
{	
	String [] drivers = {"com.mysql.cj.jdbc.Driver"};
	String [] databases = {"jdbc:mysql://localhost:3306/project3"};
	
	private Statement statement;
	private Connection connection;
	
	JFrame frame;
	JPanel topPanel;
	JPanel mainPanel;
	JPanel inputPanel;
	JPanel commandPanel;
	JPanel buttonsPanel;
	
	int textFieldLength = 23;
	int length = 850, height = 600;
	
	JButton status;
	JButton connect;
	JButton clear;
	JButton execute;
	
	JLabel driverLabel;
	JLabel urlLabel;
	JLabel userLabel;
	JLabel passwordLabel;
	JLabel commandLabel;
	
	JComboBox<String> driver;
	JComboBox<String> url;
	JTextField user;
	JTextField password;
	JTextArea command;
	
	JTable results;
	JScrollPane scroll;
	ResultSetTableModel tableModel;

	public GUI()
	{
		frame = new JFrame();
		
		mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		
		topPanel = new JPanel();
		topPanel.setLayout(new BorderLayout());
		
		setupButtonsPanel();
		setupInputPanel();
		setupCommandPanel();
		tableModel = new ResultSetTableModel();
		
		topPanel.add(commandPanel, BorderLayout.NORTH);
		topPanel.add(buttonsPanel, BorderLayout.SOUTH);
		
		mainPanel.add(topPanel, BorderLayout.NORTH);
		//mainPanel.add(scroll = new JScrollPane(), BorderLayout.SOUTH);
		
		frame.add(mainPanel);
		
		
		//Frame settings
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("SQL Client GUI");
		frame.setSize(length, height);
		frame.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width/2 -frame.getWidth()/2, Toolkit.getDefaultToolkit().getScreenSize().height/2 -frame.getHeight()/2);
		frame.setVisible(true);
	}
	

	void setupButtonsPanel()
	{
		buttonsPanel = new JPanel();
		
		FlowLayout flow = new FlowLayout();
		flow.setAlignment(FlowLayout.LEFT);
		buttonsPanel.setLayout(flow);
		buttonsPanel.add(status = new JButton("No connection now"));
		status.setEnabled(false);
		buttonsPanel.add(connect = new JButton("Connect to Database"));
		connect.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try
				{
					Connect();
				} catch (ClassNotFoundException | SQLException e1)
				{
					e1.printStackTrace();
				}
			}});
		buttonsPanel.add(clear = new JButton("Clear SQL Command"));
		clear.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Clear();
			}});
		buttonsPanel.add(execute = new JButton("Execute SQL Command"));
		execute.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try
				{
					Execute();
				} catch (ClassNotFoundException e1)
				{
					e1.printStackTrace();
				}
			}});
	}

	void setupInputPanel()
	{
		inputPanel = new JPanel();
		inputPanel.setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.insets = new Insets(0,20,0,0);
		constraints.gridx = 0;
		constraints.gridy = 0;
		JLabel label = new JLabel("Enter Database Information");
		label.setFont(new Font("Dialog", Font.BOLD, 15));
		inputPanel.add(label, constraints);
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.anchor = GridBagConstraints.EAST;
		inputPanel.add(driverLabel = new JLabel("JDBC Driver "), constraints);
		constraints.gridx = 1;
		constraints.gridy = 1;
		inputPanel.add(driver = new JComboBox<String>(drivers), constraints);
		driver.setPreferredSize(new Dimension(235, 25));
		constraints.gridx = 0;
		constraints.gridy = 2;
		inputPanel.add(urlLabel = new JLabel("Database URL"), constraints);
		constraints.gridx = 1;
		constraints.gridy = 2;
		inputPanel.add(url = new JComboBox<String>(databases), constraints);
		constraints.gridx = 0;
		constraints.gridy = 3;
		inputPanel.add(userLabel = new JLabel("Username "), constraints);
		constraints.gridx = 1;
		constraints.gridy = 3;
		inputPanel.add(user = new JTextField(textFieldLength), constraints);
		constraints.gridx = 0;
		constraints.gridy = 4;
		inputPanel.add(passwordLabel = new JLabel("Password "), constraints);
		constraints.gridx = 1;
		constraints.gridy = 4;
		inputPanel.add(password = new JTextField(textFieldLength), constraints);
		
	}
	
	void setupCommandPanel()
	{
		commandPanel = new JPanel();
		commandPanel.setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.insets = new Insets(0,20,0,0);
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.WEST;
		commandPanel.add(inputPanel, constraints);
		constraints.gridx = 1;
		constraints.gridy = 0;
		
		JPanel sqlPanel = new JPanel();
		sqlPanel.setLayout(new GridBagLayout());
		constraints.gridx = 0;
		constraints.gridy = 0;
		JLabel label = new JLabel("Enter An SQL Command");
		label.setFont(new Font("Dialog", Font.BOLD, 15));
		sqlPanel.add(label, constraints);
		constraints.gridx = 0;
		constraints.gridy = 1;
		sqlPanel.add(command = new JTextArea(8, 30), constraints);
		
		constraints.gridx = 1;
		constraints.gridy = 0;
		commandPanel.add(sqlPanel, constraints);
		
	}
	
	void Connect() throws ClassNotFoundException, SQLException 
	{
		Class.forName("com.mysql.cj.jdbc.Driver");
		
		MysqlDataSource dataSource = new MysqlDataSource();
		dataSource.setURL((String) url.getSelectedItem());
		dataSource.setUser((String) user.getText());
		dataSource.setPassword((String) password.getText()); 	
		
		try
		{
			connection = dataSource.getConnection();
			statement = connection.createStatement( ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY );
			status.setText("<html><font color = black>Connected to " + url.getSelectedItem() + "</font></html>");
		} catch (SQLException e)
		{
			 JOptionPane.showMessageDialog(null, e.getMessage());
		}
		
	}
	
	void Clear() 
	{
		command.setText("");
	}
	
	void Execute() throws ClassNotFoundException
	{
		
		try
		{
			if (scroll == null)
			{
				
				tableModel.setConnection(connection, statement);
				
				String text = command.getText().toLowerCase();
				if (text.contains("select"))
					tableModel.setQuery(text);
				else
					tableModel.setUpdate(text);
				
				results = new JTable(tableModel);
				mainPanel.add(scroll = new JScrollPane(results), BorderLayout.SOUTH);
				frame.pack();
			}
			
			else
			{
				String text = command.getText().toLowerCase();
				if (text.contains("select"))
					tableModel.setQuery(text);
				else
				{
					mainPanel.remove(scroll);
					scroll = null;
					tableModel.setUpdate(text);
					frame.repaint();
				}
			}
		}
		
		catch (SQLException e)
		{
			 JOptionPane.showMessageDialog(null, e.getMessage());
		}
		
			
	}
	
	
}
