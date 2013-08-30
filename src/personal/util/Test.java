package personal.util;
/**
 * 底层函数实现,Java函数编写示例
 * ��:Mysql,Redis,Sphinxʵ��ʾ��
 * */
public class Test{
	
	public static void main(String[] args){
	    //Test.generateChannel("http://tech.sina.com.cn/t/2012-11-12/12247790473.shtml");
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
}
/**
javac -d . personal/util/Test.java
java -cp . personal.util.Test
Before:http://tech.sina.com.cn/t/2012-11-12/12247790473.shtml
After:cn.com.sina.tech
*/