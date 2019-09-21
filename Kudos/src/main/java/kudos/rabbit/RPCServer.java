package kudos.rabbit;
import java.io.IOException;

import javax.ws.rs.core.Response;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.*;

import kudos.resources.KudosResource;

public class RPCServer extends Thread{
	
	private static final String RPC_QUEUE_NAME = "rpc_queue";
	private ConnectionFactory factory;
	private KudosResource resource;
	
	public RPCServer(KudosResource resource) throws IOException, TimeoutException {
		factory = new ConnectionFactory();
        factory.setHost("localhost");
        this.resource = resource;
        
	}
	
	@Override
	public void run(){
		try (Connection connection = factory.newConnection();
                Channel channel = connection.createChannel()) {
               channel.queueDeclare(RPC_QUEUE_NAME, false, false, false, null);
               channel.queuePurge(RPC_QUEUE_NAME);

               channel.basicQos(1);

               System.out.println(" [x] Awaiting RPC requests");

               Object monitor = new Object();
               DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                   AMQP.BasicProperties replyProps = new AMQP.BasicProperties
                           .Builder()
                           .correlationId(delivery.getProperties().getCorrelationId())
                           .build();

                   String response = "";

                   try {
                       String message = new String(delivery.getBody(), "UTF-8");
                       int n = Integer.parseInt(message);

                       System.out.println(" [.] kudos(" + message + ")");
                     //  Client client = ClientBuilder.newClient();
                     //  WebTarget target = client.target("http://localhost:4000/kudos/destino/"+message);
                                              
                       response = kudosDestino(n).getEntity().toString();//.readEntity(String.class);
                   } catch (RuntimeException e) {
                       System.out.println(" [.] Error: " + e.toString());
                   } finally {
                       channel.basicPublish("", delivery.getProperties().getReplyTo(), replyProps, response.getBytes("UTF-8"));
                       channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                       // RabbitMq consumer worker thread notifies the RPC server owner thread
                       synchronized (monitor) {
                           monitor.notify();
                       }
                   }
               };

               channel.basicConsume(RPC_QUEUE_NAME, false, deliverCallback, (consumerTag -> { }));
               // Wait and be prepared to consume the message from RPC client.
               while (true) {
                   synchronized (monitor) {
                       try {
                           monitor.wait();
                       } catch (InterruptedException e) {
                           e.printStackTrace();
                       }
                   }
               }
           } catch (IOException | TimeoutException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	private Response kudosDestino(int n) {
		return this.resource.getKudoByDestino(n);
    }

}
