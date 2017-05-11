package f15g110;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.apache.commons.math3.stat.StatUtils;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;


@ManagedBean
@SessionScoped
public class Chart {
	private ChartBean chartBean;
	private boolean pierender=false;
	private boolean barrender=false;
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
	private boolean numAnalysisRender=false;
	private  String selectedcourse;
	private  String selectedassgment;
	public Chart()
	{
		
	}
	
	public  String getSelectedcourse() {
		return selectedcourse;
	}

	public void setSelectedcourse(String selectedcourse) {
		this.selectedcourse = selectedcourse;
	}

	public  String getSelectedassgment() {
		return selectedassgment;
	}

	public void setSelectedassgment(String selectedassgment) {
	    this.selectedassgment = selectedassgment;
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

	public boolean isNumAnalysisRender() {
		return numAnalysisRender;
	}

	public void setNumAnalysisRender(boolean numAnalysisRender) {
		this.numAnalysisRender = numAnalysisRender;
	}

	public boolean isPierender() {
		return pierender;
	}

	public void setPierender(boolean pierender) {
		this.pierender = pierender;
	}

	public boolean isBarrender() {
		return barrender;
	}

	public void setBarrender(boolean barrender) {
		this.barrender = barrender;
	}
	private String finalPath;
	public ChartBean getChartBean() {
		return chartBean;
	}

	public void setChartBean(ChartBean chartBean) {
		this.chartBean = chartBean;
	}

	public String getFinalPath() {
		return finalPath;
	}

	public void setFinalPath(String finalPath) {
		this.finalPath = finalPath;
	}

	@PostConstruct
	public void init() {
	 	FacesContext context= FacesContext.getCurrentInstance();
		Map <String, Object> m= context.getExternalContext().getSessionMap();
		chartBean= (ChartBean)m.get("chartBean");
	}

	public String piechart()
	{
		setPierender(false);
		boolean status= ChartBean.datacheck();
		if(!status)
		{
			FacesMessage deleteMessage = new FacesMessage("There are no charts to view !!");
			FacesContext.getCurrentInstance().addMessage(null, deleteMessage);
			setBarrender(false);
			setPierender(false);
			setNumAnalysisRender(false);
		}
		else
		{
		//System.out.println("Inside Chart");
		try {
			FacesContext fc = FacesContext.getCurrentInstance();
			String path = fc.getExternalContext().getRealPath("/temp");
			JFreeChart chart;
			File out = null;
			out = new File(path+"/"+"pieChart.png");
			//System.out.println("path"+path);
			chart = ChartBean.createPieChart();
			ChartUtilities.saveChartAsPNG(out, chart, 500, 300);
			setFinalPath("temp/pieChart.png");
			setNumAnalysisRender(false);
			setPierender(true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		return "Success";
	}
	public String barchart()
	{
	    
		setBarrender(false);
		boolean status= ChartBean.datacheck();
		if(!status)
		{
			FacesMessage deleteMessage = new FacesMessage("There are no charts to view !!");
			FacesContext.getCurrentInstance().addMessage(null, deleteMessage);
			setBarrender(false);
			setPierender(false);
			setNumAnalysisRender(false);
		}
		else
		{
		//System.out.println("Inside bar Chart");
		try {
			FacesContext fc = FacesContext.getCurrentInstance();
			String path = fc.getExternalContext().getRealPath("/temp");
			JFreeChart chart;
			File out = null;
			out = new File(path+"/"+"barChart.png");
			//System.out.println("path"+path);
			chart = ChartBean.createBarChart();
			ChartUtilities.saveChartAsPNG(out, chart, 500, 300);
			setFinalPath("temp/barChart.png");
			setNumAnalysisRender(false);
			setBarrender(true);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		return "Success";
	}
	public String numericalanalysis()
	{   
		boolean status= ChartBean.datacheck();
		if(!status)
		{
			FacesMessage deleteMessage = new FacesMessage("There are no charts to view !!");
			FacesContext.getCurrentInstance().addMessage(null, deleteMessage);
			setBarrender(false);
			setPierender(false);
			setNumAnalysisRender(false);
		}
		
		else
		{

		setSelectedcourse(chartBean.getSelectedcourse());
		setSelectedassgment(chartBean.getSelectedassgment());
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
		  setBarrender(false);
		  setPierender(false);
		  setNumAnalysisRender(true);
		}
		  return "success";
	}

	



}
