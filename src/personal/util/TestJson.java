package personal.util;

/**
 * Json-lib:proc json file
 * */
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONFunction;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;
import net.sf.json.util.PropertyFilter;
import net.sf.json.xml.XMLSerializer;
import net.sf.json.util.JSONUtils;
import net.sf.json.JSONException;

public class TestJson {

	 private static JSONObject createJSONObject(){
         JSONObject jsonObject = new JSONObject();
         jsonObject.put("name", "kevin");
         jsonObject.put("Max.score", new Integer(100));
         jsonObject.put("Min.score", new Integer(50));
         jsonObject.put("nickname", "picglet");
         return jsonObject;
	 }
	 
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		JSONObject jsonObject = TestJson.createJSONObject();

        System.out.println("jsonObject==>"+jsonObject);

        JSONArray jsonArray = new JSONArray();
        jsonArray.add(0, "this is a jsonArray value");
        jsonArray.add(1,"another jsonArray value");
        jsonObject.element("jsonArray", jsonArray);

        JSONObject obj = new JSONObject();
        obj.element("hello", "world");
        obj.element("num", 1);

        jsonObject.element("add", obj);

        Map jmap = jsonObject;
        
        System.out.println("map :"+jmap.toString());
        /*
        boolean isArray = jsonObject.isArray();
        boolean isEmpty = jsonObject.isEmpty();
        boolean isNullObject = jsonObject.isNullObject();
        System.out.println("isArray:"+isArray+" isEmpty:"+isEmpty+" isNullObject:"+isNullObject);

        jsonObject.element("address", "swap lake");
        System.out.println("added :"+jsonObject);

        JSONArray jsonArray = new JSONArray();
        jsonArray.add(0, "this is a jsonArray value");
        jsonArray.add(1,"another jsonArray value");
        jsonObject.element("jsonArray", jsonArray);
        JSONArray array = jsonObject.getJSONArray("jsonArray");
        System.out.println("return :"+array);

        System.out.println(jsonObject);

        String jsonString = jsonObject.getString("name");
        System.out.println("jsonString==>"+jsonString);

        jsonString = jsonObject;
        System.out.println("jsonString==>"+jsonString);
*/
        //Object obj = jsonObject.get("nam");
        //System.out.println(JSONUtils.isNull(obj));
        //System.out.println(JSONUtils.valueToString(obj));
        //System.out.println(JSONUtils.isObject(jsonObject));
	}

}
//javac -d . -classpath .;../lib/json-lib-2.4-jdk15.jar;../lib/commons-collections-3.1.jar;../lib/commons-logging-1.0.4.jar;../lib/commons-net-2.0.jar;../lib/commons-beanutils.jar;../lib/commons-httpclient.jar;../lib/commons-lang-2.3.jar;../lib/ezmorph-1.0.6.jar;../lib/morph-1.1.1.jar personal/util/TestJson.java
//java -cp .;../lib/json-lib-2.4-jdk15.jar;../lib/commons-collections-3.1.jar;../lib/commons-logging-1.0.4.jar;../lib/commons-net-2.0.jar;../lib/commons-beanutils.jar;../lib/commons-httpclient.jar;../lib/commons-lang-2.3.jar;../lib/ezmorph-1.0.6.jar;../lib/morph-1.1.1.jar personal.util.TestJson
/*Java
 * jsonObject==>{"name":"kevin","Max.score":100,"Min.score":50,"nickname":"picglet"}
map :{
"name":"kevin","Max.score":100,"Min.score":50,"nickname":"picglet",
"jsonArray":["this is a jsonArray value","another jsonArray value"],
"add":{"hello":"world","num":1}
}
 * */
/**PHP
 * decodestring(68) "{"name":"kevin","Max.score":100,"Min.score":50,"nickname":"picglet"}"
Array
(
    [name] => kevin
    [Max.score] => 100
    [Min.score] => 50
    [nickname] => picglet
)

encodearray(2) {
  ["name"]=>
  string(6) "liuxin"
  ["value"]=>
  int(12)
}

string(28) "{"name":"liuxin","value":12}"
 * */
