package productService.productService;

@Endpoint
public class SampleEndpoint {
 
    public final static String NAMESPACE = &quot;http://www.springwebservices.org/product/schema/beans&quot;;
    public final static String GET_PERSONS_REQUEST = &quot;get-product-request&quot;;
 
    /**
     *
     * @param code
     * @return
     */
    @PayloadRoot(localPart = GET_PERSONS_REQUEST, namespace = NAMESPACE)
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