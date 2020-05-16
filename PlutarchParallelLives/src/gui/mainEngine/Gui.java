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
import gui.widgets.TreeConstructionPhasesWidget;
import gui.widgets.DataGenerator;
import gui.widgets.TreeConstructionWidget;
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
import services.TableService;
import tableClustering.clusterExtractor.commons.Cluster;
import tableClustering.clusterExtractor.engine.TableClusteringMainEngine;
import tableClustering.clusterValidator.engine.ClusterValidatorMainEngine;
import data.dataSorters.PldRowSorter;


public class Gui extends JFrame implements ActionListener{

	public static final long serialVersionUID = 1L;
	
	
	
	private TableConstructionIDU constructedIDUTable;
	private TableConstructionWithClusters clusterTable;
	private JvTable jvTable;
	private Configuration configuration;
	private DataGenerator dataGenerator;
	
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
						dataGenerator = new DataGenerator(configuration);
						dataGenerator.importData(file);
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
					dataGenerator = new DataGenerator(configuration);
					dataGenerator.importData(fcOpen1.getSelectedFile());
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
	            DataService service = new DataService();
	            dataGenerator = new DataGenerator(configuration);
	            dataGenerator.setPplFile(service.readFile(file));

				System.out.println(dataGenerator.getPplFile().getProjectName());
				
				CreateProjectJDialog createProjectDialog=new CreateProjectJDialog(dataGenerator.getPplFile().getProjectName(),
						dataGenerator.getPplFile().getDatasetTxt(),
						dataGenerator.getPplFile().getInputCsv(),
						dataGenerator.getPplFile().getOutputAssessment1(),
						dataGenerator.getPplFile().getOutputAssessment2(),
						dataGenerator.getPplFile().getTransitionsFile());
			
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
						dataGenerator.importData(fcOpen1.getSelectedFile());
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
				if(dataGenerator.getPplFile() == null)
				{
					JOptionPane.showMessageDialog(null, "Select a Project first");
					return;
				}
				
				TableConstructionAllSquaresIncluded table=new TableConstructionAllSquaresIncluded(dataGenerator.getGlobalDataKeeper());
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
				if(dataGenerator.getPplFile() == null)
				{
					JOptionPane.showMessageDialog(null, "Select a Project first");
					return;
				}
				
				configuration.getZoomInButton().setVisible(true);
				configuration.getZoomOutButton().setVisible(true);
				TableService service = new TableService();
				constructedIDUTable = service.createTableConstructionIDU(dataGenerator.getGlobalDataKeeper().getAllPPLSchemas(), dataGenerator.getGlobalDataKeeper().getAllPPLTransitions());
				TableConstructionIDU table = constructedIDUTable;
				configuration.setSegmentSizeZoomArea(table.getSegmentSize());
				System.out.println("Schemas: "+dataGenerator.getGlobalDataKeeper().getAllPPLSchemas().size());
				System.out.println("C: "+table.getConstructedColumns().length+" R: "+table.getConstructedRows().length);

