package tests.data;
import static org.junit.Assert.*;

import java.io.IOException;

import org.antlr.v4.runtime.RecognitionException;
import org.junit.Test;

import data.dataKeeper.GlobalDataKeeper;
import data.dataKeeper.PPLFile;
import phaseAnalyzer.engine.PhaseAnalyzerMainEngine;
import services.DataService;

public class PPLFileTest {
	
	//Testing is for the biosql.ini project
	private String path = "filesHandler/inis/biosql.ini";
	@Test
	public void testFile() throws RecognitionException, IOException 
	{
		PPLFile file = new PPLFile();
		file.importData(path);
		
		assertEquals("filesHandler/datasets/biosql.txt", file.getDatasetTxt());     
	    assertEquals("filesHandler/input/biosql.csv", file.getInputCsv());     
		assertEquals("filesHandler/output/biosql_Assessment1.txt", file.getOutputAssessment1());     
		assertEquals("filesHandler/output/biosql_Assessment2.txt", file.getOutputAssessment2());     
        assertEquals("filesHandler/transitions/biosqlTransitions.xml", file.getTransitionsFile());     

	}
	//SUCCESS
	
	@Test
	public void testSizes() throws RecognitionException, IOException 
	{
		PPLFile file = new PPLFile();
		DataService service = new DataService();
		
		file.importData(path);
		
		GlobalDataKeeper globalDataKeeper= service.initiateGlobalData(file.getDatasetTxt(), file.getTransitionsFile());

		assertEquals("47",globalDataKeeper.getAllPPLSchemas().size() + "");
		assertEquals("46",globalDataKeeper.getAllPPLTransitions().size()+ "");
		assertEquals("45",globalDataKeeper.getAllPPLTables().size()+ "");
	}
	//SUCCESS
	
	@Test
	public void testVerticalReport() throws RecognitionException, IOException 
	{
		
		PPLFile pplFile = new PPLFile();
		DataService service = new DataService();
		
		pplFile.importData(path);
		
		GlobalDataKeeper globalDataKeeper= service.initiateGlobalData(pplFile.getDatasetTxt(), pplFile.getTransitionsFile());
		PhaseAnalyzerMainEngine mainEngine = new PhaseAnalyzerMainEngine(pplFile.getInputCsv(),
																		pplFile.getOutputAssessment1(),
																		pplFile.getOutputAssessment2(), 
																	    (float)0.5,
																		(float) 0.5, 
																		false, 
																		false);
		
		String oldVerticalReport = "1	1014631726	0	0	0	1	1	0	0	0	0"
				+ "2	1014707807	76081	0	0	0	0	0	0	0	0"
				+ "3	1014889243	181436	4	5	5	5	1	0	13	10"
				+ "4	1014901725	12482	0	0	0	0	0	0	0	0"
				+ "5	1014975574	73849	1	0	0	0	0	0	5	0"
				+ "6	1019860813	4885239	0	0	0	0	0	0	0	0"
				+ "7	1020121353	260540	0	0	0	0	0	0	0	0"
				+ "8	1031164716	11043363	0	0	2	0	1	0	0	0"
				+ "9	1031709232	544516	0	0	2	0	0	0	0	0"
				+ "10	1031817528	108296	2	3	5	5	18	5	8	10"
				+ "11	1032765746	948218	0	0	1	2	0	2	0	0"
				+ "12	1033520952	755206	0	1	2	1	0	0	0	2"
				+ "13	1034036778	515826	0	0	1	0	0	0	0	0"
				+ "14	1034619576	582798	0	0	0	0	0	0	0	0"
				+ "15	1034907910	288334	0	0	2	1	0	0	0	0"
				+ "16	1035366075	458165	0	1	2	0	2	0	0	3"
				+ "17	1037042231	1676156	1	0	1	0	0	0	4	0"
				+ "18	1038213294	1171063	0	0	1	0	2	0	0	0"
				+ "19	1045468374	7255080	0	0	2	2	0	0	0	0"
				+ "20	1045475435	7061	0	0	1	1	0	0	0	0"
				+ "21	1045603387	127952	5	0	40	35	1	6	17	0"
				+ "22	1045605692	2305	0	0	3	3	0	0	0	0"
				+ "23	1045618809	13117	1	1	8	6	2	0	2	2"
				+ "24	1045626347	7538	2	0	3	0	1	0	6	0"
				+ "25	1045691561	65214	0	0	0	2	0	0	0	0"
				+ "26	1045699202	7641	1	0	1	0	0	0	3	0"
				+ "27	1047465289	1766087	3	3	9	9	0	0	16	16"
				+ "28	1047466335	1046	2	2	0	0	0	0	10	9"
				+ "29	1047886539	420204	1	0	1	0	0	0	2	0"
				+ "30	1047967554	81015	0	0	0	1	0	3	0	0"
				+ "31	1048021292	53738	0	0	0	0	0	0	0	0"
				+ "32	1049270935	1249643	0	1	3	0	0	10	0	3"
				+ "33	1049813903	542968	0	0	8	8	0	0	0	0"
				+ "34	1054073713	4259810	0	0	0	0	0	0	0	0"
				+ "35	1054456376	382663	0	0	0	0	0	0	0	0"
				+ "36	1054501384	45008	0	0	0	0	0	0	0	0"
				+ "37	1054782914	281530	0	0	0	0	0	0	0	0"
				+ "38	1096853196	42070282	1	0	0	0	0	0	2	0"
				+ "39	1099532981	2679785	0	0	0	0	0	0	0	0"
				+ "40	1113801698	14268717	0	0	0	0	0	0	0	0"
				+ "41	1203569193	89767495	0	0	0	0	0	0	0	0"
				+ "42	1203613747	44554	0	0	0	0	0	0	0	0"
				+ "43	1203732725	118978	0	0	0	0	0	0	0	0"
				+ "44	1203733325	600	0	0	0	0	0	0	0	0"
				+ "45	1217564276	13830951	0	0	0	0	2	0	0	0"
				+ "46	1347272320	129708044	0	0	0	0	0	0	0	0";
		
		assertEquals(oldVerticalReport,mainEngine.parseInput());
		
	}
}
