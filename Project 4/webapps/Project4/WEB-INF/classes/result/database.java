package result;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.IOException;
import javax.servlet.ServletException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletResponse;
import com.mysql.cj.jdbc.MysqlDataSource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServlet;

@SuppressWarnings("serial")
public class database extends HttpServlet
{
	
	private Connection connection;
	
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
	{
		try 
		{
			database(req, res);
		}
		catch (Exception e)
		{
			System.out.println(e);
		}
	}
	
	protected void database(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException, SQLException, ClassNotFoundException
	{
		res.setContentType("text/html");
		PrintWriter out = res.getWriter();
		out.println("<html>");
		out.println("<head>");
		out.println("<title>Project 4</title>");
		out.println("<meta charset=\"utf-8\" />");
		out.println("<style type=\"text/css\">");
		out.println("    body");
		out.println("    {");
		out.println("margin: 0;");
		out.println("height: 100%;");
		out.println("background-color: #262727;");
		out.println("font-size: x-large; ");
		out.println("font-family:calibri; color:#faf5ed;");
		out.println("}");
		out.println("p");
		out.println("{");
		out.println("text-align: center;");
		out.println("font-size: medium;");
		out.println("}");
		out.println("td, th {\r\n" + 
				"  border: 1px solid black;\r\n" + 
				"  text-align: left;\r\n" + 
				"  padding: 8px;\r\n" + 
				"}");
		out.println("tr:nth-child(even) {background-color: #262727;}");
		out.println("tr:nth-child(odd) {background-color: #393939;}");
		out.println("</style>");
		out.println("</head>");
		out.println("<body>");
		out.println("<br>");
		out.println("<h1 style=\"text-align: center\">");
		out.println("Welcome to the Fall 2019 Project 4 Enterprise System");
		out.println("</h1>");
		out.println("<h3 style=\"text-align: center\">");
		out.println("A Remote Database Management System");
		out.println("</h3>");
		out.println("<hr>");
		out.println("<br>");
		out.println("<p> You are connected to the Project 4 Enterprise System Database.</p>");
		out.println("<p> Please enter any valid SQL query or update statement.</p>");
		out.println("<p> If no query/update command is initally provided, the execute button will display all supplier information in the database.</p>");
		out.println("<p> All execution results will appear below.</p>");
		out.println("<form style=\"text-align: center\" action = \"database\" method = \"post\">");
		out.println("<textarea placeholder=\"" + req.getParameter("sql") + "\" style=\"background-color: #262727; color:#faf5ed;\" name=\"sql\" rows=\"15\" cols=\"100\"></textarea>");
		out.println("<br>");
		out.println("<input type = \"submit\" value = \"Submit\" />");
		out.println("<input type = \"reset\" value = \"Clear Form\" />");
		out.println("</form>");
		out.println("<hr>");
		out.println("<p> Database Results:</p>");
		out.println(connectToDatabase(req.getParameter("sql")));
		out.println("</body>");
		out.println("</html>");
		out.close();
	}
	
	public String connectToDatabase(String sql)
	{
		try
		{
			Class.forName("com.mysql.cj.jdbc.Driver");
			MysqlDataSource dataSource = new MysqlDataSource();
			dataSource.setURL("jdbc:mysql://localhost:3306/project4");
			dataSource.setUser("root");
			dataSource.setPassword("pass"); 
			connection = dataSource.getConnection();
			
			ResultSet results;
			
			String text = sql.toLowerCase();
			
			if (text.contains("select"))
			{
				results = connection.createStatement().executeQuery(sql);
				String table = "<table style=\"margin: 0px auto; border: 1px solid black; background-color: white; border-collapse: collapse; \">";
				table += "<tr>";
				for (int i = 0; i < results.getMetaData().getColumnCount(); i++)
				{
					table+= "<th>";
					table+= results.getMetaData().getColumnName(i + 1);
					table+= "</th>";
				}
				
				while (results.next())
				{
					table+= "<tr>";
					for (int i = 0; i < results.getMetaData().getColumnCount(); i++)
					{
						table+= "<td>";
						table+= (results.getObject(i + 1).toString());
						table+= "</td>";
					}
					table+= "</tr>";
				}
				table+= "</table><br>";
				
				return table;
			}
			else
			{
				try
				{
					connection.createStatement().executeUpdate(
							"set sql_safe_updates=0;");
					connection.createStatement().executeUpdate("drop table if exists beforeShipments;");
					connection.createStatement().executeUpdate(" create table beforeShipments like shipments;");
					connection.createStatement().executeUpdate("insert into beforeShipments select * from shipments;");
					
					int update = connection.createStatement().executeUpdate(sql);
							
					connection.createStatement().executeUpdate("update suppliers\r\n" + 
							"set status = status + 5\r\n" + 
							"where snum in (\r\n" + 
							"select distinct snum\r\n" + 
							"from shipments left join beforeShipments\r\n" + 
							"using (snum, pnum,jnum,quantity)\r\n" + 
							"where beforeShipments.snum is null and quantity > 100\r\n" + 
							");");
					
					return "<p>The statement executed successfully.\n" + update + " row(s) affected.</p>";
				}
				catch (Exception e)
				{
					return "<p>" + e + "</p>"; 
				}
			}
		} 
		
		catch (Exception e)
		{
			return "<p>" + e + "</p>";
		}
		
	}
	
}
