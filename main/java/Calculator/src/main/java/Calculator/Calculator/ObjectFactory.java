//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.12.18 at 12:36:17 PM EST 
//


package Calculator.Calculator;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.springwebservices.product.schema.beans package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.springwebservices.product.schema.beans
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link JobRequest }
     * 
     */
    public JobRequest createJobRequest() {
        return new JobRequest();
    }

    /**
     * Create an instance of {@link JobIDRequest }
     * 
     */
    public JobIDRequest createJobIDRequest() {
        return new JobIDRequest();
    }

    /**
     * Create an instance of {@link JobResponse }
     * 
     */
    public JobResponse createJobResponse() {
        return new JobResponse();
    }

    /**
     * Create an instance of {@link Job }
     * 
     */
    public Job createJob() {
        return new Job();
    }

}
