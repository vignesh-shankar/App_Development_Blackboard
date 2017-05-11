package f15g110;

import java.io.Serializable;
import java.util.ArrayList;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
@ManagedBean
@ViewScoped
public class AnswerBean implements Serializable{
	public AnswerBean(){
		
	}
private ArrayList<String> answerList = new ArrayList<String>();

public ArrayList<String> getAnswerList() {
	return answerList;
}

public void setAnswerList(ArrayList<String> answerList) {
	this.answerList = answerList;
}

public void submitAnswers(){
	System.out.println(answerList);
}
}
