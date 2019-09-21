package users.resources;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.params.LongParam;
import users.api.UsersApi;
import users.core.Session;
import users.core.User;
import users.db.UserDAO;
import users.influx.InfluxDataBase;
import users.influx.Log;
import users.rabbit.RPCClient;
import users.rabbit.Send;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.influxdb.InfluxDB;

import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicLong;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;

import com.google.common.base.Optional;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)

public class UsersResource {
	
	private final UserDAO userDao;
	private InfluxDataBase influx;
    
    public UsersResource(UserDAO userDao, InfluxDataBase influx) {
        this.userDao = userDao;
        this.influx = influx;
    }
    
    @GET
    @Path("/{id}")
    @UnitOfWork
    public String findById(@PathParam("id") LongParam id) throws Exception {
    	String response = null;
    	try (RPCClient rpcClient = new RPCClient()) {
    		String i_str = Long.toString(id.get());
            System.out.println(" [x] Requesting kudos(" + i_str + ")");
            response = rpcClient.call(i_str);
            System.out.println(" [.] Got '" + response + "'");
            
            response = response.replaceAll("Document", "kudo");
            
            Optional<User> preview =  userDao.findById(id.get());
        	User user = preview.get();
        	user.setKudosList(response);
        	
        	ArrayList<Object> resp = new ArrayList<Object>();
        	//JsonObject jsonObject = new JsonParser().parse("\"kudos\":"+response).getAsJsonObject();
        	
        	ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        	String json = ow.writeValueAsString(user);
        	//json.replaceAll("[", "")
        	
        	resp.add(json);
        	//resp.add("\"kudos\":"+response);
        	//resp.add("\"kudos\":"+response);
        	//resp.add(jsonObject);
        	influx.insert("User Get By ID: "+id.get());
        	return json;
        } catch (IOException | TimeoutException | InterruptedException e) {
            e.printStackTrace();
            influx.insert("User Get By ID Error: "+e);
            return null;
        }
    	
    	
    	
    }
    
    @GET
    @UnitOfWork
    public List<User> findByName(@QueryParam("firstName") Optional<String> firstName, 
    		@QueryParam("nickname") Optional<String> nickname) {
        
    	if (firstName.isPresent() && nickname.isPresent()) {
    		influx.insert("User Get By First Name && Nickname: "+firstName.get()+"     "+nickname.get());
    		return userDao.findByFirstNameNickname(firstName.get(),nickname.get());
        }
    	else {
    		influx.insert("User Get All");
    		return userDao.findAll();
    	}
    	
        
    }
    
    @GET
    @Path("/simple")
    @UnitOfWork
    public List<User> findAllSimple() throws Exception {
    	/*
    	Send send = new Send();
    	try {
			send.sendMessage();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
    	
    	
    	influx.insert("User Get Simple");
        return userDao.findAllSimple();
    }
    
    
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @UnitOfWork
    public boolean insertUser(
    		@FormParam("nickname") String nickname,
    		@FormParam("firstName") String firstName,
    		@FormParam("lastName") String lastName,
    		@FormParam("username") String username,
    		@FormParam("password") String password
    		
    		) {
       
    	influx.insert("User Created: "+username);
        return userDao.insertUser(nickname, firstName, lastName, username, password);
    }
    
    @DELETE
    @Path("/{id}")
    @UnitOfWork
    public boolean deleteUserById(@PathParam("id") LongParam id) throws Exception {
    	boolean resp = userDao.deleteUserById(id.get());
    	if (resp=true) {
    		Send send = new Send();
    		send.sendMessage(id.get());
    		influx.insert("User Deleted:"+ id.get());
    	}
    	return resp;
    }

}
