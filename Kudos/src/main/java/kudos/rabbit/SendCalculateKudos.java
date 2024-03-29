package kudos.rabbit;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class SendCalculateKudos {
	private final static String QUEUE_NAME = "calculateKudos";
	ConnectionFactory factory;
	
	
	public SendCalculateKudos() {
		factory = new ConnectionFactory();
		factory.setHost("localhost");
	}
	
	public void sendMessage(long userId) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            String message = String.valueOf(userId);
            
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes("UTF-8"));
            System.out.println(" [x] Sent '" + message + "'");
        }
    }
}
