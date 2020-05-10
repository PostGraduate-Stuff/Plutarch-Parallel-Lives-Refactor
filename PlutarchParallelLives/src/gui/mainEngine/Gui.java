package gui.mainEngine;

import gui.configurations.Configuration;
//try to extract relationship beetween gui and pplSchema and pplTransition
import gui.dialogs.CreateProjectJDialog;
import gui.dialogs.EnlargeTable;
import gui.dialogs.ParametersJDialog;
import gui.dialogs.ProjectInfoDialog;
import gui.tableElements.commons.JvTable;
import gui.tableElements.commons.MyTableModel;
import gui.tableElements.tableConstructors.PldConstruction;
import gui.tableElements.tableConstructors.TableConstructionAllSquaresIncluded;
import gui.tableElements.tableConstructors.TableConstructionClusterTablesPhasesZoomA;
import gui.tableElements.tableConstructors.TableConstructionIDU;
import gui.tableElements.tableConstructors.TableConstructionPhases;
import gui.tableElements.tableConstructors.TableConstructionWithClusters;
import gui.tableElements.tableConstructors.TableConstructionZoomArea;
import gui.tableElements.tableRenderers.IDUHeaderTableRenderer;
import gui.tableElements.tableRenderers.IDUTableRenderer;
import gui.treeElements.TreeConstructionGeneral;
import gui.treeElements.TreeConstructionPhases;
import gui.treeElements.TreeConstructionPhasesWithClusters;
import gui.widgets.GeneralTableIDUWidget;
import gui.widgets.GeneralTablePhasesWidget;
import gui.widgets.LoadProject;
import gui.widgets.ZoomAreaTableForClusterWidget;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.tree.TreePath;

import org.antlr.v4.runtime.RecognitionException;

import phaseAnalyzer.commons.Phase;
import phaseAnalyzer.engine.PhaseAnalyzerMainEngine;
import services.DataService;
import services.PPLFile;
import tableClustering.clusterExtractor.commons.Cluster;
import tableClustering.clusterExtractor.engine.TableClusteringMainEngine;
import tableClustering.clusterValidator.engine.ClusterValidatorMainEngine;
import data.dataKeeper.GlobalDataKeeper;
import data.dataSorters.PldRowSorter;


public class Gui extends JFrame implements ActionListener{

	public static final long serialVersionUID = 1L;
	
