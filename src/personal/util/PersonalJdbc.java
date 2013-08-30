package personal.util;

import java.sql.DriverManager;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Jdbc:Mysql With Java language
 * 20130822 lx
 * Combine with http://blog.csdn.net/flying_tao/article/details/6430189
 * */

public class PersonalJdbc {
    
	private		String dbDriver = "com.mysql.jdbc.Driver";//驱动类型
	private		String url = "jdbc:mysql://192.168.0.193:3306/test";//"jdbc:mysql://localhost:3306/myjsp";//连接url
	private		String username="boy";//"root";//用户名
	private		String password ="boy"; //"ictspace";//密码
	private 	java.sql.Connection mConnection=null;//连接
	private 	java.sql.Statement mStatement=null;//资源
	private 	ResultSet mResultSet=null;//记录集合
	
	//connect to Mysql
	public void getConnection() throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException{ 
		Statement mStatement=null;
		ResultSet mResultSet = null;
		Connection mConnection =null;
		//String sql = "select * from test";
		try{
			Class.forName(this.dbDriver).newInstance();
			//获取数据库连接
			this.mConnection =   DriverManager.getConnection(this.url,this.username,this.password);
			this.mStatement = this.mConnection.createStatement();
		}catch(ClassNotFoundException e){
			System.out.println("Class Not Found JDBC");
			e.printStackTrace();
		}catch(SQLException sqle){
			System.out.println("Could not connect db");
			sqle.printStackTrace();
		}
	}
	//close
	public void close(){
		try{
			if(mConnection != null){
				mConnection.close();
			}
		} catch(Exception e){
			System.out.println("Close Db Error");
			e.printStackTrace();
		}
	}
	//execute selection 
	public ResultSet selectSQL(String sql){
		ResultSet rs = null;
		try{
			mStatement = mConnection.prepareStatement(sql);
			rs = mStatement.executeQuery(sql);
		} catch (SQLException e){
			e.printStackTrace();
		}
		return rs;
	}
	//execute insert 
	boolean insertSQL(String sql){
		try{
			mStatement = mConnection.prepareStatement(sql);
			mStatement.executeUpdate(sql);
		} catch(SQLException e){
			System.out.println("Insert table Error");
			e.printStackTrace();
		} catch(Exception e){
			System.out.println("Insert error");
			e.printStackTrace();
		}
		return false;
	}
	//execute delete
	boolean deleteSQL(String sql){
		try{
			mStatement = mConnection.prepareStatement(sql);
			mStatement.executeUpdate(sql);
			return true;
		}catch(SQLException e){
			System.out.println("delete table Error");
			e.printStackTrace();
		} catch(Exception e){
			System.out.println("delete error");
			e.printStackTrace();
		}
		return false;
	}
	//execute update 
	boolean updateSQL(String sql){
		try{
			mStatement = mConnection.prepareStatement(sql);
			mStatement.executeUpdate(sql);
			return true;
		} catch(SQLException e){
			System.out.println("update table error");
		} catch(Exception e){
			System.out.println("update error");
		}
		return false;
	}
	//fetch data list in tables
	void fetchList(ResultSet rs){
		System.out.println("ResultSet List id \t\t date \t\t title \t\t content \t\t");
		try{
			while(rs.next()){
				System.out.println(rs.getInt("id")+"\t\t"
						+rs.getDate("date_added")+"\t\t"
						+rs.getString("title")+"\t\t"
						+rs.getString("content")+"\t\t"
						);
			}
		} catch(SQLException e){
			System.out.println("fetch data list error");
			e.printStackTrace();
		} catch (Exception e){
			System.out.println("fetch list error");
			e.printStackTrace();
		}
	}
	//use demo:insert ,delete,update,select
	public static void main(String args[]) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException{
		PersonalJdbc jd = new PersonalJdbc();
		jd.getConnection();
		String sql = "select * from documents";
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");//kk for 24,h for 12
		String current = df.format(new Date());
		String insert = "insert into documents(group_id,group_id2,date_added,title,content) VALUES ("+2+","+9+",'"+current+"','titlejava','contentjava')";
		String update = "update documents set group_id2=11 where id=5";
		String delete = "DELETE FROM documents ORDER BY id DESC LIMIT 1";
		System.out.println(insert);
		if(jd.insertSQL(insert) == true){
			System.out.println("insert successfully");
			ResultSet rs = jd.selectSQL(sql);
			jd.fetchList(rs);
		}
		if(jd.updateSQL(update) == true){
			System.out.println("update successfully");
			ResultSet rs = jd.selectSQL(sql);
			jd.fetchList(rs);
		}
		if(jd.deleteSQL(delete) == true){
			System.out.println("delete successfully");
			ResultSet rs = jd.selectSQL(sql);
			jd.fetchList(rs);
		}
		jd.close();
	}
}
