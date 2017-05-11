package f15g110;

import java.io.Serializable;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@SessionScoped
public class Registration implements Serializable{
	public Registration(){
		
	}
	ArrayList<String> courseList = new ArrayList<String>();
	private String[] coursesRegistered;
	private String finalMsg=null;
	private String role = null;
	private String selectedCourse;
	private long profId;
	private long taId;
	private long tutorId;
	private ArrayList<Long> professorList = new ArrayList<Long>();
	private ArrayList<Long> taList = new ArrayList<Long>();
	private ArrayList<Long> tutorList = new ArrayList<Long>();
	public ArrayList<String> getCourseList() {
		return courseList;
	}
	
	public void setCourseList(ArrayList<String> courseList) {
		this.courseList = courseList;
	}	
	
	
public String[] getCoursesRegistered() {
		return coursesRegistered;
	}

	public void setCoursesRegistered(String[] coursesRegistered) {
		this.coursesRegistered = coursesRegistered;
	}

	
	public String getFinalMsg() {
		return finalMsg;
	}
	
	
public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
	

public String getSelectedCourse() {
		return selectedCourse;
	}

	public void setSelectedCourse(String selectedCourse) {
		this.selectedCourse = selectedCourse;
	}



public long getProfId() {
		return profId;
	}

	public void setProfId(long profId) {
		this.profId = profId;
	}

	public long getTaId() {
		return taId;
	}

	public void setTaId(long taId) {
		this.taId = taId;
	}

	public long getTutorId() {
		return tutorId;
	}

	public void setTutorId(long tutorId) {
		this.tutorId = tutorId;
	}
	
public ArrayList<Long> getProfessorList() {
		return professorList;
	}

	public ArrayList<Long> getTaList() {
		return taList;
	}

