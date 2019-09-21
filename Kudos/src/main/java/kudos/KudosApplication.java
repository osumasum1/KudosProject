package kudos;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import kudos.db.MongoManaged;
import kudos.db.MongoService;
import kudos.influx.InfluxDataBase;
import kudos.rabbit.RPCServer;
import kudos.rabbit.ReceiverDeleteKudos;
import kudos.resources.KudosResource;

public class KudosApplication extends Application<KudosConfiguration> {


    public static void main(String[] args) throws Exception {
        new KudosApplication().run("server", args[0]);
    }

    @Override
    public void initialize(Bootstrap<KudosConfiguration> b) {
    }

    @Override
    public void run(KudosConfiguration config, Environment env)
            throws Exception {
        MongoClient mongoClient = new MongoClient(config.getMongoHost(), config.getMongoPort());
        MongoManaged mongoManaged = new MongoManaged(mongoClient);
        env.lifecycle().manage(mongoManaged);
        MongoDatabase db = mongoClient.getDatabase(config.getMongoDB());
        MongoCollection<Document> collection = db.getCollection(config.getCollectionName());
       
        final InfluxDataBase influx = new InfluxDataBase(config.getInfluxHost(), config.getInfluxDataBase());
       
       KudosResource resource = new KudosResource(collection, new MongoService(),influx);
       env.jersey().register(new KudosResource(collection, new MongoService(),influx));
       
       RPCServer rpc = new RPCServer(resource);
       ReceiverDeleteKudos recv =  new ReceiverDeleteKudos(resource);
       rpc.start();
       recv.start();
    }

}
