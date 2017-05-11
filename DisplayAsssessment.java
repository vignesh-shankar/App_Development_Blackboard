package f15g110;

import java.io.Serializable;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;




@ManagedBean
@ViewScoped

public class DisplayAsssessment implements Serializable{
	public boolean dbrender = false;
	public boolean dbrender1 = false;
	ArrayList<AssessmentBean> listFromDb= new ArrayList<AssessmentBean>();
	ArrayList<RosterBean> rosterList = new ArrayList<RosterBean>();
	ArrayList<String> fileList = new ArrayList<String>();
	ArrayList<String> roster = new ArrayList<String>();
	ArrayList<String> answerList = new ArrayList<String>();
	String answer=null;
	UploadFile uploadFile = new UploadFile();
	UploadRoster uploadRoster = new UploadRoster();
	private String selectedCourse;
	private String selectedFile;
	private String finalMsg;
	private boolean assmtRenderer = false;
	private boolean submitRenderer = false;
	public String getSelectedFile() {
		return selectedFile;
	}

	public void setSelectedFile(String selectedFile) {
		this.selectedFile = selectedFile;
	}

	public String getSelectedCourse() {
		return selectedCourse;
	}

	public void setSelectedCourse(String selectedCourse) {
		this.selectedCourse = selectedCourse;
	}

	public boolean isDbrender() {
		return dbrender;
	}

	public void setDbrender(boolean dbrender) {
		this.dbrender = dbrender;
	}

	public ArrayList<AssessmentBean> getListFromDb() {
		return listFromDb;
	}

	public void setListFromDb(ArrayList<AssessmentBean> listFromDb) {
		this.listFromDb = listFromDb;
	}

	public boolean isDbrender1() {
		return dbrender1;
	}

	public void setDbrender1(boolean dbrender1) {
		this.dbrender1 = dbrender1;
	}

	public ArrayList<RosterBean> getRosterList() {
		return rosterList;
	}

	public void setRosterList(ArrayList<RosterBean> rosterList) {
		this.rosterList = rosterList;
	}
	
	
	public ArrayList<String> getFileList() {
		return fileList;
	}

