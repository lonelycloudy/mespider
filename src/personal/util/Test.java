package personal.util;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 底层函数实现,Java函数编写示例
 * ��:Mysql,Redis,Sphinxʵ��ʾ��
 * */
public class Test{
	
	public static void main(String[] args) throws ParseException{
		String t = "text/html; charset=GBK";
		String t1 = "text/html; charset=utf-8";
		String t2 = "text/html; charset=UTF-8";
		String t3 = "text/html; charset=GB2312";
		System.out.println(getPageCharset(t));
		System.out.println(getPageCharset(t1));
		System.out.println(getPageCharset(t2));
		System.out.println(getPageCharset(t3));
		//Test.generateChannel("http://tech.sina.com.cn/t/2012-11-12/12247790473.shtml");
		//long current = System.currentTimeMillis();
		//System.out.println(current);
		//System.out.println(currentTimeStamp());
		//System.out.println(currentTime());
		String a = "http://tech.sina.com.cn/t/apple/2013-08-28/03238682257.shtml";
	    String b = "http://tech.sina.com.cn/t/2013-08-27/01188677643.shtml";
	    String c = "http://roll.tech.sina.com.cn/tele/gj/index.shtml";
	    /*	System.out.println("A: "+a);
		    System.out.println(checkstr(a));
		    System.out.println("B: "+b);
		    System.out.println(checkstr(b));
		    System.out.println("C: "+c);
		    System.out.println(checkstr(c));
	    */
		return ;
	}
	/**
	 * Fetch Single Page,then generate channel,
	 * http://tech.sina.com.cn/t/2012-11-12/12247790473.shtml  will return cn.com.sina.tech
	 * 20130822 lx
	 * */
	public static String generateChannel(String url){
		   //String url = "http://tech.sina.com.cn/t/2012-11-12/12247790473.shtml";
		   //System.out.println("Before:"+url);
		   url = url.replace("http://", "");
		   url = url.replace("https://", "");
		   String[] urls = url.split("/");
		   String temp = urls[0];
		   String[] channels = temp.split("\\.");//split . need proc specialy 
		   String url_new = "";
		   int len = channels.length-1;
		   for(int i=len;i>-1;i--){
			   if(len == i){
				   url_new +=channels[i];
			   }else{
				   url_new +="."+channels[i];
			   }
		   }
		   System.out.println("Channel:"+url_new);
		   return url_new;
	}
	public static boolean checkstr(String str){
		//Pattern itemRule1 = Pattern.compile("http:\\/\\/tech\\.sina\\.com\\.cn\\/t\\/([0-9]{4})-([0-9]{2})-([0-9]{2})/\\d+\\.shtml$");
		//Pattern itemRule = Pattern.compile("http://tech\\.sina\\.com\\.cn/t/\\d+-\\d+-\\d+/\\d+\\.shtml");//java: before \ need add \ again,\\d
		//Matcher linkHrefm = itemRule.matcher(str);
		String pattern = "http:\\/\\/tech\\.sina\\.com\\.cn\\/t\\/([0-9]{4})-([0-9]{2})-([0-9]{2})/\\d+\\.shtml$".toString();
		String pattern1 = "http://tech\\.sina\\.com\\.cn/t/\\d+-\\d+-\\d+/\\d+\\.shtml";
		System.out.println(pattern);
		boolean flag = str.matches(pattern);
		return flag;
	} 
	/**
	 * return current date string UTC time()
	 * */
	public static String currentTime(){
		Date now = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String current = dateFormat.format(now);//2013-09-02 09:22:59
		return current;
	}
	/**
	 * return current time stamp(like integer length 10)
	 * */
	public static String currentTime10(){
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String current = dateFormat.format(now);//2013-09-02 09:22:59
		Long current1 = now.getTime();//1378113779718
		String str = String.valueOf(current1);
		String re_time = str.substring(0, 10);
		return re_time;
	}
	/**
	 * return current time stamp(Long)
	 * */
	public static Long currentTimeStamp(){
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//String current = dateFormat.format(now);//2013-09-02 09:22:59
		Long current = now.getTime();//1378113779718
		return current;
	}
	/**
	 * 
	 * @param contentType url
	 * @return
	 */
	public static String getPageCharset(String contentType){
		String[] tt = contentType.split("\\;");
		int ttlen = tt.length;
		if(ttlen <2){
			return "";
		}else{
			String temp = tt[1];
			String[] ttt = temp.split("\\=");
			return ttt[1];
		}
	}
}
/**
javac -d . personal/util/Test.java
java -cp . personal.util.Test
Before:http://tech.sina.com.cn/t/2012-11-12/12247790473.shtml
After:cn.com.sina.tech
*/