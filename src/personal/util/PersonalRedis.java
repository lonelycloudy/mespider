/**
*javac -d . personal/util/PersonalRedis.java
*java -cp .\ personal.util.PersonalRedis
*Java-Redis 使用 
*20130822 lx
*/
package personal.util;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.Protocol;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.jedis.exceptions.JedisDataException;
import redis.clients.jedis.tests.commands.JedisCommandTestBase;
import redis.clients.util.SafeEncoder;

import personal.util.TestJson;

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

public class PersonalRedis{
	
	public static final String redis_host = "192.168.0.193";
	public static final int  redis_port = 6379;
	public static final int  redis_table = 7;//heritrix rule
	public static final String  redis_prefix = "heritrix_lx";//heritrix prefix
	public static final String aggregate_id = "uidx";//labs unique use
	public static final int  aggregate_port = 6379;//labs unique
	public static final int  aggregate_table = 0;//labs unique
	
	/**
	 * set tech.sina.com.cn rule
	 * "{\"channel\":\"cn.com.sina.tech\",\"seedURL\":\"http://tech.sina.com.cn/tele/\",\"listRule\":\"div.main_row\",\"urlPattern\":[\"http://tech.sina.com.cn/t/\\\\w/\\\\d+-\\\\d+-\\\\d+\\\\d+\\\\.shtml\",\"http://tech.sina.com.cn/t/\\\\d+-\\\\d+-\\\\d+\\\\d+\\\\.shtml\"],\"singleRule\":{\"title\":\"id:artibodyTitle\",\"pubdate\":\"id:pub_date\",\"content\":\"id:artibody\"}}"
	 * */
	public static void main(String[] args){
		String channel = "";
		for(int i = 0; i < args.length; i++) {
	        String line = args[i].trim();
	            if (line.equals("-c")) {//"cn.com.sina.tech"
	                channel = args[i+1];
	         }
	     }
		Jedis jedis = new Jedis(PersonalRedis.redis_host,PersonalRedis.redis_port);
		jedis.select(PersonalRedis.redis_table);
		
		String channelRule = jedis.get(channel);
		if(channelRule != null){
			System.out.println("PRGET"+channelRule);
		}else{
			String str = "Init";
			//Long cuidx = jedis.incr("uidx");//get current's id and  return with increment 1
			//jedis.set("foo",str);
			String listRule = "div.main_row";
			JSONObject channelObject = TestJson.createJSONObject(channel,"http://tech.sina.com.cn/tele/",listRule);
	        //Map jmap = channelObject;
			System.out.println("SS"+channelObject.toString());
	        jedis.set(channel,channelObject.toString());
			//jedis.hset(channel, field, value);
			System.out.println("PRSET:"+str);
		}
    }
	/**
	 * 用Url验证9380/0号表中是否存在,如已存在则跳过(排重)
	 * 20130830 lx
	 * */
	public static boolean getRedisUniqueInfo(String md5url){
		Jedis jedis = new Jedis(PersonalRedis.redis_host,PersonalRedis.aggregate_port);
		jedis.select(PersonalRedis.aggregate_table);
		String data = jedis.get(md5url);
		if(data != null){
			return true;
		}else{
			return false;
		}
	}
	/**
	 * 获取Heritrix Rule,抓取队列时比较是否要抓取.
	 * 提取内容时结合文档结构,使用jsoup提取
	 * 20130830 lx
	 * */
	public static String getChannelInfoForLabs(String channel){
		Jedis jedis = new Jedis(PersonalRedis.redis_host,PersonalRedis.redis_port);
		jedis.select(PersonalRedis.redis_table);
		String foo = jedis.get(channel);
		if(foo != null){
			//System.out.println("G"+foo);
			return foo;
		}else{//not has redis key
			//String str = "MySelf";
			//Long cuidx = jedis.incr("uidx");//get current's id and  return with increment 1
			//jedis.set("foo",str);
			//jedis.hset("liuxin_rule", "seed", "sina");
			//jedis.hset("liuxin_rule", "seed", "{}");
			//System.out.println("S"+str);
			return null;
		}
    }
	/**
	 * 取整站的当前文章id+1值,大表主键.
	 * 20130830 lx
	 * */
	public static Long getCurrentIdForLabs(){
		Jedis jedis = new Jedis(PersonalRedis.redis_host,PersonalRedis.redis_port);
		jedis.select(PersonalRedis.redis_table);
		//String data = jedis.get(PersonalRedis.aggregate_id);
		Long uidx = jedis.incr("uidx");//get current's id and  return with increment 1
		if(uidx != null){
			System.out.println("G"+uidx);
		}else{
			String str = "NOuidx";
			//Long cuidx = jedis.incr("uidx");//get current's id and  return with increment 1
			//jedis.set("foo",str);
			//jedis.hset("liuxin_rule", "seed", "sina");
			//jedis.hset("liuxin_rule", "seed", "{}");
			System.out.println("S"+str);
		}
		return uidx;
    }
	/**
	 * 更新排重库:6380/0:get f2430a82b14a1a5558710c87f91464c4:22027210@1376531057
	 * */
	public static String setRedisLabsURL(String md5url,String data){
		Jedis jedis = new Jedis(PersonalRedis.redis_host,PersonalRedis.aggregate_port);
		jedis.select(PersonalRedis.aggregate_table);
		String flag = jedis.set(md5url,data);
		return flag;
	}
	/*
	 //@Test
    public void useWithoutConnecting() {
		Jedis jedis = new Jedis("localhost");
		jedis.auth("foobared");
		jedis.dbSize();
    }

    //@Test
    public void checkBinaryData() {
		byte[] bigdata = new byte[1777];
		for (int b = 0; b < bigdata.length; b++) {
			bigdata[b] = (byte) ((byte) b % 255);
		}
		Map<String, String> hash = new HashMap<String, String>();
		hash.put("data", SafeEncoder.encode(bigdata));

		String status = jedis.hmset("foo", hash);
		assertEquals("OK", status);
		assertEquals(hash, jedis.hgetAll("foo"));
    }

    //@Test
    public void connectWithShardInfo() {
		JedisShardInfo shardInfo = new JedisShardInfo("localhost",
		Protocol.DEFAULT_PORT);
		shardInfo.setPassword("foobared");
		Jedis jedis = new Jedis(shardInfo);
		jedis.get("foo");
    }

    //@Test(expected = JedisConnectionException.class)
    public void timeoutConnection() throws Exception {
		jedis = new Jedis("localhost", 6379, 15000);
		jedis.auth("foobared");
		jedis.configSet("timeout", "1");
		// we need to sleep a long time since redis check for idle connections
		// every 10 seconds or so
		Thread.sleep(20000);
		jedis.hmget("foobar", "foo");
    }

    //@Test(expected = JedisDataException.class)
	public void failWhenSendingNullValues() {
		jedis.set("foo", null);
    }

    //@Test
    public void shouldReconnectToSameDB() throws IOException {
		jedis.select(1);
		jedis.set("foo", "bar");
		jedis.getClient().getSocket().shutdownInput();
		jedis.getClient().getSocket().shutdownOutput();
		assertEquals("bar", jedis.get("foo"));
    }

    //@Test
    public void startWithUrlString() {
		Jedis j = new Jedis("localhost", 6380);
		j.auth("foobared");
		j.select(2);
		j.set("foo", "bar");
		Jedis jedis = new Jedis("redis://:foobared@localhost:6380/2");
		assertEquals("PONG", jedis.ping());
		assertEquals("bar", jedis.get("foo"));
    }

    //@Test
    public void startWithUrl() throws URISyntaxException {
		Jedis j = new Jedis("localhost", 6380);
		j.auth("foobared");
		j.select(2);
		j.set("foo", "bar");
		Jedis jedis = new Jedis(new URI("redis://:foobared@localhost:6380/2"));
		assertEquals("PONG", jedis.ping());
		assertEquals("bar", jedis.get("foo"));
    }
	*/
}