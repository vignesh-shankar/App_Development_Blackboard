package f15g110;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
//import javax.servlet.http.HttpSession;

@SessionScoped
@ManagedBean
public class DatabaseLoginBean implements Serializable{
	private UIComponent mybutton;
	private Connection conn=null;
	
	private ArrayList<String> delList=new ArrayList<String>();
	private String delTablename;
	private String tableName;
	private String hostName;
	private String port;
	private String[] chosenTables;
	private String userName="f15g110";
	private String password="f15g110RScdW";
	private String database;
	private String dbschema="f15g110";
	private String retstring;
	private DatabaseActions dba= new DatabaseActions();
	private DatabaseInformationBean db=new DatabaseInformationBean();
	
	public String[] getChosenTables() {
		return chosenTables;
	}

	public void setChosenTables(String[] chosenTables) {
		this.chosenTables = chosenTables;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public DatabaseLoginBean()
	{
		
	}
	
	public String getDelTablename() {
		return delTablename;
	}
	public void setDelTablename(String delTablename) {
		this.delTablename = delTablename;
	}

	
	
	public Connection getConn() {
		return conn;
	}
	public void setConn(Connection conn) {
		this.conn = conn;
	}
	public String getHostName() {
		return hostName;
	}
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	public ArrayList<String> getDelList() {
		return delList;
	}
	public void setDelList(ArrayList<String> delList) {
		this.delList = delList;
	}
	
	@PostConstruct
	 public void init(){
	 	
	 }
	public String login()
	{
		if (conn!=null)
		{ 
			//System.out.println("second time daa");
			try {
				conn.close();
				conn=null;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		delList.clear();
		//System.out.println(hostName);
		
		//System.out.println("Size"+delList.size());
		
		String temphost="131.193.209.54";
		if (hostName.equals("54"))
		{
			//System.out.println(" 54 da Ulla irkan");
			temphost="131.193.209.54";
		}
		else if (hostName.equals("57"))
		{
			temphost="131.193.209.57";
		}
		else
		{
			temphost="localhost";
		}
		
		if(conn == null){
			if (getDatabase().equals("mysql")) {

				try {
					if(!password.equals("f15g110RScdW"))
					{
						FacesMessage message = new FacesMessage("Username or password is invalid");
						FacesContext.getCurrentInstance().addMessage(null, message);
						retstring="dbLogin";
							}
					else
					{
					    setDatabase("mysql");
					    setPort("3306");	
					Class.forName("com.mysql.jdbc.Driver");
					if(temphost.equals("131.193.209.54")){
						conn = dba.getconn(temphost,getPort(),getDatabase(), userName, password);
					}
					else{
					conn = dba.getconn(temphost,getPort(),getDatabase(), userName, password);}
					/*HttpSession hs=Sessionhandler.getSession();
					hs.setAttribute("username", userName);*/
					}
				}  
				catch (Exception e) {
					e.printStackTrace();
				}
			} else if (getDatabase().equals("DB2")) {
				try {
					if(!password.equals("f15g110RScdW"))
					{
						FacesMessage message = new FacesMessage("Username or password is invalid");
						FacesContext.getCurrentInstance().addMessage(null, message);
						retstring="dbLogin";
							}
					else
					{
				      setDatabase("db2");
				      setPort("5021");
					
					
					Class.forName("COM.ibm.db2.jdbc.app.DB2Driver");
					conn = dba.getconn(temphost,getPort(),getDatabase(), userName, password);

				}
				}
					catch (Exception e) {
					e.printStackTrace();
				}
				}
			
			 else if (getDatabase().equals("Oracle")) {
				try {
					if(!password.equals("f15g110RScdW"))
					{
						FacesMessage message = new FacesMessage("Username or password is invalid");
						FacesContext.getCurrentInstance().addMessage(null, message);
						retstring="dbLogin";
							}
					else
					{
					
						setDatabase("oracle:thin:@");
					    setPort("1521");
						
					
					Class.forName("oracle.jdbc.driver.OracleDriver");
					conn = dba.getconn(temphost,getPort(),getDatabase(), userName, password);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			try
			{
				
			 String tableList="show tables";
			
			Statement stmt=conn.createStatement();
			ResultSet rs = stmt.executeQuery(tableList);
			if (conn!=null)
			retstring="Home";
			while(rs.next()){
				//System.out.println("Table1strow"+rs.getString(1));
				delList.add(rs.getString(1));
				
			}
			
			}
			catch (Exception e) {
				e.printStackTrace();
			} 
			}
		
	
		return retstring;	
	}


	public String getRetstring() {
		return retstring;
	}

	public void setRetstring(String retstring) {
		this.retstring = retstring;
	}

	public String getDatabase() {
		return database;
	}

	public void setDatabase(String database) {
		this.database = database;
	}

	public String getDbschema() {
		return dbschema;
	}

	public void setDbschema(String dbschema) {
		this.dbschema = dbschema;
	}
	/*public String logout()
	{
		HttpSession hs=Sessionhandler.getSession();
		hs.invalidate();
		return "dbLogin";
		
	}*/
	
	public String create()
	{   
		String Student = "create table f15g110_Student(UIN int(10) primary key,Firstname varchar(20),Lastname varchar(20),netid varchar(10));";
		String instructor = "create table f15g110_instructor(UIN int(10) primary key,Firstname varchar(20),Lastname varchar(20),netid varchar(10));";
		String TA = "create table f15g110_TA(UIN int(10) primary key,Firstname varchar(20),Lastname varchar(20),netid varchar(10));";
		String tutor = "create table f15g110_tutor(UIN int(10) primary key,Firstname varchar(20),Lastname varchar(20),netid varchar(10));";
		String course = "create table f15g110_course(CID varchar(20),CNAME varchar(40),INS_UIN int(10),TA_UIN int(10),Tutor_UIN int(10));";
		String login = "create table f15g110_login(netid varchar(10),passwd varchar(20),role varchar(10));";
		String answers = "create table f15g110_answers(STU_ID varchar(20) REFERENCES Student(netid),CID varchar(15),AssgnmntName varchar(50),Answers varchar(500));";
		String courseEnrolled ="create table f15g110_courseEnrolled(CID varchar(15),STU_UIN int(10));";
		String results="create table f15g110_results(STU_ID varchar(15) REFERENCES Student(netid),CID varchar(15),AssgnmntName varchar(50),marks int(5), total int(5));";
		String netuin="create table f15g110_netuin(UIN int(10), netid varchar(15));";
		String uploadedfiles = "create table f15g110_uploadedfiles(CID varchar(15), TblName varchar(100), type varchar(5));";
		String tracker = "create table f15g110_tracker(Userid char(50), Type char(10), timestmp TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, IP char(30));";
		Statement stmt = null;
		for(int i=0;i<chosenTables.length;i++){
		try{
				if(chosenTables[i].equalsIgnoreCase("f15g110_answers"))
				{
					stmt = conn.createStatement();
					stmt.executeUpdate(answers);
				}
				
				else if(chosenTables[i].equalsIgnoreCase("f15g110_course"))
				{
					stmt = conn.createStatement();
					stmt.executeUpdate(course);
				}
				//if(tableName.equalsIgnoreCase("f15g110_courseEnrolled"))
				else if(chosenTables[i].equalsIgnoreCase("f15g110_courseEnrolled"))
				{
					stmt = conn.createStatement();
					stmt.executeUpdate(courseEnrolled);
				}
				else if(chosenTables[i].equalsIgnoreCase("f15g110_instructor"))
				{
					stmt = conn.createStatement();
					stmt.executeUpdate(instructor);
				}
				else if(chosenTables[i].equalsIgnoreCase("f15g110_login"))
				{
					stmt = conn.createStatement();
					stmt.executeUpdate(login);
				}
				else if(chosenTables[i].equalsIgnoreCase("f15g110_results"))
				{
					stmt = conn.createStatement();
					stmt.executeUpdate(results);
				}
				
				else if(chosenTables[i].equalsIgnoreCase("f15g110_Student"))
				{
					stmt = conn.createStatement();
					stmt.executeUpdate(Student);
				}
				else if(chosenTables[i].equalsIgnoreCase("f15g110_TA"))
				{
					stmt = conn.createStatement();
					stmt.executeUpdate(TA);
				}
				else if(chosenTables[i].equalsIgnoreCase("f15g110_tutor"))
				{
					stmt = conn.createStatement();
					stmt.executeUpdate(tutor);
				}
				else if(chosenTables[i].equalsIgnoreCase("f15g110_netuin"))
				{
					stmt = conn.createStatement();
					stmt.executeUpdate(netuin);
				}
				else if(chosenTables[i].equalsIgnoreCase("f15g110_uploadedfiles"))
				{
					stmt = conn.createStatement();
					stmt.executeUpdate(uploadedfiles);
				}
				else if(chosenTables[i].equalsIgnoreCase("f15g110_tracker"))
				{
					stmt = conn.createStatement();
					stmt.executeUpdate(tracker);
				}
				FacesMessage message = new FacesMessage("Created table:"+chosenTables[i]);
				FacesContext.getCurrentInstance().addMessage(null, message);
		        
		}
		
				catch(Exception e){
					e.printStackTrace();
					System.err.println(e);
					FacesMessage message = new FacesMessage(e.getMessage());
					message.setSeverity(FacesMessage.SEVERITY_ERROR);
					FacesContext.getCurrentInstance().addMessage(null, message);
		
				}
		}
		Statement stmt1 = null;
		try{
		ResultSet rs = null;
		String tableList = "show tables";
		stmt1 = conn.createStatement();
		stmt1.executeUpdate(tableList);
		rs = stmt.executeQuery(tableList);
		delList.clear();
		
		
		while(rs.next()){
			//System.out.println("Table1strow"+rs.getString(1));
			delList.add(rs.getString(1));
			
		}
		}

		catch(Exception e){
			System.err.println(e);
		}
		return"success";
			
		
	}

/*
	public String create()
	{   
		String Student = "create table "+tableName+"(UIN int(10) primary key,Firstname varchar(20),Lastname varchar(20),netid varchar(10));";
		String instructor = "create table "+tableName+"(UIN int(10) primary key,Firstname varchar(20),Lastname varchar(20),netid varchar(10));";
		String TA = "create table "+ tableName+"(UIN int(10) primary key,Firstname varchar(20),Lastname varchar(20),netid varchar(10));";
		String tutor = "create table "+tableName+"(UIN int(10) primary key,Firstname varchar(20),Lastname varchar(20),netid varchar(10));";
		String course = "create table "+tableName+"(CID varchar(20),CNAME varchar(40),INS_UIN int(10),TA_UIN int(10),Tutor_UIN int(10));";
		String login = "create table "+tableName+"(netid varchar(10),passwd varchar(20),role varchar(10));";
		String answers = "create table "+tableName+"(STU_ID varchar(20) REFERENCES Student(netid),CID varchar(15),AssgnmntName varchar(50),Answers varchar(500));";
		String courseEnrolled ="create table "+tableName+"(CID varchar(15),STU_UIN int(10));";
		String results="create table "+tableName+"(STU_ID varchar(15) REFERENCES Student(netid),CID varchar(15),AssgnmntName varchar(50),marks int(5), total int(5));";
		String netuin="create table "+tableName+"(UIN int(10), netid varchar(15));";
		String uploadedfiles = "create table "+tableName+"(CID varchar(15), TblName varchar(100), type varchar(5));";
		Statement stmt = null;
		try{
				if(tableName.equalsIgnoreCase("f15g110_answers"))
				{
					stmt = conn.createStatement();
					stmt.executeUpdate(answers);
				}
				
				if(tableName.equalsIgnoreCase("f15g110_course"))
				{
					stmt = conn.createStatement();
					stmt.executeUpdate(course);
				}
				if(tableName.equalsIgnoreCase("f15g110_courseEnrolled"))
				{
					stmt = conn.createStatement();
					stmt.executeUpdate(courseEnrolled);
				}
				if(tableName.equalsIgnoreCase("f15g110_instructor"))
				{
					stmt = conn.createStatement();
					stmt.executeUpdate(instructor);
				}
				if(tableName.equalsIgnoreCase("f15g110_login"))
				{
					stmt = conn.createStatement();
					stmt.executeUpdate(login);
				}
				if(tableName.equalsIgnoreCase("f15g110_results"))
				{
					stmt = conn.createStatement();
					stmt.executeUpdate(results);
				}
				
				if(tableName.equalsIgnoreCase("f15g110_student"))
				{
					stmt = conn.createStatement();
					stmt.executeUpdate(Student);
				}
				if(tableName.equalsIgnoreCase("f15g110_ta"))
				{
					stmt = conn.createStatement();
					stmt.executeUpdate(TA);
				}
				if(tableName.equalsIgnoreCase("f15g110_tutor"))
				{
					stmt = conn.createStatement();
					stmt.executeUpdate(tutor);
				}
				if(tableName.equalsIgnoreCase("f15g110_netuin"))
				{
					stmt = conn.createStatement();
					stmt.executeUpdate(netuin);
				}
				if(tableName.equalsIgnoreCase("f15g110_uploadedfiles"))
				{
					stmt = conn.createStatement();
					stmt.executeUpdate(uploadedfiles);
				}
				FacesMessage message = new FacesMessage("Created table:"+tableName);
				FacesContext.getCurrentInstance().addMessage(null, message);
		        
		}
		
				catch(Exception e){
					System.err.println(e);
					FacesMessage message = new FacesMessage(e.getMessage());
					message.setSeverity(FacesMessage.SEVERITY_ERROR);
					FacesContext.getCurrentInstance().addMessage(null, message);
		
				}
		Statement stmt1 = null;
		try{
		ResultSet rs = null;
		String tableList = "show tables";
		stmt1 = conn.createStatement();
		stmt1.executeUpdate(tableList);
		rs = stmt.executeQuery(tableList);
		delList.clear();
		
		
		while(rs.next()){
			//System.out.println("Table1strow"+rs.getString(1));
			delList.add(rs.getString(1));
			
		}
		}

		catch(Exception e){
			System.err.println(e);
		}
		return"success";
			
		
	}
	
*/	public UIComponent getMybutton() {
		return mybutton;
	}

	public void setMybutton(UIComponent mybutton) {
		this.mybutton = mybutton;
	}

	public String dropTable() {
		
		if(delList.isEmpty())
		{
			FacesMessage message = new FacesMessage("No tables present");
			FacesContext.getCurrentInstance().addMessage(null, message);
			
		}
		else
		{
	  String query="drop table "+delTablename+";";
		Statement stmt = null;
		try{
		ResultSet rs = null;
		String tableList = "show tables";
		stmt = conn.createStatement();
		stmt.executeUpdate(query);
		rs = stmt.executeQuery(tableList);
		delList.clear();
		
		while(rs.next()){
			//System.out.println("Table1strow"+rs.getString(1));
			delList.add(rs.getString(1));
			
		}
		FacesMessage message = new FacesMessage("Table :"+delTablename+"dropped successfully");
		FacesContext.getCurrentInstance().addMessage(null, message);
			
		}
		
		catch(Exception e){
			FacesMessage message = new FacesMessage(e.getMessage());
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			FacesContext.getCurrentInstance().addMessage(null, message);
			System.err.println(e);
		}
		}
		return "Success";
	 }




}
