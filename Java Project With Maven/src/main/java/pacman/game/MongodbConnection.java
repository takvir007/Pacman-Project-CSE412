package pacman.game;

import com.mongodb.client.*;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;

public class MongodbConnection {

    private String clientLink = "mongodb+srv://takvir:pacman@cluster0.n3gms.mongodb.net/myFirstDatabase?retryWrites=true&w=majority";
    private String databaseName = "PacmanScore";
    private String collectionName = "gameHighestScore";
    private MongoClient client;
    private MongoDatabase db;
    private MongoCollection collection;
    private ArrayList <Integer> scores = new ArrayList<Integer>();


    public void setConnection(){
        client = MongoClients.create(clientLink);
        db = client.getDatabase(databaseName);
        collection = db.getCollection(collectionName);
    }

    public void recordScore(String Id, String score){
        try( var client = MongoClients.create(clientLink)){
            var database = client.getDatabase(databaseName);
            var collection = database.getCollection(collectionName);
            var data = new Document("_id", Id);
            data.append("score", score);
            collection.insertOne(data);
        }
    }

    public Integer getHighestScore(){
        try(var client = MongoClients.create(clientLink)){
            var database = client.getDatabase(databaseName);
            MongoCollection <Document> col= database.getCollection(collectionName);
            try(MongoCursor <Document> cursor = col.find().iterator()){
                while(cursor.hasNext()){
                    int i=0;
                    var doc = cursor.next();
                    var users = new ArrayList<>(doc.values());
                    scores.add(Integer.parseInt((String) users.get(i+1)));
                    i++;
                }
            }
            catch (Exception e){
                System.out.println(e);
            }
            scores.sort(Comparator.reverseOrder());
            return scores.get(0);
        }catch (Exception e){
            System.out.println(e);
        }
        return 0;
    }




}
