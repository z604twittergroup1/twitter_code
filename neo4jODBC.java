import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;



public class neo4jODBC {
	
	
	public static void main (String args[]) throws SQLException, ClassNotFoundException
	{
		// Make sure Neo4j Driver is registered
		Class.forName("org.neo4j.jdbc.Driver");

		// Connect
		Connection con = DriverManager.getConnection("jdbc:neo4j://localhost:7474/");

		// Querying
		try(Statement stmt = con.createStatement())
		{
		    ResultSet rs = stmt.executeQuery("MATCH (n:USER) RETURN count(n.id) AS cnt");
		    while(rs.next())
		    {
		        System.out.println(rs.getString("cnt"));
		    }
		}
	}

}
