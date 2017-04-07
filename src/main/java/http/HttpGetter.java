package http;

import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.HttpHostConnectException;

public class HttpGetter extends HttpBase{

    private static class HttpGetterHolder{
    	private static final HttpGetter instance = new HttpGetter();
    }
    
    private HttpGetter(){}
    
    public static final HttpGetter getInstance(){
    	return HttpGetterHolder.instance;
    }
    
    public String crawl(String url, String encode) throws Exception {
    	return Query(getClient(null), url, encode, null);
    }
    
    public String crawl(String url, String encode, String siteKey) throws Exception {
    	return Query(getClient(siteKey), url, encode, null);
    }
    
    public String crawl(String url, String encode, ResponseHandler<?> handler) throws Exception {
    	return Query(getClient(null), url, encode, handler);
    }
    
    public String crawl(String url, String encode, String siteKey, ResponseHandler<?> handler) throws Exception {
    	return Query(getClient(siteKey), url, encode, handler);
    }
    
    protected String Query(HttpClient client, String url, String encode, ResponseHandler<?> handler) throws Exception {
    	synchronized (client){
            HttpGet getMethod = constructRequestMethod(url);
            try {
                HttpResponse res = null;
                if(handler==null)
                	res = client.execute(getMethod);
                else
                	res = (HttpResponse) client.execute(getMethod, handler);
                if (res != null) {
                    return IOUtils.toString(res.getEntity().getContent(), encode);
                }

            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (UnsupportedOperationException e) {
                e.printStackTrace();
            } catch (HttpHostConnectException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                getMethod.releaseConnection();
            }
            return "";
    	}
    }

    private HttpGet constructRequestMethod(String url) {
        URIBuilder builder;
        try {
            builder = new URIBuilder(url);
            builder.build();
            return new HttpGet(builder.build());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static void main(String[] args) throws Exception{
    	String result = HttpGetter.getInstance().crawl("http://www.iteye.com/topic/1130585", "utf-8");
    	System.out.println(result);
    }

}
