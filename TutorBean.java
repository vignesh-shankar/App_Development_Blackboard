package f15g110;

import java.io.Serializable;

public class TutorBean implements Serializable{
	private long UIN;
	private String fName;
	private String lName;
	private String netId;
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
}
