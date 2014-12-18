package Calculator.Calculator;

@Endpoint
public class SampleEndpoint {
 
    public final static String NAMESPACE = "http://www.springwebservices.org/product/schema/beans";
    public final static String GET_JOB_REQUEST = "jobRequest";
    public final static String GET_ID_REQUEST="jobIDRequest";
 
    /**
     *
     * @param code
     * @return
     */
    @PayloadRoot(localPart = GET_JOB_REQUEST, namespace = NAMESPACE)
    public @ResponsePayload JobResponse getJob(@RequestPayload JobRequest code) {
        JobResponse jobResponse = new JobResponse();
        Job job = new Job();
        job.setJobID(0);
        job.setQuestion(100);
        job.setAnswer(50);
        jobResponse.setJob(job);
        return jobResponse;
    }
    
    @PayloadRoot(localPart = GET_ID_REQUEST, namespace = NAMESPACE)
    public @ResponsePayload int getJobID(@RequestPayload JobIDRequest code) {
        //...
        return job.jobID;
    }
 
}