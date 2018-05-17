package mail;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

public class JavaMail {
	
	private static MimeMessage message;
	
	private static String mail_smtp_host = "mail.wmcloud.com";
	private static String mail_smtp_auth = "false";
	private static String sender_username = "lingjun.gao@datayes.com";
	private static String sender_password = "random";
	
	static {
		Properties mailProperties = new Properties();
		mailProperties.setProperty("mail.smtp.host", mail_smtp_host);
		mailProperties.setProperty("mail.smtp.auth", mail_smtp_auth);
        message = new MimeMessage(Session.getDefaultInstance(mailProperties, null));
	}

	public static synchronized boolean doSendHtmlEmail(String subject, String content, String[] receivers, String[] replays, File attachment) {
		try {
			// 发件人
			// InternetAddress from = new InternetAddress(sender_username);
			// 下面这个是设置发送人的Nick name
			InternetAddress from = new InternetAddress(MimeUtility.encodeWord("sender_nick_name") + " <" + sender_username + ">");
			message.setFrom(from);

			// 收件人
            List<Address> address_receivers = new LinkedList<Address>();
            for (String receiver : receivers)
            	address_receivers.add(new InternetAddress(receiver));
            message.setRecipients(Message.RecipientType.TO, address_receivers.toArray(new Address[address_receivers.size()]));
            // CC、BCC
            if(replays!=null){
                List<Address> address_replays = new LinkedList<Address>();
                for (String replay : replays)
                	address_replays.add(new InternetAddress(replay));
                message.setRecipients(Message.RecipientType.CC, address_replays.toArray(new Address[address_replays.size()]));
            }

			// 邮件主题
			message.setSubject(subject);

            // 向multipart对象中添加邮件的各个部分内容，包括文本内容和附件
            Multipart multipart = new MimeMultipart();

            // 添加邮件正文，也可以使纯文本"text/plain"
            BodyPart contentPart = new MimeBodyPart();
            contentPart.setContent(content, "text/html;charset=UTF-8");
            multipart.addBodyPart(contentPart);

            // 添加附件的内容
            if (attachment != null) {
                BodyPart attachmentBodyPart = new MimeBodyPart();
                DataSource source = new FileDataSource(attachment);
                attachmentBodyPart.setDataHandler(new DataHandler(source));
                attachmentBodyPart.setFileName(MimeUtility.encodeWord(attachment.getName()));
                multipart.addBodyPart(attachmentBodyPart);
            }
            
            // 将multipart对象放到message中
            message.setContent(multipart);

			// 保存邮件
			message.saveChanges();

			// 发送
            Transport.send(message, message.getAllRecipients());
			
			System.out.println("send success!");
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
