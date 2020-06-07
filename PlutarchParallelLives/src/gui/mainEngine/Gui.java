package gui.mainEngine;

import gui.configurations.DataConfiguration;
import gui.configurations.DataTablesConfiguration;
import gui.configurations.GuiConfiguration;
import gui.dialogs.EnlargeTable;
import gui.ui.functionality.DataGeneratorUI;
import gui.ui.functionality.InfoUI;
import gui.ui.functionality.PLDShowUI;
import gui.ui.functionality.PhasesClustersPLDUI;
import gui.ui.functionality.PhasesPLDUI;
import gui.ui.functionality.ProjectCreatorUI;
import gui.ui.functionality.ProjectEditorUI;
import gui.ui.functionality.ProjectLoaderUI;
import gui.ui.table.TableLifetimeDisplayUI;
import gui.ui.table.ZoomAreaTableForClusterUI;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import tableClustering.clusterExtractor.engine.TableClusteringMainEngine;
import tableClustering.clusterValidator.engine.ClusterValidatorMainEngine;


public class Gui extends JFrame implements ActionListener{

	public static final long serialVersionUID = 1L;

	private final GuiConfiguration configuration;
	private final DataConfiguration dataConfiguration;
	private final DataTablesConfiguration tablesConfiguration;
	private DataGeneratorUI dataGenerator;
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Gui frame = new Gui();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		});
	}
	
	public Gui() {
		
		configuration = new GuiConfiguration();
		dataConfiguration = new DataConfiguration();
		tablesConfiguration = new DataTablesConfiguration();
		dataGenerator = new DataGeneratorUI(configuration, dataConfiguration, tablesConfiguration);
		
		initializeUserInterface();
		
	}
	
	
	private void initializeUserInterface() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
				
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		menuBar.add(getFileTab());
		
		menuBar.add(getTableTab());
	
		configuration.setMnProject(new JMenu("Project"));
		menuBar.add(configuration.getMnProject());
		
		JButton buttonHelp=new JButton("Help");
		buttonHelp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String message ="To open a project, you must select a .txt file that contains the names ONLY of " +
									"the SQL files of the dataset that you want to use."+"\n" +
									"The .txt file must have EXACTLY the same name with the folder " +
									"that contains the DDL Scripts of the dataset."+ "\n" +
									"Both .txt file and dataset folder must be in the same folder.";
				JOptionPane.showMessageDialog(null,message); 				
			}
		});
		
		buttonHelp.setBounds(900,900 , 80, 40);
		menuBar.add(buttonHelp);
		
		InfoUI infoUI = new InfoUI();
		JMenuItem infoItem = infoUI.showInfo(dataGenerator);
		configuration.getMnProject().add(infoItem);
				
		configuration.setContentPaneConfig(new JPanel());
		
		configuration.getContentPane().setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(configuration.getContentPane());
		
		
		GroupLayout gl_contentPane = new GroupLayout(configuration.getContentPane());
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addComponent(configuration.getTabbedPane(), Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 1474, Short.MAX_VALUE)
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addComponent(configuration.getTabbedPane(), Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 771, Short.MAX_VALUE)
		);
		configuration.getContentPane().setLayout(gl_contentPane);

		
		configuration.addTabbedPane();
		configuration.addLabels();
		

		createZoomInButton();
		createZoomOutButton();
		
		createPOPUp();
		createUndoButton();
		
		createUniformlyDistributedButton();
		createNotUniformlyDistributedButton();

		configuration.addLifeTimePanel();
		
		pack();
		setBounds(30, 30, 1300, 700);		
	}

	private JMenu getFileTab(){
		JMenu mnFile = new JMenu("File");
		
		ProjectCreatorUI projectCreatorUI = new ProjectCreatorUI();
		JMenuItem mntmCreateProject = projectCreatorUI.createProject(dataGenerator);
		mnFile.add(mntmCreateProject);	
		
		ProjectLoaderUI projectLoaderUI = new ProjectLoaderUI();
		JMenuItem mntmLoadProject = projectLoaderUI.loadProject(dataGenerator);
		mnFile.add(mntmLoadProject);

		ProjectEditorUI projecteditorUI = new ProjectEditorUI();
		JMenuItem mntmEditProject = projecteditorUI.editProject(dataGenerator);
		mnFile.add(mntmEditProject);
		
		return mnFile;
	}
	
	private JMenu getTableTab() {
		JMenu mnTable = new JMenu("Table");
		
		dataConfiguration.getSegmentSizeDetailedTable();
		JMenuItem mntmShowGeneralLifetimeIDU = new JMenuItem("Show PLD");
		mntmShowGeneralLifetimeIDU.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				PLDShowUI pldShowWidget = new PLDShowUI();
				pldShowWidget.showPLD(configuration, dataConfiguration, dataGenerator);
			}
		});
		mnTable.add(mntmShowGeneralLifetimeIDU);
		
		PhasesPLDUI phasesPLDUI = new PhasesPLDUI();
		JMenuItem mntmShowGeneralLifetimePhasesPLD = phasesPLDUI.showPhasesPLDUI(dataGenerator, configuration, dataConfiguration, tablesConfiguration);
		mnTable.add(mntmShowGeneralLifetimePhasesPLD);
		
		PhasesClustersPLDUI phasesClustersPLDUI = new PhasesClustersPLDUI();
		JMenuItem mntmShowGeneralLifetimePhasesWithClustersPLD = phasesClustersPLDUI.showGeneralLifetimePhasesWithClusters(dataGenerator, configuration, dataConfiguration, tablesConfiguration);
		mnTable.add(mntmShowGeneralLifetimePhasesWithClustersPLD);
		
		TableLifetimeDisplayUI tableLifetimeUI = new TableLifetimeDisplayUI(configuration, dataConfiguration, dataGenerator, tablesConfiguration);	;			
		JMenuItem mntmShowLifetimeTable =tableLifetimeUI.createShowTableMenuItem();
		mnTable.add(mntmShowLifetimeTable);
		
		configuration.setSideMenu();
		
		return mnTable;
	}


	private void createNotUniformlyDistributedButton() {
		configuration.setNotUniformlyDistributedButton(new JButton("Over Time")); 
		configuration.getNotUniformlyDistributedButton().setBounds(1100, 0, 120, 30);
		
		configuration.getNotUniformlyDistributedButton().addMouseListener(new MouseAdapter() {
			@Override
			   public void mouseClicked(MouseEvent e) {
				tablesConfiguration.getLifeTimeTable().notUniformlyDistributed(dataGenerator.getGlobalDataKeeper());
			    
			  } 
		});
		
		configuration.getNotUniformlyDistributedButton().setVisible(false);
		
	}



	private void createUniformlyDistributedButton() {
		configuration.setUniformlyDistributedButton(new JButton("Same Width")); 
		configuration.getUniformlyDistributedButton().setBounds(980, 0, 120, 30);
		
		configuration.getUniformlyDistributedButton().addMouseListener(new MouseAdapter() {
			@Override
			   public void mouseClicked(MouseEvent e) {
				tablesConfiguration.getLifeTimeTable().uniformlyDistributed(1);
			    
			  } 
		});
		
		configuration.getUniformlyDistributedButton().setVisible(false);
	}



	private void createUndoButton() {
		configuration.setUndoButton(new JButton("Undo"));
		configuration.getUndoButton().setBounds(680, 560, 100, 30);
		
		configuration.getUndoButton().addMouseListener(new MouseAdapter() {
			@Override
			   public void mouseClicked(MouseEvent e) {
				if (dataConfiguration.getFirstLevelUndoColumnsZoomArea() !=null) {
					dataConfiguration.setFinalColumnsZoomArea(dataConfiguration.getFirstLevelUndoColumnsZoomArea());
					dataConfiguration.setFinalRowsZoomArea(dataConfiguration.getFirstLevelUndoRowsZoomArea());
					ZoomAreaTableForClusterUI widget = new ZoomAreaTableForClusterUI(configuration, dataGenerator.getGlobalDataKeeper(), tablesConfiguration, dataConfiguration);
					widget.makeZoomAreaTableForCluster();
				}
				
			}
		});
		
		configuration.getUndoButton().setVisible(false);
	}



	private void createPOPUp() {
		configuration.setShowThisToPopup(new JButton("Enlarge"));
		configuration.getShowThisToPopup().setBounds(800, 560, 100, 30);
		
		configuration.getShowThisToPopup().addMouseListener(new MouseAdapter() {
			@Override
			   public void mouseClicked(MouseEvent e) {
				
				EnlargeTable showEnlargmentPopup= new EnlargeTable(dataConfiguration.getFinalRowsZoomArea(),dataConfiguration.getFinalColumnsZoomArea(),dataConfiguration.getSegmentSizeZoomArea());
				showEnlargmentPopup.setBounds(100, 100, 1300, 700);
				
				showEnlargmentPopup.setVisible(true);
			}
		});
		
		configuration.getShowThisToPopup().setVisible(false);
	}



	private void createZoomOutButton() {
		configuration.setZoomOutButton(new JButton("Zoom Out"));
		configuration.getZoomOutButton().setBounds(1110, 560, 100, 30);
		
		configuration.getZoomOutButton().addMouseListener(new MouseAdapter() {
			@Override
			   public void mouseClicked(MouseEvent e) {
				
				configuration.setRowHeight(configuration.getRowHeight() - 2);
				configuration.setColumnWidth(configuration.getColumnWidth() - 1);
				if(configuration.getRowHeight()<1){
					configuration.setRowHeight(1);
				}
				if (configuration.getColumnWidth()<1) {
					configuration.setColumnWidth(1);
				}
				tablesConfiguration.getZoomAreaTable().setZoom(configuration.getRowHeight(), configuration.getColumnWidth());
				
			}
		});
		configuration.getZoomOutButton().setVisible(false);
	}



	private void createZoomInButton() {
		configuration.setZoomInButton(new JButton("Zoom In"));
		configuration.getZoomInButton().setBounds(1000, 560, 100, 30);
		
		
		configuration.getZoomInButton().addMouseListener(new MouseAdapter() {
			@Override
			   public void mouseClicked(MouseEvent e) {
				configuration.setRowHeight(configuration.getRowHeight() + 2);
				configuration.setColumnWidth(configuration.getColumnWidth() + 1);
				tablesConfiguration.getZoomAreaTable().setZoom(configuration.getRowHeight(), configuration.getColumnWidth());
			}
		});
		
		configuration.getZoomInButton().setVisible(false);
	}



	public void optimize() throws IOException{
		
		String description ="Birth Weight:"+"\tDeath Weight:"+"\tChange Weight:"+"\tTotal Cohesion:"+"\tTotal Separation:"+"\n";
		int counter=0;
		for(double weightBirth=0.0; weightBirth<=1.0; weightBirth=weightBirth+0.01){
			
			for(double weightDeath=(1.0-weightBirth); weightDeath>=0.0; weightDeath=weightDeath-0.01){
				
					double weightChange=1.0-(weightBirth+weightDeath);
					TableClusteringMainEngine clusterTableEngine = new TableClusteringMainEngine(dataGenerator.getGlobalDataKeeper(),weightBirth,weightDeath,weightChange);
					clusterTableEngine.extractClusters(dataConfiguration.getNumberOfClusters());
					dataGenerator.getGlobalDataKeeper().setClusterCollectors(clusterTableEngine.getClusterCollectors());
					
					ClusterValidatorMainEngine clusterValidateEngine = new ClusterValidatorMainEngine(dataGenerator.getGlobalDataKeeper());
					clusterValidateEngine.run();
					
					description = description + weightBirth +"\t" + weightDeath +"\t" + weightChange
							+ "\t" + clusterValidateEngine.getTotalCohesion() + "\t" + clusterValidateEngine.getTotalSeparation() + "\t" + (weightBirth+weightDeath+weightChange) + "\n";
			
					counter++;
					System.err.println(counter);
			}
		}
		
		createCSVFile(description);
		
	}
	
	public void getExternalValidityReport() throws IOException{
		
		String description="Birth Weight:"+"\tDeath Weight:"+"\tChange Weight:"+"\n";
		int counter=0;
		
		TableClusteringMainEngine clusterTableEngine = new TableClusteringMainEngine(dataGenerator.getGlobalDataKeeper(),0.333,0.333,0.333);
		clusterTableEngine.extractClusters(4);
		dataGenerator.getGlobalDataKeeper().setClusterCollectors(clusterTableEngine.getClusterCollectors());
		
		ClusterValidatorMainEngine clusterValidateEngine = new ClusterValidatorMainEngine(dataGenerator.getGlobalDataKeeper());
		clusterValidateEngine.run();
		
		description=description+"\n"+"0.333"+"\t"+"0.333"+"\t"+"0.333"
				+"\n"+clusterValidateEngine.getExternalEvaluationReport();
		
		for(double weightBirth=0.0; weightBirth<=1.0; weightBirth=weightBirth+0.5){
			
			for(double weightDeath=(1.0-weightBirth); weightDeath>=0.0; weightDeath=weightDeath-0.5){
				
					double weightChange=1.0-(weightBirth+weightDeath);
					clusterTableEngine = new TableClusteringMainEngine(dataGenerator.getGlobalDataKeeper(),weightBirth,weightDeath,weightChange);
					clusterTableEngine.extractClusters(4);
					dataGenerator.getGlobalDataKeeper().setClusterCollectors(clusterTableEngine.getClusterCollectors());
					
					clusterValidateEngine = new ClusterValidatorMainEngine(dataGenerator.getGlobalDataKeeper());
					clusterValidateEngine.run();
					
					description=description+"\n"+weightBirth+"\t"+weightDeath+"\t"+weightChange
							+"\n"+clusterValidateEngine.getExternalEvaluationReport();
			
					counter++;
					System.err.println(counter);
			}
		}
		 createCSVFile(description);
	}
	
	public void createCSVFile(String description){
		FileWriter fw;
		try {
			fw = new FileWriter("lala.csv");
			
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(description);
			bw.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(description);
	}
	
	public void setDescription(String descr){
		configuration.getDescriptionText().setText(descr);
	}
	
	public GuiConfiguration getConfiguration(){
		return this.configuration;
	}
	public DataConfiguration getDataConfiguration(){
		return this.dataConfiguration;
	}
	public DataTablesConfiguration getTablesConfiguration(){
		return this.tablesConfiguration;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	
}