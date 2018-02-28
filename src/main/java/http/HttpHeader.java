package http;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.HttpHostConnectException;

public class HttpHeader extends HttpBase {

    public static final String STATUS_CODE_KEY = "statusCode";

    private static class HttpHeaderHolder{
    	private static final HttpHeader instance = new HttpHeader();
    }
    
    private HttpHeader(){}
    
    public static final HttpHeader getInstance(){
    	return HttpHeaderHolder.instance;
    }
    
    public Map<String, String> crawl(String url, String headerName) throws Exception {
    	return Query(getClient(null), url, headerName);
    }
    
    public Map<String, String> crawl(String url, String siteKey, String headerName) throws Exception {
    	return Query(getClient(siteKey), url, headerName);
    }

	protected Map<String, String> Query(HttpClient client, String url, String headerName) throws Exception {
		synchronized (client){
	    	Map<String, String> results = new HashMap<String, String>();
			HttpHead headMethod = constructRequestMethod(url);
			try {
				HttpResponse res = client.execute(headMethod);
                if (res != null) {
                    results.put(STATUS_CODE_KEY, String.valueOf(res.getStatusLine().getStatusCode()));
                    Header[] contentSizeHeaders = res.getHeaders(headerName);
                    if (contentSizeHeaders.length > 0) {
                        results.put(headerName, contentSizeHeaders[0].getValue());
                        // return contentSizeHeaders[0].getValue();
                    }
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
                headMethod.releaseConnection();
            }
            return results;
		}
	}

    private HttpHead constructRequestMethod(String url) {
        URIBuilder builder;
        try {
            builder = new URIBuilder(url);
            builder.build();
            return new HttpHead(builder.build());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

//    public static void main(String[] args) throws Exception{
//    	Map<String, String> result = HttpHeader.getInstance().crawl("http://www.iteye.com/topic/1130585", HttpHeaders.CONTENT_LENGTH);
//    	System.out.println(result.get("statusCode"));
//    }
}
