package users;

import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.setup.Bootstrap;
import users.core.User;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.*;

import javax.validation.Valid;
import javax.validation.constraints.*;

public class UsersConfiguration extends Configuration {
	
	//influxHost: http://localhost:8086
	//	influxDataBase: logs 
	@JsonProperty
    @NotEmpty
    public String influxHost;
	
	@JsonProperty
    @NotEmpty
    public String influxDataBase;
	
    @Valid
    @NotNull
    private DataSourceFactory database = new DataSourceFactory();

    
    @JsonProperty("database")
    public DataSourceFactory getDataSourceFactory() {
        return database;
    }
    
    
    
    
    public DataSourceFactory getDatabase() {
		return database;
	}




	public void setDatabase(DataSourceFactory database) {
		this.database = database;
	}




	private final HibernateBundle<UsersConfiguration> hibernateBundle
    = new HibernateBundle<UsersConfiguration>(
            User.class
    ) {

        @Override
        public DataSourceFactory getDataSourceFactory(
        		UsersConfiguration configuration
        ) {
            return configuration.getDataSourceFactory();
        }

    };


	public String getInfluxHost() {
		return influxHost;
	}




	public void setInfluxHost(String influxHost) {
		this.influxHost = influxHost;
	}




	public String getInfluxDataBase() {
		return influxDataBase;
	}




	public void setInfluxDataBase(String influxDataBase) {
		this.influxDataBase = influxDataBase;
	}
    
    
    
}
