package personal.spider;

/* FrontierSchedulerForSina
 *Bellow Copy from FontierScheduler.java
 *And Modify by net
 *http://blog.sina.com.cn/s/blog_521e12320100nzq6.html
 */

import java.util.logging.Level;
import java.util.logging.Logger;

import org.archive.crawler.postprocessor.FrontierScheduler;
import org.archive.crawler.datamodel.CandidateURI;
import org.archive.crawler.datamodel.CrawlURI;
import org.archive.crawler.datamodel.FetchStatusCodes;
import org.archive.crawler.framework.Processor;

public class FrontierSchedulerForSina extends FrontierScheduler {

    private static final long serialVersionUID = 2L;
    
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
    	//只抓取tech.sina.com.cn下的东西
    	if(uri.contains("tech.sina.com.cn")){
    		getController().getFrontier().schedule(caUri);
    	}
    }
}
