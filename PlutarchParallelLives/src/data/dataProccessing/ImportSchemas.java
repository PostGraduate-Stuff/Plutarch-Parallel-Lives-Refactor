package data.dataProccessing;

import gr.uoi.cs.daintiness.hecate.diff.Delta;
import gr.uoi.cs.daintiness.hecate.diff.DiffResult;
import gr.uoi.cs.daintiness.hecate.parser.HecateParser;
import gr.uoi.cs.daintiness.hecate.sql.Schema;
import gr.uoi.cs.daintiness.hecate.transitions.Deletion;
import gr.uoi.cs.daintiness.hecate.transitions.Insersion;
import gr.uoi.cs.daintiness.hecate.transitions.TransitionList;
import gr.uoi.cs.daintiness.hecate.transitions.Transitions;
import gr.uoi.cs.daintiness.hecate.transitions.Update;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class ImportSchemas {
	
	private static ArrayList<Schema> allSchemas = null;

	private String filepath=null;
	private String transitionsFile=null;
	private static ArrayList<TransitionList> allTransitions = null;
	
	public ImportSchemas(String filepath,String transitionsFile) {
		allTransitions = new ArrayList<TransitionList>();
		allSchemas = new ArrayList<Schema>();
		this.filepath=filepath;
		this.transitionsFile=transitionsFile;
	}

	@SuppressWarnings("static-access")
	public void loadDataset() throws IOException{

		File file = new File(filepath);
		String dataset = (file.getName().split("\\."))[0];
		String parent = file.getParent();
		file = new File(parent);
		String path = file.getAbsolutePath() + File.separator + dataset;
		ArrayList<String> allTempSchemas = readAllSchemas();
		String standardString = path + File.separator;
		Transitions transitions = readTransitions(standardString, allTempSchemas);
		makeTransitions(transitions);
		
	}
	
	private ArrayList<String> readAllSchemas() throws IOException{
		ArrayList<String> allTempSchemas = new ArrayList<String>();
		
		BufferedReader bufferedReader = new BufferedReader(new FileReader(filepath));
		String line;
		
		line = bufferedReader.readLine();
		while(line != null) {
			allTempSchemas.add(line);
			line = bufferedReader.readLine();
		}
		
		bufferedReader.close();
		return allTempSchemas;
	}
	
	private Transitions readTransitions(String standardString, ArrayList<String> allTempSchemas){
		Transitions transitions = new Transitions();
		for(int i=0; i<allTempSchemas.size()-1; i++) {
			String firstFinalString=standardString+allTempSchemas.get(i).trim();
			allSchemas.add(HecateParser.parse(firstFinalString));
			String nextFinalString=standardString+allTempSchemas.get(i+1).trim();
			Schema oldSchema = HecateParser.parse(firstFinalString);
			Schema newSchema = HecateParser.parse(nextFinalString);
			Delta delta = new Delta();
			TransitionList transitionList =new TransitionList();
			DiffResult diffResult=new DiffResult();
			diffResult=delta.minus(oldSchema, newSchema); 
			transitionList=diffResult.tl;
			transitions.add(transitionList);
		}
		String finalString=standardString+allTempSchemas.get(allTempSchemas.size()-1);
		Schema schema=HecateParser.parse(finalString);
		allSchemas.add(schema);
		return transitions;
	}

	public  void makeTransitions(Transitions transitions) throws IOException {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(Update.class,Deletion.class, Insersion.class, TransitionList.class, Transitions.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.marshal(transitions, new FileOutputStream(transitionsFile));

			JAXBContext newJaxbContext = JAXBContext.newInstance(Update.class,Deletion.class, Insersion.class, TransitionList.class, Transitions.class);
			Unmarshaller unmarshaller=newJaxbContext.createUnmarshaller();
			File inputFile=new File(transitionsFile);
			Transitions root = (Transitions) unmarshaller.unmarshal(inputFile);
			
			allTransitions=(ArrayList<TransitionList>) root.getList();
		} 
		catch (JAXBException e) {
			JOptionPane.showMessageDialog(null, "Something seems wrong with this file");
			return;
		}
	}
	
	public ArrayList<Schema> getAllHecSchemas(){
		return allSchemas;
	}

	public ArrayList<TransitionList> getAllTransitions(){
		return allTransitions;
	}
}
