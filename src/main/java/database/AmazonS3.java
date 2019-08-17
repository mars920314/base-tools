package database;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.AmazonClientException;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.S3ClientOptions;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

public class AmazonS3 {
    private static Logger logger = LoggerFactory.getLogger(AmazonS3.class);

    private static AmazonS3Client s3Client;
    private static String endpoint = "host";
    private static String bucketName = "bucket";
    private static String accessKey = "accessKey";
    private static String secretKey = "secretKey";

    private static class SingletonHolder {
    	private static final AmazonS3 INSTANCE = new AmazonS3();
    }
    
    private AmazonS3 (){
    	initClient();
    }
    
    public static final AmazonS3 getInstance() {
    	return SingletonHolder.INSTANCE;
    }
    
    public void initClient() {
        try {
            logger.info("初始化s3连接, bucket = " + bucketName);
            AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
            ClientConfiguration clientConfig = new ClientConfiguration();
            clientConfig.setProtocol(Protocol.HTTP);
            s3Client = new AmazonS3Client(credentials, clientConfig);
            s3Client.setEndpoint(endpoint);
            s3Client.setS3ClientOptions(new S3ClientOptions().withPathStyleAccess(true));
            logger.info("初始化s3连接完成, bucket = " + bucketName);
        } catch (AmazonClientException e) {
            logger.error("初始化s3遇到错误: " + e.getMessage() + "", e);
        }
    }
    
    public boolean uploadFile(String docKey, ByteArrayInputStream docInput, int maxRetryTimes, String contentType) {
        boolean uploadResult = false;
        int retryTimes = 0;
        try {
            ObjectMetadata docMeta = new ObjectMetadata();
            docMeta.setContentType(contentType);
            docMeta.setHeader("Content-Type", contentType);
            docMeta.setCacheControl("public,max-age=31536000");
            docMeta.setContentLength(Long.valueOf(docInput.available()));
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, docKey, docInput, docMeta);
            putObjectRequest.setCannedAcl(CannedAccessControlList.PublicRead);
            while (retryTimes < maxRetryTimes) {
                try {
                    logger.debug("开始上传文档: {}", docKey);
                    s3Client.putObject(putObjectRequest);
                    logger.debug("上传文件: {}, 完成", docKey);
                    uploadResult = true;
                    break;
                } catch (Exception e) {
                    logger.warn(e.getLocalizedMessage(), e);
                    retryTimes++;
                    if (retryTimes == maxRetryTimes) {
                        uploadResult = false;
                        break;
                    }
                    logger.info("上传文件失败，重试");
                }
            }
            IOUtils.closeQuietly(docInput);
        } catch (Exception e) {
            logger.error("上传文件到S3遇到错误: " + e.getMessage() + "");
        }
        return uploadResult;
    }
    
    public boolean deleteFile(String docKey){
        boolean deleteResult = false;
        try {
            logger.debug("开始删除文档: {}", docKey);
            s3Client.deleteObject(bucketName, docKey);
            logger.debug("删除文件: {}, 完成", docKey);
            deleteResult = true;
        } catch (Exception e) {
            logger.warn(e.getLocalizedMessage(), e);
            logger.info("删除文件失败");
        }

        return deleteResult;
    }
    
    public ByteArrayInputStream convert(BufferedImage bufferedImage){
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
			if(ImageIO.write(bufferedImage, "format", os)){
				return new ByteArrayInputStream(os.toByteArray());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
        finally{
	        try {
				os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
		return null;
    }

}
