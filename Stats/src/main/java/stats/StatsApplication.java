package stats;

import io.dropwizard.Application;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jetty.HttpConnectorFactory;
import io.dropwizard.server.DefaultServerFactory;
import io.dropwizard.server.SimpleServerFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import stats.core.User;
import stats.db.MongoManaged;
import stats.db.MongoService;
import stats.db.UserDAO;
import stats.influx.InfluxDataBase;
import stats.rabbit.CalculateKudos;
import stats.rabbit.Send;
import stats.resources.StatsResource;

import org.bson.Document;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class StatsApplication extends Application<StatsConfiguration> {

    public static void main(final String[] args) throws Exception {
        new StatsApplication().run(args);
    }
    
    private final HibernateBundle<StatsConfiguration> hibernateBundle =
            new HibernateBundle<StatsConfiguration>(User.class) {
                @Override
                public DataSourceFactory getDataSourceFactory(StatsConfiguration configuration) {
                    return configuration.getDataSourceFactory();
                }
            };


    @Override
    public void initialize(final Bootstrap<StatsConfiguration> bootstrap) {
    	bootstrap.addBundle(hibernateBundle);
    	
    }
    

    @Override
    public void run(final StatsConfiguration configuration,
                    final Environment environment) throws Exception {

    	String clientSite = configuration.getClientSite();
    	
    	/*
    	final UsersResource resource = new UsersResource(
    	        configuration.getTemplate(),
    	        configuration.getDefaultName()
    	    );
    	    environment.jersey().register(resource);
    	    */
    	
    	
    	
    	MongoClient mongoClient = new MongoClient(configuration.getMongoHost(), configuration.getMongoPort());
        MongoManaged mongoManaged = new MongoManaged(mongoClient);
        environment.lifecycle().manage(mongoManaged);
        MongoDatabase db = mongoClient.getDatabase(configuration.getMongoDB());
        MongoCollection<Document> collection = db.getCollection(configuration.getCollectionName());
    	
        final UserDAO userDao = new UserDAO(hibernateBundle.getSessionFactory());
        final InfluxDataBase influx = new InfluxDataBase(configuration.getInfluxHost(), configuration.getInfluxDataBase());
    	
    	StatsResource resource = new StatsResource(userDao,collection,new MongoService(),influx);
    	environment.jersey().register(resource);
    	
    	CalculateKudos calculateKudos = new CalculateKudos(clientSite);
    	calculateKudos.start();
    	
    }

}
