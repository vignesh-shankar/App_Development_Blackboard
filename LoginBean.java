package f15g110;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;

@ManagedBean 
@SessionScoped
public class LoginBean implements Serializable{
private String userName;
private String password;
private String passwd;
private String role;
private String finalMsg;
private String errorMsg = null;
private String userType;
private List<String> courseList = new ArrayList<String>(); 
private List<String> courseListForStudents = new ArrayList<String>();  
private List<TrackingBean> trackList = new ArrayList<TrackingBean>();
private boolean trackrender=false;
 public List<TrackingBean> getTrackList() {
	return trackList;
}

public void setTrackList(List<TrackingBean> trackList) {
	this.trackList = trackList;
}

public boolean isTrackrender() {
	return trackrender;
}

public void setTrackrender(boolean trackrender) {
	this.trackrender = trackrender;
}

public LoginBean(){
	 
 }
 
 @PostConstruct
 public void init(){
 	
 }

 
public List<String> getCourseList() {
	return courseList;
}

public void setCourseList(List<String> courseList) {
	this.courseList = courseList;
}

public void setUserName(String userName) {
	this.userName = userName;
}

public void setPassword(String password) {
	this.password = password;
}

public String getUserName() {
	return userName;
}

public String getPassword() {
	return password;
}


public List<String> getCourseListForStudents() {
	return courseListForStudents;
}

public void setCourseListForStudents(List<String> courseListForStudents) {
	this.courseListForStudents = courseListForStudents;
}

public String getErrorMsg() {
	return errorMsg;
}




public String login() throws UnknownHostException{
	
	InetAddress ipAddr = InetAddress.getLocalHost();
	//System.out.println("IP Address"+ipAddr.getHostAddress());
	DatabaseActions databaseActions = new DatabaseActions();
	try{
	String query1 = "select passwd from f15g110_login where netid = '"+getUserName()+"';";
	String query2 = "select role from f15g110_login where netid = '"+getUserName()+"';";
	//System.out.println(query1);
	//System.out.println(query2);
	ResultSet rs = databaseActions.execute(query1);
	////System.out.println(rs.getString(1));
	ResultSet rs1 = databaseActions.execute(query2);
	////System.out.println(rs1.getString(1));
	while(rs.next()){
		passwd = rs.getString(1);
		//System.out.println(passwd);
	}
	while(rs1.next()){
		role = rs1.getString(1);
		//System.out.println(role);
	}
	if(passwd.equals(password)){
		String querytrack="insert into f15g110_tracker (Userid,Type,IP) values('"+getUserName()+"','"+role+"','"+ipAddr.getHostAddress()+"');";
      //String querytrack="insert into f15g110_tracker (Userid,Type,IP) values("+getUserName()+",'"+role+"','"+ipAddr.getHostAddress()+"');";
		databaseActions.update(querytrack);
		if(role.equals("STU")){
			String query3 = "select s.* from f15g110_student s, f15g110_login l where l.netid= '"+userName+"' and s.netid=l.netid;"; 
			ResultSet rs2 = databaseActions.execute(query3);
			StudentBean studentBean = new StudentBean();
			while(rs2.next()){
				studentBean.setUIN(Long.parseLong(rs2.getString(1)));
				studentBean.setfName(rs2.getString(2));
				studentBean.setlName(rs2.getString(3));
				studentBean.setNetId(rs2.getString(4));	
				String courseQuery = "select CID from f15g110_courseEnrolled where STU_UIN="+studentBean.getUIN();
				courseListForStudents(courseQuery);
			}
			finalMsg = "STUDENT";
		}
		else if(role.equals("INS")){
			String query4 = "select i.* from f15g110_instructor i, f15g110_login l where l.netid= '" +userName+ "'  and i.netid=l.netid;"; 
			ResultSet rs3 = databaseActions.execute(query4);
			ProfessorBean professorBean = new ProfessorBean();
			while(rs3.next()){
				professorBean.setUIN(Long.parseLong(rs3.getString(1)));
				professorBean.setfName(rs3.getString(2));
				professorBean.setlName(rs3.getString(3));
				professorBean.setNetId(rs3.getString(4));
				String courseQuery = "select CID from f15g110_course where INS_UIN="+professorBean.getUIN();
				courseList(courseQuery);
				String courseQuery1 = "select CID from f15g110_courseEnrolled where STU_UIN="+professorBean.getUIN();
				courseListForStudents(courseQuery1);
			}
			finalMsg = "Instructor";
			
		}
		else if(role.equals("TA")){
			String query4 = "select t.* from f15g110_ta t, f15g110_login l where l.netid= '" +userName+ "'  and t.netid=l.netid;"; 
			ResultSet rs4 = databaseActions.execute(query4);
			TABean tABean = new TABean();
			while(rs4.next()){
				tABean.setUIN(Long.parseLong(rs4.getString(1)));
				tABean.setfName(rs4.getString(2));
				tABean.setlName(rs4.getString(3));
				tABean.setNetId(rs4.getString(4));
				String courseQuery1 = "select CID from f15g110_course where ta_UIN="+tABean.getUIN();
				courseList(courseQuery1);
				String courseQuery2 = "select CID from f15g110_courseEnrolled where STU_UIN="+tABean.getUIN();
				courseListForStudents(courseQuery2);
			}
			finalMsg = "TA";
		}
		else if(role.equals("Tut")){
			String query5 = "select t.* from f15g110_tutor t, f15g110_login l where l.netid= '" +userName+ "'  and t.netid=l.netid;"; 
			ResultSet rs5 = databaseActions.execute(query5);
			TutorBean tutorBean = new TutorBean();
			while(rs5.next()){
				tutorBean.setUIN(Long.parseLong(rs5.getString(1)));
				tutorBean.setfName(rs5.getString(2));
				tutorBean.setlName(rs5.getString(3));
				tutorBean.setNetId(rs5.getString(4));
				String courseQuery2 = "select CID from f15g110_course where tutor_UIN="+tutorBean.getUIN();
				courseList(courseQuery2);
			}
			finalMsg = "Tutor";
		}
		else if(role.equals("DBA")){
			finalMsg = "dbactions";
			}
			
		}
	
	else 
	{
		errorMsg = "The user name and password entered do not match. \n Kindly verify whether you have the user name entered correctly \n If user name is correct, check the password. Check whether your capslock is on.";
		return "FAIL";
	}
	}
	catch(Exception e){
		errorMsg = "The user name and password entered do not match. \n Kindly verify whether you have the user name entered correctly \n If user name is correct, check the password. Check whether your capslock is on.";
		return "FAIL";
	}
	return finalMsg;
}
public void courseList(String query){
	courseList.clear();
	DatabaseActions databaseActions = new DatabaseActions();
	ResultSet rs = databaseActions.execute(query);
	try{
	while(rs.next()){
		courseList.add(rs.getString(1));
	}
	}
	catch(Exception e){
		e.printStackTrace();
	}
}
public void courseListForStudents(String query){
	courseListForStudents.clear();
	DatabaseActions databaseActions = new DatabaseActions();
	ResultSet rs = databaseActions.execute(query);
	try{
	while(rs.next()){
		courseListForStudents.add(rs.getString(1));
	}
	}
	catch(Exception e){
		e.printStackTrace();
	}
}
public String viewtrack()
{
	
	DatabaseActions databaseActions = new DatabaseActions();
	String tracker = "select * from f15g110_tracker;";
	ResultSet rs = databaseActions.execute(tracker);
	try{
	while(rs.next()){
		TrackingBean track=new TrackingBean();
		track.setUserID(rs.getString(1));
		track.setType(rs.getString(2));
		track.setTimestamp(rs.getString(3));
		track.setIp(rs.getString(4));
		trackList.add(track);
		
	}
	setTrackrender(true);
	
	}
	catch(Exception e){
		e.printStackTrace();
	}
	
	return "viewtrack";
	
}
public String userType(){
	DatabaseActions databaseActions = new DatabaseActions();
	String fetchUserType = "select role from f15g110_login where netid='"+userName+"';";
	ResultSet rs = databaseActions.execute(fetchUserType);
	try{
	while(rs.next()){
		userType = rs.getString(1);
	}
	}
	catch(Exception e){
		e.printStackTrace();
	}
	return userType;
}
public void close(){
	DatabaseActions databaseActions = new DatabaseActions();
	try{
	databaseActions.conn.close();
	}
	catch(Exception e){
		e.printStackTrace();
	}
}
}
