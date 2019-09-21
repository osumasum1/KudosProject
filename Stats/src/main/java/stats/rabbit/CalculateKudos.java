package stats.rabbit;


import java.io.IOException;
import java.util.concurrent.TimeoutException;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;


public class CalculateKudos extends Thread {

  private final static String QUEUE_NAME = "calculateKudos";
  private ConnectionFactory factory;
  private Connection connection;
  private Channel channel;
  private String clientSite;
  
  public CalculateKudos(String clientSite) {
	  factory = new ConnectionFactory();
      factory.setHost("localhost");
	  this.clientSite = clientSite;
  }
   
  @Override
  public void run() {
	  try {
		connection = factory.newConnection();
		channel = connection.createChannel();
		
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
	      System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

	      DeliverCallback deliverCallback = (consumerTag, delivery) -> {
	          String message = new String(delivery.getBody(), "UTF-8");
	          System.out.println(" [x] Received '" + message + "'");
	          
	          Client client = ClientBuilder.newClient();
	          String kudosAmount = client.target(clientSite+"/stats/kudos/"+message).request().get().readEntity(String.class);
	          
	          Form form = new Form();
	          form.param("kudosAmount", kudosAmount);
	          form.param("userId", message); 
	          
	          client.target(clientSite+"/stats").request().put(Entity.form(form));
	          client.close();
	          
	      };
	      channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> { });
		
	} catch (IOException | TimeoutException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
      
  }

  public static void main(String[] argv) throws Exception {
      

      
  }
}
