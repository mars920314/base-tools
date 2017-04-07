package http;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;

public class HttpPoster extends HttpBase {

    public static final String RESPONSE_STATUS_CODE_KEY = "statusCode";
    public static final String RESPONSE_BODY_KEY = "responseBody";
    public static final String default_encoding = "UTF-8";
	
    private static class HttpPosterHolder{
    	private static final HttpPoster instance = new HttpPoster();
    }
    
    private HttpPoster(){}
    
    public static final HttpPoster getInstance(){
    	return HttpPosterHolder.instance;
    }
    
    public Map<String, String> crawl(String url, String requestBody, String encoding) throws Exception {
    	if(encoding==null)
    		encoding = default_encoding;
    	return Query(getClient(null), url, requestBody, encoding);
    }

    public Map<String, String> crawl(String url, String siteKey, String requestBody, String encoding) throws Exception {
    	if(encoding==null)
    		encoding = default_encoding;
    	return Query(getClient(siteKey), url, requestBody, encoding);
    }
    
    public Map<String, String> crawl(String url, Map<String, String> params, String encoding) throws Exception {
    	if(encoding==null)
    		encoding = default_encoding;
    	return Query(getClient(null), url, new HashMap<String, String>(), params, encoding);
    }

    public Map<String, String> crawl(String url, String siteKey, Map<String, String> params, String encoding) throws Exception {
    	if(encoding==null)
    		encoding = default_encoding;
    	return Query(getClient(siteKey), url, new HashMap<String, String>(), params, encoding);
    }
    
    protected Map<String, String> Query(HttpClient client, String url, String params, String encoding)  throws Exception {
        Map<String, String> responseMap = new HashMap<String, String>();
        HttpPost postMethod = new HttpPost(url);
        try {
            postMethod.setEntity(new StringEntity(params, Charset.forName(encoding)));
            // postMethod.setHeader(new BasicHeader(Headers.CONTENT_TYPE, "application/json"));
            
            HttpResponse res = client.execute(postMethod);
            responseMap.put(RESPONSE_STATUS_CODE_KEY, String.valueOf(res.getStatusLine().getStatusCode()));
            responseMap.put(RESPONSE_BODY_KEY, IOUtils.toString(res.getEntity().getContent(), encoding));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            postMethod.releaseConnection();
        }
        return responseMap;
    }
    
    protected Map<String, String> Query(HttpClient client, String url, Map<String, String> headers, Map<String, String> params, String encoding)  throws Exception {
        Map<String, String> responseMap = new HashMap<String, String>();
        HttpPost postMethod = new HttpPost(url);
        List<NameValuePair> payload = new ArrayList<NameValuePair>();

        try {
            for (String key : params.keySet()) {
                payload.add(new BasicNameValuePair(key, params.get(key)));
            }

            postMethod.setEntity(new UrlEncodedFormEntity(payload, Charset.forName(encoding)));

            for (String headerKey : headers.keySet()) {
                postMethod.setHeader(headerKey, headers.get(headerKey));
            }

            HttpResponse res = client.execute(postMethod);
            responseMap.put(RESPONSE_STATUS_CODE_KEY, String.valueOf(res.getStatusLine().getStatusCode()));
            responseMap.put(RESPONSE_BODY_KEY, IOUtils.toString(res.getEntity().getContent(), encoding));

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            postMethod.releaseConnection();
        }
        return responseMap;
    }
    
}
