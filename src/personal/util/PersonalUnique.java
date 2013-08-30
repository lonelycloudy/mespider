package personal.util;
/**
 * URL过滤,读取排重库
 * 20130828 lx
 * */
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;
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
import personal.util.PersonalMd5;

public class PersonalUnique {
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		return ;
	}
	public boolean checkURL(String url) throws NoSuchAlgorithmException{
		String md5url = PersonalMd5.MyMd5(url.getBytes());//md5 url
		Jedis jedis = new Jedis("192.168.0.193",6379);
		jedis.select(1);
		String foo = jedis.get(md5url);
		if(foo != null){
			return false;
			//System.out.println("G"+foo);
		}else{
			//String str = "MySelf";
			//jedis.set("foo",str);
			return true;
			//System.out.println("S"+str);
		}
	}

}
