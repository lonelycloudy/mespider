package personal.spider;

/* FrontierSchedulerForSina
 *Bellow Copy from FontierScheduler.java
 *And Modify by net
 *http://blog.sina.com.cn/s/blog_521e12320100nzq6.html
 */

import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.archive.crawler.postprocessor.FrontierScheduler;
import org.archive.crawler.datamodel.CandidateURI;
import org.archive.crawler.datamodel.CrawlURI;
import org.archive.crawler.datamodel.FetchStatusCodes;
import org.archive.crawler.framework.Processor;

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

import personal.util.PersonalRedis;
import personal.util.Test;
import personal.util.TestJson;

import personal.util.PersonalMd5;

public class FrontierSchedulerForSina extends FrontierScheduler {

    private static final long serialVersionUID = 2L;
    public static final String redisPrefix = "heritrix_lx";
    
    /**
     * @param name Name of this filter.
     */
    public FrontierSchedulerForSina(String name) {
        super(name);
    }
    /**
     * Schedule the given {@link CandidateURI CandidateURI} with the Frontier.
     * @param caUri The CandidateURI to be scheduled.
     */
    protected void schedule(CandidateURI caUri) {
    	String uri = caUri.toString();
    	System.out.println(uri);
    	//过滤.zip,.exe,.rar,.pdf,.doc的链接
    	if(uri.endsWith(".zip") 
			|| uri.endsWith(".rar") 
			|| uri.endsWith(".exe")
			|| uri.endsWith(".pdf")
			|| uri.endsWith(".doc")
			|| uri.endsWith(".xls")){ 
    		return ;
    	}
    	if(uri.contains("dns:")){//dns page not check
    		System.out.println("MDNS"+uri);
			getController().getFrontier().schedule(caUri);
    	}else{
    		String currentChannel = Test.generateChannel(uri);//from url to channel
        	JSONObject  channelRule = TestJson.fetchChannelRule(currentChannel);//fetch rule with channel
        	if(channelRule == null){
        		System.out.println("NO-Channel-RULE");
        		return;
        	}
    		//String channel = channelRule.getString("channel");//explain channel's redis content
    		String seedURL = channelRule.getString("seedURL");
    		String itemRule = channelRule.getString("itemRule");//list a page
    		//JSONArray urlPattern = channelRule.getJSONArray("urlPattern");
    		//JSONObject ruleList = channelRule.getJSONObject("ruleList");
    		if(uri.equals(seedURL)){//seed page
    			System.out.println("FSEED: "+uri);
    			getController().getFrontier().schedule(caUri);
    		}else{//single page 
    			if(uri.matches(itemRule.toString())){//need spider single page
					System.out.println("MSY: "+uri);
					try {//need check unique or not,then check
						String md5url = PersonalMd5.MyMd5(uri.getBytes());
						boolean flag = PersonalRedis.getRedisUniqueInfo(md5url);
						if(flag == true){//has spider that
							System.out.println("FY");
						}else {//hasn't spider thath
							System.out.println("FN");
							getController().getFrontier().schedule(caUri);
						}
					} catch (NoSuchAlgorithmException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else{//other single page,don't spider
					System.out.println("MS-N: "+uri);
				}
    		}
    	}
    	
    }
}
