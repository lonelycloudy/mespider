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
        	
    		String channel = channelRule.getString("channel");//explain channel's redis content
    		String seedURL = channelRule.getString("seedURL");
    		JSONArray urlPattern = channelRule.getJSONArray("urlPattern");
    		JSONObject ruleList = channelRule.getJSONObject("ruleList");
    		if(uri.equals(seedURL)){//seed page
    			System.out.println("MSEED"+uri);
    			getController().getFrontier().schedule(caUri);
    		}else{//single page 
    			for(int i=0;i<urlPattern.size();i++){
    				System.out.println("MPattern"+urlPattern.getString(i));
    				if(uri.matches(urlPattern.getString(i))){//need spider single page
    					System.out.println("MSingle-Y"+uri);
    					try {//need check unique or not,then check
    						String md5url = PersonalMd5.MyMd5(uri.getBytes());
    						boolean flag = PersonalRedis.getRedisUniqueInfo(md5url);
    						if(flag == true){//has that
    							System.out.println("MHAS");
    						}else {//not has
    							System.out.println("MNHAS");
    							getController().getFrontier().schedule(caUri);
    						}
    					} catch (NoSuchAlgorithmException e) {
    						// TODO Auto-generated catch block
    						e.printStackTrace();
    					}
    					break;
    				}else{//other single page,don't spider
    					System.out.println("MSingle-N"+uri);
    					continue;
    				}
    			}
    		}
    	}
    	
    }
}
