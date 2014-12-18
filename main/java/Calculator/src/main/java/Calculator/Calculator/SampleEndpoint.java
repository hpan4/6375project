package Calculator.Calculator;

import java.sql.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;

import com.mycompany.hr.service.HumanResourceService;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.filter.Filters;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;

@Endpoint
public class SampleEndpoint {

    public final static String NAMESPACE = "http://www.springwebservices.org/project/schema/beans";
    public final static String GET_JOB_REQUEST = "jobRequest";
    public final static String GET_JOBID_REQUEST="jobIDRequest";
 
	/**
	 * takes the same input but return an integer job ID 
	 * immediately without blocking for the computation to finish. 
	 * @param jobRequest
	 * @return jobResponse
	 */
    @PayloadRoot(localPart = GET_JOB_REQUEST, namespace = NAMESPACE)
    public @ResponsePayload JobResponse getJob(@RequestPayload JobRequest jobRequest) {
        JobResponse jobResponse = new JobResponse();
        Job job = new Job(jobRequest.question);
		//update count whenever submit() is called
		Job.jobIDCount++;
		job.calculate();
		//store to JobList.db
	    Connection c = null;
	    Statement stmt = null;
	    try {
	      Class.forName("org.sqlite.JDBC");
	      c = DriverManager.getConnection("jdbc:sqlite:JobList.db");
	      c.setAutoCommit(false);
	      stmt = c.createStatement();
	      String sql = "INSERT INTO JOBLIST (ID, QUESTION, ANSWER) " +
	                   "VALUES ("+job.jobID+", "+job.question+", "+job.answer+");"; 
	      stmt.executeUpdate(sql);
	      stmt.close();
	      c.commit();
	      c.close();
	    } catch ( Exception e ) {
	      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	      System.exit(0);
	    }
		jobResponse.setJobID(job.jobID);
		return jobResponse;
    }
    
    /**
    *
    * @param jobIDRequest
    * @return jobIDResponse
    */
    @PayloadRoot(localPart = GET_JOBID_REQUEST, namespace = NAMESPACE)
    public @ResponsePayload JobIDResponse getJobID(@RequestPayload JobIDRequest jobIDRequest) {
    	JobIDResponse JobIDResponse = new JobIDResponse();
		int result=0; //answer
		int id=jobIDRequest.jobID;
		//retrieve answer from JobList.db
		Connection c = null;
		Statement stmt = null;
		try {
		      Class.forName("org.sqlite.JDBC");
		      c = DriverManager.getConnection("jdbc:sqlite:JobList.db");
		      c.setAutoCommit(false);
		      stmt = c.createStatement();
		      ResultSet rs = stmt.executeQuery("SELECT * FROM JOBLIST where ID="+id+";");
	          result = rs.getInt("answer");
		      rs.close();
		      stmt.close();
		      c.close();
		} catch ( Exception e ) {
		      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		      System.exit(0);
		}
		JobIDResponse.setAnswer(result);
        return JobIDResponse;
    }
 
}