package Calculator.Calculator;
import java.sql.*;

/**
 * initialize the database and table for job list
 */
public class App 
{
    public static void main( String[] args )
    {
    	initializeJobDB();
    }
    
	/**
	 * Create a db and a table to record JobIDs and p-values
	 */
	public static void initializeJobDB() {
	    Connection c = null;
	    Statement stmt = null;
	    try {
	      Class.forName("org.sqlite.JDBC");
	      c = DriverManager.getConnection("jdbc:sqlite:JobList.db");
	      stmt = c.createStatement();
	      String sql = "CREATE TABLE JOBLIST " +
	                   "(ID INT PRIMARY KEY     NOT NULL," +
	                   " QUESTION	INT     NOT NULL, " +
	                   " ANSWER		INT)"; 
	      stmt.executeUpdate(sql);
	      stmt.close();
	      c.close();
	    } catch ( Exception e ) {
	      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	      System.exit(0);
	    }
	}
}
