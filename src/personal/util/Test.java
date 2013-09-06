package personal.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.net.URI;

/**
 * 底层函数实现,Java函数编写示例
 * ��:Mysql,Redis,Sphinxʵ��ʾ��
 * */
public class Test{
	
	public static void main(String[] args) throws ParseException{	
		String content = "<div id=\"artibody\"> <div><a target=\"_blank\" href=\"http://stock.finance.sina.com.cn/usstock/quotes/NOK.html\"><img alt=\"诺基亚周二收盘大涨31%微软跌幅接近5%\" src=\"http://image.sinajs.cn/newchart/png/min/us/NOK.png\"></a></div> <p>新浪科技讯 北京时间9月4日早间消息，截至美国股市周二收盘，</p><br><br/><p>&#12288;&#12288;早前开盘的芬兰赫尔辛基股市中，诺基亚亦大幅上涨，涨超40%。</p></div>";
		String newcontent = filterString(content,"http://tech.sina.com.cn/t/2013-08-23/08318668243.shtml"); 
		System.out.println(newcontent.toString());
		/*String t = "text/html; charset=GBK";
		String t1 = "text/html; charset=utf-8";
		String t2 = "text/html; charset=UTF-8";
		String t3 = "text/html; charset=GB2312";
		System.out.println(getPageCharset(t));
		System.out.println(getPageCharset(t1));
		System.out.println(getPageCharset(t2));
		System.out.println(getPageCharset(t3));
		*/
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
	private static String getHexString(byte b) {   
        String hexStr = Integer.toHexString(b);   
        int m = hexStr.length();   
        if (m < 2) {   
            hexStr = "0" + hexStr;   
        } else {   
            hexStr = hexStr.substring(m - 2);   
        }   
        return hexStr;   
    }   
	 private static String getBinaryString(int i) {   
	        String binaryStr = Integer.toBinaryString(i);   
	        int length = binaryStr.length();   
	        for (int l = 0; l < 8 - length; l++) {   
	            binaryStr = "0" + binaryStr;   
	        }   
	        return binaryStr;   
	    }   
	/**
	 * 
	 * @param str(gb2312 to utf8)
	 * @return 
	 * @throws UnsupportedEncodingException
	 */
	public static String gbToUtf8(String str) throws UnsupportedEncodingException {   
        StringBuffer sb = new StringBuffer();   
        for (int i = 0; i < str.length(); i++) {   
            String s = str.substring(i, i + 1);   
            if (s.charAt(0) > 0x80) {   
                byte[] bytes = s.getBytes("Unicode");   
                String binaryStr = "";   
                for (int j = 2; j < bytes.length; j += 2) {   
                    // the first byte   
                    String hexStr = getHexString(bytes[j + 1]);   
                    String binStr = getBinaryString(Integer.valueOf(hexStr, 16));   
                    binaryStr += binStr;   
                    // the second byte   
                    hexStr = getHexString(bytes[j]);   
                    binStr = getBinaryString(Integer.valueOf(hexStr, 16));   
                    binaryStr += binStr;   
                }   
                // convert unicode to utf-8   
                String s1 = "1110" + binaryStr.substring(0, 4);   
                String s2 = "10" + binaryStr.substring(4, 10);   
                String s3 = "10" + binaryStr.substring(10, 16);   
                byte[] bs = new byte[3];   
                bs[0] = Integer.valueOf(s1, 2).byteValue();   
                bs[1] = Integer.valueOf(s2, 2).byteValue();   
                bs[2] = Integer.valueOf(s3, 2).byteValue();   
                String ss = new String(bs, "UTF-8");   
                sb.append(ss);   
            } else {   
                sb.append(s);   
            }   
        }   
        return sb.toString();   
    }
	/***
	 * Filter Spider Content
	 * @param tempcontent
	 * @return
	 */
	public static String filterString(String tempcontent,String currentUrl){
		tempcontent.replaceAll("<[//s]*?script[^>]*?>[//s//S]*?<[//s]*?///[//s]*?script[//s]*?>", "");
		tempcontent.replaceAll("<script>*?<\\/script>", "");
		tempcontent.replaceAll("<script\\s type=\"text\\/javascript\">*?<\\/script>", "");
		tempcontent.replaceAll("<[//s]*?style[^>]*?>[//s//S]*?<[//s]*?///[//s]*?style[//s]*?>", "");
		tempcontent.replaceAll("<style>.*?</style>","");
		tempcontent.replaceAll("<style type=\"text\\/css\">.*?<\\/style>", "");
		tempcontent.replaceAll("<link.*?>", "");
		tempcontent.replaceAll("<link.*?\\/>", "");
		tempcontent.replaceAll("<!--.*?\\s-->", "");
		tempcontent.replaceAll("<!--.*?-->", "");
		tempcontent.replaceAll("/　　/Uis", "");
		tempcontent.replaceAll("/<p.*>/Uis", "  ");
		tempcontent.replaceAll("/\\<\\/p\\>/is", " ");//windows:\r\n,linux \r
		tempcontent.replaceAll("/\\<\\/div\\>/is", "\\<\\/div\\> ");
		tempcontent.replaceAll("/\\<br \\/>/is", "  ");
		tempcontent.replaceAll("/\\<br\\>/is", "  ");
		String regEx_script = "<[//s]*?script[^>]*?>[//s//S]*?<[//s]*?///[//s]*?script[//s]*?>"; // 定义script的正则表达式{或<script[^>]*?>[//s//S]*?<///script>      
        String regEx_style = "<[//s]*?style[^>]*?>[//s//S]*?<[//s]*?///[//s]*?style[//s]*?>"; // 定义style的正则表达式{或<style[^>]*?>[//s//S]*?<///style>      
        String regEx_html = "<[^>]+>"; //html regx
        String regEx_html1 = "<[^>]+";
        String htmlStr = tempcontent; 
        String textStr = "";
        Pattern  p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);      
        Matcher m_script = p_script.matcher(htmlStr);      
        htmlStr = m_script.replaceAll("");//replace script    

        Pattern p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);      
        Matcher m_style = p_style.matcher(htmlStr);      
        htmlStr = m_style.replaceAll("");//replace style

        Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);      
        Matcher m_html = p_html.matcher(htmlStr);      
        htmlStr = m_html.replaceAll("");//replace html

        Pattern p_html1 = Pattern.compile(regEx_html1, Pattern.CASE_INSENSITIVE);      
        Matcher m_html1 = p_html1.matcher(htmlStr);      
        htmlStr = m_html1.replaceAll("");//replace html
        
        textStr = htmlStr;      
		//StripTags strip = new StripTags();
		//tempcontent = strip.parse(tempcontent,"<img>");//just allow img,from baidu net
		//tempcontent.replaceAll("/<\\s*img\\s+([^>]*)\\s*>/", "");
		//tempcontent.replaceAll("<\\s*img\\s*(?:[^>]*)src\\s*=\\s*([^>]+)", "$1");
		//bellow proc img
		//Pattern p = Pattern.compile("<img[^>]+src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>");//<img[^<>]*src=[\'\"]([0-9A-Za-z.\\/]*)[\'\"].(.*?)>");
        //Matcher m = p.matcher(tempcontent);
        //System.out.println(m.find());
        //System.out.println(m.groupCount());
        //while(m.find()){
          //  System.out.println(m.group()+"-------------↓↓↓↓↓↓");
           // System.out.println(m.group(1));
        //}
		
		//tempcontent.replaceAll("(<img.*?)align=([\"\\'])?.*?(?(2)\\2|\\s)([^\\>]+\\>)/is", "$1$3");
		//tempcontent  = relativeToabsolute(tempcontent,currentUrl);//convert relative link to absolute link
		return textStr;
	}
	/***
	 * extract content's img
	 * @param htmlStr
	 * @return
	 */
	public static List<String> getImgStr(String htmlStr){        
        String img="";        
        Pattern p_image;        
        Matcher m_image;        
        List<String> pics = new ArrayList<String>();     
        String regEx_img = "<img.*src=(.*?)[^>]*?>"; //图片链接地址        
        p_image = Pattern.compile(regEx_img,Pattern.CASE_INSENSITIVE);        
        m_image = p_image.matcher(htmlStr);      
        while(m_image.find()){        
            img = img + "," + m_image.group();        
            Matcher m  = Pattern.compile("src=\"?(.*?)(\"|>|\\s+)").matcher(img); //匹配src     
            while(m.find()){     
               pics.add(m.group(1));     
            }     
        }        
        return pics;        
    }       
	/***
	 * Proc content's relative url
	 * @param content
	 * @param feedUrl
	 * @return
	 */
	public static String relativeToabsolute(String content,String currentUrl){
		String[] hosts = currentUrl.split("/");
		if(hosts[0] == ""){
			return content;
		}else{
			content = content.replaceAll("/src=\"\\//", "src=\\"+hosts[0]);
			return content;
		}
	}
	/***
	 * Cut String 
	 * @param text length endWith
	 * @return
	 */
	public static String strCut(String text,int length,String endWith){
		int textLength = text.length();  
        int byteLength = 0;  
        StringBuffer returnStr =  new StringBuffer();  
        for(int i = 0; i<textLength && byteLength < length*2; i++){  
            String str_i = text.substring(i, i+1);   
            if(str_i.getBytes().length == 1){//英文  
                byteLength++;  
            }else{//中文  
                byteLength += 2 ;  
            }  
            returnStr.append(str_i);  
        }  
        try {  
            if(byteLength<text.getBytes("GBK").length){//getBytes("GBK")每个汉字长2，getBytes("UTF-8")每个汉字长度为3  
                returnStr.append(endWith);  
            }  
        } catch (UnsupportedEncodingException e) {  
            e.printStackTrace();  
        }  
        return returnStr.toString();  
	}
}
/**
javac -d . personal/util/Test.java
java -cp . personal.util.Test
Before:http://tech.sina.com.cn/t/2012-11-12/12247790473.shtml
After:cn.com.sina.tech
*/