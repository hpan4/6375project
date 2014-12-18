//package Calculator;
import java.sql.*;

public class Calculator {
	static int jobIDCount = 0;
	
	  public static void main( String args[] ){
		  initializeJobDB();
		  int jobID=submit(0.1);
		  int joobID2=submit(0.2);
		  double pvalue=query(joobID2);
		  System.out.println("pvalue="+pvalue);
	  }
/*

	public class Input {
		private int numberOfGroups;
		private double pvalueThreshold;
		
	}
	
	public class Output {
		private double pvalue;
		private boolean accept;
	}
	
	public void Input() {
		//execute();
		//submit();
		//query();
	}
*/	
	/**
	 * takes in the input data for a complex computational task, 
	 * and returns the result when the computation is done. 
	 * If you don't any other proposal, 
	 * please use t-test or ANOVA analysis from geWorkbench project
	 * as the computational task.
	 */
	//public double execute(Input myInput) {}
	
	/**
	 * Create a db and a table to record JobIDs and p-values
	 */
	public static void initializeJobDB() {
	    Connection c = null;
	    Statement stmt = null;
	    try {
	      Class.forName("org.sqlite.JDBC");
	      c = DriverManager.getConnection("jdbc:sqlite:JobList.db");
	      System.out.println("Opened database successfully");

	      stmt = c.createStatement();
	      String sql = "CREATE TABLE JOBLIST " +
	                   "(ID INT PRIMARY KEY     NOT NULL," +
	                   " PVALUE REAL)"; 
	      stmt.executeUpdate(sql);
	      stmt.close();
	      c.close();
	    } catch ( Exception e ) {
	      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	      System.exit(0);
	    }
	    System.out.println("Table created successfully");
	}
	
	/**
	 * takes the same input but return an integer job ID 
	 * immediately without blocking for the computation to finish. 
	 * (This is where you would need data persistence.)
	 */
	public static int submit(double pvalue) {
		jobIDCount++;
		int jobID = jobIDCount;
		//int pvalue=execute(myInput);
		//insert record
	    Connection c = null;
	    Statement stmt = null;
	    try {
	      Class.forName("org.sqlite.JDBC");
	      c = DriverManager.getConnection("jdbc:sqlite:JobList.db");
	      c.setAutoCommit(false);
	      stmt = c.createStatement();
	      String sql = "INSERT INTO JOBLIST (ID, PVALUE) " +
	                   "VALUES ("+ jobID + ", "+pvalue+");"; 
	      stmt.executeUpdate(sql);
	      stmt.close();
	      c.commit();
	      c.close();
	    } catch ( Exception e ) {
	      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	      System.exit(0);
	    }
		return jobID;
	}
	
	/**
	 * Takes an integer job ID as input, 
	 * and returns the actual output data.
	 */
	public static double query(int jobID) {
		double result=0;
		Connection c = null;
	    Statement stmt = null;
	    try {
	      Class.forName("org.sqlite.JDBC");
	      c = DriverManager.getConnection("jdbc:sqlite:JobList.db");
	      c.setAutoCommit(false);
	      System.out.println("Opened database successfully");

	      stmt = c.createStatement();
	      ResultSet rs = stmt.executeQuery("SELECT * FROM JOBLIST where ID="+jobID+";");
	      
          result = rs.getDouble("pvalue");
          System.out.println( "ID = " + jobID );
          System.out.println( "PVALUE = " + result );
          System.out.println();
	      
	      rs.close();
	      stmt.close();
	      c.close();
	    } catch ( Exception e ) {
	      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	      System.exit(0);
	    }
	    System.out.println("Operation done successfully");
		return result;
	} //end query()
/*	
	public static void test() {
	    Connection c = null;
	    Statement stmt = null;
	    try {
	      Class.forName("org.sqlite.JDBC");
	      c = DriverManager.getConnection("jdbc:sqlite:JobList.db");
	      c.setAutoCommit(false);
	      System.out.println("Opened database successfully");

	      stmt = c.createStatement();
	      String sql = "INSERT INTO JOBLIST (ID,PVALUE) " +
	                   "VALUES (1, 0.2);"; 
	      stmt.executeUpdate(sql);

	      sql = "INSERT INTO JOBLIST (ID,PVALUE) " +
	            "VALUES (2, 0.05);"; 
	      stmt.executeUpdate(sql);

	      stmt.close();
	      c.commit();
	      c.close();
	    } catch ( Exception e ) {
	      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	      System.exit(0);
	    }
	    System.out.println("Records created successfully");
	}
*/
}
