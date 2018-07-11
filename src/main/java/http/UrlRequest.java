package http;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UrlRequest {
    private static Logger logger = LoggerFactory.getLogger(UrlRequest.class);
    
    private static Integer connectionTimeOut = 600000;

    public static String getURLResponse(String urlStr, String encoding) {
        //此数字记录随机生成有效host的次数，当cnt大于一定值时候，重新从数据库取一批host
        String htmlPage = "";
        try {
            URL url = new URL(urlStr);
            BufferedReader br;
            // buffer reader from url
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 5.1; rv:19.0) Gecko/20100101 Firefox/19.0");
            conn.setConnectTimeout(connectionTimeOut);
            conn.setReadTimeout(connectionTimeOut);
            // open the stream and put it into BufferedReader
            br = new BufferedReader(new InputStreamReader(conn.getInputStream(), encoding));
            // convert buffer to string
            String inputLine;
            while ((inputLine = br.readLine()) != null) {
                htmlPage += inputLine;
            }
            br.close();
        } catch (MalformedURLException e) {
            logger.error("Malformed url Error: {}", urlStr);
            logger.error(e.getMessage());
            return "";
        } catch (Exception e) {
            logger.error("Error: {}", urlStr);
            logger.error(e.getMessage());
            return "";
        }
        if(htmlPage.equals(""))
        	logger.error("url get no response from {}", urlStr);
        return htmlPage;
    }

    /**
     * convert url to destination file(local)
     */
    public static void urlToFile(String url, String destFileName) {
        try {
            URL website = new URL(url);
            ReadableByteChannel rbc = Channels.newChannel(website.openStream());
            FileOutputStream fos = new FileOutputStream(destFileName);
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        	fos.close();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

}
