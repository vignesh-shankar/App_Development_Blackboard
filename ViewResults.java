package f15g110;

import java.io.Serializable;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
@ManagedBean
@SessionScoped
public class ViewResults implements Serializable{
private ArrayList<String> assessmentList = new ArrayList<String>();
private String selectedAssessment;
private String selectedCourse;
private String totalScore;
private int totalMarks = 0;
private String outOf;
private boolean assessmentRenderer = false;
private boolean resultRenderer = false;
public int getTotalMarks() {
	return totalMarks;
}

public void setTotalMarks(int totalMarks) {
	this.totalMarks = totalMarks;
}

public String getSelectedCourse() {
	return selectedCourse;
}

public void setSelectedCourse(String selectedCourse) {
	this.selectedCourse = selectedCourse;
}

public ViewResults(){
	
}
public ArrayList<String> getAssessmentList() {
	return assessmentList;
}

public void setAssessmentList(ArrayList<String> assessmentList) {
	this.assessmentList = assessmentList;
}

public String getSelectedAssessment() {
	return selectedAssessment;
}

public void setSelectedAssessment(String selectedAssessment) {
	this.selectedAssessment = selectedAssessment;
}


public String getTotalScore() {
	return totalScore;
}


public boolean isAssessmentRenderer() {
	return assessmentRenderer;
}

public boolean isResultRenderer() {
	return resultRenderer;
}


public String getOutOf() {
	return outOf;
}

public void setOutOf(String outOf) {
	this.outOf = outOf;
}

public void generateList(){
	assessmentList.clear();
	StudentBean sb = new StudentBean();
	DatabaseActions databaseActions = new DatabaseActions();
	String query = "select AssgnmntName from f15g110_answers where STU_ID = '"+sb.getNetId()+"' and CID = '"+selectedCourse+"';";
	ResultSet rs = databaseActions.execute(query);
	try{
	while(rs.next()){
		assessmentList.add(rs.getString(1));
	}
	assessmentRenderer = true;
	}
	catch(Exception e){
		e.printStackTrace();
	}
}
public void computeResult(){
	
	/*StudentBean sb = new StudentBean();
	DatabaseActions databaseActions = new DatabaseActions();
	String allAnswers = null;
	String extractAnswer = "select Answers from answers where STU_ID = '"+sb.getNetId()+"' and AssgnmntName = '"+selectedAssessment+"';";
	ResultSet rs = databaseActions.execute(extractAnswer);
	try{
	while(rs.next()){
		allAnswers = rs.getString(1);
	}
	String[] answers = allAnswers.split(",");
	ArrayList<String> correctAnswers = new ArrayList<String>();
	String extractCorrectAnswer = "select Answer from "+selectedCourse+"_"+selectedAssessment+";";
	ResultSet rs1 = databaseActions.execute(extractCorrectAnswer);
	while(rs1.next()){
		correctAnswers.add(rs1.getString(1));
	}
	String correctAnswersArray[] = new String[correctAnswers.size()];
	for(int i=0;i<correctAnswers.size();i++){
		correctAnswersArray[i] = correctAnswers.get(i);
		if((answers[i].trim()).equalsIgnoreCase(correctAnswersArray[i])){
			totalMarks = totalMarks+1;
		}
	}
	String insertMarks = "insert into results values('"+sb.getNetId()+"','"+selectedCourse+"','"+selectedAssessment+"',"+totalMarks+"');";
	databaseActions.update(insertMarks);
	
	}
	catch(Exception e){
		e.printStackTrace();
	}*/
	StudentBean sb = new StudentBean();
	DatabaseActions databaseActions = new DatabaseActions();
	String fetchMarks = "select marks,total from f15g110_results where STU_ID = '"+sb.getNetId()+"' and CID = '"+selectedCourse+"' and AssgnmntName ='"+selectedAssessment+"';";
	ResultSet rs = databaseActions.execute(fetchMarks);
	try{
		while(rs.next()){
			totalScore = rs.getString(1);
			outOf = rs.getString(2);
		}
		resultRenderer = true;
	}
	catch(Exception e){
		e.printStackTrace();
	}
}
public void reset(){
	assessmentRenderer = false;
	resultRenderer = false;
}
}
