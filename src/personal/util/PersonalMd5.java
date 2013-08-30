package personal.util;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
/**
 * Base Java Md5 Functions from net
 * 20130828  lx
 * */
public class PersonalMd5 {

	/**
	 * @param args
	 * @throws NoSuchAlgorithmException 
	 */
	
	public static void main(String[] args) throws NoSuchAlgorithmException {
		// TODO Auto-generated method stub
		String url = "123456";
		System.out.println(url);
		System.out.println(PersonalMd5.MyMd5(url.getBytes()));
	}
	public final static String MyMd5(byte[] source) throws NoSuchAlgorithmException{
		String s = null;  
        char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',  
                'a', 'b', 'c', 'd', 'e', 'f' };// 用来将字节转换成16进制表示的字符  
        try {  
            java.security.MessageDigest md = java.security.MessageDigest  
                    .getInstance("MD5");  
            md.update(source);  
            byte tmp[] = md.digest();// MD5的计算结果是一个128位的长整数,用字节表示就是16个字节  
            char str[] = new char[16 * 2];//每个字节用16进制表示的话,使用两个字符,所以表示成 16进制需要 32 个字符  
            int k = 0;// 表示转换结果中对应的字符位置  
            for (int i = 0; i < 16; i++) {//从第一个字节开始,对 MD5 的每一个字节转换成16进制字符的转换  
                byte byte0 = tmp[i];// 取第 i 个字节  
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];// 取字节中高4位的数字转换,>>>为逻辑右移,将符号位一起右移  
                str[k++] = hexDigits[byte0 & 0xf];//取字节中低4位的数字转换  
            }  
            s = new String(str);//换后的结果转换为字符串  
        } catch (NoSuchAlgorithmException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }  
        return s;  
	}
}
