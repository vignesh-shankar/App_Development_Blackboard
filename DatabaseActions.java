package f15g110;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
@ManagedBean
@SessionScoped
public class DatabaseActions implements Serializable{
	DatabaseInformationBean dbi= new DatabaseInformationBean();
	Connection conn=null; 
	public DatabaseActions(){
		if(conn == null){
			try{
			Class.forName("com.mysql.jdbc.Driver");
			//conn = DriverManager.getConnection("jdbc:mysql://131.193.209.57:3306/f15g110", "f15g110", "f15g110RScdW");
			conn = DriverManager.getConnection("jdbc:"+dbi.getDatabase()+"://"+dbi.getHostName()+":"+dbi.getPort()+"/"+dbi.getDbschema(), dbi.getUsername(), dbi.getPassword());
			}
			catch(Exception e){
				System.err.println(e);
				e.printStackTrace();
			}
		}
	}
	
	public Connection getconn(String host,String port,String database,String username,String password)
	{
		dbi.setDatabase(database);
		
		dbi.setHostName(host);
		dbi.setPort(port);
		dbi.setUsername(username);
		dbi.setPassword(password);
		//System.out.println("username set"+dbi.getUsername());
		try {
			conn=DriverManager.getConnection("jdbc:"+dbi.getDatabase()+"://"+dbi.getHostName()+":"+dbi.getPort()+"/"+dbi.getDbschema(), dbi.getUsername(), dbi.getPassword());
		}  
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return conn;
		
	}
	public ResultSet execute(String query){
		ResultSet rs = null;
		try{
			//Class.forName("com.mysql.jdbc.Driver");
		//DatabaseInformationBean dbi = new DatabaseInformationBean();
			//Connection conn = DriverManager.getConnection("jdbc:mysql://131.193.209.57:3306/f15g110", "f15g110", "f15g110RScdW");
		Statement stmt1 = conn.createStatement();
		rs = stmt1.executeQuery(query);
		
	}
		catch (Exception e){
			System.err.println(e);
			e.printStackTrace();
		}
		return rs;
}
	
	public void update(String query){
		PreparedStatement stmt = null;
		//DatabaseInformationBean dbi = new DatabaseInformationBean();
		try{
			//Class.forName("com.mysql.jdbc.Driver");
			//Connection conn = DriverManager.getConnection("jdbc:mysql://131.193.209.57:3306/f15g110", "f15g110", "f15g110RScdW");
		stmt = conn.prepareStatement(query);
		stmt.executeUpdate();
		}
		catch(Exception e){
			System.err.println(e);
			e.printStackTrace();
		}
		
	}
	
	
}
