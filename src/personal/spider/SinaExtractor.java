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
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

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

import personal.util.Test;
import personal.util.TestJson;
import personal.util.PersonalRedis;
import personal.util.PersonalMd5;
import personal.util.PersonalJdbc;
import personal.util.PersonalSphx;
import personal.util.PersonalMongodb;

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
	public static final String OUTPUTS = "mysql";//mongodb,mysql
	public static final boolean USERT = false;//coreseekRT flag
	
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
		//System.out.println("E"+url);
		String pageCharset = "utf-8";
		ReplayCharSequence cs = null;
		String currentChannel = "";
		JSONObject  channelRule = null;
		if(url.contains("dns:")){
			this.addLinkFromString(curi,url,"",Link.NAVLINK_HOP);
			return;
		}else{//not dns
			  currentChannel = Test.generateChannel(url);//from url to channel
			  channelRule = TestJson.fetchChannelRule(currentChannel);//fetch rule with channel
			  try{ 
					HttpRecorder hr = curi.getHttpRecorder();
					if(hr == null){ 
						throw new IOException("Why is recorder null here?");
					}
					pageCharset  = channelRule.getString("charset");
					hr.setCharacterEncoding(pageCharset);//ISO-8859-1  utf-8 gb2312
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
				String content = cs.toString();//spider content
				//byte[] content1 = content.getBytes(Charset.forName("ISO-8859-1"));
				//String content2 = new String(content.getBytes(Charset.forName("gb2312")),Charset.forName("utf-8"));
				//String currentChannel = Test.generateChannel(url);//from url to channel
		    	//JSONObject  channelRule = TestJson.fetchChannelRule(currentChannel);//fetch rule with channel
		    	try{
					String listRule = channelRule.getString("listRule");
			    	//String channel = channelRule.getString("channel");//explain channel's redis content
					String seedURL = channelRule.getString("seedURL");
					//JSONArray urlPattern = channelRule.getJSONArray("urlPattern");
					JSONObject singleRule = channelRule.getJSONObject("singleRule");//single page content rule
					String itemRule = channelRule.getString("itemRule");//list a page
					try{		
						Document listdoc = Jsoup.parse(content);
						//jsoup parse String to Document 2013-08-02
						if(url.equals(seedURL)){//list page
							System.out.println("Y"+seedURL+listRule);
							Elements mainrow = listdoc.select(listRule);//"div.main_row"
							for(Element row : mainrow){//all a element in seed page
								Elements links = row.getElementsByTag("a");
								for (Element link : links) {
								  String linkHref = link.attr("href");
								  String linkText = link.text();
								  //linkText = new String(linkText.getBytes());
								  if(linkHref != ""){//&& linkHref.contains("http://tech.sina.com.cn/t/" 
									  if(linkHref.matches(itemRule)){//match list page's item
										  System.out.println("Match: "+linkHref);
										  this.addLinkFromString(curi,linkHref,"",Link.NAVLINK_HOP);
									  }
								  }
								}
							}
						}else{//single page
							//Elements articles = listdoc.select("div.blkContainer");
							System.out.println("Y2");
							//for(Element article : articles){}
							//Element eTitle = listdoc.getElementById("artibodyTitle");
							Element eTitle = listdoc.select(singleRule.getString("title")).first(); 
							String title = eTitle.text().toString();
							//title = new String(title.getBytes("UTF-8"),"UTF-8");
							Element ePubdate = listdoc.select(singleRule.getString("pubdate")).first();
							String pubdate = ePubdate.text();
							String pubdate1 = Test.currentTime();
							String pubdate2 = Test.currentTime10();
							Element eContent = listdoc.select(singleRule.getString("content")).first();
							String imgStr = "  ";//
							Elements imgs = eContent.select("img");
							if(imgs != null){
								for(Element img:imgs){//extract img element
									if(img.attr("src") !=null){
										imgStr+="<img src=\""+img.attr("src")+"\">";
									}
								}
							}
							//String itemcontent = eContent.text().toString(); //text
							String itemcontent = eContent.html().toString();//html
							itemcontent =  Test.filterString(itemcontent,url);//proc html to text
							imgStr = Test.relativeToabsolute(imgStr,url);//proc img's url to absolute url
							String summary = Test.strCut(eContent.text().toString(), 127, "...");//filter content to summary,table.summary tinytext to text for fixed insert error
							itemcontent = imgStr+itemcontent;//img str extract,append before content
							//itemcontent = new String(itemcontent.getBytes(pageCharset),"UTF-8");//ISO-8859-1 UTF-8
							//title =  Test.gbToUtf8(title);
							//title = new String(title.getBytes(pageCharset),"UTF-8");
							//mysql labs
							long uidx = PersonalRedis.getCurrentIdForLabs();
							if(SinaExtractor.OUTPUTS == "mysql"){
								//outputs to mysql
								PersonalJdbc jdbc = new PersonalJdbc();
								jdbc.getConnection();
								//long uidx = PersonalRedis.getCurrentIdForLabs();
								String insertSql = "insert into ictspace_entry_content(id,title,summary,publishTime,channel,content,originalURL,source) VALUES ("+uidx+",'"+title.toString()+"','"+summary.toString()+"','"+pubdate1+"','"+currentChannel+"','"+itemcontent.toString()+"','"+url+"','heritrix')";
								//String rtSql = "insert into labsrt(id,title,publishtime,channel,content,summary) VALUES ("+uidx+",'"+title+"','"+pubdate1+"','"+currentChannel+"','"+itemcontent+"','"+summary+"')";
								boolean inFlag = jdbc.insertSQL(insertSql.toString());
								if(inFlag == true){//insert succ
									System.out.println("S: "+insertSql);
									String md5Url = PersonalMd5.MyMd5(url.getBytes());//md5 url
									String data = ""+uidx+"@"+pubdate2+"";
									String eflag = PersonalRedis.setRedisLabsURL(md5Url,data);
									System.out.println("IDA: "+data);
									if(SinaExtractor.USERT == true){
										String insertRT = "insert into labsrt (id,author,title,summary,keywords,channel,content,publishtime) VALUES ("+uidx+",'','"+title+"','"+summary+"','','"+currentChannel+"','"+itemcontent+"','"+pubdate2+"'";
										PersonalSphx spx  = new PersonalSphx();
										spx.getConnection();
										boolean sphxinFlag = spx.insertSQL(insertRT);
										if(sphxinFlag == true){
											System.out.println("SPHXS: "+insertRT);
										}else{
											System.out.println("SPHXF: "+insertRT);
										}
										spx.close();
									}
									
									//System.out.println("EI: "+uidx+",title"+title+"md5Url"+md5Url);//+"source"+source+"sourceUrl"+sourceUrl
								}else{//insert faild
									System.out.println("F: "+insertSql);
									//System.out.println("EI: "+uidx+",title"+title+"pubdate"+pubdate);//+"source"+source+"sourceUrl"+sourceUrl
								}					
								jdbc.close();
							}else if(SinaExtractor.OUTPUTS == "mongodb"){
								//mongodb yuqing
								Map<String,String> documentMap = new HashMap<String,String>();
								documentMap.put("id", ""+uidx+"");
								documentMap.put("title", title);
								documentMap.put("summary", summary);
								documentMap.put("publishTime", pubdate1.toString());
								documentMap.put("channel", currentChannel);
								documentMap.put("content", itemcontent);
								documentMap.put("originalURL", url);
								documentMap.put("source", "heritrix");
								boolean mflag = PersonalMongodb.insertDocument(documentMap);
								if(mflag == true){
									System.out.println("MONS: "+mflag);
									String md5Url = PersonalMd5.MyMd5(url.getBytes());//md5 url
									String data = ""+uidx+"@"+pubdate2+"";
									String eflag = PersonalRedis.setRedisLabsURL(md5Url,data);
								}else{
									System.out.println("MONF: "+url);
								}
							}
							
							this.addLinkFromString(curi,url,"",Link.NAVLINK_HOP);
						}
					} catch(Exception e){
						e.printStackTrace();
					}
				}catch(Exception e){
					e.printStackTrace();
				}
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

