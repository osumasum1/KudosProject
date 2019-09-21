package stats.db;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Projections;

import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
 
import static com.mongodb.client.model.Filters.*;
 
public class MongoService {
	
	public int countKudosByDestino(MongoCollection<Document> collection, long destino) {
        return collection.find(eq("destino", destino)).into(new ArrayList<>()).size();
    }
 

}