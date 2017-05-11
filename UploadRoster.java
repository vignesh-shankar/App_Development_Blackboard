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
public class UploadRoster implements Serializable{
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
	private String statusMsg = null;
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
	
	
	public String getTempFileName1() {
		return tempFileName1;
	}
	
	
	public String getFinalMsg() {
		return finalMsg;
	}

	public String getSuccessMsg() {
		return successMsg;
	}
	
	
	public String getCourseId() {
		return courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}

	
	public String getStatusMsg() {
		return statusMsg;
	}

	public String upload(){
		
		File tempFile = null;
		//System.out.println("File type: " + uploadedFile.getContentType());
        //System.out.println("File name: " + uploadedFile.getName());
        //System.out.println("File size: " + uploadedFile.getSize() + " bytes");
	uploadedFileContents = null;
	//tempFile = File.createTempFile(uploadedFile.getName(),".", new File("c:/upload"));
    //output = new FileOutputStream(file);
	FacesContext context =FacesContext.getCurrentInstance();
	String path =context.getExternalContext().getRealPath("/temp");
	
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
	} // end catch
 catch (ClassNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return "Success";
	}
	
	/*public void databaseUpdate() throws SQLException, ClassNotFoundException
	{ 
		String lines[] = uploadedFileContents.split("\\r?\\n");
		DatabaseActions databaseActions=new DatabaseActions();
	    //Connection conn=databaseActions.connect();
	    //System.out.println("After connection");
	    String createQuery = "create table " +tempFileName1+"(LastName varchar(30),FirstName varchar(30),NetID varchar(10),UIN int(10), role varchar(15), status varchar (15));"; 
        databaseActions.update(createQuery);
	    int k=1;
        int length=lines.length;
        while(k<length)
        
		 {
			String values[]=lines[k].split(",");
			//System.out.println(values[0]+" "+values[1]+" "+values[2]+" "+values[3]+" "+values[4]);
	    	String sql="INSERT INTO " +tempFileName1+ " VALUES(?,?,?,?,?,?);";
		    PreparedStatement preparedStatement1 = databaseActions.conn.prepareStatement(sql);
		    preparedStatement1.setString(1, values[0]);
		    preparedStatement1.setString(2, values[1]);
		    preparedStatement1.setString(3, values[2]);
		    preparedStatement1.setInt(4, Integer.parseInt(values[3]));
		    preparedStatement1.setString(5, values[4]);
		    preparedStatement1.setString(6, values[5]);
		    preparedStatement1.executeUpdate();
		    //String query = "insert into " +tempFileName1+" values('"+values[0]+"','"+values[1]+"','"+values[2]+"','"+values[3]+"');";
		    //databaseActions.update(query); 
			 
		    k++;
		    
		 }

	}*/
	
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
	    		String fileNameQuery = "select TblName from f15g110_uploadedFiles where CID = '"+courseId+"' and type = 'R';";
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
	    		e.printStackTrace();
	    	}
	    	if(firstFlag == 0){
	    		try{
	    		String createQuery = "create table f15g110_"+courseId+"_"+tempFileName1+"(LastName varchar(30),FirstName varchar(30),NetID varchar(10),UIN int(10), role varchar(15), status varchar (15));";
	    		databaseActions.update(createQuery);
	    	}
	    	catch(Exception e){
	    		e.printStackTrace();
	    		finalMsg = "A Problem occured while uploading the file. Try again";
	    	}
	    String insertQuery = "insert into f15g110_uploadedFiles values('"+courseId+"','"+tempFileName1+"','R');";
	    databaseActions.update(insertQuery);
        int k=1;
        int length=lines.length;
        statusMsg = "Uploading File...";
        while(k<length)
        {
			String values[]=lines[k].split(",");
			//System.out.println(values[0]+" "+values[1]+" "+values[2]+" "+values[3]+" "+values[4]);
	    	String sql="INSERT INTO f15g110_"+courseId+"_"+tempFileName1+ " VALUES(?,?,?,?,?,?);";
		    PreparedStatement preparedStatement1 = databaseActions.conn.prepareStatement(sql);
		    preparedStatement1.setString(1, values[0]);
		    preparedStatement1.setString(2, values[1]);
		    preparedStatement1.setString(3, values[2]);
		    preparedStatement1.setInt(4, Integer.parseInt(values[3]));
		    preparedStatement1.setString(5, values[4]);
		    preparedStatement1.setString(6, values[5]);
		    preparedStatement1.executeUpdate();
		    //String query = "insert into " +tempFileName1+" values('"+values[0]+"','"+values[1]+"','"+values[2]+"','"+values[3]+"');";
		    //databaseActions.update(query); 
		    k++;
		    
		 }
        statusMsg = null;
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
	    		String fileNameQuery = "select TblName from f15g110_uploadedFiles where CID = '"+courseId+"' and type = 'R';";
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
	    		e.printStackTrace();
	    	}
	    	if(secondFlag == 0){
	    	try{
	    	String createQuery = "create table f15g110_"+courseId+"_"+fileLabel+"(LastName varchar(30),FirstName varchar(30),NetID varchar(10),UIN int(10), role varchar(15), status varchar (15));";
	    	databaseActions.update(createQuery);
	    	}
	    	catch(Exception e){
	    		finalMsg = "A Problem occured while uploading the file. Try again";
	    		e.printStackTrace();
	    	}
	    	String insertQuery = "insert into f15g110_uploadedFiles values('"+courseId+"','"+fileLabel+"','R');";
	    	databaseActions.update(insertQuery);
	    	int k=1;
	        int length=lines.length;
	        statusMsg = "Uploading File...";
	        while(k<length)
	        {
				String values[]=lines[k].split(",");
				//System.out.println(values[0]+" "+values[1]+" "+values[2]+" "+values[3]+" "+values[4]);
		    	String sql="INSERT INTO f15g110_"+courseId+"_"+fileLabel+ " VALUES(?,?,?,?,?,?);";
			    PreparedStatement preparedStatement1 = databaseActions.conn.prepareStatement(sql);
			    preparedStatement1.setString(1, values[0]);
			    preparedStatement1.setString(2, values[1]);
			    preparedStatement1.setString(3, values[2]);
			    preparedStatement1.setInt(4, Integer.parseInt(values[3]));
			    preparedStatement1.setString(5, values[4]);
			    preparedStatement1.setString(6, values[5]);
			    preparedStatement1.executeUpdate();
			    //String query = "insert into " +tempFileName1+" values('"+values[0]+"','"+values[1]+"','"+values[2]+"','"+values[3]+"');";
			    //databaseActions.update(query); 
			    k++;
			    
			 }
	        statusMsg = null;
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
