package Calculator;

@Endpoint
public class SampleEndpoint {
 
    public final static String NAMESPACE = "http://www.springwebservices.org/product/schema/beans";
    public final static String SUBMIT = "submit";
 
    /**
     *
     * @param code
     * @return
     */
    @PayloadRoot(localPart = SUBMIT, namespace = NAMESPACE)
    public @ResponsePayload ProductResponse getProducts(@RequestPayload GetProductRequest  code) {
        ProductResponse productResponse = new ProductResponse();
        Product product = new Product();
        product.setCode(&quot;Code1&quot;);
        product.setPrice(100.00);
        product.setDescription(&quot;test&quot;);
        productResponse.getProduct().add(product);
        return productResponse;
    }
 
}