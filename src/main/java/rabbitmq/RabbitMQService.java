package rabbitmq;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;

public class RabbitMQService {
    private static final Logger logger = LoggerFactory.getLogger(RabbitMQService.class);
	
    private RabbitMQClient rabbitMQClient;
    private QueueingConsumer consumer;
    private Channel channel;
    
    public boolean initSender(ConfigMQ configMQ){
        try {
            rabbitMQClient = new RabbitMQClient(configMQ.MQ_ADDRESS, configMQ.MQ_USER, configMQ.MQ_PASSWORD, configMQ.MQ_PORT);
        	channel = rabbitMQClient.newChannel();
        	rabbitMQClient.declareExchange(channel, configMQ.MQ_EXCHANGE_TYPE, configMQ.MQ_EXCHANGE_NAME);
        	return true;
        } catch (IOException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        } catch (TimeoutException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    public void send(String exchange, String routingKey, String body){
		try {
			channel.basicPublish(exchange, routingKey, null, body.getBytes());
		} catch (IOException e) {
            logger.error(e.getMessage());
			e.printStackTrace();
		}
    }
    
    public boolean initReceiver(ConfigMQ configMQ){
        try {
            rabbitMQClient = new RabbitMQClient(configMQ.MQ_ADDRESS, configMQ.MQ_USER, configMQ.MQ_PASSWORD, configMQ.MQ_PORT);
            channel = rabbitMQClient.newChannel();
            consumer = new QueueingConsumer(channel);
            rabbitMQClient.declearQueueAndConsumer(channel, configMQ.MQ_QUEUE_NAME, consumer);
            return true;
        } catch (IOException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        } catch (TimeoutException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    public String receive(Long timeout){
		try {
			QueueingConsumer.Delivery delivery = null;
			if(timeout==null)
				delivery = consumer.nextDelivery();
			else
				delivery = consumer.nextDelivery(timeout);
			if(delivery==null)
				return null;
			else
				return new String(delivery.getBody());
		} catch (ShutdownSignalException e) {
            logger.error(e.getMessage());
			e.printStackTrace();
		} catch (ConsumerCancelledException e) {
            logger.error(e.getMessage());
			e.printStackTrace();
		} catch (InterruptedException e) {
            logger.error(e.getMessage());
			e.printStackTrace();
		}
		return null;
    }
    
    public boolean close(){
        try {
            if (channel != null)
            	channel.close();
            return true;
        } catch (IOException e) {
        	logger.error(e.getMessage());
            e.printStackTrace();
        } catch (TimeoutException e) {
            logger.error("RabbitMQSpout AMQP setup failed for TimeoutException", e);
			e.printStackTrace();
		}
        return false;
    }

}