	public void setFileList(ArrayList<String> fileList) {
		this.fileList = fileList;
	}
	
	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
		answerList.add(answer);
	}

	
	public String getFinalMsg() {
		return finalMsg;
	}

	
	public boolean isAssmtRenderer() {
		return assmtRenderer;
	}

	public boolean isSubmitRenderer() {
		return submitRenderer;
	}
	
	
	public ArrayList<String> getRoster() {
		return roster;
	}
	
	
	public void generateList(){
		finalMsg = null;
		fileList.clear();
		DatabaseActions databaseActions = new DatabaseActions();
		String query = "select TblName from f15g110_uploadedFiles where CID = '"+selectedCourse+"' and type ='A';";
		ResultSet rs = databaseActions.execute(query);
		try{
		while(rs.next()){
			//System.out.println(rs.getString(1));
			fileList.add(rs.getString(1));
		}
		//System.out.println(fileList);
		assmtRenderer = true;
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void generateRosterList(){
		roster.clear();
		finalMsg = null;
		DatabaseActions databaseActions = new DatabaseActions();
		String query = "select TblName from f15g110_uploadedFiles where CID = '"+selectedCourse+"' and type ='R';";
		ResultSet rs = databaseActions.execute(query);
		try{
		while(rs.next()){
			//System.out.println(rs.getString(1));
			roster.add(rs.getString(1));
		}
		//System.out.println(roster);
		assmtRenderer = true;
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	public void fetchData(){
		//System.out.println(selectedCourse);
		//System.out.println(selectedFile);
		listFromDb.clear();
		DatabaseActions databaseActions = new DatabaseActions();
		//reset();
		ResultSet rs = databaseActions.execute("select * from f15g110_"+selectedCourse+"_"+selectedFile);
		try{
		while(rs.next()){
			AssessmentBean ab = new AssessmentBean();
			ab.setqNo(rs.getString(1));
			ab.setQuestion(rs.getString(2));
			ab.setAnswer(rs.getString(3));
			ab.setResult(rs.getString(4));
			listFromDb.add(ab);
			dbrender = true;
		}
	}
		catch(Exception e){
			
		}
	}
	public void fetchDataForStudent(){
		finalMsg=null;
		listFromDb.clear();
		answerList.clear();
		DatabaseActions databaseActions = new DatabaseActions();
		StudentBean sb = new StudentBean();
		ArrayList<String> assessmentsTaken = new ArrayList<String>();
		//reset();
		int flag = 0;
		ResultSet rs1 = databaseActions.execute("select AssgnmntName from f15g110_results where STU_ID = '"+sb.getNetId()+"' and CID = '"+selectedCourse+"';");
		try{
		while(rs1.next()){
			assessmentsTaken.add(rs1.getString(1));
		}
		String[] assessmentsArray = new String[assessmentsTaken.size()];
		for(int i=0;i<assessmentsTaken.size();i++){
			assessmentsArray[i] = assessmentsTaken.get(i);
			if(assessmentsArray[i].equals(selectedFile)){
				flag=1;
			}
		}
		}
		catch(Exception e){
		}
		if(flag==1){
			finalMsg = "You have already taken this assessment. Kindly view your scores in the View Scores section.";
		}
		else{
		try{
			
		ResultSet rs = databaseActions.execute("select * from f15g110_"+selectedCourse+"_"+selectedFile);
		
		while(rs.next()){
			AssessmentBean ab = new AssessmentBean();
			ab.setqNo(rs.getString(1));
			ab.setQuestion(rs.getString(2));
			ab.setAnswer(rs.getString(3));
			ab.setResult(rs.getString(4));
			listFromDb.add(ab);
			dbrender = true;
		}
		submitRenderer = true;
	}
		catch(Exception e){
			
		}
	}
	}
	public void reset(){
		AssessmentBean ab = new AssessmentBean();
		ab.setqNo(null);
		ab.setQuestion(null);
		ab.setAnswer(null);
		ab.setResult(null);
		selectedFile = null;
		dbrender = false;
		fileList.clear();
		assmtRenderer = false;
		submitRenderer = false;
	}
	
	public void reset1(){
		RosterBean rb = new RosterBean();
		rb.setLastName(null);
		rb.setFirstName(null);
		rb.setNetId(null);
		rb.setUIN(0);
		rb.setRole(null);
		rb.setStatus(null);
		dbrender1 = false;
		selectedFile = null;
		roster.clear();
		rosterList.clear();
	}
	
	public void fetchRoster(){
		rosterList.clear();
		DatabaseActions databaseActions = new DatabaseActions();
		//System.out.println("select * from f15g110_"+uploadRoster.getTempFileName1());
		ResultSet rs = databaseActions.execute("select * from f15g110_"+selectedCourse+"_"+selectedFile+";");
		try{
		while(rs.next()){
			RosterBean rb = new RosterBean();
			rb.setLastName(rs.getString(1));
			rb.setFirstName(rs.getString(2));
			rb.setNetId(rs.getString(3));
			rb.setUIN(Long.parseLong(rs.getString(4)));
			rb.setRole(rs.getString(5));
			rb.setStatus(rs.getString(6));
			rosterList.add(rb);
			dbrender1 = true;	
		}
		
	}
		catch(Exception e){
			
		}
	}
	public void submitAnswers(){
		//System.out.println(answerList);
		//LoginBean lb = new LoginBean();
		StudentBean sb = new StudentBean();
		//System.out.println(sb.getNetId());
		DatabaseActions databaseActions = new DatabaseActions();
		String answers = answerList.toString().substring(1, answerList.toString().length()-1);
		String insertQuery = "insert into f15g110_answers values('"+sb.getNetId()+"','"+selectedCourse+"','"+selectedFile+"','"+answers+"');";
		databaseActions.update(insertQuery);
		computeResult();
		reset();
		finalMsg = "Thank you! Your responses have been recorded. You can view your results by navigating to View Results Tab and selecting the respective course and assessment name";
	}
	
	public void computeResult(){
		int totalMarks = 0;
		int totalQuestions = 0;
		
		StudentBean sb = new StudentBean();
		DatabaseActions databaseActions = new DatabaseActions();
		String allAnswers = null;
		String extractAnswer = "select Answers from f15g110_answers where STU_ID = '"+sb.getNetId()+"' and AssgnmntName = '"+selectedFile+"';";
		ResultSet rs = databaseActions.execute(extractAnswer);
		try{
		while(rs.next()){
			allAnswers = rs.getString(1);
		}
		ArrayList<String> ansType = new ArrayList<String>();
		String extractType = "select Result from f15g110_"+selectedCourse+"_"+selectedFile+";";
		ResultSet rs2 = databaseActions.execute(extractType);
		while(rs2.next()){
			ansType.add(rs2.getString(1));
		}
		//System.out.println(" answer Types:"+ ansType);
		String[] answers = allAnswers.split(",");
		ArrayList<String> correctAnswers = new ArrayList<String>();
		String extractCorrectAnswer = "select Answer from f15g110_"+selectedCourse+"_"+selectedFile+";";
		ResultSet rs1 = databaseActions.execute(extractCorrectAnswer);
		while(rs1.next()){
			correctAnswers.add(rs1.getString(1));
			totalQuestions = totalQuestions + 1;
			//System.out.println(totalQuestions);
		}
		String correctAnswersArray[] = new String[correctAnswers.size()];
		String ansTypeArray[] = new String[correctAnswers.size()];
		for(int i=0;i<correctAnswers.size();i++){
			ansTypeArray[i] = ansType.get(i);
			}
		//System.out.println("size daa"+correctAnswers.size());
		for(int i=0;i<correctAnswers.size();i++){
			correctAnswersArray[i] = correctAnswers.get(i);
			if(!ansTypeArray[i].equals("null")){
				Double correctAns = Double.parseDouble(correctAnswersArray[i]);
				//System.out.println(answers[i].isEmpty()+"length"+answers[i].length());
				if(!answers[i].equals(" ")) 
						{
						//answers[i].isEmpty()){
				Double enteredAns = Double.parseDouble(answers[i]);
				if(correctAns.equals(enteredAns)){
					totalMarks = totalMarks+1;
				}
			}
			}
			else{
			if((answers[i].trim()).equalsIgnoreCase(correctAnswersArray[i])){
				totalMarks = totalMarks+1;
			}
			}
		}
		
		
		String insertMarks = "insert into f15g110_results values('"+sb.getNetId()+"','"+selectedCourse+"','"+selectedFile+"',"+totalMarks+","+totalQuestions+");";
		databaseActions.update(insertMarks);
		
		}
		catch(Exception e){
			e.printStackTrace();
		}
		}

	/*public void computeResult(){
		int totalMarks = 0;
		int totalQuestions = 0;
		StudentBean sb = new StudentBean();
		DatabaseActions databaseActions = new DatabaseActions();
		String allAnswers = null;
		String extractAnswer = "select Answers from f15g110_answers where STU_ID = '"+sb.getNetId()+"' and AssgnmntName = '"+selectedFile+"';";
		ResultSet rs = databaseActions.execute(extractAnswer);
		try{
		while(rs.next()){
			allAnswers = rs.getString(1);
		}
		String[] answers = allAnswers.split(",");
		ArrayList<String> correctAnswers = new ArrayList<String>();
		String extractCorrectAnswer = "select Answer from f15g110_"+selectedCourse+"_"+selectedFile+";";
		ResultSet rs1 = databaseActions.execute(extractCorrectAnswer);
		while(rs1.next()){
			correctAnswers.add(rs1.getString(1));
			totalQuestions = totalQuestions + 1;
			//System.out.println(totalQuestions);
		}
		String correctAnswersArray[] = new String[correctAnswers.size()];
		for(int i=0;i<correctAnswers.size();i++){
			correctAnswersArray[i] = correctAnswers.get(i);
			//if(Integer.parseInt((answers[i].trim())).equalsIgnoreCase(correctAnswersArray[i])){
			if(Integer.parseInt((answers[i].trim()))==Integer.parseInt((correctAnswersArray[i]))){
				totalMarks = totalMarks+1;
			}
		}
		String insertMarks = "insert into f15g110_results values('"+sb.getNetId()+"','"+selectedCourse+"','"+selectedFile+"',"+totalMarks+","+totalQuestions+");";
		databaseActions.update(insertMarks);
		
		}
		catch(Exception e){
			e.printStackTrace();
		}
		}
*/}
