package f15g110;

import java.io.Serializable;

public class RosterBean implements Serializable{
private String lastName;
private String firstName;
private String netId;
private long UIN;
private String role;
private String status;
public String getLastName() {
	return lastName;
}
public void setLastName(String lastName) {
	this.lastName = lastName;
}
public String getFirstName() {
	return firstName;
}
public void setFirstName(String firstName) {
	this.firstName = firstName;
}
public String getNetId() {
	return netId;
}
public void setNetId(String netId) {
	this.netId = netId;
}

public long getUIN() {
	return UIN;
}
public void setUIN(long uIN) {
	UIN = uIN;
}
public String getRole() {
	return role;
}
public void setRole(String role) {
	this.role = role;
}
public String getStatus() {
	return status;
}
public void setStatus(String status) {
	this.status = status;
}

}
