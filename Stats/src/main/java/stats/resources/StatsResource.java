package stats.resources;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.params.LongParam;
import stats.core.User;
import stats.db.MongoService;
import stats.db.UserDAO;
import stats.influx.InfluxDataBase;
import stats.rabbit.Send;

import javax.annotation.concurrent.ThreadSafe;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.bson.Document;
import org.skife.jdbi.v2.sqlobject.Transaction;

import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicLong;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;

import com.google.common.base.Optional;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mongodb.client.MongoCollection;

/*
 private MongoCollection<Document> collection;
    private final MongoService mongoService;
 
    public KudosResource(MongoCollection<Document> collection, MongoService mongoService) {
        this.collection = collection;
        this.mongoService = mongoService;
    }
 */


@Path("/stats")
@Produces(MediaType.APPLICATION_JSON)
public class StatsResource {
	
	private final UserDAO userDao;
	private MongoCollection<Document> collection;
    private final MongoService mongoService;
    private InfluxDataBase influx;
    
    public StatsResource(UserDAO userDao,MongoCollection<Document> collection, MongoService mongoService, InfluxDataBase influx) {
        this.userDao = userDao;
        this.collection = collection;
        this.mongoService=mongoService;
        this.influx = influx;
    }
    @GET
    @Timed
    @Path("/kudos/{id}")
    @UnitOfWork
    public int getKudosAmountByUserId(@PathParam("id") final int id) {
    	influx.insert("Stats: Get Amount of kudos user "+id);
        return mongoService.countKudosByDestino(collection, id);
    }
    
    @PUT
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @UnitOfWork
    
    public boolean updateKudosAmount(
    		@FormParam("kudosAmount") String kudosAmount,
    		@FormParam("userId") String userId
    		
    		) {
    	influx.insert("Stats: Update kudos amount. User: "+userId+" Amount: "+kudosAmount);
        return userDao.updateKudosAmount(Integer.valueOf(kudosAmount), Integer.valueOf(userId));
    }
}