	private DataService service = new DataService();
	private PPLFile pplFile;
	private GlobalDataKeeper globalDataKeeper;
	private TableConstructionIDU constructedIDUTable;
	private TableConstructionWithClusters clusterTable;
	private JvTable jvTable;
	private Configuration configuration;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Gui frame = new Gui();
					frame.setVisible(true);
				} catch (Exception e) {
					//return;
					e.printStackTrace();
				}
				
			}
		});
	}
	
	
	
	/**
	 * Create the frame.
	 */
	public Gui() {
		
		configuration = new Configuration();
		service = new DataService();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
				
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmCreateProject = new JMenuItem("Create Project");
		mntmCreateProject.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				CreateProjectJDialog createProjectDialog=new CreateProjectJDialog("","","","","","");

				createProjectDialog.setModal(true);
				
				
				createProjectDialog.setVisible(true);
				
				if(createProjectDialog.getConfirmation()){
					
					createProjectDialog.setVisible(false);
					
					File file = createProjectDialog.getFile();
		            System.out.println(file.toString());
		            
		            String fileName=file.toString();
		            System.out.println("!!"+file.getName());
		          
					try  {
						importData(file);
					} catch (RecognitionException e) {
						
						JOptionPane.showMessageDialog(null, "Something seems wrong with this file");
						return;
					}
					
					
				}
				
		            
				
			}
		});
		mnFile.add(mntmCreateProject);
		
		//LoadProject loadProject = new LoadProject();
		//loadProject.initialize(this);
		//mnFile.add(loadProject.getLoadProject());
		
		
		JMenuItem mntmLoadProject = new JMenuItem("Load Project");
		mntmLoadProject.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				File dir=new File("filesHandler/inis");
				JFileChooser fcOpen1 = new JFileChooser();
				fcOpen1.setCurrentDirectory(dir);
				int returnVal = fcOpen1.showDialog(Gui.this, "Open");
				
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					initiateLoadState();
					importData(fcOpen1.getSelectedFile());
				}
				
			}
		});
		
		mnFile.add(mntmLoadProject);
		JMenuItem mntmEditProject = new JMenuItem("Edit Project");
		mntmEditProject.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				String fileName=null;
				File dir=new File("filesHandler/inis");
				JFileChooser fcOpen1 = new JFileChooser();
				fcOpen1.setCurrentDirectory(dir);
				int returnVal = fcOpen1.showDialog(Gui.this, "Open");
				
				if (returnVal != JFileChooser.APPROVE_OPTION) 
				{
					return;
				}
					
	            File file = fcOpen1.getSelectedFile();
	            PPLFile pplFile = service.readFile(file);

				System.out.println(pplFile.getProjectName());
				
				CreateProjectJDialog createProjectDialog=new CreateProjectJDialog(pplFile.getProjectName(),
						pplFile.getDatasetTxt(),
						pplFile.getInputCsv(),
						pplFile.getOutputAssessment1(),
						pplFile.getOutputAssessment2(),
						pplFile.getTransitionsFile());
			
				createProjectDialog.setModal(true);
				
				createProjectDialog.setVisible(true);
				
				if(createProjectDialog.getConfirmation())
				{
					createProjectDialog.setVisible(false);
					file = createProjectDialog.getFile();
		            System.out.println(file.toString());
		            System.out.println("!!"+file.getName());
		            
					try 
					{
						importData(file);
					} 
					catch (RecognitionException e) 
					{
						JOptionPane.showMessageDialog(null, "Something seems wrong with this file");
						return;
					}
					
				}
					
			}
		});
		mnFile.add(mntmEditProject);
		
		
		JMenu mnTable = new JMenu("Table");
		menuBar.add(mnTable);
		
		JMenuItem mntmShowLifetimeTable = new JMenuItem("Show Full Detailed LifeTime Table");
		mntmShowLifetimeTable.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				if(pplFile == null)
				{
					JOptionPane.showMessageDialog(null, "Select a Project first");
					return;
				}
				
				TableConstructionAllSquaresIncluded table=new TableConstructionAllSquaresIncluded(globalDataKeeper);
				//TODO in service
				table.constructColumns();
				table.constructRows();
				final String[] columns= table.getConstructedColumns();
				final String[][] rows= table.getConstructedRows();
				configuration.setSegmentSizeDetailedTable(table.getSegmentSize());
				configuration.getTabbedPane().setSelectedIndex(0);
				makeDetailedTable(columns,rows,true);
			
			}
		});
		
		JMenuItem mntmShowGeneralLifetimeIDU = new JMenuItem("Show PLD");
		mntmShowGeneralLifetimeIDU.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(pplFile == null)
				{
					JOptionPane.showMessageDialog(null, "Select a Project first");
					return;
				}
				
				configuration.getZoomInButton().setVisible(true);
				configuration.getZoomOutButton().setVisible(true);
				constructedIDUTable = service.createTableConstructionIDU(globalDataKeeper.getAllPPLSchemas(), globalDataKeeper.getAllPPLTransitions());
				TableConstructionIDU table = constructedIDUTable;
				configuration.setSegmentSizeZoomArea(table.getSegmentSize());
				System.out.println("Schemas: "+globalDataKeeper.getAllPPLSchemas().size());
				System.out.println("C: "+table.getConstructedColumns().length+" R: "+table.getConstructedRows().length);

				configuration.setFinalColumnsZoomArea(table.getConstructedColumns());
				configuration.setFinalRowsZoomArea(table.getConstructedRows());
				configuration.getTabbedPane().setSelectedIndex(0);
				makeGeneralTableIDU();
				fillTree();
					
				
			}
		});
		mnTable.add(mntmShowGeneralLifetimeIDU);
		
		JMenuItem mntmShowGeneralLifetimePhasesPLD = new JMenuItem("Show Phases PLD");
		mntmShowGeneralLifetimePhasesPLD.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				if(pplFile == null || pplFile.getProjectName()==null)
				{
					JOptionPane.showMessageDialog(null, "Please select a project first!");
					return;
				}
				
					configuration.setWholeCol(-1);
					ParametersJDialog jD=new ParametersJDialog(false);
					
					jD.setModal(true);
					
					
					jD.setVisible(true);
					
					if(jD.getConfirmation()){
					
						configuration.setTimeWeight(jD.getTimeWeight());
						configuration.setChangeWeight(jD.getChangeWeight());
						configuration.setPreProcessingTime(jD.getPreProcessingTime());
						configuration.setPreProcessingChange(jD.getPreProcessingChange());
						configuration.setNumberOfPhases(jD.getNumberOfPhases());
			            
			            System.out.println(configuration.getTimeWeight()+" "+configuration.getChangeWeight());
			            
			            
			            createPhaseAnalyserEngine();
						
			    		
						if(globalDataKeeper.getPhaseCollectors().size() == 0){
							JOptionPane.showMessageDialog(null, "Extract Phases first");
							return;
						}
						
						TableConstructionPhases table=new TableConstructionPhases(globalDataKeeper);
						//TODO in service
						table.constructColumns();
						table.constructRows();
						final String[] columns= table.getConstructedColumns();
						final String[][] rows= table.getConstructedRows();
						configuration.setSegmentSize(table.getSegmentSize());
						System.out.println("Schemas: "+globalDataKeeper.getAllPPLSchemas().size());
						System.out.println("C: "+columns.length+" R: "+rows.length);
	
						configuration.setFinalColumns(columns);
						configuration.setFinalRows(rows);
						configuration.getTabbedPane().setSelectedIndex(0);
						makeGeneralTablePhases();
						fillPhasesTree();
						
					}
				
				
				
			}
		});
		mnTable.add(mntmShowGeneralLifetimePhasesPLD);
		
		JMenuItem mntmShowGeneralLifetimePhasesWithClustersPLD = new JMenuItem("Show Phases With Clusters PLD");
		mntmShowGeneralLifetimePhasesWithClustersPLD.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				configuration.setWholeCol(-1);
				if(pplFile == null)
				{
					JOptionPane.showMessageDialog(null, "Please select a project first!");
					return;
				}
					ParametersJDialog jD=new ParametersJDialog(true);
					
					jD.setModal(true);
					
					jD.setVisible(true);
					
					if(jD.getConfirmation()){
					
			            configuration.setTimeWeight(jD.getTimeWeight());
			            configuration.setChangeWeight(jD.getChangeWeight());
			            configuration.setPreProcessingTime(jD.getPreProcessingTime());
			            configuration.setPreProcessingChange(jD.getPreProcessingChange());
			            configuration.setNumberOfPhases(jD.getNumberOfPhases());
			            configuration.setNumberOfClusters(jD.getNumberOfClusters());
			            configuration.setBirthWeight(jD.geBirthWeight());
			            configuration.setDeathWeight(jD.getDeathWeight());
			            configuration.setChangeWeightCl(jD.getChangeWeightCluster());
			            
			            System.out.println(configuration.getTimeWeight()+" "+configuration.getChangeWeight());
			            
			            /*PhaseAnalyzerMainEngine mainPhaseEngine= service.createPhaseAnalyserEngine(configuration,globalDataKeeper,pplFile);
			    		service.connectTransitionsWithPhases(mainPhaseEngine, globalDataKeeper.getAllPPLTransitions());
			    		
			    		globalDataKeeper.setPhaseCollectors(mainPhaseEngine.getPhaseCollectors());*/
			            
			    		createPhaseAnalyserEngine();
			    		
			    		
			    		createTableClusteringMainEngine(configuration.getNumberOfClusters());
			    		/*TableClusteringMainEngine mainClusterEngine = service.createTableClusteringMainEngine(globalDataKeeper, configuration.getNumberOfClusters());
			    		globalDataKeeper.setClusterCollectors(mainClusterEngine.getClusterCollectors());
			    		*/
						if(globalDataKeeper.getPhaseCollectors().size()==0)
						{
							JOptionPane.showMessageDialog(null, "Extract Phases first");
							return;
						}
						TableConstructionWithClusters table=new TableConstructionWithClusters(globalDataKeeper);
						//TODO in service
						table.constructColumns();
						table.constructRows();
						final String[] columns= table.getConstructedColumns();
						final String[][] rows= table.getConstructedRows();
						configuration.setSegmentSize(table.getSegmentSize());
						System.out.println("Schemas: "+globalDataKeeper.getAllPPLSchemas().size());
						System.out.println("C: "+columns.length+" R: "+rows.length);
	
						configuration.setFinalColumns(columns);
						configuration.setFinalRows(rows);
						configuration.getTabbedPane().setSelectedIndex(0);
						makeGeneralTablePhases();
						fillClustersTree(globalDataKeeper.getClusterCollectors().get(0).getClusters());
							
					}
				
			}
		});
		mnTable.add(mntmShowGeneralLifetimePhasesWithClustersPLD);
		
		
		mnTable.add(mntmShowLifetimeTable);
		

		configuration.getSideMenu().setBounds(0, 0, 280, 600);
		configuration.getSideMenu().setBackground(Color.DARK_GRAY);
		
		
		
		GroupLayout gl_sideMenu = new GroupLayout(configuration.getSideMenu());
		gl_sideMenu.setHorizontalGroup(
				gl_sideMenu.createParallelGroup(Alignment.LEADING)
		);
		gl_sideMenu.setVerticalGroup(
				gl_sideMenu.createParallelGroup(Alignment.LEADING)
		);
		
		
		configuration.getSideMenu().setLayout(gl_sideMenu);
		
		configuration.getTablesTreePanel().setBounds(10, 400, 260, 180);
		configuration.getTablesTreePanel().setBackground(Color.LIGHT_GRAY);
		
		GroupLayout gl_tablesTreePanel = new GroupLayout(configuration.getTablesTreePanel());
		gl_tablesTreePanel.setHorizontalGroup(
				gl_tablesTreePanel.createParallelGroup(Alignment.LEADING)
		);
		gl_tablesTreePanel.setVerticalGroup(
				gl_tablesTreePanel.createParallelGroup(Alignment.LEADING)
		);
		configuration.getTablesTreePanel().setLayout(gl_tablesTreePanel);
		
		configuration.setTreeLabel(new JLabel());
		configuration.getTreeLabel().setBounds(10, 370, 260, 40);
		configuration.getTreeLabel().setForeground(Color.WHITE);
		configuration.getTreeLabel().setText("Tree");
		
		configuration.getDescriptionPanel().setBounds(10, 190, 260, 180);
		configuration.getDescriptionPanel().setBackground(Color.LIGHT_GRAY);
		
		GroupLayout gl_descriptionPanel = new GroupLayout(configuration.getDescriptionPanel());
		gl_descriptionPanel.setHorizontalGroup(
				gl_descriptionPanel.createParallelGroup(Alignment.LEADING)
		);
		gl_descriptionPanel.setVerticalGroup(
				gl_descriptionPanel.createParallelGroup(Alignment.LEADING)
		);
		
		configuration.getDescriptionPanel().setLayout(gl_descriptionPanel);
		
		configuration.setDescriptionText(new JTextArea());
		configuration.getDescriptionText().setBounds(5, 5, 250, 170);
		configuration.getDescriptionText().setForeground(Color.BLACK);
		configuration.getDescriptionText().setText("");
		configuration.getDescriptionText().setBackground(Color.LIGHT_GRAY);
		
		configuration.getDescriptionPanel().add(configuration.getDescriptionText());
		
		
		configuration.setDescriptionLabel(new JLabel());
		configuration.getDescriptionLabel().setBounds(10, 160, 260, 40);
		configuration.getDescriptionLabel().setForeground(Color.WHITE);
		configuration.getDescriptionLabel().setText("Description");
		
		configuration.getSideMenu().add(configuration.getTreeLabel());
		configuration.getSideMenu().add(configuration.getTablesTreePanel());
		
		configuration.getSideMenu().add(configuration.getDescriptionLabel());
		configuration.getSideMenu().add(configuration.getDescriptionPanel());

		configuration.getLifeTimePanel().add(configuration.getSideMenu());
		
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
		
		configuration.setMnProject(new JMenu("Project"));
		menuBar.add(configuration.getMnProject());
		
		configuration.setMntmInfo(new JMenuItem("Info"));
		configuration.getMntmInfo().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				
				if(pplFile != null){
					
					
					System.out.println("Project Name:"+pplFile.getProjectName());
					System.out.println("Dataset txt:"+pplFile.getDatasetTxt());
					System.out.println("Input Csv:"+pplFile.getInputCsv());
					System.out.println("Output Assessment1:"+pplFile.getOutputAssessment1());
					System.out.println("Output Assessment2:"+pplFile.getOutputAssessment2());
					System.out.println("Transitions File:"+pplFile.getTransitionsFile());
					
					System.out.println("Schemas:"+globalDataKeeper.getAllPPLSchemas().size());
					System.out.println("Transitions:"+globalDataKeeper.getAllPPLTransitions().size());
					System.out.println("Tables:"+globalDataKeeper.getAllPPLTables().size());
					
					
					ProjectInfoDialog infoDialog = new ProjectInfoDialog(pplFile.getProjectName(),
																		 pplFile.getDatasetTxt(),
																		 pplFile.getInputCsv(),
																		 pplFile.getTransitionsFile(),
																		 globalDataKeeper.getAllPPLSchemas().size(),
																		 globalDataKeeper.getAllPPLTransitions().size(), 
																		 globalDataKeeper.getAllPPLTables().size());
					
					infoDialog.setVisible(true);
				}
				else{
					JOptionPane.showMessageDialog(null, "Select a Project first");
					return;
				}
				
				
				
				
			}
		});
		configuration.getMnProject().add(configuration.getMntmInfo());
		buttonHelp.setBounds(900,900 , 80, 40);
		menuBar.add(buttonHelp);
		
		
		
		configuration.setContentPane(new JPanel());
		
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
		
		
		configuration.getTabbedPane().addTab("LifeTime Table", null, configuration.getLifeTimePanel(), null);
		
		GroupLayout gl_lifeTimePanel = new GroupLayout(configuration.getLifeTimePanel());
		gl_lifeTimePanel.setHorizontalGroup(
			gl_lifeTimePanel.createParallelGroup(Alignment.LEADING)
				.addGap(0, 1469, Short.MAX_VALUE)
		);
		gl_lifeTimePanel.setVerticalGroup(
			gl_lifeTimePanel.createParallelGroup(Alignment.LEADING)
				.addGap(0, 743, Short.MAX_VALUE)
		);
		configuration.getLifeTimePanel().setLayout(gl_lifeTimePanel);
		
		
		configuration.setGeneralTableLabel(new JLabel("Parallel Lives Diagram"));
		configuration.getGeneralTableLabel().setBounds(300, 0, 150, 30);
		configuration.getGeneralTableLabel().setForeground(Color.BLACK);
		
		configuration.setZoomAreaLabel(new JLabel());
		configuration.getZoomAreaLabel().setText("<HTML>Z<br>o<br>o<br>m<br><br>A<br>r<br>e<br>a</HTML>");
		configuration.getZoomAreaLabel().setBounds(1255, 325, 15, 300);
		configuration.getZoomAreaLabel().setForeground(Color.BLACK);
		
		configuration.setZoomInButton(new JButton("Zoom In"));
		configuration.getZoomInButton().setBounds(1000, 560, 100, 30);
		
		
		
		configuration.getZoomInButton().addMouseListener(new MouseAdapter() {
			@Override
			   public void mouseClicked(MouseEvent e) {
				configuration.setRowHeight(configuration.getRowHeight() + 2);
				configuration.setColumnWidth(configuration.getColumnWidth() + 1);
//				rowHeight=rowHeight+2;
//				columnWidth=columnWidth+1;
//				zoomAreaTable.setZoom(rowHeight,columnWidth);
				configuration.getZoomAreaTable().setZoom(configuration.getRowHeight(), configuration.getColumnWidth());

				
			}
		});
		
		configuration.setZoomOutButton(new JButton("Zoom Out"));
		configuration.getZoomOutButton().setBounds(1110, 560, 100, 30);
		
		configuration.getZoomOutButton().addMouseListener(new MouseAdapter() {
			@Override
			   public void mouseClicked(MouseEvent e) {
				
				configuration.setRowHeight(configuration.getRowHeight() - 2);
				//rowHeight=rowHeight-2;
				configuration.setColumnWidth(configuration.getColumnWidth() - 1);
				//columnWidth=columnWidth-1;
				if(configuration.getRowHeight()<1){
					configuration.setRowHeight(1);
				}
				if (configuration.getColumnWidth()<1) {
					configuration.setColumnWidth(1);
				}
				configuration.getZoomAreaTable().setZoom(configuration.getRowHeight(), configuration.getColumnWidth());
				
			}
		});
		
		configuration.getZoomInButton().setVisible(false);
		configuration.getZoomOutButton().setVisible(false);
		
		
		configuration.setShowThisToPopup(new JButton("Enlarge"));
		configuration.getShowThisToPopup().setBounds(800, 560, 100, 30);
		
		configuration.getShowThisToPopup().addMouseListener(new MouseAdapter() {
			@Override
			   public void mouseClicked(MouseEvent e) {
				
				EnlargeTable showEnlargmentPopup= new EnlargeTable(configuration.getFinalRowsZoomArea(),configuration.getFinalColumnsZoomArea(),configuration.getSegmentSizeZoomArea());
				showEnlargmentPopup.setBounds(100, 100, 1300, 700);
				
				showEnlargmentPopup.setVisible(true);
				
				
			}
		});
		
		configuration.getShowThisToPopup().setVisible(false);
		
		
		configuration.setUndoButton(new JButton("Undo"));
		configuration.getUndoButton().setBounds(680, 560, 100, 30);
		
		configuration.getUndoButton().addMouseListener(new MouseAdapter() {
			@Override
			   public void mouseClicked(MouseEvent e) {
				if (configuration.getFirstLevelUndoColumnsZoomArea() !=null) {
					configuration.setFinalColumnsZoomArea(configuration.getFirstLevelUndoColumnsZoomArea());
					configuration.setFinalRowsZoomArea(configuration.getFirstLevelUndoRowsZoomArea());
					ZoomAreaTableForClusterWidget widget = new ZoomAreaTableForClusterWidget(configuration, globalDataKeeper, service);
					widget.makeZoomAreaTableForCluster();
				}
				
			}
		});
		
		configuration.getUndoButton().setVisible(false);
		
		
		configuration.setUniformlyDistributedButton(new JButton("Same Width")); 
		configuration.getUniformlyDistributedButton().setBounds(980, 0, 120, 30);
		
		configuration.getUniformlyDistributedButton().addMouseListener(new MouseAdapter() {
			@Override
			   public void mouseClicked(MouseEvent e) {
				configuration.getLifeTimeTable().uniformlyDistributed(1);
			    
			  } 
		});
		
		configuration.getUniformlyDistributedButton().setVisible(false);
		
		configuration.setNotUniformlyDistributedButton(new JButton("Over Time")); 
		configuration.getNotUniformlyDistributedButton().setBounds(1100, 0, 120, 30);
		
		configuration.getNotUniformlyDistributedButton().addMouseListener(new MouseAdapter() {
			@Override
			   public void mouseClicked(MouseEvent e) {
				configuration.getLifeTimeTable().notUniformlyDistributed(globalDataKeeper);
			    
			  } 
		});
		
		configuration.getNotUniformlyDistributedButton().setVisible(false);
		
		configuration.getLifeTimePanel().add(configuration.getZoomInButton());
		configuration.setLifeTimePanel(	configuration.getLifeTimePanel());
		configuration.getLifeTimePanel().add(configuration.getUndoButton());
		configuration.getLifeTimePanel().add(configuration.getZoomOutButton());
		configuration.getLifeTimePanel().add(configuration.getUniformlyDistributedButton());
		configuration.getLifeTimePanel().add(configuration.getNotUniformlyDistributedButton());
		configuration.getLifeTimePanel().add(configuration.getShowThisToPopup());

		configuration.getLifeTimePanel().add(configuration.getZoomAreaLabel());
		
		configuration.getLifeTimePanel().add(configuration.getGeneralTableLabel());
		
		configuration.getContentPane().setLayout(gl_contentPane);
		
		pack();
		setBounds(30, 30, 1300, 700);

		
	}
	
	
	




	public void makeDetailedTable(String[] columns , String[][] rows, final boolean levelized){
		
		configuration.setDetailedModel(new MyTableModel(columns,rows));
		
		final JvTable tmpLifeTimeTable= new JvTable(configuration.getDetailedModel());
		
		tmpLifeTimeTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
		if(levelized==true){
			for(int i=0; i<tmpLifeTimeTable.getColumnCount(); i++){
				if(i==0){
					tmpLifeTimeTable.getColumnModel().getColumn(0).setPreferredWidth(150);
					tmpLifeTimeTable.getColumnModel().getColumn(0).setMaxWidth(150);
					tmpLifeTimeTable.getColumnModel().getColumn(0).setMinWidth(150);
				}
				else{
					if(tmpLifeTimeTable.getColumnName(i).contains("v")){
						tmpLifeTimeTable.getColumnModel().getColumn(i).setPreferredWidth(100);
						tmpLifeTimeTable.getColumnModel().getColumn(i).setMaxWidth(100);
						tmpLifeTimeTable.getColumnModel().getColumn(i).setMinWidth(100);
					}
					else{
						tmpLifeTimeTable.getColumnModel().getColumn(i).setPreferredWidth(25);
						tmpLifeTimeTable.getColumnModel().getColumn(i).setMaxWidth(25);
						tmpLifeTimeTable.getColumnModel().getColumn(i).setMinWidth(25);
					}
				}
			}
		}
		else{
			for(int i=0; i<tmpLifeTimeTable.getColumnCount(); i++){
				if(i==0){
					tmpLifeTimeTable.getColumnModel().getColumn(0).setPreferredWidth(150);
					tmpLifeTimeTable.getColumnModel().getColumn(0).setMaxWidth(150);
					tmpLifeTimeTable.getColumnModel().getColumn(0).setMinWidth(150);
				}
				else{
					
					tmpLifeTimeTable.getColumnModel().getColumn(i).setPreferredWidth(20);
					tmpLifeTimeTable.getColumnModel().getColumn(i).setMaxWidth(20);
					tmpLifeTimeTable.getColumnModel().getColumn(i).setMinWidth(20);
					
				}
			}
		}
		
		tmpLifeTimeTable.setName("LifeTimeTable");
		
		
		tmpLifeTimeTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer()
		{
		    
			public static final long serialVersionUID = 1L;

			@Override
		    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
		    {
		        final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		        
		        String tmpValue=(String) table.getValueAt(row, column);
		        String columnName=table.getColumnName(column);
		        Color fr=new Color(0,0,0);
		        c.setForeground(fr);
		        
		        
		        if(configuration.getSelectedColumn()==0){
		        	if (isSelected){
		        		Color cl = new Color(255,69,0, 100);

		        		c.setBackground(cl);
		        		
		        		return c;
		        	}
		        }
		        else{
		        	if (isSelected && hasFocus){
			        	
		        		c.setBackground(Color.YELLOW);
		        		return c;
			        }
		        	
		        }
		        
		        try{
		        	int numericValue=Integer.parseInt(tmpValue);
		        	Color insersionColor=null;
		        	
		        	if(columnName.equals("I")){
		        		if(numericValue==0){
		        			insersionColor=new Color(255,231,186);
		        		}
		        		else if(numericValue>0 && numericValue<=configuration.getSegmentSizeDetailedTable()[0]){
		        			
		        			insersionColor=new Color(193,255,193);
			        	}
		        		else if(numericValue>configuration.getSegmentSizeDetailedTable()[0] && numericValue<=2*configuration.getSegmentSizeDetailedTable()[0]){
		        			insersionColor=new Color(84,255,159);
		        		}
		        		else if(numericValue>2*configuration.getSegmentSizeDetailedTable()[0] && numericValue<=3*configuration.getSegmentSizeDetailedTable()[0]){
		        			
		        			insersionColor=new Color(0,201,87);
		        		}
		        		else{
		        			insersionColor=new Color(0,100,0);
		        		}
		        		c.setBackground(insersionColor);
		        	}
		        	
		        	if(columnName.equals("U")){
		        		if(numericValue==0){
		        			insersionColor=new Color(255,231,186);
		        		}
		        		else if(numericValue>0 && numericValue<=configuration.getSegmentSizeDetailedTable()[1]){
		        			
		        			insersionColor=new Color(176,226,255);
			        	}
		        		else if(numericValue>configuration.getSegmentSizeDetailedTable()[1] && numericValue<=2*configuration.getSegmentSizeDetailedTable()[1]){
		        			insersionColor=new Color(92,172,238);
		        		}
		        		else if(numericValue>2*configuration.getSegmentSizeDetailedTable()[1] && numericValue<=3*configuration.getSegmentSizeDetailedTable()[1]){
		        			
		        			insersionColor=new Color(28,134,238);
		        		}
		        		else{
		        			insersionColor=new Color(16,78,139);
		        		}
		        		c.setBackground(insersionColor);
		        	}
		        	
		        	if(columnName.equals("D")){
		        		if(numericValue==0){
		        			insersionColor=new Color(255,231,186);
		        		}
		        		else if(numericValue>0 && numericValue<=configuration.getSegmentSizeDetailedTable()[2]){
		        			
		        			insersionColor=new Color(255,106,106);
			        	}
		        		else if(numericValue>configuration.getSegmentSizeDetailedTable()[2] && numericValue<=2*configuration.getSegmentSizeDetailedTable()[2]){
		        			insersionColor=new Color(255,0,0);
		        		}
		        		else if(numericValue>2*configuration.getSegmentSizeDetailedTable()[2] && numericValue<=3*configuration.getSegmentSizeDetailedTable()[2]){
		        			
		        			insersionColor=new Color(205,0,0);
		        		}
		        		else{
		        			insersionColor=new Color(139,0,0);
		        		}
		        		c.setBackground(insersionColor);
		        	}
		        	
		        	return c;
		        }
		        catch(Exception e){
		        		
		        		if(tmpValue.equals("")){
		        			c.setBackground(Color.black);
			        		return c; 
		        		}
		        		else{
		        			if(columnName.contains("v")){
		        				c.setBackground(Color.lightGray);
		        				if(levelized==false){
		        					setToolTipText(columnName);
		        				}
		        			}
		        			else{
		        				Color tableNameColor=new Color(205,175,149);
		        				c.setBackground(tableNameColor);
		        			}
			        		return c; 
		        		}
		        		
		        		
		        }
		    }
		});
		
		tmpLifeTimeTable.setOpaque(true);
	    
		tmpLifeTimeTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
	    tmpLifeTimeTable.getSelectionModel().addListSelectionListener(new RowListener());
	    tmpLifeTimeTable.getColumnModel().getSelectionModel().addListSelectionListener(new ColumnListener());
	    
	    
	    
	    JScrollPane detailedScrollPane=new JScrollPane();
	    detailedScrollPane.setViewportView(tmpLifeTimeTable);
	    detailedScrollPane.setAlignmentX(0);
	    detailedScrollPane.setAlignmentY(0);
	    detailedScrollPane.setBounds(0,0,1280,650);
	    detailedScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	    detailedScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
       
	    detailedScrollPane.setCursor(getCursor());
	    
	    JDialog detailedDialog=new JDialog();
	    detailedDialog.setBounds(100, 100, 1300, 700);
	    
	    JPanel panelToAdd=new JPanel();
	    
	    GroupLayout gl_panel = new GroupLayout(panelToAdd);
	    gl_panel.setHorizontalGroup(
	    		gl_panel.createParallelGroup(Alignment.LEADING)
		);
	    gl_panel.setVerticalGroup(
	    		gl_panel.createParallelGroup(Alignment.LEADING)
		);
		panelToAdd.setLayout(gl_panel);
	    
	    panelToAdd.add(detailedScrollPane);
	    detailedDialog.getContentPane().add(panelToAdd);
	    detailedDialog.setVisible(true);
		
		
	}
	
	public class RowListener implements ListSelectionListener {
        public void valueChanged(ListSelectionEvent event) {
            if (event.getValueIsAdjusting()) {
                return;
            }
            
            int selectedRow=configuration.getLifeTimeTable().getSelectedRow();
            configuration.getSelectedRows().add(selectedRow);
           
     
        }
    }
	
	public class ColumnListener implements ListSelectionListener {
        public void valueChanged(ListSelectionEvent event) {
            if (event.getValueIsAdjusting()) {
                return;
            }
           
        }
    }


	
	
	public void optimize() throws IOException{
		
		String lalaString="Birth Weight:"+"\tDeath Weight:"+"\tChange Weight:"+"\tTotal Cohesion:"+"\tTotal Separation:"+"\n";
		int counter=0;
		for(double wb=0.0; wb<=1.0; wb=wb+0.01){
			
			for(double wd=(1.0-wb); wd>=0.0; wd=wd-0.01){
				
					double wc=1.0-(wb+wd);
					TableClusteringMainEngine mainEngine2 = new TableClusteringMainEngine(globalDataKeeper,wb,wd,wc);
					mainEngine2.extractClusters(configuration.getNumberOfClusters());
					globalDataKeeper.setClusterCollectors(mainEngine2.getClusterCollectors());
					
					ClusterValidatorMainEngine lala = new ClusterValidatorMainEngine(globalDataKeeper);
					lala.run();
					
					lalaString=lalaString+wb+"\t"+wd+"\t"+wc
							+"\t"+lala.getTotalCohesion()+"\t"+lala.getTotalSeparation()+"\t"+(wb+wd+wc)+"\n";
			
					counter++;
					System.err.println(counter);
				
				
			}
			
			
			
		}
		
		FileWriter fw;
		try {
			fw = new FileWriter("lala.csv");
			
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(lalaString);
			bw.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		System.out.println(lalaString);
		
		
	}
	
	public void getExternalValidityReport() throws IOException{
		
		String lalaString="Birth Weight:"+"\tDeath Weight:"+"\tChange Weight:"+"\n";
		int counter=0;
		
		TableClusteringMainEngine mainEngine2 = new TableClusteringMainEngine(globalDataKeeper,0.333,0.333,0.333);
		mainEngine2.extractClusters(4);
		globalDataKeeper.setClusterCollectors(mainEngine2.getClusterCollectors());
		
		ClusterValidatorMainEngine lala = new ClusterValidatorMainEngine(globalDataKeeper);
		lala.run();
		
		lalaString=lalaString+"\n"+"0.333"+"\t"+"0.333"+"\t"+"0.333"
				+"\n"+lala.getExternalEvaluationReport();
		
		for(double wb=0.0; wb<=1.0; wb=wb+0.5){
			
			for(double wd=(1.0-wb); wd>=0.0; wd=wd-0.5){
				
					double wc=1.0-(wb+wd);
					mainEngine2 = new TableClusteringMainEngine(globalDataKeeper,wb,wd,wc);
					mainEngine2.extractClusters(4);
					globalDataKeeper.setClusterCollectors(mainEngine2.getClusterCollectors());
					
					lala = new ClusterValidatorMainEngine(globalDataKeeper);
					lala.run();
					
					lalaString=lalaString+"\n"+wb+"\t"+wd+"\t"+wc
							+"\n"+lala.getExternalEvaluationReport();
			
					counter++;
					System.err.println(counter);
				
				
			}
			
			
			
		}
		
		FileWriter fw;
		try {
			fw = new FileWriter("lala.csv");
			
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(lalaString);
			bw.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		System.out.println(lalaString);
		
		
	}
	
	
	
	public void fillPhasesTree(){
		
		 TreeConstructionPhases tc=new TreeConstructionPhases(globalDataKeeper);
		 configuration.setTablesTree(tc.constructTree());
		 
		 configuration.getTablesTree().addTreeSelectionListener(new TreeSelectionListener () {
			    public void valueChanged(TreeSelectionEvent ae) { 
			    	TreePath selection = ae.getPath();
			    	configuration.getSelectedFromTree().add(selection.getLastPathComponent().toString());
			    	System.out.println(selection.getLastPathComponent().toString()+" is selected");
			    	
			    }
		 });
		 
		 configuration.getTablesTree().addMouseListener(new MouseAdapter() {
				@Override
				   public void mouseReleased(MouseEvent e) {
					
						if(SwingUtilities.isRightMouseButton(e)){
							System.out.println("Right Click Tree");
							
							final JPopupMenu popupMenu = new JPopupMenu();
					        JMenuItem showDetailsItem = new JMenuItem("Show This into the Table");
					        showDetailsItem.addActionListener(new ActionListener() {

					            @Override
					            public void actionPerformed(ActionEvent e) {
					          
					            	configuration.getLifeTimeTable().repaint();
					            	
					            }
					        });
					        popupMenu.add(showDetailsItem);
					        popupMenu.show(configuration.getTablesTree(), e.getX(),e.getY());
							        							        
							
						}
					
				   }
			});
		 
		 configuration.getTreeScrollPane().setViewportView(configuration.getTablesTree());
		 configuration.getTreeScrollPane().setBounds(5, 5, 250, 170);
		 configuration.getTreeScrollPane().setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		 configuration.getTreeScrollPane().setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		 configuration.getTreeScrollPane().add(configuration.getTreeScrollPane());
		 
		 configuration.getTreeLabel().setText("Phases Tree");

		 configuration.getSideMenu().revalidate();
		 configuration.getSideMenu().repaint();
		
	}
	
	public void fillClustersTree(ArrayList<Cluster> clusters){
		
		 TreeConstructionPhasesWithClusters treePhaseWithClusters = new TreeConstructionPhasesWithClusters(clusters);
		 configuration.setTablesTree(treePhaseWithClusters.constructTree());
		 
		 configuration.getTablesTree().addTreeSelectionListener(new TreeSelectionListener () {
			    public void valueChanged(TreeSelectionEvent ae) { 
			    	TreePath selection = ae.getPath();
			    	configuration.getSelectedFromTree().add(selection.getLastPathComponent().toString());
			    	System.out.println(selection.getLastPathComponent().toString()+" is selected");
			    	
			    }
		 });
		 
		 configuration.getTablesTree().addMouseListener(new MouseAdapter() {
				@Override
				   public void mouseReleased(MouseEvent e) {
					
						if(SwingUtilities.isRightMouseButton(e)){
							System.out.println("Right Click Tree");
							
							final JPopupMenu popupMenu = new JPopupMenu();
					        JMenuItem showDetailsItem = new JMenuItem("Show This into the Table");
					        showDetailsItem.addActionListener(new ActionListener() {

					            @Override
					            public void actionPerformed(ActionEvent e) {
					          
					            	configuration.getLifeTimeTable().repaint();
					            	
					            }
					        });
					        popupMenu.add(showDetailsItem);
					        popupMenu.show(configuration.getTablesTree(), e.getX(),e.getY());
							        	
						}
					
				   }
			});
		 
		 configuration.getTreeScrollPane().setViewportView(configuration.getTablesTree());
		 
		 
		 configuration.getTreeScrollPane().setBounds(5, 5, 250, 170);
		 configuration.getTreeScrollPane().setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		 configuration.getTreeScrollPane().setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		 configuration.getTablesTreePanel().add(configuration.getTreeScrollPane());

		 configuration.getTreeLabel().setText("Clusters Tree");

		 configuration.getSideMenu().revalidate();
		 configuration.getSideMenu().repaint();
		 
		
	}
	
	public void setDescription(String descr){
		configuration.getDescriptionText().setText(descr);
	}
	
	
	public void initiateLoadState()
	{
		configuration.setTimeWeight((float)0.5); 
		configuration.setChangeWeight((float)0.5);
		configuration.setPreProcessingTime(false);
		configuration.setPreProcessingChange(false);
		
		
	}
	public void makeGeneralTableIDU()
	{
		GeneralTableIDUWidget widget = new GeneralTableIDUWidget(configuration, globalDataKeeper, service);
		widget.makeGeneralTableIDU();
		widget.constructGeneralTableIDU();
	}
	
	
	public void importData(File file)
	{
		
		importDataFromFile(file);
		constructedIDUTable = service.createTableConstructionIDU(globalDataKeeper.getAllPPLSchemas(), globalDataKeeper.getAllPPLTransitions());
		
		setIDUPanel();
			
		makeGeneralTableIDU();
		
		createPhaseAnalyserEngine();
		
		configuration.setNumberOfClusters(14);
		createTableClusteringMainEngine(configuration.getNumberOfClusters());
		
		fillTable();
		printPPLData();
		fillTree();
	}
	
	
	
	
	public void setIDUPanel()
	{
		configuration.setFinalColumnsZoomArea(constructedIDUTable.getConstructedColumns());
		configuration.setFinalRowsZoomArea(constructedIDUTable.getConstructedRows());
		configuration.getTabbedPane().setSelectedIndex(0);
		configuration.setSegmentSizeZoomArea(constructedIDUTable.getSegmentSize());
	}
	
	
	
	
	
	
	public void fillTable() 
	{
		if(globalDataKeeper.getPhaseCollectors().size()==0)
		{
			return;
		}
		constructTableWithClusters();
		
		configuration.getTabbedPane().setSelectedIndex(0);
		configuration.getUniformlyDistributedButton().setVisible(true);
		configuration.getNotUniformlyDistributedButton().setVisible(true);
		
		makeGeneralTablePhases();
		
		fillClustersTree(globalDataKeeper.getClusterCollectors().get(0).getClusters());
	}
	
	public void constructTableWithClusters() 
	{
		clusterTable = constructClustersTable();
		
		final String[] columns= clusterTable.getConstructedColumns();
		final String[][] rows= clusterTable.getConstructedRows();
		
		configuration.setSegmentSize(clusterTable.getSegmentSize());
		configuration.setFinalColumns(columns);
		configuration.setFinalRows(rows);
		configuration.setSelectedRows(new ArrayList<Integer>());
	}
	
	public TableConstructionWithClusters constructClustersTable() 
	{
		TableConstructionWithClusters table=new TableConstructionWithClusters(globalDataKeeper);
		
		table.constructColumns();
		table.constructRows();
		
		return table;
	}
	
	public void makeGeneralTablePhases(){
		GeneralTablePhasesWidget widget = new GeneralTablePhasesWidget(configuration, globalDataKeeper,service) ;
		widget.initializeGeneralTablePhases();
	}
	
	
	public void printPPLData() 
	{
		System.out.println("Schemas:"+globalDataKeeper.getAllPPLSchemas().size());
		System.out.println("Transitions:"+globalDataKeeper.getAllPPLTransitions().size());
		System.out.println("Tables:"+globalDataKeeper.getAllPPLTables().size());
	}
	
	public Configuration getConfiguration() 
	{
		return configuration;
	}

	
	public void fillTree(){
		
		 TreeConstructionGeneral treeConstruction=new TreeConstructionGeneral(globalDataKeeper.getAllPPLSchemas());
		 configuration.setTablesTree(new JTree());
		 configuration.setTablesTree(treeConstruction.constructTree());
		 configuration.getTablesTree().addTreeSelectionListener(new TreeSelectionListener () {
			    public void valueChanged(TreeSelectionEvent ae) { 
			    	TreePath selection = ae.getPath();
			    	configuration.getSelectedFromTree().add(selection.getLastPathComponent().toString());
			    	System.out.println(selection.getLastPathComponent().toString()+" is selected");
			    	
			    }
		 });
		 
		 configuration.getTablesTree().addMouseListener(new MouseAdapter() {
				@Override
				   public void mouseReleased(MouseEvent e) {
					
						if(SwingUtilities.isRightMouseButton(e)){
							System.out.println("Right Click Tree");
								
									final JPopupMenu popupMenu = new JPopupMenu();
							        JMenuItem showDetailsItem = new JMenuItem("Show This into the Table");
							        showDetailsItem.addActionListener(new ActionListener() {
		
							            @Override
							            public void actionPerformed(ActionEvent e) {
							          
							            	configuration.getLifeTimeTable().repaint();
							            	
							            }
							        });
							        popupMenu.add(showDetailsItem);
							        popupMenu.show(configuration.getTablesTree(), e.getX(),e.getY());
							        							        
								//}
							//}
						}
					
				   }
			});
		 
		 configuration.getTreeScrollPane().setViewportView(configuration.getTablesTree());
		 
		 configuration.getTreeScrollPane().setBounds(5, 5, 250, 170);
		 configuration.getTreeScrollPane().setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		 configuration.getTreeScrollPane().setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		 configuration.getTablesTreePanel().add(configuration.getTreeScrollPane());
		 
		 configuration.getTreeLabel().setText("General Tree");

		 configuration.getSideMenu().revalidate();
		 configuration.getSideMenu().repaint();		
		
	}

	public void setGlobalData(){
		try{
			globalDataKeeper=new GlobalDataKeeper(pplFile.getDatasetTxt(), pplFile.getTransitionsFile());
			globalDataKeeper.setData();
			System.out.println(globalDataKeeper.getAllPPLTables().size());
			
	        System.out.println(pplFile.getFile().toString());

		}
		catch(Exception ex){
			
		}
	}

	
	public void importDataFromFile(File file)
	{
		pplFile = service.readFile(file);
		globalDataKeeper= service.setGlobalData(pplFile.getDatasetTxt(), pplFile.getTransitionsFile());
		
	}

	

	public void createPhaseAnalyserEngine()
	{
		PhaseAnalyzerMainEngine mainPhaseEngine= service.createPhaseAnalyserEngine(configuration,globalDataKeeper,pplFile);
		service.connectTransitionsWithPhases(mainPhaseEngine, globalDataKeeper.getAllPPLTransitions());
		globalDataKeeper.setPhaseCollectors(mainPhaseEngine.getPhaseCollectors());
	}
	
	
	public void createTableClusteringMainEngine(int numberOfClusters)
	{
		TableClusteringMainEngine mainClusterEngine = service.createTableClusteringMainEngine(globalDataKeeper, numberOfClusters);
		globalDataKeeper.setClusterCollectors(mainClusterEngine.getClusterCollectors());
		
	}
	


	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	
}