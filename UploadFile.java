package f15g110;
import java.util.ArrayList;
import java.util.Scanner;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.apache.myfaces.custom.fileupload.UploadedFile;
import org.apache.myfaces.util.FilenameUtils;
//import org.apache.tomcat.util.http.fileupload.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
@ManagedBean
@SessionScoped
public class UploadFile implements Serializable{
	private UploadedFile uploadedFile;
	private String fileLabel;
	private String fileName;
	private long fileSize;
	private String fileContentType;
	private String uploadedFileContents;
	private String tempFileName;
	private String tempFileName1;
	String[] tempFileArr = new String[2];
	private String courseId;
	private String finalMsg = null;
	private String successMsg = null;
	private String error = null;
	public UploadFile()
	{
		
	}
	public void init(){
		
	}
	
	public String getTempFileName1() {
		return tempFileName1;
	}

	public UploadedFile getUploadedFile() {
		return uploadedFile;
	}
	public void setUploadedFile(UploadedFile uploadedFile) {
		this.uploadedFile = uploadedFile;
	}
	public String getFileLabel() {
		return fileLabel;
	}
	public void setFileLabel(String fileLabel) {
		this.fileLabel = fileLabel;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public long getFileSize() {
		return fileSize;
	}
	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}
	public String getFileContentType() {
		return fileContentType;
	}
	public void setFileContentType(String fileContentType) {
		this.fileContentType = fileContentType;
	}
	public String getUploadedFileContents() {
		return uploadedFileContents;
	}
	public void setUploadedFileContents(String uploadedFileContents) {
		this.uploadedFileContents = uploadedFileContents;
	}
	
	public String getCourseId() {
		return courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}
	
	public String getFinalMsg() {
		return finalMsg;
	}
	
	
	public String getSuccessMsg() {
		return successMsg;
	}
	
	
	public String getError() {
		return error;
	}
	public String processFileUpload() throws IOException, SQLException, ClassNotFoundException {
		File tempFile = null;
		finalMsg = null;
		successMsg = null;
		//System.out.println("File type: " + uploadedFile.getContentType());
        //System.out.println("File name: " + uploadedFile.getName());
        //System.out.println("File size: " + uploadedFile.getSize() + " bytes");
	uploadedFileContents = null;
	//tempFile = File.createTempFile(uploadedFile.getName(),".", new File("c:/upload"));
    //output = new FileOutputStream(file);
	FacesContext context =FacesContext.getCurrentInstance();
	String path =context.getExternalContext().getRealPath("/temp");
	//System.out.println(path);
	FileOutputStream fos = null;
	try {
	fileName = uploadedFile.getName();
	fileSize = uploadedFile.getSize();
	String value1[]=fileName.split("\\\\");
	int l=value1.length-1;
	//System.out.println("filename only"+value1[l]);
	fileContentType = uploadedFile.getContentType();
	//System.out.println("Filename"+fileName);
	uploadedFileContents = new // in memory
	String(uploadedFile.getBytes());
	////System.out.println("Contents of file:"+uploadedFileContents);
	//String lines[] = uploadedFileContents.split("\\r?\\n");
	////System.out.println("first line"+lines[1]);
	tempFile = new File(path + "/" +value1[l]);
	tempFileName = value1[l];
	//System.out.println(tempFileName);
	tempFileArr = tempFileName.split("\\.");
	tempFileName1 = tempFileArr[0];
	fos = new FileOutputStream(tempFile);
	// or uploaded to disk
	fos.write(uploadedFile.getBytes());
	fos.close();
	databaseUpdate();
	
	}
	 // end try
	catch (IOException e) {
		//System.out.println(e.getMessage());
		error = e.getMessage();
	} // end catch
	return "Success";
	} // end processFileUpload()
	
