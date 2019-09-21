package kudos.resources;

import com.codahale.metrics.annotation.Timed;
import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;

import kudos.core.Kudo;
import kudos.db.MongoService;
import kudos.influx.InfluxDataBase;
import kudos.rabbit.RPCServer;
import kudos.rabbit.SendCalculateKudos;

import org.bson.Document;
 
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/kudos")
@Produces(MediaType.APPLICATION_JSON)
public class KudosResource {
 
    private MongoCollection<Document> collection;
    private final MongoService mongoService;
    private InfluxDataBase influx;
 
    public KudosResource(MongoCollection<Document> collection, MongoService mongoService,InfluxDataBase influx) {
        this.collection = collection;
        this.mongoService = mongoService;
        this.influx = influx;
    }
 
    @POST
    @Timed
    @Path("/createkudo")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createKudo(@NotNull @Valid final Kudo kudo) throws Exception {
        Gson gson = new Gson();
        String json = gson.toJson(kudo);
        mongoService.insertOne(collection, new Document(BasicDBObject.parse(json)));
        Map<String, String> response = new HashMap<>();
        response.put("message", "Kudo created successfully");
        
        SendCalculateKudos calculateKudos = new SendCalculateKudos();
        calculateKudos.sendMessage(Long.valueOf(kudo.getDestino()));
        influx.insert("Kudo Created: "+json);
        return Response.ok(response).build();
    }
 
    @GET
    @Timed
    public Response getKudos() {
        List<Document> documents = mongoService.find(collection);
        influx.insert("Kudo: GET all kudos");
        return Response.ok(documents).build();
    }
    
    @GET
    @Timed
    @Path("/simpleKudos")
    public Response getSimpleKudos() {
        List<Document> documents = mongoService.findSimple(collection);
        influx.insert("Kudo: Get All Simple Kudos");
        return Response.ok(documents).build();
    }
 
    @GET
    @Timed
    @Path("/destino/{id}")
    public Response getKudoByDestino(@PathParam("id") final int id) {
        List<Document> documents = mongoService.findByKey(collection, "destino", id);
        influx.insert("Kudo: GET by destino "+id);
        return Response.ok(documents).build();
    }
    
    @DELETE
    @Timed
    @Path("{id}")
    public Response deleteKudo(@PathParam("id") final String id) {
        mongoService.deleteOneByObjectId(collection,id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Kudo deleted successfully");
        influx.insert("Kudo Deleted: "+id);
        return Response.ok(response).build();
    }
    
    @DELETE
    @Timed
    @Path("/user/{id}")
    public Response deleteAllKudoUser(@PathParam("id") final String id) throws NumberFormatException, Exception {
        mongoService.deleteAllKudosUser(collection,Long.valueOf(id));
        Map<String, String> response = new HashMap<>();
        response.put("message", "Kudos deleted successfully");
        influx.insert("Kudo Deleted: All kudos where the user appears (fuente and destino)");
        return Response.ok(response).build();
    }
    
 
}
    
    
