package f15g110;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.apache.commons.math3.stat.StatUtils;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

@ManagedBean
@SessionScoped
public class ChartBean {
	public ChartBean()
	{
		
	}
	
	private String fullpath;
	private boolean chartrender=false;
	private static ArrayList<String> assess_names=new ArrayList<String>();
    private static ArrayList<Integer> assess_marks=new ArrayList<Integer>();
    private DatabaseInformationBean dbi=new DatabaseInformationBean();
    private ArrayList<String> courseList = new ArrayList<String>();
	private ArrayList<String> assessmentList = new ArrayList<String>();
	private DatabaseActions dba=new DatabaseActions();
	private boolean courseRender=true;
	private boolean assessmentRender=false;
	private static String selectedcourse;
	private static String selectedassgment;
	private double minValue; 
	private  double maxValue; 
	private  double mean; 
	private  double variance; 
	private  double std; 
	private  double median; 
	private  double q1; 
	private  double q3; 
	private  double iqr; 
	private  double range; 
	private boolean scorerender=false;
	private boolean numAnalysisRender=false;
	private boolean chartstatus=true;
	private Chart ch;
	   @PostConstruct
		public void init() {
	 	FacesContext context= FacesContext.getCurrentInstance();
		Map <String, Object> m= context.getExternalContext().getSessionMap();
		ch= (Chart)m.get("ch");
	}

	

	public boolean getChartstatus() {
		return chartstatus;
	}



	public void setChartstatus(boolean chartstatus) {
		//System.out.println("set paniyachu");
		this.chartstatus = chartstatus;
	}



	public Chart getCh() {
		return ch;
	}

	public void setCh(Chart ch) {
		this.ch = ch;
	}
	public boolean isScorerender() {
		return scorerender;
	}

	public boolean isNumAnalysisRender() {
		return numAnalysisRender;
	}

	public void setNumAnalysisRender(boolean numAnalysisRender) {
		this.numAnalysisRender = numAnalysisRender;
	}

	public void setScorerender(boolean scorerender) {
		this.scorerender = scorerender;
	}

	public ArrayList<ScoreBean> getListscore() {
		return listscore;
	}

	public void setListscore(ArrayList<ScoreBean> listscore) {
		this.listscore = listscore;
	}
	private ArrayList<ScoreBean> listscore= new ArrayList<ScoreBean>();
	public static ArrayList<String> getAssess_names() {
		return assess_names;
	}

	public static void setAssess_names(ArrayList<String> assess_names) {
		ChartBean.assess_names = assess_names;
	}

	public static ArrayList<Integer> getAssess_marks() {
		return assess_marks;
	}

	public static void setAssess_marks(ArrayList<Integer> assess_marks) {
		ChartBean.assess_marks = assess_marks;
	}

	public ArrayList<String> getCourseList() {
		return courseList;
	}

	public void setCourseList(ArrayList<String> courseList) {
		this.courseList = courseList;
	}

	public ArrayList<String> getAssessmentList() {
		return assessmentList;
	}

	public void setAssessmentList(ArrayList<String> assessmentList) {
		this.assessmentList = assessmentList;
	}

	public boolean isCourseRender() {
		return courseRender;
	}

	public void setCourseRender(boolean courseRender) {
		this.courseRender = courseRender;
	}

	public boolean isAssessmentRender() {
		return assessmentRender;
	}

	public void setAssessmentRender(boolean assessmentRender) {
		this.assessmentRender = assessmentRender;
	}

	public String getSelectedcourse() {
		return selectedcourse;
	}

	public void setSelectedcourse(String selectedcourse) {
		this.selectedcourse = selectedcourse;
	}

	public String getSelectedassgment() {
		return selectedassgment;
	}

