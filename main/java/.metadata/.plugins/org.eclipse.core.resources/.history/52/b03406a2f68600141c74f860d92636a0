package Calculator.Calculator;

@Endpoint
public class SampleEndpoint {
 
    public final static String NAMESPACE = "http://www.springwebservices.org/product/schema/beans";
    public final static String SUBMIT_JOB_REQUEST = "jobRequest";
    public final static String GET_JOB_REQUEST="jobIDRequest";
 
    /**
     *
     * @param jobRequest
     * @return (int) jobID
     */
    @PayloadRoot(localPart = SUBMIT_JOB_REQUEST, namespace = NAMESPACE)
    public @ResponsePayload int submitJob(@RequestPayload JobRequest jobRequest) {
    	int jobID; //the return variable
        JobResponse jobResponse = new JobResponse();
        Job job = new Job(jobRequest.question);
		
		//update count whenever submit() is called
		Job.jobIDCount++; 
		jobID = Job.jobIDCount;
		
		job.calculate();
		storeToDB(job);
		return jobID;
    }
    
    /**
    *
    * @param code
    * @return (int) answer
    */
    @PayloadRoot(localPart = GET_JOB_REQUEST, namespace = NAMESPACE)
    public @ResponsePayload int getJob(@RequestPayload JobIDRequest code) {
    	int answer; //the return variable;
        //...
        return answer;
    }
 
}