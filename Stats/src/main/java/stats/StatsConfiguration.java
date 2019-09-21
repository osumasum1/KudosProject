package stats;

import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.setup.Bootstrap;
import stats.core.User;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.*;

import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.*;

public class StatsConfiguration extends Configuration {
	@JsonProperty
    @NotEmpty
    public String clientSite;
	
	@JsonProperty
    @NotEmpty
    public String mongoHost;

    @JsonProperty
    @Min(1)
    @Max(65535)
    public int mongoPort;

    @JsonProperty
    @NotEmpty
    public String mongoDB;

    @JsonProperty
    @NotEmpty
    public String collectionName;
    
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




	private final HibernateBundle<StatsConfiguration> hibernateBundle
    = new HibernateBundle<StatsConfiguration>(
            User.class
    ) {

        @Override
        public DataSourceFactory getDataSourceFactory(
        		StatsConfiguration configuration
        ) {
            return configuration.getDataSourceFactory();
        }

    };


	public String getMongoHost() {
		return mongoHost;
	}




	public void setMongoHost(String mongoHost) {
		this.mongoHost = mongoHost;
	}




	public int getMongoPort() {
		return mongoPort;
	}




	public void setMongoPort(int mongoPort) {
		this.mongoPort = mongoPort;
	}




	public String getMongoDB() {
		return mongoDB;
	}




	public void setMongoDB(String mongoDB) {
		this.mongoDB = mongoDB;
	}




	public String getCollectionName() {
		return collectionName;
	}




	public void setCollectionName(String collectionName) {
		this.collectionName = collectionName;
	}


	public String getClientSite() {
		return clientSite;
	}


	public void setClientSite(String clientSite) {
		this.clientSite = clientSite;
	}


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
