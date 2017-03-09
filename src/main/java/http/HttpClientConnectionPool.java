package http;

import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

public class HttpClientConnectionPool {

	private static ThreadSafeClientConnManager cm = null;
	
	static {
		//初始化SchemeRegistry,设置访问协议
		//Scheme类表示一个协议方案，例如"http"或者"https"和包含许多的协议属性，
		//例如缺省的端口和socket工厂常用于为指定的协议创建java.net.Socket实例，SchemeRegistry类被用来维护一个Schemes的集合
		SchemeRegistry schemeRegistry = new SchemeRegistry();  
		schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
		schemeRegistry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
        cm = new ThreadSafeClientConnManager(schemeRegistry);  
        try {
        	//设置最大连接数
            int maxTotal = 100;  
            cm.setMaxTotal(maxTotal);
        } catch (NumberFormatException e) {
        	e.printStackTrace();
        }  
        //每条通道的并发连接数设置（连接池）  
        try {  
            int defaultMaxConnection = 50;  
            cm.setDefaultMaxPerRoute(defaultMaxConnection);  
        } catch (NumberFormatException e) {
        	e.printStackTrace();
        }  
	}
	
    public static HttpClient getHttpClient() {
    	//初始化HttpParams，设置组件参数
    	//HttpParams接口代表一个不可改变值的集合，定义一个组件运行时行为。代表一个对象集合，该集合是一个键到值的映射。
    	//HttpParams作用是定义其他组件的行为，一般每个复杂的组件都有它自己的HttpParams对象。
        HttpParams params = new BasicHttpParams();
        // HTTP 协议的版本,1.1/1.0/0.9
        params.setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
        params.setParameter(CoreProtocolPNames.USER_AGENT, "HttpComponents/1.1");
        params.setParameter(CoreProtocolPNames.USE_EXPECT_CONTINUE, true);
        params.setParameter(CoreProtocolPNames.HTTP_CONTENT_CHARSET, "utf-8");
        params.setParameter(CoreProtocolPNames.HTTP_ELEMENT_CHARSET, "utf-8");
        //设置ConnectionTimeout超时时间,ReadTimeOut超时时间,3000ms
        params.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 3000);
        params.setParameter(CoreConnectionPNames.SO_TIMEOUT, 3000);
        return new DefaultHttpClient(cm, params);  
    }  
  
    public static void release() {  
        if (cm != null) {  
            cm.shutdown();  
        }  
    }  
}
