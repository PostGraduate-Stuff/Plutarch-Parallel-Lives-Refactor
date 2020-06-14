package data.dataKeeper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JOptionPane;

import org.antlr.v4.runtime.RecognitionException;

public class PPLFile {
	
	private File file;	
	private String projectName="";
	private String datasetTxt="";
	private String inputCsv="";
	private String outputAssessment1="";
	private String outputAssessment2="";
	private String transitionsFile="";
	
	public void readFile(File file) throws IOException
	{
		try 
		{
			this.file = file;
			String fileContents = null;
	        System.out.println(file.toString());
	        fileContents = file.toString();
	        System.out.println("!!"+file.getName());
	        importData(fileContents);
	
		}
		catch (RecognitionException e) 
		{
			JOptionPane.showMessageDialog(null, "Something seems wrong with this file");
		}
	}
	
	
	public void importData(String fileName) throws IOException, RecognitionException {
		
		
		BufferedReader bufferReader = new BufferedReader(new FileReader(fileName));
		
		String line = bufferReader.readLine();
		
		while(line != null) {
			
			String[] entityTable = line.split(":");
			if(entityTable.length < 2){
				throw new IOException("Out of bounds!!");
			}
			
			if(line.contains("Project-name")){
				projectName = entityTable[1];
			}
			else if(line.contains("Dataset-txt")){
				datasetTxt = entityTable[1];
			}
			else if(line.contains("Input-csv")){
				inputCsv = entityTable[1];
			}
			else if(line.contains("Assessement1-output")){
				outputAssessment1 = entityTable[1];
			}
			else if(line.contains("Assessement2-output")){
				outputAssessment2 = entityTable[1];
			}
			else if(line.contains("Transition-xml")){
				transitionsFile = entityTable[1];
			}
			line = bufferReader.readLine();
		};	
		
		bufferReader.close();
		
		displayProjectsDescription();
	}
	
	public void displayProjectsDescription(){
		System.out.println("Project Name:"+projectName);
		System.out.println("Dataset txt:"+datasetTxt);
		System.out.println("Input Csv:"+inputCsv);
		System.out.println("Output Assessment1:"+outputAssessment1);
		System.out.println("Output Assessment2:"+outputAssessment2);
		System.out.println("Transitions File:"+transitionsFile);
	}

	public File getFile() {
		return file;
	}
	
	
	public void setFile(File file) {
		this.file = file;
	}
	
	
	public String getProjectName() {
		return projectName;
	}
	
	
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	
	
	public String getDatasetTxt() {
		return datasetTxt;
	}
	
	
	public void setDatasetTxt(String datasetTxt) {
		this.datasetTxt = datasetTxt;
	}
	
	
	public String getInputCsv() {
		return inputCsv;
	}
	
	
	public void setInputCsv(String inputCsv) {
		this.inputCsv = inputCsv;
	}
	
	
	public String getOutputAssessment1() {
		return outputAssessment1;
	}
	
	
	public void setOutputAssessment1(String outputAssessment1) {
		this.outputAssessment1 = outputAssessment1;
	}
	
	
	public String getOutputAssessment2() {
		return outputAssessment2;
	}
	
	
	public void setOutputAssessment2(String outputAssessment2) {
		this.outputAssessment2 = outputAssessment2;
	}
	
	
	public String getTransitionsFile() {
		return transitionsFile;
	}
	
	
	public void setTransitionsFile(String transitionsFile) {
		this.transitionsFile = transitionsFile;
	}
		

}
