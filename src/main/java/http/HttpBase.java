package http;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.http.client.HttpClient;

public abstract class HttpBase {
	
    protected Map<String, HttpClient> clients = new ConcurrentHashMap<String, HttpClient>();
    
    public HttpClient getClient(String siteKey) {
    	if(siteKey==null)
    		return HttpClientConnectionPool.getHttpClient();
    	HttpClient client = clients.get(siteKey);
    	if(client==null){
    		client = HttpClientConnectionPool.getHttpClient();
    		clients.put(siteKey, client);
    	}
    	return client;
    }

}
