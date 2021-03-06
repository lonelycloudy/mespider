package personal.spider;

/* TianyaExtractor
*
* $Id: TianyaExtractor.java 4497 2006-08-15 01:31:35Z stack-sf $
*
* Created on June 25, 2013
*
*/ 

import java.io.IOException;
import java.nio.charset.Charset;
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
 * http://tech.sina.com.cn/t/2013-08-16/01198642973.shtml
 * http://tech.sina.com.cn/tele/
 * http://www.open-open.com/jsoup/
 */
public class SinaExtractor extends Extractor {
	
	private static Logger logger = Logger.getLogger(SinaExtractor.class.getName());
	//1st preg pattern for tianya post page
	public static final String PATTERN_SINA_POST = "http://tech.sina.com.cn/t/[\\d]-[\\d]-[\\d]/[\\d]+.shtml";
	//2nd pattern for list
	public static final String PATTERN_SINA_LIST = "http://tech.sina.com.cn";
	//3rd preg pattern for a
	public static final String PATTERN_A_HREF = "<a\\s+href\\s*=\\s*(\"([^\"]*)\"|[^\\s>])\\s*>";
	public String name = "";
	//construct
	public SinaExtractor(String name){
		this(name,"Sina Post Extractor");
	}
    //consturct
	public SinaExtractor(String name,String description){
		super(name,description);
	}
	//extends extract
	protected void extract(CrawlURI curi){
		//将链接对象转换为字符串
		String url = curi.toString();
		/**
		 * 下面一段代码主要用来取得当前链接返回字符串,以便对内容进行分析时使用
		 */
		System.out.println("E"+url);
		ReplayCharSequence cs = null;
		try{ 
			HttpRecorder hr = curi.getHttpRecorder();
			if(hr == null){ 
				throw new IOException("Why is recorder null here?");
			}
			hr.setCharacterEncoding("gb2312");//ISO-8859-1
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
		//byte[] content1 = content.getBytes(Charset.forName("ISO-8859-1"));
		//String content2 = new String(content.getBytes(Charset.forName("gb2312")),Charset.forName("utf-8"));
		try{		
			Document listdoc = Jsoup.parse(content);
			//jsoup parse String to Document 2013-08-02
			if(url.contains("http://tech.sina.com.cn/t/")){
				Elements articles = listdoc.select("div.blkContainer");
				System.out.println("Y2");
				for(Element article : articles){
					Element eTitle = article.getElementById("artibodyTitle");
					String title = eTitle.text();
					Element ePubdate = article.getElementById("pub_date");
					String pubdate = ePubdate.text();
					//pubdate = new String(pubdate.getBytes());//utf-8 gb2312
					Element eSource = article.getElementById("media_name");
					String source = "";
					String sourceUrl="";
					try{
						 source  = eSource.child(0).text();
						 sourceUrl  = eSource.child(0).attr("href");
					}catch(Exception e){
						 source  = eSource.text();
						 sourceUrl  = "";
					}
					//source = new String(source.getBytes());
					Element eContent = article.getElementById("artibody");
					String itemcontent = eContent.html();
					//itemcontent = new String(itemcontent.getBytes());
					System.out.println("EI:"+"title"+title+"pubdate"+pubdate+"source"+source+"sourceUrl"+sourceUrl);
					this.addLinkFromString(curi,url,"",Link.NAVLINK_HOP);
				}
			}else{
				Elements mainrow = listdoc.select("div.main_row");
				System.out.println("Y");
				for(Element row : mainrow){
					Elements links = row.getElementsByTag("a");
					for (Element link : links) {
					  String linkHref = link.attr("href");
					  String linkText = link.text();
					  //linkText = new String(linkText.getBytes());
					  if(linkHref != "" && linkHref.contains("http://tech.sina.com.cn/t/")){ 
						  System.out.println("L:"+linkHref+",T:"+linkText);
						  this.addLinkFromString(curi,linkHref,"",Link.NAVLINK_HOP);
					  }
					}
				}
			}
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

