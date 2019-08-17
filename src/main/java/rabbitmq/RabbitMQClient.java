package rabbitmq;

import java.io.IOException;
import java.net.ConnectException;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;


public class RabbitMQClient {

    private static final Logger logger = LoggerFactory.getLogger(RabbitMQClient.class);
    private int MAX_RETRY_TIMES = 20;
    private ConnectionFactory connectionFactory;
    private Connection connection;

    public RabbitMQClient(String address, String username, String password, int port) {
        connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(address);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        connectionFactory.setPort(port);
        connectionFactory.setAutomaticRecoveryEnabled(true);
    }

    public Channel newChannel() throws IOException, InterruptedException, TimeoutException {
    	//每次取mq，用新的channel
        Channel channel = null;
        int retryCount = 0;
        //每次取mq，可以不用新的connection
        while (connection == null && retryCount++ < MAX_RETRY_TIMES) {
        	if(retryCount>0)
        		Thread.sleep(1000);
            connection = connectionFactory.newConnection();
        }
        if (connection == null){
            logger.info("rabbitmq can't get connection after retry {} times", retryCount);
            throw new ConnectException("rabbitmq can't get connection");
        }
        while (channel == null && retryCount++ < MAX_RETRY_TIMES) {
        	if(retryCount>0)
        		Thread.sleep(1000);
            channel = connection.createChannel();
        }
        if (channel == null){
            logger.info("rabbitmq can't get channel after retry {} times", retryCount);
            throw new ConnectException("rabbitmq can't get channel");
        }
        return channel;
    }
    
    public void declearQueueAndConsumer(Channel channel, String queueName, QueueingConsumer consumer) throws IOException{
        // channel声明queue
    	channel.queueDeclare(queueName, true, false, false, null);
    	// consumer对接到queue上
        channel.basicConsume(queueName, true, consumer);
        logger.info("success init rabbitmq queue and consumer");
    }

    public void declareExchange(Channel channel, String exchangeType, String exchangeName) throws IOException {
    	// channel声明exchange
    	channel.exchangeDeclare(exchangeName, exchangeType, true, false, false, null);
        logger.info("success init rabbitmq exchange");
    }

	// 如果需要将消息输出到queue上，这里要绑定queue到exchange
    public void bind(Channel channel, String queueName, String exchangeName, String routingKey) throws IOException{
    	// channel声明queue，如果不存在则会创建
        channel.queueDeclare(queueName, true, false, false, null);
        // queue绑定到exchange上
        channel.queueBind(queueName, exchangeName, routingKey);
    }

}
