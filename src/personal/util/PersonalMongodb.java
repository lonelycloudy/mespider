package personal.util;
import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.mongodb.*;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;
import com.mongodb.util.JSON;

import org.bson.*;

/***
 * 
 * @author freshair1517
 * visit tech.it168.com/a2011/0617/1206/000001206231_all.shtml
 * http://www.mongodb.org/display/DOCS
 */
public class PersonalMongodb {

	/**
	 * Java+MongoDB insert and search
	 * @param args
	 * @throws UnknownHostException 
	 */
	public static void main(String[] args) throws UnknownHostException {
		// TODO Auto-generated method stub
		Mongo mg = new Mongo("192.168.0.193",27018);//mongo connect
		DB db = mg.getDB("ext");//connect to mongodb,not has will create
		DBCollection collection = db.getCollection("part01");//select part01,not has will create
		BasicDBObject document = new BasicDBObject();//create a document
		document.put("liuxin","liuxin");//assign document content
		collection.insert(document);//insert above document to collection
		//select above create
		BasicDBObject search = new BasicDBObject();//create search document
		search.put("liuxin","liuxin");//assign document data
		DBCursor cursor = collection.find(search);//use collection's find function to search doument
		//rotate outputs data
		while(cursor.hasNext()){
			System.out.println(cursor.next());
		}
		System.out.print("Done");
	}
	/***
	 * get collection from MongoDB
	 * @throws UnknownHostException
	 */
	public static void getCollection() throws UnknownHostException{
		Mongo mg = new Mongo("192.168.0.193",27018);//mongo connect
		DB db = mg.getDB("ext");//connect to mongodb,not has will create
		Set<String> collections = db.getCollectionNames();
		for(String collectionName:collections){
			System.out.println(collectionName);
		}
		DBCollection collection = db.getCollection("pathurl");
		System.out.println(collection.toString());
		System.out.println("Done");
	}
	/***
	 *Three method to insert
	 * @throws UnknownHostException 
	 */
	public static void insertDocument() throws UnknownHostException{
		try{
			Mongo mongo =new Mongo("192.168.0.193", 27018);
			DB db = mongo.getDB("ext");
			// get a single collection
			DBCollection collection = db.getCollection("part01");
			// BasicDBObject example
			System.out.println("BasicDBObject example...");
			BasicDBObject document =new BasicDBObject();
			document.put("database", "mkyongDB");
			document.put("table", "hosting");
			BasicDBObject documentDetail =new BasicDBObject();
			documentDetail.put("records", "99");
			documentDetail.put("index", "vps_index1");
			documentDetail.put("active", "true");
			document.put("detail", documentDetail);
			collection.insert(document);
			DBCursor cursorDoc = collection.find();
			while(cursorDoc.hasNext()){
				System.out.println(cursorDoc.next());
			}
			collection.remove(new BasicDBObject());
			// BasicDBObjectBuilder example
			System.out.println("BasicDBObjectBuilder example...");
			BasicDBObjectBuilder documentBuilder = BasicDBObjectBuilder.start()
			.add("database", "mkyongDB")
			.add("table", "hosting");
			BasicDBObjectBuilder documentBuilderDetail = BasicDBObjectBuilder.start()
			.add("records", "99")
			.add("index", "vps_index1")
			.add("active", "true");
			documentBuilder.add("detail", documentBuilderDetail.get());
			collection.insert(documentBuilder.get());
			DBCursor cursorDocBuilder = collection.find();
			while(cursorDocBuilder.hasNext()){
				System.out.println(cursorDocBuilder.next());
			}
			collection.remove(new BasicDBObject());
			// Map example
			System.out.println("Map example...");
			Map documentMap =new HashMap();
			documentMap.put("database", "mkyongDB");
			documentMap.put("table", "hosting");
			Map documentMapDetail =new HashMap();
			documentMapDetail.put("records", "99");
			documentMapDetail.put("index", "vps_index1");
			documentMapDetail.put("active", "true");
			documentMap.put("detail", documentMapDetail);
			collection.insert(new BasicDBObject(documentMap));
			DBCursor cursorDocMap = collection.find();
			while(cursorDocMap.hasNext()){
				System.out.println(cursorDocMap.next());
			}
			collection.remove(new BasicDBObject());
			// JSON parse example
			System.out.println("JSON parse example...");
			String json ="{'database' : 'mkyongDB','table' : 'hosting',"+
			"'detail' : {'records' : 99, 'index' : 'vps_index1', 'active' : 'true'}}}";
			DBObject dbObject =(DBObject)JSON.parse(json);
			collection.insert(dbObject);
			DBCursor cursorDocJSON = collection.find();
			while(cursorDocJSON.hasNext()){
				System.out.println(cursorDocJSON.next());
			}
			collection.remove(new BasicDBObject());
			}catch(UnknownHostException e){
				e.printStackTrace();
			}catch(MongoException e){
				e.printStackTrace();
			}
	}
	/***
	 * Inert Data
	 * @param documentMap
	 * @throws UnknownHostException
	 */
	public static boolean insertDocument(Map documentMap) throws UnknownHostException{
		try{
			Mongo mongo =new Mongo("192.168.0.193", 27018);
			DB db = mongo.getDB("ext");
			// get a single collection
			DBCollection collection = db.getCollection("part02");
			//Map documentMap =new HashMap();
			/*documentMap.put("database", "mkyongDB");
			documentMap.put("table", "hosting");
			Map documentMapDetail =new HashMap();
			documentMapDetail.put("records", "99");
			documentMapDetail.put("index", "vps_index1");
			documentMapDetail.put("active", "true");
			documentMap.put("detail", documentMapDetail);*/
			collection.insert(new BasicDBObject(documentMap));
			//WriteResult wresult = collection.save(new BasicDBObject(documentMap));
			//return wresult.getN(); //int
			return true;
		}catch(UnknownHostException e){
			e.printStackTrace();
		 }catch(MongoException e){
			e.printStackTrace();
		 }
		return false;
	}
	/***
	 * update hosting:hostB
	 * @throws UnknownHostException
	 */
	public static void updateMongoDB() throws UnknownHostException{
		Mongo mongo =new Mongo("192.168.0.193", 27018);
		DB db = mongo.getDB("ext");
		// get a single collection
		DBCollection collection = db.getCollection("part01");
		BasicDBObject newDocument = new BasicDBObject();
		newDocument.put("liuxin", "liuxin");
		newDocument.put("type","javamongodb");
		newDocument.put("extra", "update");
		collection.update(new BasicDBObject().append("liuxin", "liuxin"), newDocument);
		//$inc update:clients:+99
		BasicDBObject newDocument2 = new BasicDBObject().append("$inc", new BasicDBObject().append("clients",99));
		collection.update(new BasicDBObject().append("liuxin", "liuxin"), newDocument2);
		//$set use,update liuxin:liuxin(update type)
		BasicDBObject newDocument3 = new BasicDBObject().append("$set", new BasicDBObject().append("type", "update set"));
		collection.update(new BasicDBObject().append("liuxin", "liuxin"), newDocument3);
		//append:not $set will update all related data
		BasicDBObject newDocument4 = new BasicDBObject().append("type", "update set");
		collection.update(new BasicDBObject().append("liuxin","liuxin"), newDocument4);
		//update muti document's same value:$multi;will update type:update(set clients:888)
		BasicDBObject updateStr = new BasicDBObject().append("$set", new BasicDBObject().append("clients", "888"));
		collection.update(new BasicDBObject().append("type", "update"), updateStr,false,true);
	}
	/***
	 * insert 10 Document
	 * @throws UnknownHostException 
	 */
	public static void insertDocument10() throws UnknownHostException{
		Mongo mongo =new Mongo("192.168.0.193", 27018);
		DB db = mongo.getDB("ext");
		// get a single collection
		DBCollection collection = db.getCollection("part01");
		for(int i=1;i<11;i++){
			collection.insert(new BasicDBObject().append("number",i));
		}
		return ;
	}
	/***
	 * Search Document
	 * @throws UnknownHostException 
	 */
	public static void searchDocument() throws UnknownHostException{
		Mongo mongo =new Mongo("192.168.0.193", 27018);
		DB db = mongo.getDB("ext");
		// get a single collection
		DBCollection collection = db.getCollection("part01");
		//find 1st document
		DBObject doc = collection.findOne();
		System.out.println(doc);
		//find document's set
		DBCursor cursor = collection.find();
		while(cursor.hasNext()){
			System.out.println(cursor.next());
		}
		//fetch number:5's document
		BasicDBObject query1 = new BasicDBObject();
		query1.put("number",5);
		DBCursor cursor1 = collection.find(query1);
		while(cursor1.hasNext()){
			System.out.println(cursor1.next());
		}
		//use $in
		BasicDBObject query2 = new BasicDBObject();
		List list = new ArrayList();
		list.add(9);
		list.add(10);
		query2.put("number", new BasicDBObject("$in",list));
		DBCursor cursor2 = collection.find(query2);
		while(cursor2.hasNext()){
			System.out.println(cursor2.next());
		}
		//use >,<,=
		BasicDBObject query3 =new BasicDBObject();
		query3.put("number", new BasicDBObject("$gt", 5));
		DBCursor cursor3 = collection.find(query3);
		while(cursor3.hasNext()){
			System.out.println(cursor3.next());
		}
		//mutil where:number>5 and number<8;("$ne",8):!=8
		BasicDBObject query4 =new BasicDBObject();
		query4.put("number", new BasicDBObject("$gt", 5).append("$lt",8));
		DBCursor cursor4 = collection.find(query4);
		while(cursor4.hasNext()){
			System.out.println(cursor4.next());
		}
		
		return ;
	}
	/***
	 * Delete document
	 * @throws UnknownHostException
	 */
	public static void deleteDocument() throws UnknownHostException{
		Mongo mongo =new Mongo("192.168.0.193", 27018);
		DB db = mongo.getDB("ext");
		// get a single collection
		DBCollection collection = db.getCollection("part01");
		//delete number:3 document
		BasicDBObject document = new BasicDBObject();
		document.put("number", 2);//will be overload
		document.put("number", 3);
		//delete number:4,number:5's documents;("$gt",9):delete number>9's documents
		BasicDBObject query1 = new BasicDBObject();
		List list = new ArrayList();
		list.add(4);
		list.add(5);
		query1.put("number", new BasicDBObject("$in",list));
		collection.remove(query1);
		//
	}
	/***
	 * save image file or binary file to MongoDB
	 * read image file from MongoDB
	 * outputs all saved image files
	 * read image from MongoDB and save as
	 * delete image
	 * http://www.mongodb.org/display/DOCS
	 * @throws IOException 
	 */
	public static void saveImageFile() throws IOException{
		Mongo mongo =new Mongo("192.168.0.193", 27018);
		DB db = mongo.getDB("ext");
		//will save f:\java.png to MongoDB,rename to my-java-image
		String newFileName = "my-java-image";
		File imageFile = new File("f:\\java.png");
		GridFS gfsPhoto = new GridFS(db,"photo");
		GridFSInputFile gfsFile = gfsPhoto.createFile(imageFile);
		gfsFile.setFilename(newFileName);
		gfsFile.save();
		//read image information
		String readFileName = "my-java-image";
		GridFS gfsPhoto1 = new GridFS(db,"photo");
		GridFSDBFile imageForOutput = gfsPhoto1.findOne(readFileName);
		System.out.println(imageForOutput);
		//will output file's information:{"_id" :{"$oid" : "4dc9511a14a7d017fee35746"} ,"chunkSize" : 262144 ,"length" : 22672 ,"md5" : "1462a6cfa27669af1d8d21c2d7dd1f8b" ,"filename" : "my-java-image" ,"contentType" : null ,"uploadDate" :{"$date" : "2011-05-10T14:52:10Z"} ,"aliases" : null}
		//output all the saved file
		GridFS gfsPhoto2 = new GridFS(db,"photo");
		DBCursor cursor = gfsPhoto2.getFileList();
		while(cursor.hasNext()){
			System.out.println(cursor.next());
		}
		//read from MongoDB and save as
		String newFileName3 ="my-java-image";
		GridFS gfsPhoto3 =new GridFS(db, "photo");
		GridFSDBFile imageForOutput3 = gfsPhoto.findOne(newFileName3);
		imageForOutput3.writeTo("F:\\JavaNew.png");
		//delete image
		String newFileName4 ="mkyong-java-image";
		GridFS gfsPhoto4 =new GridFS(db, "photo");
		gfsPhoto4.remove(gfsPhoto4.findOne(newFileName));
		return ;
	}
	/***
	 * convert JSON  to DBObject 
	 * com.mongodb.util.JSON:JSON->DBObject
	 * {"name":"java","age":30}->DBObject dbObject = (DBObject) JSON.parse("{'name':'java','age':30}");
	 * @throws UnknownHostException 
	 */
	public static void convertJsonToDBObject() throws UnknownHostException{
		try{
			Mongo mongo =new Mongo("192.168.0.193", 27018);
			DB db = mongo.getDB("ext");
			// get a single collection
			DBCollection collection = db.getCollection("part01");
			DBObject dbObject = (DBObject) JSON.parse("{'name':'mkyong', 'age':30}");
			collection.insert(dbObject);
			DBCursor cursorDoc = collection.find();
			while(cursorDoc.hasNext()){
				System.out.println(cursorDoc.next());
			}
			System.out.println("Done");
		} catch(UnknownHostException e){
			e.printStackTrace();
		}catch(MongoException e){
			e.printStackTrace();
		}
		//will outputs:　{"_id" : {"$oid" : "4dc9ebb5237f275c2fe4959f"} , "name" : "java" , "age" : 30} Done
		
	}
}
