package users;

import io.dropwizard.Application;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import users.auth.UserAuthenticator;
//import users.auth.UserAuthorizer;
import users.core.Session;
import users.core.User;
import users.db.UserDAO;
import users.influx.InfluxDataBase;
import users.rabbit.Send;
import users.resources.UsersResource;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;

public class UsersApplication extends Application<UsersConfiguration> {

    public static void main(final String[] args) throws Exception {
        new UsersApplication().run(args);
    }
    
    private final HibernateBundle<UsersConfiguration> hibernateBundle =
            new HibernateBundle<UsersConfiguration>(User.class) {
                @Override
                public DataSourceFactory getDataSourceFactory(UsersConfiguration configuration) {
                    return configuration.getDataSourceFactory();
                }
            };


    @Override
    public void initialize(final Bootstrap<UsersConfiguration> bootstrap) {
    	bootstrap.addBundle(hibernateBundle);
    	
    }
    

    @Override
    public void run(final UsersConfiguration configuration,
                    final Environment environment) throws Exception {
    	
    	/*
    	final UsersResource resource = new UsersResource(
    	        configuration.getTemplate(),
    	        configuration.getDefaultName()
    	    );
    	    environment.jersey().register(resource);
    	    */
    	
    	final UserDAO userDao = new UserDAO(hibernateBundle.getSessionFactory());
    	final InfluxDataBase influx = new InfluxDataBase(configuration.getInfluxHost(), configuration.getInfluxDataBase());
    	
    	
    	environment.jersey().register(new UsersResource(userDao,influx));
    	
    	environment.jersey().register(new AuthDynamicFeature(new BasicCredentialAuthFilter.Builder<Session>()
                .setAuthenticator(new UserAuthenticator(userDao))
                //.setAuthorizer(new UserAuthorizer())
                .setRealm("SUPER SECRET STUFF")
                .buildAuthFilter()));
    	environment.jersey().register(new AuthValueFactoryProvider.Binder<>(Session.class));
    	environment.jersey().register(RolesAllowedDynamicFeature.class);
    	
    }

}
