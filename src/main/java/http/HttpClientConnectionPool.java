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
		//��ʼ��SchemeRegistry,���÷���Э��
		//Scheme���ʾһ��Э�鷽��������?"http"����"https"�Ͱ�������Э�����ԣ�
		//����ȱʡ�Ķ˿ں�socket����������Ϊָ����Э�鴴��java.net.Socketʵ����SchemeRegistry�౻����ά��һ��Schemes�ļ���
		SchemeRegistry schemeRegistry = new SchemeRegistry();  
		schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
		schemeRegistry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
        cm = new ThreadSafeClientConnManager(schemeRegistry);  
        try {
        	//�������������?
            int maxTotal = 100;  
            cm.setMaxTotal(maxTotal);
        } catch (NumberFormatException e) {
        	e.printStackTrace();
        }  
        //ÿ��ͨ���Ĳ������������ã����ӳأ�  
        try {  
            int defaultMaxConnection = 50;  
            cm.setDefaultMaxPerRoute(defaultMaxConnection);  
        } catch (NumberFormatException e) {
        	e.printStackTrace();
        }  
	}
	
    public static HttpClient getHttpClient() {
    	//��ʼ��HttpParams�������������?
    	//HttpParams�ӿڴ���һ�����ɸı�ֵ�ļ��ϣ�����һ���������ʱ��Ϊ������һ�����󼯺ϣ��ü�����һ������ֵ��ӳ��?
    	//HttpParams�����Ƕ��������������Ϊ��һ��ÿ�����ӵ�����������Լ���HttpParams����
        HttpParams params = new BasicHttpParams();
        // HTTP Э��İ�?,1.1/1.0/0.9
        params.setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
        params.setParameter(CoreProtocolPNames.USER_AGENT, "HttpComponents/1.1");
        params.setParameter(CoreProtocolPNames.USE_EXPECT_CONTINUE, true);
        params.setParameter(CoreProtocolPNames.HTTP_CONTENT_CHARSET, "utf-8");
        params.setParameter(CoreProtocolPNames.HTTP_ELEMENT_CHARSET, "utf-8");
        //����ConnectionTimeout��ʱʱ��,ReadTimeOut��ʱʱ��,3000ms
        params.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 30000);
        params.setParameter(CoreConnectionPNames.SO_TIMEOUT, 30000);
        return new DefaultHttpClient(cm, params);  
    }  
  
    public static void release() {  
        if (cm != null) {  
            cm.shutdown();  
        }  
    }  
}