	public ArrayList<Long> getTutorList() {
		return tutorList;
	}

public void generateCourseList(){
	courseList.clear();
	DatabaseActions databaseActions = new DatabaseActions();
	String fetchCourse = "select CID from f15g110_course;";
	ResultSet rs = databaseActions.execute(fetchCourse);
	try{
	while(rs.next()){
		courseList.add(rs.getString(1));
	}
	}
	catch(Exception e){
		e.printStackTrace();
	}
}
public String generateLists(){
	professorList.clear();
	taList.clear();
	tutorList.clear();
	DatabaseActions databaseActions = new DatabaseActions();
	String returnVar = null;
	try{
	ResultSet rs1= databaseActions.execute("select UIN from f15g110_instructor;");
	while(rs1.next()){
		professorList.add(rs1.getLong(1));
	}
	ResultSet rs2 = databaseActions.execute("select UIN from f15g110_ta;");
	while(rs2.next()){
		taList.add(rs2.getLong(1));
	}
	ResultSet rs3 = databaseActions.execute("select UIN from f15g110_tutor;");
	while(rs3.next()){
		tutorList.add(rs3.getLong(1));
	}
	returnVar = "SUCCESS";
	}
	catch(Exception e){
		e.printStackTrace();
	}
	return returnVar;
}
public void registerStudent(){
	int flag = 0;
	int flag1 = 0;
	StudentBean sb = new StudentBean();
	DatabaseActions da = new DatabaseActions();
	//System.out.println(sb.getfName());
	//System.out.println(sb.getlName());
	//System.out.println(sb.getNetId());
	//System.out.println(sb.getUIN());
	try{
	String selectQuery = "select UIN,netid from f15g110_netuin;";
	ResultSet rs1 = da.execute(selectQuery);
	while(rs1.next()){
		if(rs1.getString(1).equals(sb.getUIN()) || rs1.getString(2).equals(sb.getNetId())){
			flag = 1;
			//finalMsg = "The given UIN and NetID pair already exists.";
			FacesMessage message = new FacesMessage("The given UIN and NetID pair already exists.");
			FacesContext.getCurrentInstance().addMessage(null, message);
			break;
		}
	}
	}
	catch(Exception e){
		//System.out.println("Inside first catch");
	e.printStackTrace();
	}
	if(flag == 0){
	String getCourse = "select CID from f15g110_courseEnrolled where STU_UIN = "+sb.getUIN()+";";
	ResultSet rs = da.execute(getCourse);
	try{
	while(rs.next()){
		for(int i=0;i<coursesRegistered.length;i++){
			if(rs.getString(1).equalsIgnoreCase(coursesRegistered[i])){
				//finalMsg = "The course "+coursesRegistered[i]+"has already been registered for this particular user.";
				FacesMessage message = new FacesMessage("The course "+coursesRegistered[i]+"has already been registered for this particular user.");
				FacesContext.getCurrentInstance().addMessage(null, message);
				flag1 = 1;
				break;
			}
			
		}
	}
	if(flag1==0){
		String insertQuery1 = "insert into f15g110_student values("+sb.getUIN()+",'"+sb.getfName()+"','"+sb.getlName()+"','"+sb.getNetId()+"');";
		String insertQuery2 = "insert into f15g110_login values('"+sb.getNetId()+"','"+sb.getPassword()+"','STU');";
		String insertQuery4 = "insert into f15g110_netuin values("+sb.getUIN()+",'"+sb.getNetId()+"');";
		da.update(insertQuery1);
		da.update(insertQuery2);
		da.update(insertQuery4);
		for(int i=0;i<coursesRegistered.length;i++){
		String insertQuery3 = "insert into f15g110_courseEnrolled values('"+coursesRegistered[i]+"',"+sb.getUIN()+");";
		da.update(insertQuery3);
		}
		//finalMsg = "Student Registered";
		FacesMessage message = new FacesMessage("Student Registered");
		FacesContext.getCurrentInstance().addMessage(null, message);
	}
	
	}
	catch(Exception e){
		e.printStackTrace();
	}

	}
	reset();
	
}
public void registerOthers(){
	int flag = 0;
	int flag1 = 0;
	StudentBean sb = new StudentBean();
	DatabaseActions da = new DatabaseActions();
	if(role.equals("INS")){
		try{
			String selectQuery = "select UIN,netid from f15g110_netuin;";
			ResultSet rs1 = da.execute(selectQuery);
			while(rs1.next()){
				if(rs1.getString(1).equals(sb.getUIN()) || rs1.getString(2).equals(sb.getNetId())){
					flag = 1;
					//finalMsg = "The given UIN and NetID pair already exists.";
					FacesMessage message = new FacesMessage("The given UIN and NetID pair already exists.");
					FacesContext.getCurrentInstance().addMessage(null, message);
					break;
				}
			}
			if(flag == 0){
				String insertQuery1 = "insert into f15g110_instructor values("+sb.getUIN()+",'"+sb.getfName()+"','"+sb.getlName()+"','"+sb.getNetId()+"');";
				String insertQuery2 = "insert into f15g110_login values('"+sb.getNetId()+"','"+sb.getPassword()+"','INS');";
				String insertQuery4 = "insert into f15g110_netuin values("+sb.getUIN()+",'"+sb.getNetId()+"');";
				
				da.update(insertQuery1);
				da.update(insertQuery2);
				da.update(insertQuery4);
				//finalMsg = "Instructor Registered";
				FacesMessage message = new FacesMessage("Instructor Registered");
				FacesContext.getCurrentInstance().addMessage(null, message);
			}
			
		}
			catch(Exception e){
				e.printStackTrace();
			}

			}
	
	else if(role.equals("TA")){
		try{
			String selectQuery = "select UIN,netid from f15g110_netuin;";
			ResultSet rs1 = da.execute(selectQuery);
			while(rs1.next()){
				if(rs1.getString(1).equals(sb.getUIN()) || rs1.getString(2).equals(sb.getNetId())){
					flag = 1;
					//finalMsg = "The given UIN and NetID pair already exists.";
					FacesMessage message = new FacesMessage("The given UIN and NetID pair already exists.");
					FacesContext.getCurrentInstance().addMessage(null, message);
					break;
				}
			}
			if(flag == 0){
				String insertQuery1 = "insert into f15g110_ta values("+sb.getUIN()+",'"+sb.getfName()+"','"+sb.getlName()+"','"+sb.getNetId()+"');";
				String insertQuery2 = "insert into f15g110_login values('"+sb.getNetId()+"','"+sb.getPassword()+"','TA');";
				String insertQuery4 = "insert into f15g110_netuin values("+sb.getUIN()+",'"+sb.getNetId()+"');";
				da.update(insertQuery1);
				da.update(insertQuery2);
				da.update(insertQuery4);
				//finalMsg = "TA Registered";
				FacesMessage message = new FacesMessage("TA Registered");
				FacesContext.getCurrentInstance().addMessage(null, message);
			}
			
		}
			catch(Exception e){
				e.printStackTrace();
			}
	}
	else
	{
		try{
			String selectQuery = "select UIN,netid from f15g110_netuin;";
			ResultSet rs1 = da.execute(selectQuery);
			while(rs1.next()){
				if(rs1.getString(1).equals(sb.getUIN()) || rs1.getString(2).equals(sb.getNetId())){
					flag = 1;
					//finalMsg = "The given UIN and NetID pair already exists.";
					FacesMessage message = new FacesMessage("The given UIN and NetID pair already exists.");
					FacesContext.getCurrentInstance().addMessage(null, message);
					break;
				}
			}
			if(flag == 0){
				String insertQuery1 = "insert into f15g110_tutor values("+sb.getUIN()+",'"+sb.getfName()+"','"+sb.getlName()+"','"+sb.getNetId()+"');";
				String insertQuery2 = "insert into f15g110_login values('"+sb.getNetId()+"','"+sb.getPassword()+"','Tut');";
				String insertQuery4 = "insert into f15g110_netuin values("+sb.getUIN()+",'"+sb.getNetId()+"');";
				da.update(insertQuery1);
				da.update(insertQuery2);
				da.update(insertQuery4);
				//finalMsg = "Tutor Registered";
				FacesMessage message = new FacesMessage("Tutor Registered");
				FacesContext.getCurrentInstance().addMessage(null, message);
			}
			
		}
			catch(Exception e){
				e.printStackTrace();
			}
	}
	reset();
}
public void registerCourses(){
	finalMsg = null;
	DatabaseActions da = new DatabaseActions();
	int flag = 0;
	String fetchCourse = "select CID from f15g110_course;";
	ResultSet rs = da.execute(fetchCourse);
	try{
	while(rs.next()){
		if(rs.getString(1).equals(selectedCourse)){
			//finalMsg = "The course has already been registered";
			FacesMessage message = new FacesMessage("The course has already been registered");
			FacesContext.getCurrentInstance().addMessage(null, message);
			flag = 1;
		}
	}
	if(flag==0){
		String insertQuery = "insert into f15g110_course values('"+selectedCourse+"','null',"+profId+","+taId+","+tutorId+");";
		String insertQuery1 = "insert into f15g110_courseenrolled values('"+selectedCourse+"',"+profId+");";
		String insertQuery2 = "insert into f15g110_courseenrolled values('"+selectedCourse+"',"+taId+");";
		da.update(insertQuery);
		da.update(insertQuery1);
		da.update(insertQuery2);
		//finalMsg = "The course "+selectedCourse+" is registered for the respective instructor, TA and Tutor";
		FacesMessage message = new FacesMessage("The course "+selectedCourse+" is registered for the respective instructor, TA and Tutor");
		FacesContext.getCurrentInstance().addMessage(null, message);
	}
	
	}
	catch(Exception e){
		e.printStackTrace();
	}
	reset();
}
public void reset(){
	StudentBean sb = new StudentBean();
	sb.setfName(null);
	sb.setlName(null);
	sb.setNetId(null);
	sb.setUIN(0);
	finalMsg = null;
}
}