	public void databaseUpdate() throws SQLException, ClassNotFoundException
	{ 
		int firstFlag = 0;
		int secondFlag = 0;
		String lines[] = uploadedFileContents.split("\\r?\\n");
		DatabaseActions databaseActions=new DatabaseActions();
	    //Connection conn=databaseActions.connect();
	    //System.out.println("After connection");
	    //System.out.println(courseId);
	    if(fileLabel.isEmpty()){
	    	try{
	    		ArrayList<String> existingFiles = new ArrayList<String>();
	    		String fileNameQuery = "select TblName from f15g110_uploadedFiles where CID = '"+courseId+"' and type = 'A';";
	    		ResultSet rs = databaseActions.execute(fileNameQuery);	
	    		while(rs.next())
	    		{
	    			existingFiles.add(rs.getString(1));
	    		}
	    		for(int i=0;i<existingFiles.size();i++){
	    			if(existingFiles.get(i).equals(tempFileName1)){
	    					firstFlag = 1;
	    					finalMsg = "A file with the same name has already been uploaded for this Course. Try giving a file label or upload with a different file name.";
	    					break;
	    			}
	    		}
	    	}
	    	catch(Exception e){
	    		error = e.getMessage();
	    		e.printStackTrace();
	    	}
	    	if(firstFlag == 0){
	    		try{
	    		String createQuery = "create table f15g110_"+courseId+"_"+tempFileName1+"(QNO varchar(10),Question varchar(1000),Answer varchar(500),Result varchar(20));";
	    		databaseActions.update(createQuery);
	    	}
	    	catch(Exception e){
	    		error = e.getMessage();
	    		e.printStackTrace();
	    	}
	    String insertQuery = "insert into f15g110_uploadedFiles values('"+courseId+"','"+tempFileName1+"','A');";
	    databaseActions.update(insertQuery);
        int k=1;
        int length=lines.length;
        while(k<length)
        {
        if(lines[k].contains("\""))
		 {
			String tsamp="null";
			 String value1[]=lines[k].split(",");
					 String sample[]=lines[k].split("\"");
					 String sample2[]=sample[1].split(",");
					 int l=sample2.length-1;
			 //System.out.println(value1[0]+" "+sample[1]+" "+value1[2+l]+" "+"null");
			 String sql="INSERT INTO f15g110_"+courseId+"_"+tempFileName1+ " VALUES(?,?,?,?);";
			 PreparedStatement preparedStatement = databaseActions.conn.prepareStatement(sql);
			 preparedStatement.setString(1, value1[0]);
			 preparedStatement.setString(2, sample[1]);
			 preparedStatement.setString(3, value1[2+l]);
			 preparedStatement.setString(4, "null");
        	 preparedStatement.executeUpdate();
			 //String query = "insert into " +tempFileName1+" values('"+Integer.parseInt(value1[0])+"','"+sample[1]+"','"+value1[2+1]+"','null');";
			 //databaseActions.update(query); 
			 k++;
			 successMsg = "File Uploaded successfully";
		 }
		 else
		 {
			String values[]=lines[k].split(",");
			//System.out.println(values[0]+" "+values[1]+" "+values[2]+" "+values[3]);
	    	String sql="INSERT INTO f15g110_" +courseId+"_"+tempFileName1+ " VALUES(?,?,?,?);";
		    PreparedStatement preparedStatement1 = databaseActions.conn.prepareStatement(sql);
		    preparedStatement1.setString(1, values[0]);
		    preparedStatement1.setString(2, values[1]);
		    preparedStatement1.setString(3, values[2]);
		    preparedStatement1.setString(4, values[3]);
		    preparedStatement1.executeUpdate();
		    //String query = "insert into " +tempFileName1+" values('"+values[0]+"','"+values[1]+"','"+values[2]+"','"+values[3]+"');";
		    //databaseActions.update(query); 
			 
		    k++;
		    
		 }
        }
        if(k==length){
        	successMsg = "File Uploaded successfully "+ (k-1) + "rows added";
        }
        else{
        	finalMsg = "There was a problem loading the file. Check the file you are trying to upload. Only "+(k-1)+" rows were added out of "+(length-1)+" rows";
        	String deleteQuery = "drop table f15g110_" +courseId+"_"+tempFileName1+";";
        	databaseActions.update(deleteQuery);
        }
	   }
	  }
	    else
	    {
	    	try{
	    		ArrayList<String> existingFiles1 = new ArrayList<String>();
	    		String fileNameQuery = "select TblName from f15g110_uploadedFiles where CID = '"+courseId+"' and type = 'A';";
	    		ResultSet rs = databaseActions.execute(fileNameQuery);	
	    		while(rs.next())
	    		{
	    			existingFiles1.add(rs.getString(1));
	    		}
	    		for(int i=0;i<existingFiles1.size();i++){
	    			//System.out.println(existingFiles1.get(i).trim());
	    			//System.out.println(fileLabel);
	    			if(existingFiles1.get(i).equals(fileLabel)){
	    					secondFlag = 1;
	    					finalMsg = "A file with the same name has already been uploaded for this Course. Try giving a different File Label";
	    					break;
	    			}
	    		}
	    	}
	    	catch(Exception e){
	    		error = e.getMessage();
	    		e.printStackTrace();
	    	}
	    	if(secondFlag == 0){
	    	try{
	    	String createQuery = "create table f15g110_"+courseId+"_"+fileLabel+"(QNO varchar(10),Question varchar(1000),Answer varchar(500),Result varchar(20));";
	    	databaseActions.update(createQuery);
	    	}
	    	catch(Exception e){
	    		error = e.getMessage();
	    		e.printStackTrace();
	    	}
	    	String insertQuery = "insert into f15g110_uploadedFiles values('"+courseId+"','"+fileLabel+"','A');";
	    	databaseActions.update(insertQuery);
	    	int k=1;
	        int length=lines.length;
	        while(k<length)
	        {
	        if(lines[k].contains("\""))
			 {
				String tsamp="null";
				 String value1[]=lines[k].split(",");
						 String sample[]=lines[k].split("\"");
						 String sample2[]=sample[1].split(",");
						 int l=sample2.length-1;
				 //System.out.println(value1[0]+" "+sample[1]+" "+value1[2+l]+" "+"null");
				 String sql="INSERT INTO f15g110_"+courseId+"_"+fileLabel+ " VALUES(?,?,?,?);";
				 PreparedStatement preparedStatement = databaseActions.conn.prepareStatement(sql);
				 preparedStatement.setString(1, value1[0]);
				 preparedStatement.setString(2, sample[1]);
				 preparedStatement.setString(3, value1[2+l]);
				 preparedStatement.setString(4, "null");
	        	 preparedStatement.executeUpdate();
				 //String query = "insert into " +tempFileName1+" values('"+Integer.parseInt(value1[0])+"','"+sample[1]+"','"+value1[2+1]+"','null');";
				 //databaseActions.update(query); 
				 k++;
				 successMsg = "File Uploaded successfully";
			 }
	        
			 else
			 {
				String values[]=lines[k].split(",");
	   		//System.out.println(values[0]+" "+values[1]+" "+values[2]+" "+values[3]);
		    	String sql="INSERT INTO f15g110_"+courseId+"_"+fileLabel+ " VALUES(?,?,?,?);";
			    PreparedStatement preparedStatement1 = databaseActions.conn.prepareStatement(sql);
			    preparedStatement1.setString(1, values[0]);
			    preparedStatement1.setString(2, values[1]);
			    preparedStatement1.setString(3, values[2]);
			    preparedStatement1.setString(4, values[3]);
			    preparedStatement1.executeUpdate();
			    //String query = "insert into " +tempFileName1+" values('"+values[0]+"','"+values[1]+"','"+values[2]+"','"+values[3]+"');";
			    //databaseActions.update(query); 
				 
			    k++;
			    
			 }

			
		}
	        if(k==length){
	        	successMsg = "File Uploaded successfully "+ (k-1) + "rows added";
	        }
	        else{
	        	finalMsg = "There was a problem loading the file. Check the file you are trying to upload. Only "+(k-1)+" rows were added out of "+(length-1)+" rows";
	        	String deleteQuery = "drop table f15g110_" +courseId+"_"+fileLabel+";";
	        	databaseActions.update(deleteQuery);
	        }
	    }
	    	
	    }
	   
	}
}