	public void setSelectedassgment(String selectedassgment) {
		this.selectedassgment = selectedassgment;
	}

		
	public String retrieveAssessmentList() {
	
		Statement stmt = null;
		try {
			assessmentList.clear();
			ResultSet rs = null;
			String Assgment = "SELECT distinct(AssgnmntName) FROM f15g110_answers where CID = '"+selectedcourse+"';";
			rs = dba.execute(Assgment);
			assessmentRender = true;
			while (rs.next()) {
				assessmentList.add(rs.getString(1));
				}
			
			if (assessmentList.isEmpty()) {
				FacesMessage deleteMessage = new FacesMessage("There are no assessments to view !!");
				FacesContext.getCurrentInstance().addMessage(null, deleteMessage);
				assessmentRender = false;
			}

		} catch (Exception e) {
			System.err.println(e);
		}
		return "true";
	}

public static boolean datacheck()
{
	assess_names.clear();
	assess_marks.clear();
	//System.out.println("inside data check");
	DatabaseActions databaseActions = new DatabaseActions();
	String query = "select STU_ID,marks from f15g110_results where CID = '"+selectedcourse+"' and AssgnmntName ='"+selectedassgment+"';";
	ResultSet rs = databaseActions.execute(query);
	try{
	while(rs.next()){
		assess_names.add(rs.getString(1));
		assess_marks.add(Integer.parseInt(rs.getString(2)));
		
	}
	}
	catch(SQLException e)
	{
		//System.out.println(e.getMessage());
	}
	if(assess_names.isEmpty())
	return false;
	else
		return true;
	
}

public static JFreeChart createPieChart() { 
	
	DatabaseActions databaseActions = new DatabaseActions();
	String query = "select STU_ID,marks from f15g110_results where CID = '"+selectedcourse+"' and AssgnmntName ='"+selectedassgment+"';";
	ResultSet rs = databaseActions.execute(query);
	try{
	while(rs.next()){
		assess_names.add(rs.getString(1));
		assess_marks.add(Integer.parseInt(rs.getString(2)));
		
	}
		}
	catch(SQLException e)
	{
		//System.out.println(e.getMessage());
	}
        // create a dataset...         
 DefaultPieDataset data = new DefaultPieDataset();
 int i=0;
while(i<assess_names.size())
{
	//System.out.println(assess_names.get(i));
	//System.out.println(assess_marks.get(i));
data.setValue(assess_names.get(i), new Double(assess_marks.get(i)));
i++;
}
JFreeChart chart = ChartFactory.createPieChart("Pie Chart", data, true, true, false);          
 return chart;  
    
}

public static JFreeChart createBarChart() {  
	
	////System.out.println("INside bar");
	DatabaseActions databaseActions = new DatabaseActions();
	String query = "select STU_ID,marks from f15g110_results where CID = '"+selectedcourse+"' and AssgnmntName ='"+selectedassgment+"';";
	ResultSet rs = databaseActions.execute(query);
	try{
	while(rs.next()){
		assess_names.add(rs.getString(1));
		assess_marks.add(Integer.parseInt(rs.getString(2)));
		
	}
		}
	catch(SQLException e)
	{
		//System.out.println(e.getMessage());
	}
	
    
DefaultCategoryDataset dataset = new DefaultCategoryDataset();
int i=0;
while(i<assess_names.size())
{

dataset.addValue(assess_marks.get(i),assess_names.get(i), "C1");
i++;
}
JFreeChart chart = ChartFactory.createBarChart3D("Bar Chart","Students","Marks",dataset,PlotOrientation.VERTICAL,true,true,false);
  
	return chart;
}

/*public String processbarchart()
{
	JFreeChart chart;  
	//chart= createPieChart();
	
	try
	{
	FacesContext context = FacesContext.getCurrentInstance(); 
	String path = context.getExternalContext().getRealPath("/temp"); 
	File out = null; 
	//System.out.println(path);
	out = new File(path+"/"+"barchart.png"); 
	chart=createBarChart();
    ChartUtilities.saveChartAsPNG(out, chart, 600, 450);
    setChartrender(true);
	}
       catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
       }
	
	 setFullpath("temp/barchart.png");
	 //System.out.println(fullpath);
	 return fullpath;
	 
	 
}
*/
/*public String processpiechart()
{
	JFreeChart chart;  
	
	try
	{
	FacesContext context = FacesContext.getCurrentInstance(); 
	String path = context.getExternalContext().getRealPath("/temp"); 
	//System.out.println(path);
	File out = null;  
	out = new File(path+"piechart.png"); 
	chart= createPieChart();
    ChartUtilities.saveChartAsPNG(out, chart, 600, 450);
    setChartrender(true);
	}
	  
		catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	//chart = createPieChart();
	

	 setFullpath("temp/piechart.png");
	 //System.out.println(fullpath);
	 return fullpath;
	 
	 
}
*/
public String viewscore()
{
	listscore.clear();
	DatabaseActions databaseActions = new DatabaseActions();
	String query = "select STU_ID,marks from f15g110_results where CID = '"+selectedcourse+"' and AssgnmntName ='"+selectedassgment+"';";
	ResultSet rs = databaseActions.execute(query);
	try{
	while(rs.next()){
		ScoreBean sb=new ScoreBean();
		sb.setStudentID(rs.getString(1));
		sb.setMarks(rs.getString(2));
		listscore.add(sb);
		
	}
	scorerender=true;
	}
	catch(SQLException e)
	{
		//System.out.println(e.getMessage());
	}
    
	return "success";
}

public String numericalanalysis()
{   
	//System.out.println("Numerical analysis");
    ArrayList<String> assessdata = new ArrayList<String>();
    ArrayList<Double> assessnumber = new ArrayList<Double>();
	DatabaseActions databaseActions = new DatabaseActions();
	String query = "select marks from f15g110_results where CID = '"+selectedcourse+"' and AssgnmntName ='"+selectedassgment+"';";
	ResultSet rs = databaseActions.execute(query);
	try{
	while(rs.next()){
		assessdata.add(rs.getString(1));
		
	}
	}
	catch(SQLException e)
	{
		//System.out.println(e.getMessage());
	}
    
	int listLength=0;
    		 for(String stringValue : assessdata)
    		 {
    			 assessnumber.add(Double.parseDouble(stringValue));
    			 
    		 }
    		 for(int i=0;i<assessnumber.size();i++)
     		{
     		listLength = listLength+1;
    		 //System.out.println("assesment: "+assessnumber.get(i));
     		}
    		 double values[] = new double[listLength];
    		for(int i=0;i<assessnumber.size();i++)
    		{
    			
    			values[i]=assessnumber.get(i);
    		}
    		//System.out.println("values"+values[0]);
	
	  minValue = StatUtils.min(values); 
	  maxValue = StatUtils.max(values); 
	  mean = StatUtils.mean(values); 
	  variance = StatUtils.variance(values, mean); 
	  std = Math.sqrt(variance); 
	  median = StatUtils.percentile(values, 50.0); 
	  q1 = StatUtils.percentile(values, 25.0); 
	  q3 = StatUtils.percentile(values, 75.0); 
	  iqr = q3 - q1; 
	  range = maxValue - minValue; 
	  ////System.out.println("min:"+minValue+"max:"+maxValue+"variance:"+variance+"iqr"+iqr);
	  ch.setBarrender(false);
	  ch.setPierender(false);
	  setNumAnalysisRender(true);
	  return "success";
}


public DatabaseInformationBean getDbi() {
	return dbi;
}

public void setDbi(DatabaseInformationBean dbi) {
	this.dbi = dbi;
}

public DatabaseActions getDba() {
	return dba;
}

public void setDba(DatabaseActions dba) {
	this.dba = dba;
}



public double getMinValue() {
	return minValue;
}

public void setMinValue(double minValue) {
	this.minValue = minValue;
}

public double getMaxValue() {
	return maxValue;
}

public void setMaxValue(double maxValue) {
	this.maxValue = maxValue;
}

public double getMean() {
	return mean;
}

public void setMean(double mean) {
	this.mean = mean;
}

public double getVariance() {
	return variance;
}

public void setVariance(double variance) {
	this.variance = variance;
}

public double getStd() {
	return std;
}

public void setStd(double std) {
	this.std = std;
}

public double getMedian() {
	return median;
}

public void setMedian(double median) {
	this.median = median;
}

public double getQ1() {
	return q1;
}

public void setQ1(double q1) {
	this.q1 = q1;
}

public double getQ3() {
	return q3;
}

public void setQ3(double q3) {
	this.q3 = q3;
}

public double getIqr() {
	return iqr;
}

public void setIqr(double iqr) {
	this.iqr = iqr;
}

public double getRange() {
	return range;
}

public void setRange(double range) {
	this.range = range;
}

public String getFullpath() {
	return fullpath;
}

public void setFullpath(String fullpath) {
	this.fullpath = fullpath;
}

public boolean isChartrender() {
	return chartrender;
}

public void setChartrender(boolean chartrender) {
	this.chartrender = chartrender;
}
}
