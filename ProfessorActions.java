package f15g110;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;  
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
//import javax.servlet.http.Part;

import org.apache.myfaces.custom.fileupload.UploadedFile;  

@ManagedBean  
public class ProfessorActions<HttpServletResponse>{
		
	private UploadedFile uploadedFile;
	private String fileLabel;
	
	public String getFileLabel() {
		return fileLabel;
	}

	public void setFileLabel(String fileLabel) {
		this.fileLabel = fileLabel;
	}
	
	
	public UploadedFile getUploadedFile() {
		return uploadedFile;
	}

	public void setUploadedFile(UploadedFile uploadedFile) {
		//System.out.println("before this");
		this.uploadedFile = uploadedFile;
		//System.out.println("after this");
	}

	public String processFileUpload() {
	String uploadedFileContents = null;
	FacesContext context =
	FacesContext.getCurrentInstance();
	String path =
	context.getExternalContext().getRealPath("/tmp");
	File tempFile = null;
	FileOutputStream fos = null;
	try {
	String fileName = uploadedFile.getName();
	long fileSize = uploadedFile.getSize();
	String fileContentType = uploadedFile.getContentType();
	uploadedFileContents = new // in memory
	String(uploadedFile.getBytes());
	tempFile = new File("C:\\"+fileName);
	fos = new FileOutputStream(tempFile);
	// or uploaded to disk
	fos.write(uploadedFile.getBytes());
	fos.close();
	return "SUCCESS";
	} // end try
	catch (Exception e)
	{System.err.println(e);
	e.printStackTrace();
	return "FAIL";
	} // end catch
	}
	}  

