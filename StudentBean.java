package f15g110;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
@ManagedBean
@ViewScoped
public class StudentBean implements Serializable{
	public StudentBean(){
		Registration reg = new Registration();
		reg.generateCourseList();
	}
private static long UIN;
private static String fName;
private static String lName;
private static String netId;
private static String password;
public long getUIN() {
	return UIN;
}
public void setUIN(long uIN) {
	UIN = uIN;
}
public String getfName() {
	return fName;
}
public void setfName(String fName) {
	this.fName = fName;
}
public String getlName() {
	return lName;
}
public void setlName(String lName) {
	this.lName = lName;
}
public String getNetId() {
	return netId;
}
public void setNetId(String netId) {
	this.netId = netId;
}
public String getPassword() {
	return password;
}
public void setPassword(String password) {
	this.password = password;
}


}