				configuration.setFinalColumnsZoomArea(table.getConstructedColumns());
				configuration.setFinalRowsZoomArea(table.getConstructedRows());
				configuration.getTabbedPane().setSelectedIndex(0);
				dataGenerator.makeGeneralTableIDU();
				dataGenerator.fillTree();
					
				
			}
		});
		mnTable.add(mntmShowGeneralLifetimeIDU);
		
		JMenuItem mntmShowGeneralLifetimePhasesPLD = new JMenuItem("Show Phases PLD");
		mntmShowGeneralLifetimePhasesPLD.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				if(dataGenerator.getPplFile() == null || dataGenerator.getPplFile().getProjectName()==null)
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
			            
			            
			            dataGenerator.createPhaseAnalyserEngine();
						
			    		
						if(dataGenerator.getGlobalDataKeeper().getPhaseCollectors().size() == 0){
							JOptionPane.showMessageDialog(null, "Extract Phases first");
							return;
						}
						
						TableConstructionPhases table=new TableConstructionPhases(dataGenerator.getGlobalDataKeeper());
						//TODO in service
						table.constructColumns();
						table.constructRows();
						final String[] columns= table.getConstructedColumns();
						final String[][] rows= table.getConstructedRows();
						configuration.setSegmentSize(table.getSegmentSize());
						System.out.println("Schemas: "+dataGenerator.getGlobalDataKeeper().getAllPPLSchemas().size());
						System.out.println("C: "+columns.length+" R: "+rows.length);
	
						configuration.setFinalColumns(columns);
						configuration.setFinalRows(rows);
						configuration.getTabbedPane().setSelectedIndex(0);
						dataGenerator.makeGeneralTablePhases();
						
						TreeConstructionPhasesWidget widget = new TreeConstructionPhasesWidget(dataGenerator.getGlobalDataKeeper(), configuration);
						widget.fillPhasesTree();
					}
				
				
				
			}
		});
		mnTable.add(mntmShowGeneralLifetimePhasesPLD);
		
		JMenuItem mntmShowGeneralLifetimePhasesWithClustersPLD = new JMenuItem("Show Phases With Clusters PLD");
		mntmShowGeneralLifetimePhasesWithClustersPLD.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				configuration.setWholeCol(-1);
				if(dataGenerator.getPplFile() == null)
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
			            
			            /*PhaseAnalyzerMainEngine mainPhaseEngine= service.createPhaseAnalyserEngine(configuration,dataGenerator.getGlobalDataKeeper(),dataGenerator.getPplFile());
			    		service.connectTransitionsWithPhases(mainPhaseEngine, dataGenerator.getGlobalDataKeeper().getAllPPLTransitions());
			    		
			    		dataGenerator.getGlobalDataKeeper().setPhaseCollectors(mainPhaseEngine.getPhaseCollectors());*/
			            
			            dataGenerator.createPhaseAnalyserEngine();
			    		
			    		
			            dataGenerator.createTableClusteringMainEngine(configuration.getNumberOfClusters());
			    		/*TableClusteringMainEngine mainClusterEngine = service.createTableClusteringMainEngine(dataGenerator.getGlobalDataKeeper(), configuration.getNumberOfClusters());
			    		dataGenerator.getGlobalDataKeeper().setClusterCollectors(mainClusterEngine.getClusterCollectors());
			    		*/
						if(dataGenerator.getGlobalDataKeeper().getPhaseCollectors().size()==0)
						{
							JOptionPane.showMessageDialog(null, "Extract Phases first");
							return;
						}
						TableConstructionWithClusters table=new TableConstructionWithClusters(dataGenerator.getGlobalDataKeeper());
						//TODO in service
						table.constructColumns();
						table.constructRows();
						final String[] columns= table.getConstructedColumns();
						final String[][] rows= table.getConstructedRows();
						configuration.setSegmentSize(table.getSegmentSize());
						System.out.println("Schemas: "+dataGenerator.getGlobalDataKeeper().getAllPPLSchemas().size());
						System.out.println("C: "+columns.length+" R: "+rows.length);
	
						configuration.setFinalColumns(columns);
						configuration.setFinalRows(rows);
						configuration.getTabbedPane().setSelectedIndex(0);
						dataGenerator.makeGeneralTablePhases();
						dataGenerator.fillClustersTree(dataGenerator.getGlobalDataKeeper().getClusterCollectors().get(0).getClusters());
							
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
				
				
				if(dataGenerator.getPplFile() != null){
					
					
					System.out.println("Project Name:"+dataGenerator.getPplFile().getProjectName());
					System.out.println("Dataset txt:"+dataGenerator.getPplFile().getDatasetTxt());
					System.out.println("Input Csv:"+dataGenerator.getPplFile().getInputCsv());
					System.out.println("Output Assessment1:"+dataGenerator.getPplFile().getOutputAssessment1());
					System.out.println("Output Assessment2:"+dataGenerator.getPplFile().getOutputAssessment2());
					System.out.println("Transitions File:"+dataGenerator.getPplFile().getTransitionsFile());
					
					System.out.println("Schemas:"+dataGenerator.getGlobalDataKeeper().getAllPPLSchemas().size());
					System.out.println("Transitions:"+dataGenerator.getGlobalDataKeeper().getAllPPLTransitions().size());
					System.out.println("Tables:"+dataGenerator.getGlobalDataKeeper().getAllPPLTables().size());
					
					
					ProjectInfoDialog infoDialog = new ProjectInfoDialog(dataGenerator.getPplFile().getProjectName(),
																		 dataGenerator.getPplFile().getDatasetTxt(),
																		 dataGenerator.getPplFile().getInputCsv(),
																		 dataGenerator.getPplFile().getTransitionsFile(),
																		 dataGenerator.getGlobalDataKeeper().getAllPPLSchemas().size(),
																		 dataGenerator.getGlobalDataKeeper().getAllPPLTransitions().size(), 
																		 dataGenerator.getGlobalDataKeeper().getAllPPLTables().size());
					
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
					ZoomAreaTableForClusterWidget widget = new ZoomAreaTableForClusterWidget(configuration, dataGenerator.getGlobalDataKeeper());
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
				configuration.getLifeTimeTable().notUniformlyDistributed(dataGenerator.getGlobalDataKeeper());
			    
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
					TableClusteringMainEngine mainEngine2 = new TableClusteringMainEngine(dataGenerator.getGlobalDataKeeper(),wb,wd,wc);
					mainEngine2.extractClusters(configuration.getNumberOfClusters());
					dataGenerator.getGlobalDataKeeper().setClusterCollectors(mainEngine2.getClusterCollectors());
					
					ClusterValidatorMainEngine lala = new ClusterValidatorMainEngine(dataGenerator.getGlobalDataKeeper());
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
		
		TableClusteringMainEngine mainEngine2 = new TableClusteringMainEngine(dataGenerator.getGlobalDataKeeper(),0.333,0.333,0.333);
		mainEngine2.extractClusters(4);
		dataGenerator.getGlobalDataKeeper().setClusterCollectors(mainEngine2.getClusterCollectors());
		
		ClusterValidatorMainEngine lala = new ClusterValidatorMainEngine(dataGenerator.getGlobalDataKeeper());
		lala.run();
		
		lalaString=lalaString+"\n"+"0.333"+"\t"+"0.333"+"\t"+"0.333"
				+"\n"+lala.getExternalEvaluationReport();
		
		for(double wb=0.0; wb<=1.0; wb=wb+0.5){
			
			for(double wd=(1.0-wb); wd>=0.0; wd=wd-0.5){
				
					double wc=1.0-(wb+wd);
					mainEngine2 = new TableClusteringMainEngine(dataGenerator.getGlobalDataKeeper(),wb,wd,wc);
					mainEngine2.extractClusters(4);
					dataGenerator.getGlobalDataKeeper().setClusterCollectors(mainEngine2.getClusterCollectors());
					
					lala = new ClusterValidatorMainEngine(dataGenerator.getGlobalDataKeeper());
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
	
	public void setDescription(String descr){
		configuration.getDescriptionText().setText(descr);
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}