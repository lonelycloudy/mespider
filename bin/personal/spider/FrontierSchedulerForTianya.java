package personal.spider;

/* FrontierSchedulerForTianya
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

public class FrontierSchedulerForTianya extends FrontierScheduler {

    private static final long serialVersionUID = 1L;
    
    /**
     * @param name Name of this filter.
     */
    public FrontierSchedulerForTianya(String name) {
        super(name);
    }
    /**
     * Schedule the given {@link CandidateURI CandidateURI} with the Frontier.
     * @param caUri The CandidateURI to be scheduled.
     */
    protected void schedule(CandidateURI caUri) {
    	String uri = caUri.toString();
    	//过滤.zip,.exe,.rar,.pdf,.doc的链接
    	if(uri.endsWith(".zip") 
			|| uri.endsWith(".rar") 
			|| uri.endsWith(".exe")
			|| uri.endsWith(".pdf")
			|| uri.endsWith(".doc")
			|| uri.endsWith(".xls")){ 
    		return ;
    	}
    	//只抓取bbs.tianya.com的广西社区79列表和帖子内容 http://www.usitrip.com   bbs.tianya.cn
    	if(uri.contains("bbs.tianya.cn")){
    		/*if(uri.contains("bbs.tianya.cn/post-79") 
	    		|| uri.contains("bbs.tianya.cn/list-79")){ 
	    		//在控制台输出当前处理的URI
	    		
	    	}*/
    		//System.out.println(uri);
    		getController().getFrontier().schedule(caUri);
    	}
    	
        
    }
}
