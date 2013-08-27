package personal.spider;

/* TianyaExtractor
*
* $Id: TianyaExtractor.java 4497 2006-08-15 01:31:35Z stack-sf $
*
* Created on June 25, 2013
*
*/ 

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import  org.archive.crawler.extractor.Extractor;
import org.apache.commons.httpclient.URIException;
import org.archive.crawler.datamodel.CrawlURI;
import org.archive.crawler.extractor.Extractor;
import org.archive.crawler.extractor.Link;
import org.archive.io.ReplayCharSequence;
import org.archive.util.HttpRecorder;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
/**
 * extends Extractor for Filter tianya url
 * 提取URL匹配
 * http://bbs.tianya.cn/post-79-575154-1.shtml
 * http://bbs.tianya.cn/list-79-1.shtml
 */
public class TianyaExtractor extends Extractor {
	
	private static Logger logger = Logger.getLogger(TianyaExtractor.class.getName());
	//1st preg pattern for tianya post page
	public static final String PATTERN_TIANYA_POST = "http://bbs.tianya.cn/post-79-[\\d]+.shtml";
	//2nd pattern for list
	public static final String PATTERN_TIANYA_LIST = "http://bbs.tianya.cn/list-79-[\\d]+.shtml";
	//3rd preg pattern for a
	public static final String PATTERN_A_HREF = "<a\\s+href\\s*=\\s*(\"([^\"]*)\"|[^\\s>])\\s*>";
	public String name = "";
	//construct
	public TianyaExtractor(String name){
		this(name,"Tianya Post Extractor");
	}
    //consturct
	public TianyaExtractor(String name,String description){
		super(name,description);
	}
	//extends extract
	protected void extract(CrawlURI curi){
		//将链接对象转换为字符串
		String url = curi.toString();
		/**
		 * 下面一段代码主要用来取得当前链接返回字符串,以便对内容进行分析时使用
		 */
		ReplayCharSequence cs = null;
		try{ 
			HttpRecorder hr = curi.getHttpRecorder();
			if(hr == null){ 
				throw new IOException("Why is recorder null here?");
			}
			cs = hr.getReplayCharSequence();
		} catch(IOException e){ 
			curi.addLocalizedError(this.getName(), e, "Failed get of replay char sequence"+curi.toString()+" "+e.getMessage());
			logger.log(Level.SEVERE,"Failed get of replay char sequence in "+Thread.currentThread().getName(),e);
		}
		//如果什么也没抓到,就返回
		if(cs == null){ 
			return;
		}
		//将链接返回的内容转换成字符串
		String content = cs.toString();
		try{
			//jsoup parse String to Document 2013-06-26
			Document doc = Jsoup.parse(content);
			Element tcontent = doc.getElementById("main");
			if(tcontent != null){//fixed bellow java.lang.NullPointerException
				Elements links = tcontent.getElementsByTag("a");
				for (Element link : links) {
				  String linkHref = link.attr("href");
				  String linkText = link.text();
				  if(linkHref != "" && linkHref.contains("post-79")){ 
					  this.addLinkFromString(curi,linkHref,"",Link.NAVLINK_HOP);
				  }
				}
			}else{
				  this.addLinkFromString(curi,url,"",Link.NAVLINK_HOP);
			}
			
			//抓取url匹配 version2
			/*if(url.contains("bbs.tianya.cn/post-79") 
	    		|| url.contains("bbs.tianya.cn/list-79")){ 
				System.out.println("Y"+url);
				addLinkFromString(curi,url,"",Link.NAVLINK_HOP);
	    	}else{ 
	    		System.out.println("N"+url);
	    	}*/
			/*
			//将字符串的内容进行正则匹配,取出其中的链接信息和其它元素,如img version3
			Pattern pattern = Pattern.compile(PATTERN_A_HREF, Pattern.CASE_INSENSITIVE);
			Matcher matcher = pattern.matcher(content);
			//若找到了一个链接
			while (matcher.find()){ 
				String newUrl = matcher.group(2);
				//查看是否为tianya帖子格式
				if(newUrl.matches(PATTERN_TIANYA_POST) || newUrl.matches(PATTERN_TIANYA_LIST)){ 
					//若是,则将链接加入队列中,以备后续处理
					this.addLinkFromString(curi,newUrl,"",Link.NAVLINK_HOP);
				}
			}*/
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	//将链接记录保存下来,以备后续处理
	private void addLinkFromString(CrawlURI curi,String uri,CharSequence context,char hopType){
		try{//
			curi.createAndAddLinkRelativeToBase(uri, context.toString(), hopType);
		} catch(URIException e){ 
			if(getController() != null){ 
				getController().logUriError(e, curi.getUURI(), uri);
			}else{ 
				logger.info("Failed createAndAddLinkRelativeToBase"+curi+", "+uri+", "+context+", "+hopType+": "+e);
			}
		}
	}

}

