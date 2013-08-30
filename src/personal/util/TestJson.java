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

	/**
	 * 创建JSONObject变量,由Channel,seedURL,Single Page组成
	 * */
	public static JSONObject createJSONObject(String channel,String seedURL,String listRule){
         JSONObject jsonObject = new JSONObject();
         //jsonObject.put("channel", "cn.com.sina.tech");
         jsonObject.put("channel", channel);
         //jsonObject.put("Max.score", new Integer(100));
         jsonObject.put("seedURL", seedURL);
         jsonObject.put("listRule", listRule);
         System.out.println("jsonObject==>"+jsonObject);
         JSONArray jsonArray = new JSONArray();
         jsonArray.add(0, "http://tech.sina.com.cn/t/\\w/\\d+-\\d+-\\d+\\d+\\.shtml");//spider single url pattern
         jsonArray.add(1,"http://tech.sina.com.cn/t/\\d+-\\d+-\\d+\\d+\\.shtml");//spider single url pattern,when php use(\\->\\\\)
         jsonObject.element("urlPattern", jsonArray);
         JSONObject obj = new JSONObject();
         obj.element("title", "id:artibodyTitle");
         obj.element("pubdate", "id:pub_date");
         obj.element("content", "id:artibody");
         jsonObject.element("singleRule", obj);
         System.out.println(jsonObject);
         return jsonObject;
	 }
	 
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String listRule = "div.main_row";
		JSONObject jsonObject = TestJson.createJSONObject("cn.com.sina.tech","http://tech.sina.com.cn/tele/",listRule);
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
	/**
	 * 取得Channel在Redis中定义的规则,用来抓取单页URL识别.
	 * 20130830 lx
	 * */
	public static JSONObject fetchChannelRule(String tchannel) {
		System.out.println("Fetch-Channel-Rule"+tchannel);
		JSONObject obj = new JSONObject();
		String objStr = PersonalRedis.getChannelInfoForLabs(tchannel);
		obj = JSONObject.fromObject(objStr);
		return obj;
		/*
		ArrayList singlePage = new ArrayList();
		singlePage.add("http://tech.sina.com.cn/t/\\w/\\d+-\\d+-\\d+\\d+\\.shtml");
		singlePage.add("http://tech.sina.com.cn/t/\\d+-\\d+-\\d+\\d+\\.shtml");
		
		JSONObject jsonObject = TestJson.createJSONObject("cn.com.sina.tech","http://tech.sina.com.cn/tele/",singlePage);
        //System.out.println("jsonObject==>"+jsonObject);

        JSONArray jsonArray = new JSONArray();
        jsonArray.add(0, "http://tech.sina.com.cn/t/\\w/\\d+-\\d+-\\d+\\d+\\.shtml");//spider single url pattern
        jsonArray.add(1,"http://tech.sina.com.cn/t/\\d+-\\d+-\\d+\\d+\\.shtml");//spider single url pattern,when php use(\\->\\\\)
        jsonObject.element("urlPattern", jsonArray);

        JSONObject obj = new JSONObject();
        obj.element("title", "id:artibodyTitle");
        obj.element("pubdate", "id:pub_date");
        obj.element("content", "id:artibody");
        jsonObject.element("ruleList", obj);
        //Map jmap = jsonObject;
        //return jmap.toString();*/
        //return jsonObject;
	}
}
//javac -d . -classpath .;../lib/json-lib-2.4-jdk15.jar;../lib/commons-collections-3.1.jar;../lib/commons-logging-1.0.4.jar;../lib/commons-net-2.0.jar;../lib/commons-beanutils.jar;../lib/commons-httpclient.jar;../lib/commons-lang-2.3.jar;../lib/ezmorph-1.0.6.jar;../lib/morph-1.1.1.jar personal/util/TestJson.java
//java -cp .;../lib/json-lib-2.4-jdk15.jar;../lib/commons-collections-3.1.jar;../lib/commons-logging-1.0.4.jar;../lib/commons-net-2.0.jar;../lib/commons-beanutils.jar;../lib/commons-httpclient.jar;../lib/commons-lang-2.3.jar;../lib/ezmorph-1.0.6.jar;../lib/morph-1.1.1.jar personal.util.TestJson
/*Java For Spider
jsonObject==>{
	"channel":"cn.com.sina.tech","seedURL":"http://tech.sina.com.cn/tele/"
}
map :{
	"channel":"cn.com.sina.tech",
	"seedURL":"http://tech.sina.com.cn/tele/",
	"urlPattern":[
		"http://tech.sina.com.cn/t/\\w/\\d+-\\d+-\\d+\\d+\\.shtml",
		"http://tech.sina.com.cn/t/\\d+-\\d+-\\d+\\d+\\.shtml"
	 ],
	 "ruleList":{"title":"id:artibodyTitle","pubdate":"id:pub_date","content":"id:artibody"}
}
 * 
 * */
/**PHP
 *
 *F:\web_study\php_study>php json.php
string(289) "{"channel":"cn.com.sina.tech","seedURL":"http://tech.sina.com.cn/te
le/","urlPattern":["http://tech.sina.com.cn/t/\\w/\\d+-\\d+-\\d+\\d+\\.shtml","h
ttp://tech.sina.com.cn/t/\\d+-\\d+-\\d+\\d+\\.shtml"],"ruleList":{"title":"id:ar
tibodyTitle","pubdate":"id:pub_date","content":"id:artibody"}}"
array(4) {
  ["channel"]=>
  string(16) "cn.com.sina.tech"
  ["seedURL"]=>
  string(29) "http://tech.sina.com.cn/tele/"
  ["urlPattern"]=>
  array(2) {
    [0]=>
    string(50) "http://tech.sina.com.cn/t/\w/\d+-\d+-\d+\d+\.shtml"
    [1]=>
    string(47) "http://tech.sina.com.cn/t/\d+-\d+-\d+\d+\.shtml"
  }
  ["ruleList"]=>
  array(3) {
    ["title"]=>
    string(16) "id:artibodyTitle"
    ["pubdate"]=>
    string(11) "id:pub_date"
    ["content"]=>
    string(11) "id:artibody"
  }
}
 * */
