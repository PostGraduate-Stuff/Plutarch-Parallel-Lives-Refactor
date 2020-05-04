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
import gui.widgets.LoadProject;

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
import services.GeneralDatakeeper;
import services.PPLFile;
import tableClustering.clusterExtractor.commons.Cluster;
import tableClustering.clusterExtractor.engine.TableClusteringMainEngine;
import tableClustering.clusterValidator.engine.ClusterValidatorMainEngine;
import data.dataKeeper.GlobalDataKeeper;
import data.dataSorters.PldRowSorter;


public class Gui extends JFrame implements ActionListener{

	private static final long serialVersionUID = 1L;
	
	protected JPanel contentPane;
	protected JPanel lifeTimePanel = new JPanel();
	protected JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
	
	protected MyTableModel detailedModel = null;
	protected MyTableModel generalModel = null;
	protected MyTableModel zoomModel = null;

	protected JvTable LifeTimeTable=null;
	protected JvTable zoomAreaTable=null;
	
	
	
	protected JScrollPane tmpScrollPane =new JScrollPane();
	protected JScrollPane treeScrollPane= new JScrollPane();
	protected JScrollPane tmpScrollPaneZoomArea =new JScrollPane();
	
	
	
	//private ArrayList<Integer> selectedRows=new ArrayList<Integer>();
	//private generalDatakeeper.getGlobalDataKeeper() generalDatakeeper.getGlobalDataKeeper()=null;

	
	
	
	
	
	protected String[] firstLevelUndoColumnsZoomArea=null;
	protected String[][] firstLevelUndoRowsZoomArea=null;
	//private String currentProject=null; == getFileContent
	//private String project=null;
	
	
	
	protected Integer[] segmentSizeZoomArea=new Integer[4];
	protected Integer[] segmentSizeDetailedTable=new Integer[3];


	protected Configuration configuration;
	
	protected GeneralDatakeeper generalDatakeeper;


	

	protected ArrayList<String> selectedFromTree=new ArrayList<String>();
	
	protected JTree tablesTree=new JTree();
	protected JPanel sideMenu=new JPanel();
	protected JPanel tablesTreePanel=new JPanel();
	protected JPanel descriptionPanel=new JPanel();
	protected JLabel treeLabel;
	protected JLabel generalTableLabel;
	protected JLabel zoomAreaLabel;
	protected JLabel descriptionLabel;
	protected JTextArea descriptionText;
	protected JButton zoomInButton;
	protected JButton zoomOutButton;
	protected JButton uniformlyDistributedButton;
	protected JButton notUniformlyDistributedButton;
	protected JButton showThisToPopup;


	protected int[] selectedRowsFromMouse;
	protected int selectedColumn=-1;
	protected int selectedColumnZoomArea=-1;

	protected int wholeColZoomArea=-1;
	
	

	protected ArrayList<String> tablesSelected = new ArrayList<String>();

	protected boolean showingPld=false;
	
	protected JButton undoButton;
	protected JMenu mnProject;
	protected JMenuItem mntmInfo;
	
	
	
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
	            PPLFile pplFile = generalDatakeeper.readFile(file);

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
				if(generalDatakeeper==null)
				{
					JOptionPane.showMessageDialog(null, "Select a Project first");
					return;
				}
				
				TableConstructionAllSquaresIncluded table=new TableConstructionAllSquaresIncluded(generalDatakeeper.getGlobalDataKeeper());
				//TODO in service
				table.constructColumns();
				table.constructRows();
				final String[] columns= table.getConstructedColumns();
				final String[][] rows= table.getConstructedRows();
				segmentSizeDetailedTable=table.getSegmentSize();
				tabbedPane.setSelectedIndex(0);
				makeDetailedTable(columns,rows,true);
			
			}
		});
		
		JMenuItem mntmShowGeneralLifetimeIDU = new JMenuItem("Show PLD");
		mntmShowGeneralLifetimeIDU.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(generalDatakeeper==null)
				{
					JOptionPane.showMessageDialog(null, "Select a Project first");
					return;
				}
				
				zoomInButton.setVisible(true);
				zoomOutButton.setVisible(true);
				generalDatakeeper.createTableConstructionIDU();
				TableConstructionIDU table=generalDatakeeper.getTableConstructionIDU();
				segmentSizeZoomArea = table.getSegmentSize();
				System.out.println("Schemas: "+generalDatakeeper.getGlobalDataKeeper().getAllPPLSchemas().size());
				System.out.println("C: "+table.getConstructedColumns().length+" R: "+table.getConstructedRows().length);

				configuration.setFinalColumnsZoomArea(table.getConstructedColumns());
				configuration.setFinalRowsZoomArea(table.getConstructedRows());
				tabbedPane.setSelectedIndex(0);
				makeGeneralTableIDU();
				fillTree();
					
				
			}
		});
		mnTable.add(mntmShowGeneralLifetimeIDU);
		
		JMenuItem mntmShowGeneralLifetimePhasesPLD = new JMenuItem("Show Phases PLD");
		mntmShowGeneralLifetimePhasesPLD.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				if(generalDatakeeper == null ||
				   generalDatakeeper.getPPLFile() == null ||
				   generalDatakeeper.getPPLFile().getProjectName()==null)
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
			            
			            
			            generalDatakeeper.createPhaseAnalyserEngine(configuration);
						
			    		
						if(generalDatakeeper.getGlobalDataKeeper().getPhaseCollectors().size() == 0){
							JOptionPane.showMessageDialog(null, "Extract Phases first");
							return;
						}
						
						TableConstructionPhases table=new TableConstructionPhases(generalDatakeeper.getGlobalDataKeeper());
						//TODO in service
						table.constructColumns();
						table.constructRows();
						final String[] columns= table.getConstructedColumns();
						final String[][] rows= table.getConstructedRows();
						configuration.setSegmentSize(table.getSegmentSize());
						System.out.println("Schemas: "+generalDatakeeper.getGlobalDataKeeper().getAllPPLSchemas().size());
						System.out.println("C: "+columns.length+" R: "+rows.length);
	
						configuration.setFinalColumns(columns);
						configuration.setFinalRows(rows);
						tabbedPane.setSelectedIndex(0);
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
				if(generalDatakeeper==null||
				   generalDatakeeper.getPPLFile() == null)
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
			            
			            /*PhaseAnalyzerMainEngine mainPhaseEngine= service.createPhaseAnalyserEngine(configuration,generalDatakeeper.getGlobalDataKeeper(),generalDatakeeper.getPPLFile());
			    		service.connectTransitionsWithPhases(mainPhaseEngine, generalDatakeeper.getGlobalDataKeeper().getAllPPLTransitions());
			    		
			    		generalDatakeeper.getGlobalDataKeeper().setPhaseCollectors(mainPhaseEngine.getPhaseCollectors());*/
			            
			    		generalDatakeeper.createPhaseAnalyserEngine(configuration);
			    		
			    		
			    		generalDatakeeper.createTableClusteringMainEngine(configuration.getNumberOfClusters());
			    		/*TableClusteringMainEngine mainClusterEngine = service.createTableClusteringMainEngine(generalDatakeeper.getGlobalDataKeeper(), configuration.getNumberOfClusters());
			    		generalDatakeeper.getGlobalDataKeeper().setClusterCollectors(mainClusterEngine.getClusterCollectors());
			    		*/
						if(generalDatakeeper.getGlobalDataKeeper().getPhaseCollectors().size()==0)
						{
							JOptionPane.showMessageDialog(null, "Extract Phases first");
							return;
						}
						TableConstructionWithClusters table=new TableConstructionWithClusters(generalDatakeeper.getGlobalDataKeeper());
						//TODO in service
						table.constructColumns();
						table.constructRows();
						final String[] columns= table.getConstructedColumns();
						final String[][] rows= table.getConstructedRows();
						configuration.setSegmentSize(table.getSegmentSize());
						System.out.println("Schemas: "+generalDatakeeper.getGlobalDataKeeper().getAllPPLSchemas().size());
						System.out.println("C: "+columns.length+" R: "+rows.length);
	
						configuration.setFinalColumns(columns);
						configuration.setFinalRows(rows);
						tabbedPane.setSelectedIndex(0);
						makeGeneralTablePhases();
						fillClustersTree(generalDatakeeper.getGlobalDataKeeper().getClusterCollectors().get(0).getClusters());
							
					}
				
			}
		});
		mnTable.add(mntmShowGeneralLifetimePhasesWithClustersPLD);
		
		
		mnTable.add(mntmShowLifetimeTable);
		

		sideMenu.setBounds(0, 0, 280, 600);
		sideMenu.setBackground(Color.DARK_GRAY);
		
		
		
		GroupLayout gl_sideMenu = new GroupLayout(sideMenu);
		gl_sideMenu.setHorizontalGroup(
				gl_sideMenu.createParallelGroup(Alignment.LEADING)
		);
		gl_sideMenu.setVerticalGroup(
				gl_sideMenu.createParallelGroup(Alignment.LEADING)
		);
		
		sideMenu.setLayout(gl_sideMenu);
		
		tablesTreePanel.setBounds(10, 400, 260, 180);
		tablesTreePanel.setBackground(Color.LIGHT_GRAY);
		
		GroupLayout gl_tablesTreePanel = new GroupLayout(tablesTreePanel);
		gl_tablesTreePanel.setHorizontalGroup(
				gl_tablesTreePanel.createParallelGroup(Alignment.LEADING)
		);
		gl_tablesTreePanel.setVerticalGroup(
				gl_tablesTreePanel.createParallelGroup(Alignment.LEADING)
		);
		
		tablesTreePanel.setLayout(gl_tablesTreePanel);
		
		treeLabel=new JLabel();
		treeLabel.setBounds(10, 370, 260, 40);
		treeLabel.setForeground(Color.WHITE);
		treeLabel.setText("Tree");
		
		descriptionPanel.setBounds(10, 190, 260, 180);
		descriptionPanel.setBackground(Color.LIGHT_GRAY);
		
		GroupLayout gl_descriptionPanel = new GroupLayout(descriptionPanel);
		gl_descriptionPanel.setHorizontalGroup(
				gl_descriptionPanel.createParallelGroup(Alignment.LEADING)
		);
		gl_descriptionPanel.setVerticalGroup(
				gl_descriptionPanel.createParallelGroup(Alignment.LEADING)
		);
		
		descriptionPanel.setLayout(gl_descriptionPanel);
		
		descriptionText=new JTextArea();
		descriptionText.setBounds(5, 5, 250, 170);
		descriptionText.setForeground(Color.BLACK);
		descriptionText.setText("");
		descriptionText.setBackground(Color.LIGHT_GRAY);
		
		descriptionPanel.add(descriptionText);
		
		
		descriptionLabel=new JLabel();
		descriptionLabel.setBounds(10, 160, 260, 40);
		descriptionLabel.setForeground(Color.WHITE);
		descriptionLabel.setText("Description");
		
		sideMenu.add(treeLabel);
		sideMenu.add(tablesTreePanel);
		
		sideMenu.add(descriptionLabel);
		sideMenu.add(descriptionPanel);

		lifeTimePanel.add(sideMenu);
		
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
		
		mnProject = new JMenu("Project");
		menuBar.add(mnProject);
		
		mntmInfo = new JMenuItem("Info");
		mntmInfo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				
				if(generalDatakeeper!=null &&
				   generalDatakeeper.getPplFile()!=null){
					
					
					System.out.println("Project Name:"+generalDatakeeper.getPplFile().getProjectName());
					System.out.println("Dataset txt:"+generalDatakeeper.getPplFile().getDatasetTxt());
					System.out.println("Input Csv:"+generalDatakeeper.getPplFile().getInputCsv());
					System.out.println("Output Assessment1:"+generalDatakeeper.getPplFile().getOutputAssessment1());
					System.out.println("Output Assessment2:"+generalDatakeeper.getPplFile().getOutputAssessment2());
					System.out.println("Transitions File:"+generalDatakeeper.getPplFile().getTransitionsFile());
					
					System.out.println("Schemas:"+generalDatakeeper.getGlobalDataKeeper().getAllPPLSchemas().size());
					System.out.println("Transitions:"+generalDatakeeper.getGlobalDataKeeper().getAllPPLTransitions().size());
					System.out.println("Tables:"+generalDatakeeper.getGlobalDataKeeper().getAllPPLTables().size());
					
					
					ProjectInfoDialog infoDialog = new ProjectInfoDialog(generalDatakeeper.getPplFile().getProjectName(),
																		 generalDatakeeper.getPplFile().getDatasetTxt(),
																		 generalDatakeeper.getPplFile().getInputCsv(),
																		 generalDatakeeper.getPplFile().getTransitionsFile(),
																		 generalDatakeeper.getGlobalDataKeeper().getAllPPLSchemas().size(),
																		 generalDatakeeper.getGlobalDataKeeper().getAllPPLTransitions().size(), 
																		 generalDatakeeper.getGlobalDataKeeper().getAllPPLTables().size());
					
					infoDialog.setVisible(true);
				}
				else{
					JOptionPane.showMessageDialog(null, "Select a Project first");
					return;
				}
				
				
				
				
			}
		});
		mnProject.add(mntmInfo);
		buttonHelp.setBounds(900,900 , 80, 40);
		menuBar.add(buttonHelp);
		
		
		
		contentPane = new JPanel();
		
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addComponent(tabbedPane, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 1474, Short.MAX_VALUE)
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addComponent(tabbedPane, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 771, Short.MAX_VALUE)
		);
		
		
		tabbedPane.addTab("LifeTime Table", null, lifeTimePanel, null);
		
		GroupLayout gl_lifeTimePanel = new GroupLayout(lifeTimePanel);
		gl_lifeTimePanel.setHorizontalGroup(
			gl_lifeTimePanel.createParallelGroup(Alignment.LEADING)
				.addGap(0, 1469, Short.MAX_VALUE)
		);
		gl_lifeTimePanel.setVerticalGroup(
			gl_lifeTimePanel.createParallelGroup(Alignment.LEADING)
				.addGap(0, 743, Short.MAX_VALUE)
		);
		lifeTimePanel.setLayout(gl_lifeTimePanel);
		
		
		generalTableLabel=new JLabel("Parallel Lives Diagram");
		generalTableLabel.setBounds(300, 0, 150, 30);
		generalTableLabel.setForeground(Color.BLACK);
		
		zoomAreaLabel=new JLabel();
		zoomAreaLabel.setText("<HTML>Z<br>o<br>o<br>m<br><br>A<br>r<br>e<br>a</HTML>");
		zoomAreaLabel.setBounds(1255, 325, 15, 300);
		zoomAreaLabel.setForeground(Color.BLACK);
		
		zoomInButton = new JButton("Zoom In");
		zoomInButton.setBounds(1000, 560, 100, 30);
		
		
		
		zoomInButton.addMouseListener(new MouseAdapter() {
			@Override
			   public void mouseClicked(MouseEvent e) {
				configuration.setRowHeight(configuration.getRowHeight() + 2);
				configuration.setColumnWidth(configuration.getColumnWidth() + 1);
//				rowHeight=rowHeight+2;
//				columnWidth=columnWidth+1;
//				zoomAreaTable.setZoom(rowHeight,columnWidth);
				zoomAreaTable.setZoom(configuration.getRowHeight(), configuration.getColumnWidth());

				
			}
		});
		
		zoomOutButton = new JButton("Zoom Out");
		zoomOutButton.setBounds(1110, 560, 100, 30);
		
		zoomOutButton.addMouseListener(new MouseAdapter() {
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
				zoomAreaTable.setZoom(configuration.getRowHeight(), configuration.getColumnWidth());
				
			}
		});
		
		zoomInButton.setVisible(false);
		zoomOutButton.setVisible(false);
		
		
		showThisToPopup = new JButton("Enlarge");
		showThisToPopup.setBounds(800, 560, 100, 30);
		
		showThisToPopup.addMouseListener(new MouseAdapter() {
			@Override
			   public void mouseClicked(MouseEvent e) {
				
				EnlargeTable showEnlargmentPopup= new EnlargeTable(configuration.getFinalRowsZoomArea(),configuration.getFinalColumnsZoomArea(),segmentSizeZoomArea);
				showEnlargmentPopup.setBounds(100, 100, 1300, 700);
				
				showEnlargmentPopup.setVisible(true);
				
				
			}
		});
		
		showThisToPopup.setVisible(false);
		
		
		undoButton = new JButton("Undo");
		undoButton.setBounds(680, 560, 100, 30);
		
		undoButton.addMouseListener(new MouseAdapter() {
			@Override
			   public void mouseClicked(MouseEvent e) {
				if (firstLevelUndoColumnsZoomArea!=null) {
					configuration.setFinalColumnsZoomArea(firstLevelUndoColumnsZoomArea);
					configuration.setFinalRowsZoomArea(firstLevelUndoRowsZoomArea);
					makeZoomAreaTableForCluster();
				}
				
			}
		});
		
		undoButton.setVisible(false);
		
		
		uniformlyDistributedButton = new JButton("Same Width"); 
		uniformlyDistributedButton.setBounds(980, 0, 120, 30);
		
		uniformlyDistributedButton.addMouseListener(new MouseAdapter() {
			@Override
			   public void mouseClicked(MouseEvent e) {
			    LifeTimeTable.uniformlyDistributed(1);
			    
			  } 
		});
		
		uniformlyDistributedButton.setVisible(false);
		
		notUniformlyDistributedButton = new JButton("Over Time"); 
		notUniformlyDistributedButton.setBounds(1100, 0, 120, 30);
		
		notUniformlyDistributedButton.addMouseListener(new MouseAdapter() {
			@Override
			   public void mouseClicked(MouseEvent e) {
			    LifeTimeTable.notUniformlyDistributed(generalDatakeeper.getGlobalDataKeeper());
			    
			  } 
		});
		
		notUniformlyDistributedButton.setVisible(false);
		
		lifeTimePanel.add(zoomInButton);
		lifeTimePanel.add(undoButton);
		lifeTimePanel.add(zoomOutButton);
		lifeTimePanel.add(uniformlyDistributedButton);
		lifeTimePanel.add(notUniformlyDistributedButton);
		lifeTimePanel.add(showThisToPopup);

		lifeTimePanel.add(zoomAreaLabel);
		
		lifeTimePanel.add(generalTableLabel);
		
		contentPane.setLayout(gl_contentPane);
		
		pack();
		setBounds(30, 30, 1300, 700);

		
	}
	
	
	

protected void showSelectionToZoomArea(int selectedColumn){
	
	TableConstructionZoomArea table=new TableConstructionZoomArea(generalDatakeeper.getGlobalDataKeeper(),tablesSelected,selectedColumn);
	table.constructColumns();
	table.constructRows();
	final String[] columns= table.getConstructedColumns();
	final String[][] rows= table.getConstructedRows();
	segmentSizeZoomArea = table.getSegmentSize();

	System.out.println("Schemas: "+generalDatakeeper.getGlobalDataKeeper().getAllPPLSchemas().size());
	System.out.println("C: "+columns.length+" R: "+rows.length);

	configuration.setFinalColumnsZoomArea(columns);
	configuration.setFinalRowsZoomArea(rows);
	tabbedPane.setSelectedIndex(0);
	makeZoomAreaTable();
	
	
	
}

protected void showClusterSelectionToZoomArea(int selectedColumn,String selectedCluster){

	
	ArrayList<String> tablesOfCluster=new ArrayList<String>();
	for(int i=0; i <tablesSelected.size(); i++){
		String[] selectedClusterSplit= tablesSelected.get(i).split(" ");
		int cluster=Integer.parseInt(selectedClusterSplit[1]);
		ArrayList<String> namesOfTables=generalDatakeeper.getGlobalDataKeeper().getClusterCollectors().get(0).getClusters().get(cluster).getNamesOfTables();
		for(int j=0; j<namesOfTables.size(); j++){
			tablesOfCluster.add(namesOfTables.get(j));
		}
		System.out.println(tablesSelected.get(i));
	}
	
	PldConstruction table;
	if (selectedColumn==0) {
		table= new TableConstructionClusterTablesPhasesZoomA(generalDatakeeper.getGlobalDataKeeper(),tablesOfCluster);
	}
	else{
		table= new TableConstructionZoomArea(generalDatakeeper.getGlobalDataKeeper(),tablesOfCluster,selectedColumn);
	}
	//TODO in service
	table.constructColumns();
	table.constructRows();
	final String[] columns= table.getConstructedColumns();
	final String[][] rows= table.getConstructedRows();
	segmentSizeZoomArea = table.getSegmentSize();
	System.out.println("Schemas: "+generalDatakeeper.getGlobalDataKeeper().getAllPPLSchemas().size());
	System.out.println("C: "+columns.length+" R: "+rows.length);

	configuration.setFinalColumnsZoomArea(columns);
	configuration.setFinalRowsZoomArea(rows);
	tabbedPane.setSelectedIndex(0);
	makeZoomAreaTableForCluster();
	
	
}

private void makeZoomAreaTable() {
	showingPld=false;
	int numberOfColumns=configuration.getFinalRowsZoomArea()[0].length;
	int numberOfRows=configuration.getFinalRowsZoomArea().length;
	
	
	final String[][] rowsZoom=new String[numberOfRows][numberOfColumns];
	
	for(int i=0; i<numberOfRows; i++){
		
		rowsZoom[i][0]=configuration.getFinalRowsZoomArea()[i][0];
		
	}
	
	zoomModel=new MyTableModel(configuration.getFinalColumnsZoomArea(), rowsZoom);
	
	final JvTable zoomTable=new JvTable(zoomModel);
	
	zoomTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	

	zoomTable.setShowGrid(false);
	zoomTable.setIntercellSpacing(new Dimension(0, 0));
	
	
	for(int i=0; i<zoomTable.getColumnCount(); i++){
		if(i==0){
			zoomTable.getColumnModel().getColumn(0).setPreferredWidth(150);
			
		}
		else{
			
			
			zoomTable.getColumnModel().getColumn(i).setPreferredWidth(20);
			zoomTable.getColumnModel().getColumn(i).setMaxWidth(20);
			zoomTable.getColumnModel().getColumn(i).setMinWidth(20);
		}
	}
	
	zoomTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer()
	{
	    
		private static final long serialVersionUID = 1L;

		@Override
	    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
	    {
	        final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
	        
	        
	        
	        String tmpValue=configuration.getFinalRowsZoomArea()[row][column];
	        String columnName=table.getColumnName(column);
	        Color fr=new Color(0,0,0);
	        c.setForeground(fr);
	        
	        if(column==wholeColZoomArea){
	        	
	        	String description="Transition ID:"+table.getColumnName(column)+"\n";
	        	description=description+"Old Version Name:"+generalDatakeeper.getGlobalDataKeeper().getAllPPLTransitions().
        				get(Integer.parseInt(table.getColumnName(column))).getOldVersionName()+"\n";
        		description=description+"New Version Name:"+generalDatakeeper.getGlobalDataKeeper().getAllPPLTransitions().
        				get(Integer.parseInt(table.getColumnName(column))).getNewVersionName()+"\n";		        		
        		
    			description=description+"Transition Changes:"+generalDatakeeper.getGlobalDataKeeper().getAllPPLTransitions().get(Integer.parseInt(table.getColumnName(column))).getNumberOfClusterChangesForOneTr(rowsZoom)+"\n";
    			description=description+"Additions:"+generalDatakeeper.getGlobalDataKeeper().getAllPPLTransitions().get(Integer.parseInt(table.getColumnName(column))).getNumberOfClusterAdditionsForOneTr(rowsZoom)+"\n";
    			description=description+"Deletions:"+generalDatakeeper.getGlobalDataKeeper().getAllPPLTransitions().get(Integer.parseInt(table.getColumnName(column))).getNumberOfClusterDeletionsForOneTr(rowsZoom)+"\n";
    			description=description+"Updates:"+generalDatakeeper.getGlobalDataKeeper().getAllPPLTransitions().get(Integer.parseInt(table.getColumnName(column))).getNumberOfClusterUpdatesForOneTr(rowsZoom)+"\n";

    		
	        	
	        	descriptionText.setText(description);
	        	Color cl = new Color(255,69,0,100);
        		c.setBackground(cl);
        		
        		return c;
	        }
	        else if(selectedColumnZoomArea==0){
	        	if (isSelected){
	        		String description="Table:"+configuration.getFinalRowsZoomArea()[row][0]+"\n";
	        		description=description+"Birth Version Name:"+generalDatakeeper.getGlobalDataKeeper().getAllPPLTables().get(configuration.getFinalRowsZoomArea()[row][0]).getBirth()+"\n";
	        		description=description+"Birth Version ID:"+generalDatakeeper.getGlobalDataKeeper().getAllPPLTables().get(configuration.getFinalRowsZoomArea()[row][0]).getBirthVersionID()+"\n";
	        		description=description+"Death Version Name:"+generalDatakeeper.getGlobalDataKeeper().getAllPPLTables().get(configuration.getFinalRowsZoomArea()[row][0]).getDeath()+"\n";
	        		description=description+"Death Version ID:"+generalDatakeeper.getGlobalDataKeeper().getAllPPLTables().get(configuration.getFinalRowsZoomArea()[row][0]).getDeathVersionID()+"\n";
	        		description=description+"Total Changes:"+generalDatakeeper.getGlobalDataKeeper().getAllPPLTables().get(configuration.getFinalRowsZoomArea()[row][0]).
	        				getTotalChangesForOnePhase(Integer.parseInt(table.getColumnName(1)), Integer.parseInt(table.getColumnName(table.getColumnCount()-1)))+"\n";
	        		descriptionText.setText(description);
	        		
	        		Color cl = new Color(255,69,0,100);

	        		c.setBackground(cl);
	        		return c;
	        	}
	        }
	        else{
	        	if (isSelected && hasFocus){
		        	
	        		String description="";
	        		if(!table.getColumnName(column).contains("Table name")){
		        		description="Table:"+configuration.getFinalRowsZoomArea()[row][0]+"\n";
		        		
		        		description=description+"Old Version Name:"+generalDatakeeper.getGlobalDataKeeper().getAllPPLTransitions().
		        				get(Integer.parseInt(table.getColumnName(column))).getOldVersionName()+"\n";
		        		description=description+"New Version Name:"+generalDatakeeper.getGlobalDataKeeper().getAllPPLTransitions().
		        				get(Integer.parseInt(table.getColumnName(column))).getNewVersionName()+"\n";		        		
		        		if(generalDatakeeper.getGlobalDataKeeper().getAllPPLTables().get(configuration.getFinalRowsZoomArea()[row][0]).
		        				getTableChanges().getTableAtChForOneTransition(Integer.parseInt(table.getColumnName(column)))!=null){
		        			description=description+"Transition Changes:"+generalDatakeeper.getGlobalDataKeeper().getAllPPLTables().get(configuration.getFinalRowsZoomArea()[row][0]).
		        				getTableChanges().getTableAtChForOneTransition(Integer.parseInt(table.getColumnName(column))).size()+"\n";
		        			description=description+"Additions:"+generalDatakeeper.getGlobalDataKeeper().getAllPPLTables().get(configuration.getFinalRowsZoomArea()[row][0]).
		        					getNumberOfAdditionsForOneTr(Integer.parseInt(table.getColumnName(column)))+"\n";
		        			description=description+"Deletions:"+generalDatakeeper.getGlobalDataKeeper().getAllPPLTables().get(configuration.getFinalRowsZoomArea()[row][0]).
		        					getNumberOfDeletionsForOneTr(Integer.parseInt(table.getColumnName(column)))+"\n";
		        			description=description+"Updates:"+generalDatakeeper.getGlobalDataKeeper().getAllPPLTables().get(configuration.getFinalRowsZoomArea()[row][0]).
		        					getNumberOfUpdatesForOneTr(Integer.parseInt(table.getColumnName(column)))+"\n";
		        			
		        		}
		        		else{
		        			description=description+"Transition Changes:0"+"\n";
		        			description=description+"Additions:0"+"\n";
		        			description=description+"Deletions:0"+"\n";
		        			description=description+"Updates:0"+"\n";
		        			
		        		}
		        		
		        		descriptionText.setText(description);
	        		}
	        		
	        		Color cl = new Color(255,69,0,100);

	        		c.setBackground(cl);
	        		return c;
		        }
	        	
	        }


	        try{
	        	int numericValue=Integer.parseInt(tmpValue);
	        	Color insersionColor=null;
				setToolTipText(Integer.toString(numericValue));

	        	
        		if(numericValue==0){
        			insersionColor=new Color(0,100,0);
        		}
        		else if(numericValue> 0&& numericValue<=segmentSizeZoomArea[3]){
        			
        			insersionColor=new Color(176,226,255);
	        	}
        		else if(numericValue>segmentSizeZoomArea[3] && numericValue<=2*segmentSizeZoomArea[3]){
        			insersionColor=new Color(92,172,238);
        		}
        		else if(numericValue>2*segmentSizeZoomArea[3] && numericValue<=3*segmentSizeZoomArea[3]){
        			
        			insersionColor=new Color(28,134,238);
        		}
        		else{
        			insersionColor=new Color(16,78,139);
        		}
        		c.setBackground(insersionColor);
	        	
	        	return c;
	        }
	        catch(Exception e){
	        		

	        	
        		if(tmpValue.equals("")){
        			c.setBackground(Color.DARK_GRAY);
        			return c; 
        		}
        		else{
        			if(columnName.contains("v")){
        				c.setBackground(Color.lightGray);
        				setToolTipText(columnName);
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
	
	zoomTable.addMouseListener(new MouseAdapter() {
		@Override
		   public void mouseClicked(MouseEvent e) {
			
			if (e.getClickCount() == 1) {
				JTable target = (JTable)e.getSource();
		         
		         selectedRowsFromMouse = target.getSelectedRows();
		         selectedColumnZoomArea = target.getSelectedColumn();
		         zoomAreaTable.repaint();
			}
		    
		   }
	});
	
	zoomTable.addMouseListener(new MouseAdapter() {
		@Override
		   public void mouseReleased(MouseEvent e) {
			
				if(SwingUtilities.isRightMouseButton(e)){
					System.out.println("Right Click");

					JTable target1 = (JTable)e.getSource();
					selectedColumnZoomArea=target1.getSelectedColumn();
					selectedRowsFromMouse=target1.getSelectedRows();
					System.out.println(target1.getSelectedColumn());
					System.out.println(target1.getSelectedRow());
					final ArrayList<String> tablesSelected = new ArrayList<String>();
					for(int rowsSelected=0; rowsSelected<selectedRowsFromMouse.length; rowsSelected++){
						tablesSelected.add((String) zoomTable.getValueAt(selectedRowsFromMouse[rowsSelected], 0));
						System.out.println(tablesSelected.get(rowsSelected));
					}
						
					
				}
			
		   }
	});
	
	// listener
	zoomTable.getTableHeader().addMouseListener(new MouseAdapter() {
	    @Override
	    public void mouseClicked(MouseEvent e) {
	    	wholeColZoomArea = zoomTable.columnAtPoint(e.getPoint());
	        String name = zoomTable.getColumnName(wholeColZoomArea);
	        System.out.println("Column index selected " + configuration.getWholeCol() + " " + name);
	        zoomTable.repaint();
	    }
	});
	
	zoomTable.getTableHeader().addMouseListener(new MouseAdapter() {
	    @Override
	    public void mouseReleased(MouseEvent e) {
	    	if(SwingUtilities.isRightMouseButton(e)){
				System.out.println("Right Click");
				
						final JPopupMenu popupMenu = new JPopupMenu();
				        JMenuItem showDetailsItem = new JMenuItem("Clear Column Selection");
				        showDetailsItem.addActionListener(new ActionListener() {

				            @Override
				            public void actionPerformed(ActionEvent e) {
				            	wholeColZoomArea=-1;
				            	zoomTable.repaint();
				            }
				        });
				        popupMenu.add(showDetailsItem);
				        popupMenu.show(zoomTable, e.getX(),e.getY());
				    
			}
		
	   }
	    
	});
	
	
	zoomAreaTable=zoomTable;
	
	tmpScrollPaneZoomArea.setViewportView(zoomAreaTable);
	tmpScrollPaneZoomArea.setAlignmentX(0);
	tmpScrollPaneZoomArea.setAlignmentY(0);
	tmpScrollPaneZoomArea.setBounds(300,300,950,250);
	tmpScrollPaneZoomArea.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	tmpScrollPaneZoomArea.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
	

	lifeTimePanel.setCursor(getCursor());
	lifeTimePanel.add(tmpScrollPaneZoomArea);
	
	
	
}

private void makeZoomAreaTableForCluster() {
	showingPld=false;
	int numberOfColumns=configuration.getFinalRowsZoomArea()[0].length;
	int numberOfRows=configuration.getFinalRowsZoomArea().length;
	undoButton.setVisible(true);
	
	final String[][] rowsZoom=new String[numberOfRows][numberOfColumns];
	
	for(int i=0; i<numberOfRows; i++){
		
		rowsZoom[i][0]=configuration.getFinalRowsZoomArea()[i][0];
		
	}
	
	zoomModel=new MyTableModel(configuration.getFinalColumnsZoomArea(), rowsZoom);
	
	final JvTable zoomTable=new JvTable(zoomModel);
	
	zoomTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	
	
	zoomTable.setShowGrid(false);
	zoomTable.setIntercellSpacing(new Dimension(0, 0));
	

	
	for(int i=0; i<zoomTable.getColumnCount(); i++){
		if(i==0){
			zoomTable.getColumnModel().getColumn(0).setPreferredWidth(150);
			
		}
		else{
			
			zoomTable.getColumnModel().getColumn(i).setPreferredWidth(10);
			zoomTable.getColumnModel().getColumn(i).setMaxWidth(10);
			zoomTable.getColumnModel().getColumn(i).setMinWidth(70);
		}
	}
	
	zoomTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer()
	{
	    
		private static final long serialVersionUID = 1L;

		@Override
	    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
	    {
	        final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
	        
	        
	        
	        String tmpValue=configuration.getFinalRowsZoomArea()[row][column];
	        String columnName=table.getColumnName(column);
	        Color fr=new Color(0,0,0);
	        c.setForeground(fr);
	        
	        if(column==wholeColZoomArea && wholeColZoomArea!=0){
	        	
	        	String description=table.getColumnName(column)+"\n";
	          	description=description+"First Transition ID:"+generalDatakeeper.getGlobalDataKeeper().getPhaseCollectors().get(0).getPhases().
        				get(column-1).getStartPos()+"\n";
        		description=description+"Last Transition ID:"+generalDatakeeper.getGlobalDataKeeper().getPhaseCollectors().get(0).getPhases().
        				get(column-1).getEndPos()+"\n";
        		description=description+"Total Changes For This Phase:"+generalDatakeeper.getGlobalDataKeeper().getPhaseCollectors().get(0).getPhases().
        				get(column-1).getTotalUpdates()+"\n";
        		description=description+"Additions For This Phase:"+generalDatakeeper.getGlobalDataKeeper().getPhaseCollectors().get(0).getPhases().
        				get(column-1).getTotalAdditionsOfPhase()+"\n";
        		description=description+"Deletions For This Phase:"+generalDatakeeper.getGlobalDataKeeper().getPhaseCollectors().get(0).getPhases().
        				get(column-1).getTotalDeletionsOfPhase()+"\n";
        		description=description+"Updates For This Phase:"+generalDatakeeper.getGlobalDataKeeper().getPhaseCollectors().get(0).getPhases().
        				get(column-1).getTotalUpdatesOfPhase()+"\n";
	        	
        		descriptionText.setText(description);
	        	
	        	Color cl = new Color(255,69,0,100);

        		c.setBackground(cl);
        		return c;
	        }
	        else if(selectedColumnZoomArea==0){
	        	if (isSelected){
	        		
	        		
		        		String description="Table:"+configuration.getFinalRowsZoomArea()[row][0]+"\n";
		        		description=description+"Birth Version Name:"+generalDatakeeper.getGlobalDataKeeper().getAllPPLTables().get(configuration.getFinalRowsZoomArea()[row][0]).getBirth()+"\n";
		        		description=description+"Birth Version ID:"+generalDatakeeper.getGlobalDataKeeper().getAllPPLTables().get(configuration.getFinalRowsZoomArea()[row][0]).getBirthVersionID()+"\n";
		        		description=description+"Death Version Name:"+generalDatakeeper.getGlobalDataKeeper().getAllPPLTables().get(configuration.getFinalRowsZoomArea()[row][0]).getDeath()+"\n";
		        		description=description+"Death Version ID:"+generalDatakeeper.getGlobalDataKeeper().getAllPPLTables().get(configuration.getFinalRowsZoomArea()[row][0]).getDeathVersionID()+"\n";
		        		description=description+"Total Changes:"+generalDatakeeper.getGlobalDataKeeper().getAllPPLTables().get(configuration.getFinalRowsZoomArea()[row][0]).getTotalChanges()+"\n";
		        		descriptionText.setText(description);

	        	
	        		
	        		Color cl = new Color(255,69,0,100);
	        		
	        		c.setBackground(cl);
	        		return c;
	        	}
	        }
	        else{
	        	
	        	
	        	if (isSelected && hasFocus){
		        	
	        		String description="";
	        		if(!table.getColumnName(column).contains("Table name")){
	        			
		        		
	        			description="Transition "+table.getColumnName(column)+"\n";
		        		
	        			description=description+"Old Version:"+generalDatakeeper.getGlobalDataKeeper().getAllPPLTransitions().get(Integer.parseInt(table.getColumnName(column))).getOldVersionName()+"\n";
		        		description=description+"New Version:"+generalDatakeeper.getGlobalDataKeeper().getAllPPLTransitions().get(Integer.parseInt(table.getColumnName(column))).getNewVersionName()+"\n\n";
		
	        			//description=description+"First Transition ID:"+generalDatakeeper.getGlobalDataKeeper().getPhaseCollectors().get(0).getPhases().
		        				//get(column-1).getStartPos()+"\n";
		        		//description=description+"Last Transition ID:"+generalDatakeeper.getGlobalDataKeeper().getPhaseCollectors().get(0).getPhases().
		        			//	get(column-1).getEndPos()+"\n\n";
	        			description=description+"Table:"+configuration.getFinalRowsZoomArea()[row][0]+"\n";
		        		description=description+"Birth Version Name:"+generalDatakeeper.getGlobalDataKeeper().getAllPPLTables().get(configuration.getFinalRowsZoomArea()[row][0]).getBirth()+"\n";
		        		description=description+"Birth Version ID:"+generalDatakeeper.getGlobalDataKeeper().getAllPPLTables().get(configuration.getFinalRowsZoomArea()[row][0]).getBirthVersionID()+"\n";
		        		description=description+"Death Version Name:"+generalDatakeeper.getGlobalDataKeeper().getAllPPLTables().get(configuration.getFinalRowsZoomArea()[row][0]).getDeath()+"\n";
		        		description=description+"Death Version ID:"+generalDatakeeper.getGlobalDataKeeper().getAllPPLTables().get(configuration.getFinalRowsZoomArea()[row][0]).getDeathVersionID()+"\n";
		        		description=description+"Total Changes For This Phase:"+tmpValue+"\n";
		        		
		        		descriptionText.setText(description);

	        		}
	        		
	        		Color cl = new Color(255,69,0,100);
	        		
	        		c.setBackground(cl);
	        		return c;
		        }
	        	
	        }


	        try{
	        	int numericValue=Integer.parseInt(tmpValue);
	        	Color insersionColor=null;
				setToolTipText(Integer.toString(numericValue));

	        	
        		if(numericValue==0){
        			insersionColor=new Color(0,100,0);
        		}
        		else if(numericValue> 0&& numericValue<=segmentSizeZoomArea[3]){
        			
        			insersionColor=new Color(176,226,255);
	        	}
        		else if(numericValue>segmentSizeZoomArea[3] && numericValue<=2*segmentSizeZoomArea[3]){
        			insersionColor=new Color(92,172,238);
        		}
        		else if(numericValue>2*segmentSizeZoomArea[3] && numericValue<=3*segmentSizeZoomArea[3]){
        			
        			insersionColor=new Color(28,134,238);
        		}
        		else{
        			insersionColor=new Color(16,78,139);
        		}
        		c.setBackground(insersionColor);
	        	
	        	return c;
	        }
	        catch(Exception e){
	        		

	        	
        		if(tmpValue.equals("")){
        			c.setBackground(Color.DARK_GRAY);
        			return c; 
        		}
        		else{
        			if(columnName.contains("v")){
        				c.setBackground(Color.lightGray);
        				setToolTipText(columnName);
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
	
	zoomTable.addMouseListener(new MouseAdapter() {
		@Override
		   public void mouseClicked(MouseEvent e) {
			
			if (e.getClickCount() == 1) {
				JTable target = (JTable)e.getSource();
		         
		         selectedRowsFromMouse = target.getSelectedRows();
		         selectedColumnZoomArea = target.getSelectedColumn();
		         zoomAreaTable.repaint();
			}
		   
		   }
	});
	
	zoomTable.addMouseListener(new MouseAdapter() {
		@Override
		   public void mouseReleased(MouseEvent e) {
			
				if(SwingUtilities.isRightMouseButton(e)){
					System.out.println("Right Click");

						JTable target1 = (JTable)e.getSource();
						selectedColumnZoomArea=target1.getSelectedColumn();
						selectedRowsFromMouse=target1.getSelectedRows();
						System.out.println(target1.getSelectedColumn());
						System.out.println(target1.getSelectedRow());
						
						tablesSelected = new ArrayList<String>();

						for(int rowsSelected=0; rowsSelected<selectedRowsFromMouse.length; rowsSelected++){
							tablesSelected.add((String) zoomTable.getValueAt(selectedRowsFromMouse[rowsSelected], 0));
							System.out.println(tablesSelected.get(rowsSelected));
						}
		            	if (zoomTable.getColumnName(selectedColumnZoomArea).contains("Phase")) {

							final JPopupMenu popupMenu = new JPopupMenu();
					        JMenuItem showDetailsItem = new JMenuItem("Show Details");
					        showDetailsItem.addActionListener(new ActionListener() {

					            @Override
					            public void actionPerformed(ActionEvent e) {
				            		firstLevelUndoColumnsZoomArea=configuration.getFinalColumnsZoomArea();
					            	firstLevelUndoRowsZoomArea=configuration.getFinalRowsZoomArea();
				            		showSelectionToZoomArea(selectedColumnZoomArea);
									
					            	
					            }
					        });
					        popupMenu.add(showDetailsItem);
					        popupMenu.show(zoomTable, e.getX(),e.getY());
		            	}
					        
					
				}
			
		   }
	});
	
	// listener
	zoomTable.getTableHeader().addMouseListener(new MouseAdapter() {
	    @Override
	    public void mouseClicked(MouseEvent e) {
	    	wholeColZoomArea = zoomTable.columnAtPoint(e.getPoint());
	        String name = zoomTable.getColumnName(wholeColZoomArea);
	        System.out.println("Column index selected " + configuration.getWholeCol() + " " + name);
	        zoomTable.repaint();
	    }
	});
	
	zoomTable.getTableHeader().addMouseListener(new MouseAdapter() {
	    @Override
	    public void mouseReleased(MouseEvent e) {
	    	if(SwingUtilities.isRightMouseButton(e)){
				System.out.println("Right Click");
				
						final JPopupMenu popupMenu = new JPopupMenu();
				        JMenuItem showDetailsItem = new JMenuItem("Clear Column Selection");
				        showDetailsItem.addActionListener(new ActionListener() {

				            @Override
				            public void actionPerformed(ActionEvent e) {
				            	wholeColZoomArea=-1;
				            	zoomTable.repaint();
				            }
				        });
				        popupMenu.add(showDetailsItem);
				        popupMenu.show(zoomTable, e.getX(),e.getY());
				    
			}
		
	   }
	    
	});
	
	
	zoomAreaTable=zoomTable;
	
	tmpScrollPaneZoomArea.setViewportView(zoomAreaTable);
	tmpScrollPaneZoomArea.setAlignmentX(0);
	tmpScrollPaneZoomArea.setAlignmentY(0);
	tmpScrollPaneZoomArea.setBounds(300,300,950,250);
	tmpScrollPaneZoomArea.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	tmpScrollPaneZoomArea.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
	

	lifeTimePanel.setCursor(getCursor());
	lifeTimePanel.add(tmpScrollPaneZoomArea);
	
	
	
}

	private void makeDetailedTable(String[] columns , String[][] rows, final boolean levelized){
		
		detailedModel=new MyTableModel(columns,rows);
		
		final JvTable tmpLifeTimeTable= new JvTable(detailedModel);
		
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
		    
			private static final long serialVersionUID = 1L;

			@Override
		    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
		    {
		        final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		        
		        String tmpValue=(String) table.getValueAt(row, column);
		        String columnName=table.getColumnName(column);
		        Color fr=new Color(0,0,0);
		        c.setForeground(fr);
		        
		        
		        if(selectedColumn==0){
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
		        		else if(numericValue>0 && numericValue<=segmentSizeDetailedTable[0]){
		        			
		        			insersionColor=new Color(193,255,193);
			        	}
		        		else if(numericValue>segmentSizeDetailedTable[0] && numericValue<=2*segmentSizeDetailedTable[0]){
		        			insersionColor=new Color(84,255,159);
		        		}
		        		else if(numericValue>2*segmentSizeDetailedTable[0] && numericValue<=3*segmentSizeDetailedTable[0]){
		        			
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
		        		else if(numericValue>0 && numericValue<=segmentSizeDetailedTable[1]){
		        			
		        			insersionColor=new Color(176,226,255);
			        	}
		        		else if(numericValue>segmentSizeDetailedTable[1] && numericValue<=2*segmentSizeDetailedTable[1]){
		        			insersionColor=new Color(92,172,238);
		        		}
		        		else if(numericValue>2*segmentSizeDetailedTable[1] && numericValue<=3*segmentSizeDetailedTable[1]){
		        			
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
		        		else if(numericValue>0 && numericValue<=segmentSizeDetailedTable[2]){
		        			
		        			insersionColor=new Color(255,106,106);
			        	}
		        		else if(numericValue>segmentSizeDetailedTable[2] && numericValue<=2*segmentSizeDetailedTable[2]){
		        			insersionColor=new Color(255,0,0);
		        		}
		        		else if(numericValue>2*segmentSizeDetailedTable[2] && numericValue<=3*segmentSizeDetailedTable[2]){
		        			
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
	
	private class RowListener implements ListSelectionListener {
        public void valueChanged(ListSelectionEvent event) {
            if (event.getValueIsAdjusting()) {
                return;
            }
            
            int selectedRow=LifeTimeTable.getSelectedRow();
            configuration.getSelectedRows().add(selectedRow);
           
     
        }
    }
	
	private class ColumnListener implements ListSelectionListener {
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
					TableClusteringMainEngine mainEngine2 = new TableClusteringMainEngine(generalDatakeeper.getGlobalDataKeeper(),wb,wd,wc);
					mainEngine2.extractClusters(configuration.getNumberOfClusters());
					generalDatakeeper.getGlobalDataKeeper().setClusterCollectors(mainEngine2.getClusterCollectors());
					
					ClusterValidatorMainEngine lala = new ClusterValidatorMainEngine(generalDatakeeper.getGlobalDataKeeper());
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
		
		TableClusteringMainEngine mainEngine2 = new TableClusteringMainEngine(generalDatakeeper.getGlobalDataKeeper(),0.333,0.333,0.333);
		mainEngine2.extractClusters(4);
		generalDatakeeper.getGlobalDataKeeper().setClusterCollectors(mainEngine2.getClusterCollectors());
		
		ClusterValidatorMainEngine lala = new ClusterValidatorMainEngine(generalDatakeeper.getGlobalDataKeeper());
		lala.run();
		
		lalaString=lalaString+"\n"+"0.333"+"\t"+"0.333"+"\t"+"0.333"
				+"\n"+lala.getExternalEvaluationReport();
		
		for(double wb=0.0; wb<=1.0; wb=wb+0.5){
			
			for(double wd=(1.0-wb); wd>=0.0; wd=wd-0.5){
				
					double wc=1.0-(wb+wd);
					mainEngine2 = new TableClusteringMainEngine(generalDatakeeper.getGlobalDataKeeper(),wb,wd,wc);
					mainEngine2.extractClusters(4);
					generalDatakeeper.getGlobalDataKeeper().setClusterCollectors(mainEngine2.getClusterCollectors());
					
					lala = new ClusterValidatorMainEngine(generalDatakeeper.getGlobalDataKeeper());
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
		
		 TreeConstructionPhases tc=new TreeConstructionPhases(generalDatakeeper.getGlobalDataKeeper());
		 tablesTree=tc.constructTree();
		 
		 tablesTree.addTreeSelectionListener(new TreeSelectionListener () {
			    public void valueChanged(TreeSelectionEvent ae) { 
			    	TreePath selection = ae.getPath();
			    	selectedFromTree.add(selection.getLastPathComponent().toString());
			    	System.out.println(selection.getLastPathComponent().toString()+" is selected");
			    	
			    }
		 });
		 
		 tablesTree.addMouseListener(new MouseAdapter() {
				@Override
				   public void mouseReleased(MouseEvent e) {
					
						if(SwingUtilities.isRightMouseButton(e)){
							System.out.println("Right Click Tree");
							
							final JPopupMenu popupMenu = new JPopupMenu();
					        JMenuItem showDetailsItem = new JMenuItem("Show This into the Table");
					        showDetailsItem.addActionListener(new ActionListener() {

					            @Override
					            public void actionPerformed(ActionEvent e) {
					          
					                LifeTimeTable.repaint();
					            	
					            }
					        });
					        popupMenu.add(showDetailsItem);
					        popupMenu.show(tablesTree, e.getX(),e.getY());
							        							        
							
						}
					
				   }
			});
		 
		 treeScrollPane.setViewportView(tablesTree);
		 treeScrollPane.setBounds(5, 5, 250, 170);
		 treeScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		 treeScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		 tablesTreePanel.add(treeScrollPane);
		 
		 treeLabel.setText("Phases Tree");

		 sideMenu.revalidate();
		 sideMenu.repaint();
		
	}
	
	public void fillClustersTree(ArrayList<Cluster> clusters){
		
		 TreeConstructionPhasesWithClusters treePhaseWithClusters = new TreeConstructionPhasesWithClusters(clusters);
		 tablesTree=treePhaseWithClusters.constructTree();
		 
		 tablesTree.addTreeSelectionListener(new TreeSelectionListener () {
			    public void valueChanged(TreeSelectionEvent ae) { 
			    	TreePath selection = ae.getPath();
			    	selectedFromTree.add(selection.getLastPathComponent().toString());
			    	System.out.println(selection.getLastPathComponent().toString()+" is selected");
			    	
			    }
		 });
		 
		 tablesTree.addMouseListener(new MouseAdapter() {
				@Override
				   public void mouseReleased(MouseEvent e) {
					
						if(SwingUtilities.isRightMouseButton(e)){
							System.out.println("Right Click Tree");
							
							final JPopupMenu popupMenu = new JPopupMenu();
					        JMenuItem showDetailsItem = new JMenuItem("Show This into the Table");
					        showDetailsItem.addActionListener(new ActionListener() {

					            @Override
					            public void actionPerformed(ActionEvent e) {
					          
					                LifeTimeTable.repaint();
					            	
					            }
					        });
					        popupMenu.add(showDetailsItem);
					        popupMenu.show(tablesTree, e.getX(),e.getY());
							        	
						}
					
				   }
			});
		 
		 treeScrollPane.setViewportView(tablesTree);
		 
		 
		 treeScrollPane.setBounds(5, 5, 250, 170);
		 treeScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		 treeScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		 tablesTreePanel.add(treeScrollPane);

		 treeLabel.setText("Clusters Tree");

		 sideMenu.revalidate();
		 sideMenu.repaint();
		 
		
	}
	
	public void setDescription(String descr){
		descriptionText.setText(descr);
	}
	
	
	public void initiateLoadState()
	{
		configuration.setTimeWeight((float)0.5); 
		configuration.setChangeWeight((float)0.5);
		configuration.setPreProcessingTime(false);
		configuration.setPreProcessingChange(false);
		
		generalDatakeeper = new GeneralDatakeeper(); 
		
	}
	public void makeGeneralTableIDU()
	{
		String[][] sortedRows = generalDatakeeper.sortRows(configuration.getFinalRowsZoomArea());
		
		setSortedRows(sortedRows);
		setPLDVisibility(true);
		
		setGeneralTablesListeners(generalDatakeeper.makeGeneralTableIDU(configuration));
	}
	
	
	public void importData(File file)
	{
		this.generalDatakeeper = new GeneralDatakeeper();
		generalDatakeeper.importDataFromFile(file);
		generalDatakeeper.createTableConstructionIDU();
		
		setIDUPanel();
			
		makeGeneralTableIDU();
		
		generalDatakeeper.createPhaseAnalyserEngine(configuration);
		
		configuration.setNumberOfClusters(14);
		generalDatakeeper.createTableClusteringMainEngine(configuration.getNumberOfClusters());
		
		fillTable();
		printPPLData();
		fillTree();
	}
	
	
	
	
	public void setIDUPanel()
	{
		configuration.setFinalColumnsZoomArea(generalDatakeeper.getTableConstructionIDU().getConstructedColumns());
		configuration.setFinalRowsZoomArea(generalDatakeeper.getTableConstructionIDU().getConstructedRows());
		tabbedPane.setSelectedIndex(0);
		segmentSizeZoomArea = generalDatakeeper.getTableConstructionIDU().getSegmentSize();
	}
	
	public void setSortedRows(String[][] sortedRows)
	{
		//makeGeneralTableIDU
		
		configuration.setFinalRowsZoomArea(sortedRows);
		configuration.setSelectedRows(new ArrayList<Integer>());
	}
	
	public void setPLDVisibility(boolean action)
	{
		showingPld=action;
		zoomInButton.setVisible(action);
		zoomOutButton.setVisible(action);
		showThisToPopup.setVisible(action); 
		
	}
	
	
	
	
	public void fillTable() 
	{
		if(generalDatakeeper.getGlobalDataKeeper().getPhaseCollectors().size()==0)
		{
			return;
		}
		constructTableWithClusters();
		
		tabbedPane.setSelectedIndex(0);
		uniformlyDistributedButton.setVisible(true);
		notUniformlyDistributedButton.setVisible(true);
		
		makeGeneralTablePhases();
		
		fillClustersTree(generalDatakeeper.getGlobalDataKeeper().getClusterCollectors().get(0).getClusters());
	}
	
	public void constructTableWithClusters() 
	{
		TableConstructionWithClusters table = generalDatakeeper.constructTableWithClusters();
		
		final String[] columns= table.getConstructedColumns();
		final String[][] rows= table.getConstructedRows();
		
		configuration.setSegmentSize(table.getSegmentSize());
		configuration.setFinalColumns(columns);
		configuration.setFinalRows(rows);
		configuration.setSelectedRows(new ArrayList<Integer>());
	}
	
	private void makeGeneralTablePhases(){
		JvTable generalTable = generalDatakeeper.makeGeneralTablePhases(configuration);
		setMoreTableListeners(generalTable);
	}
	
	
	public void printPPLData() 
	{
		System.out.println("Schemas:"+generalDatakeeper.getGlobalDataKeeper().getAllPPLSchemas().size());
		System.out.println("Transitions:"+generalDatakeeper.getGlobalDataKeeper().getAllPPLTransitions().size());
		System.out.println("Tables:"+generalDatakeeper.getGlobalDataKeeper().getAllPPLTables().size());
	}
	
	
	private void setGeneralTablesListeners(final JvTable generalTable){
		final IDUTableRenderer renderer = new IDUTableRenderer(Gui.this,configuration.getFinalRowsZoomArea(), 
				generalDatakeeper.getGlobalDataKeeper(), 
				configuration.getSegmentSize());
		//generalTable.setDefaultRenderer(Object.class, renderer);
		
		
		
		generalTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer()
		{
		    
			private static final long serialVersionUID = 1L;

			@Override
		    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
		    {
		        final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		        
		        String tmpValue=configuration.getFinalRowsZoomArea()[row][column];
		        String columnName=table.getColumnName(column);
		        Color fr=new Color(0,0,0);
		        
		        c.setForeground(fr);
		        setOpaque(true);
		      
		        if(column==wholeColZoomArea && wholeColZoomArea!=0){
		        	
		        	String description="Transition ID:"+table.getColumnName(column)+"\n";
		        	description=description+"Old Version Name:"+generalDatakeeper.getGlobalDataKeeper().getAllPPLTransitions().
	        				get(Integer.parseInt(table.getColumnName(column))).getOldVersionName()+"\n";
	        		description=description+"New Version Name:"+generalDatakeeper.getGlobalDataKeeper().getAllPPLTransitions().
	        				get(Integer.parseInt(table.getColumnName(column))).getNewVersionName()+"\n";		        		
	        		
        			description=description+"Transition Changes:"+generalDatakeeper.getGlobalDataKeeper().getAllPPLTransitions().get(Integer.parseInt(table.getColumnName(column))).getNumberOfChangesForOneTr()+"\n";
        			description=description+"Additions:"+generalDatakeeper.getGlobalDataKeeper().getAllPPLTransitions().get(Integer.parseInt(table.getColumnName(column))).getNumberOfAdditionsForOneTr()+"\n";
        			description=description+"Deletions:"+generalDatakeeper.getGlobalDataKeeper().getAllPPLTransitions().get(Integer.parseInt(table.getColumnName(column))).getNumberOfDeletionsForOneTr()+"\n";
        			description=description+"Updates:"+generalDatakeeper.getGlobalDataKeeper().getAllPPLTransitions().get(Integer.parseInt(table.getColumnName(column))).getNumberOfUpdatesForOneTr()+"\n";

        			
	        		descriptionText.setText(description);
		        	
		        	Color cl = new Color(255,69,0,100);

	        		c.setBackground(cl);
	        		return c;
		        }
		        else if(selectedColumnZoomArea==0){
		    		
		        	if (isSelected){
		        		Color cl = new Color(255,69,0,100);
		        		c.setBackground(cl);
		        		
		        		String description="Table:"+configuration.getFinalRowsZoomArea()[row][0]+"\n";
		        		description=description+"Birth Version Name:"+generalDatakeeper.getGlobalDataKeeper().getAllPPLTables().get(configuration.getFinalRowsZoomArea()[row][0]).getBirth()+"\n";
		        		description=description+"Birth Version ID:"+generalDatakeeper.getGlobalDataKeeper().getAllPPLTables().get(configuration.getFinalRowsZoomArea()[row][0]).getBirthVersionID()+"\n";
		        		description=description+"Death Version Name:"+generalDatakeeper.getGlobalDataKeeper().getAllPPLTables().get(configuration.getFinalRowsZoomArea()[row][0]).getDeath()+"\n";
		        		description=description+"Death Version ID:"+generalDatakeeper.getGlobalDataKeeper().getAllPPLTables().get(configuration.getFinalRowsZoomArea()[row][0]).getDeathVersionID()+"\n";
		        		description=description+"Total Changes:"+generalDatakeeper.getGlobalDataKeeper().getAllPPLTables().get(configuration.getFinalRowsZoomArea()[row][0]).getTotalChanges()+"\n";

		        		
		        		descriptionText.setText(description);
		        		
		        		return c;
		        		
		        		
		        	}
		        }
		        else{


		        	if(selectedFromTree.contains(configuration.getFinalRowsZoomArea()[row][0])){


		        		Color cl = new Color(255,69,0,100);
		        		
		        		c.setBackground(cl);
		        		
		        		return c;
		        	}
		        	
			      
		        	
		        	if (isSelected && hasFocus){

		        		String description="";
		        		if(!table.getColumnName(column).contains("Table name")){
			        		description="Table:"+configuration.getFinalRowsZoomArea()[row][0]+"\n";
			        		
			        		description=description+"Old Version Name:"+generalDatakeeper.getGlobalDataKeeper().getAllPPLTransitions().
			        				get(Integer.parseInt(table.getColumnName(column))).getOldVersionName()+"\n";
			        		description=description+"New Version Name:"+generalDatakeeper.getGlobalDataKeeper().getAllPPLTransitions().
			        				get(Integer.parseInt(table.getColumnName(column))).getNewVersionName()+"\n";		        		
			        		if(generalDatakeeper.getGlobalDataKeeper().getAllPPLTables().get(configuration.getFinalRowsZoomArea()[row][0]).
			        				getTableChanges().getTableAtChForOneTransition(Integer.parseInt(table.getColumnName(column)))!=null){
			        			description=description+"Transition Changes:"+generalDatakeeper.getGlobalDataKeeper().getAllPPLTables().get(configuration.getFinalRowsZoomArea()[row][0]).
			        				getTableChanges().getTableAtChForOneTransition(Integer.parseInt(table.getColumnName(column))).size()+"\n";
			        			description=description+"Additions:"+generalDatakeeper.getGlobalDataKeeper().getAllPPLTables().get(configuration.getFinalRowsZoomArea()[row][0]).
			        					getNumberOfAdditionsForOneTr(Integer.parseInt(table.getColumnName(column)))+"\n";
			        			description=description+"Deletions:"+generalDatakeeper.getGlobalDataKeeper().getAllPPLTables().get(configuration.getFinalRowsZoomArea()[row][0]).
			        					getNumberOfDeletionsForOneTr(Integer.parseInt(table.getColumnName(column)))+"\n";
			        			description=description+"Updates:"+generalDatakeeper.getGlobalDataKeeper().getAllPPLTables().get(configuration.getFinalRowsZoomArea()[row][0]).
			        					getNumberOfUpdatesForOneTr(Integer.parseInt(table.getColumnName(column)))+"\n";
			        			
			        		}
			        		else{
			        			description=description+"Transition Changes:0"+"\n";
			        			description=description+"Additions:0"+"\n";
			        			description=description+"Deletions:0"+"\n";
			        			description=description+"Updates:0"+"\n";
			        			
			        		}
			        		
			        		descriptionText.setText(description);
		        		}
		        		Color cl = new Color(255,69,0,100);
		        		
		        		c.setBackground(cl);
		        		
		        		return c;
			        }
		        	
		        	
		        	
		        }

		        try{
		        	int numericValue=Integer.parseInt(tmpValue);
		        	Color insersionColor=null;
					setToolTipText(Integer.toString(numericValue));

		        	
	        		if(numericValue==0){
	        			insersionColor=new Color(154,205,50,200);
	        		}
	        		else if(numericValue> 0&& numericValue<=segmentSizeZoomArea[3]){
	        			
	        			insersionColor=new Color(176,226,255);
		        	}
	        		else if(numericValue>segmentSizeZoomArea[3] && numericValue<=2*segmentSizeZoomArea[3]){
	        			insersionColor=new Color(92,172,238);
	        		}
	        		else if(numericValue>2*segmentSizeZoomArea[3] && numericValue<=3*segmentSizeZoomArea[3]){
	        			
	        			insersionColor=new Color(28,134,238);
	        		}
	        		else{
	        			insersionColor=new Color(16,78,139);
	        		}
	        		c.setBackground(insersionColor);
		        	
		        	return c;
		        }
		        catch(Exception e){
		        		

	        		if(tmpValue.equals("")){
	        			c.setBackground(Color.GRAY);
	        			return c; 
	        		}
	        		else{
	        			if(columnName.contains("v")){
	        				c.setBackground(Color.lightGray);
	        				setToolTipText(columnName);
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
				
		generalTable.addMouseListener(new MouseAdapter() {
			@Override
			   public void mouseClicked(MouseEvent e) {
				
				if (e.getClickCount() == 1) {
					JTable target = (JTable)e.getSource();
			         
			         selectedRowsFromMouse = target.getSelectedRows();
			         selectedColumnZoomArea = target.getSelectedColumn();
			         renderer.setSelCol(selectedColumnZoomArea);
			         target.getSelectedColumns();
			         
			         zoomAreaTable.repaint();
				}
				
			  }
		});
		
		generalTable.addMouseListener(new MouseAdapter() {
			@Override
			   public void mouseReleased(MouseEvent e) {
				
					if(SwingUtilities.isRightMouseButton(e)){
						System.out.println("Right Click");

						JTable target1 = (JTable)e.getSource();
						target1.getSelectedColumns();
						selectedRowsFromMouse=target1.getSelectedRows();
						System.out.println(target1.getSelectedColumns().length);
						System.out.println(target1.getSelectedRow());
						for(int rowsSelected=0; rowsSelected<selectedRowsFromMouse.length; rowsSelected++){
							System.out.println(generalTable.getValueAt(selectedRowsFromMouse[rowsSelected], 0));
						}
						final JPopupMenu popupMenu = new JPopupMenu();
				        JMenuItem showDetailsItem = new JMenuItem("Clear Selection");
				        showDetailsItem.addActionListener(new ActionListener() {

				            @Override
				            public void actionPerformed(ActionEvent e) {
				            	selectedFromTree=new ArrayList<String>();
				            	zoomAreaTable.repaint();
				            }
				        });
				        popupMenu.add(showDetailsItem);
				        popupMenu.show(generalTable, e.getX(),e.getY());
						        						        
						    
					}
				
			   }
		});
		
		
		generalTable.getTableHeader().addMouseListener(new MouseAdapter() {
		    @Override
		    public void mouseClicked(MouseEvent e) {
		        wholeColZoomArea = generalTable.columnAtPoint(e.getPoint());
		        renderer.setWholeCol(generalTable.columnAtPoint(e.getPoint()));
		        //String name = generalTable.getColumnName(wholeColZoomArea);
		        //System.out.println("Column index selected " + wholeColZoomArea + " " + name);
		        generalTable.repaint();
		    }
		});
		
		generalTable.getTableHeader().addMouseListener(new MouseAdapter() {
		    @Override
		    public void mouseReleased(MouseEvent e) {
		    	if(SwingUtilities.isRightMouseButton(e)){
					System.out.println("Right Click");
					
							final JPopupMenu popupMenu = new JPopupMenu();
					        JMenuItem showDetailsItem = new JMenuItem("Clear Column Selection");
					        showDetailsItem.addActionListener(new ActionListener() {

					            @Override
					            public void actionPerformed(ActionEvent e) {
					            	wholeColZoomArea=-1;
							        renderer.setWholeCol(wholeColZoomArea);

					            	generalTable.repaint();
					            }
					        });
					        popupMenu.add(showDetailsItem);
					        popupMenu.show(generalTable, e.getX(),e.getY());
					    
				}
			
		   }
		    
		});
		
		zoomAreaTable=generalTable;
		tmpScrollPaneZoomArea.setViewportView(zoomAreaTable);
		tmpScrollPaneZoomArea.setAlignmentX(0);
		tmpScrollPaneZoomArea.setAlignmentY(0);
		tmpScrollPaneZoomArea.setBounds(300,300,950,250);
		tmpScrollPaneZoomArea.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		tmpScrollPaneZoomArea.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        
		lifeTimePanel.setCursor(getCursor());
		lifeTimePanel.add(tmpScrollPaneZoomArea);
	
	
}

	private void setMoreTableListeners(final JvTable generalTable)
	{
		generalTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer()
		{
		    
			private static final long serialVersionUID = 1L;

			@Override
		    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
		    {
		        final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		        	        
		        String tmpValue=configuration.getFinalRows()[row][column];
		        String columnName=table.getColumnName(column);
		        Color fr=new Color(0,0,0);
		        c.setForeground(fr);
		        
		        if(column==configuration.getWholeCol() && configuration.getWholeCol()!=0){
		        	
		        	String description=table.getColumnName(column)+"\n";
		          	description=description+"First Transition ID:"+generalDatakeeper.getGlobalDataKeeper().getPhaseCollectors().get(0).getPhases().
	        				get(column-1).getStartPos()+"\n";
	        		description=description+"Last Transition ID:"+generalDatakeeper.getGlobalDataKeeper().getPhaseCollectors().get(0).getPhases().
	        				get(column-1).getEndPos()+"\n";
	        		description=description+"Total Changes For This Phase:"+generalDatakeeper.getGlobalDataKeeper().getPhaseCollectors().get(0).getPhases().
	        				get(column-1).getTotalUpdates()+"\n";
	        		description=description+"Additions For This Phase:"+generalDatakeeper.getGlobalDataKeeper().getPhaseCollectors().get(0).getPhases().
	        				get(column-1).getTotalAdditionsOfPhase()+"\n";
	        		description=description+"Deletions For This Phase:"+generalDatakeeper.getGlobalDataKeeper().getPhaseCollectors().get(0).getPhases().
	        				get(column-1).getTotalDeletionsOfPhase()+"\n";
	        		description=description+"Updates For This Phase:"+generalDatakeeper.getGlobalDataKeeper().getPhaseCollectors().get(0).getPhases().
	        				get(column-1).getTotalUpdatesOfPhase()+"\n";
		        	
	        		descriptionText.setText(description);
		        	
		        	Color cl = new Color(255,69,0,100);

	        		c.setBackground(cl);
	        		return c;
		        }
		        else if(selectedColumn==0){
		        	if (isSelected){
		        		
		        		if(configuration.getFinalRows()[row][0].contains("Cluster")){
			        		String description="Cluster:"+configuration.getFinalRows()[row][0]+"\n";
			        		description=description+"Birth Version Name:"+generalDatakeeper.getGlobalDataKeeper().getClusterCollectors().get(0).getClusters().get(row).getBirthSqlFile()+"\n";
			        		description=description+"Birth Version ID:"+generalDatakeeper.getGlobalDataKeeper().getClusterCollectors().get(0).getClusters().get(row).getBirth()+"\n";
			        		description=description+"Death Version Name:"+generalDatakeeper.getGlobalDataKeeper().getClusterCollectors().get(0).getClusters().get(row).getDeathSqlFile()+"\n";
			        		description=description+"Death Version ID:"+generalDatakeeper.getGlobalDataKeeper().getClusterCollectors().get(0).getClusters().get(row).getDeath()+"\n";
			        		description=description+"Tables:"+generalDatakeeper.getGlobalDataKeeper().getClusterCollectors().get(0).getClusters().get(row).getNamesOfTables().size()+"\n";
			        		description=description+"Total Changes:"+generalDatakeeper.getGlobalDataKeeper().getClusterCollectors().get(0).getClusters().get(row).getTotalChanges()+"\n";
			        		
			        		descriptionText.setText(description);
		        		}
		        		else{
			        		String description="Table:"+configuration.getFinalRows()[row][0]+"\n";
			        		description=description+"Birth Version Name:"+generalDatakeeper.getGlobalDataKeeper().getAllPPLTables().get(configuration.getFinalRows()[row][0]).getBirth()+"\n";
			        		description=description+"Birth Version ID:"+generalDatakeeper.getGlobalDataKeeper().getAllPPLTables().get(configuration.getFinalRows()[row][0]).getBirthVersionID()+"\n";
			        		description=description+"Death Version Name:"+generalDatakeeper.getGlobalDataKeeper().getAllPPLTables().get(configuration.getFinalRows()[row][0]).getDeath()+"\n";
			        		description=description+"Death Version ID:"+generalDatakeeper.getGlobalDataKeeper().getAllPPLTables().get(configuration.getFinalRows()[row][0]).getDeathVersionID()+"\n";
			        		description=description+"Total Changes:"+generalDatakeeper.getGlobalDataKeeper().getAllPPLTables().get(configuration.getFinalRows()[row][0]).getTotalChanges()+"\n";
			        		descriptionText.setText(description);

		        		}

		        		
		        		
		        		Color cl = new Color(255,69,0,100);
		        		
		        		c.setBackground(cl);
		        		return c;
		        	}
		        }
		        else{
		        	
		        	if(selectedFromTree.contains(configuration.getFinalRows()[row][0])){

		        		Color cl = new Color(255,69,0,100);
		        		
		        		c.setBackground(cl);
		        		
		        		return c;
		        	}
		        	
		        	if (isSelected && hasFocus){
			        	
		        		String description="";
		        		if(!table.getColumnName(column).contains("Table name")){
		        			
			        		if(configuration.getFinalRows()[row][0].contains("Cluster")){

				        		description=configuration.getFinalRows()[row][0]+"\n";
				        		description=description+"Tables:"+generalDatakeeper.getGlobalDataKeeper().getClusterCollectors().get(0).getClusters().get(row).getNamesOfTables().size()+"\n\n";

				        		description=description+table.getColumnName(column)+"\n";
				        		description=description+"First Transition ID:"+generalDatakeeper.getGlobalDataKeeper().getPhaseCollectors().get(0).getPhases().
				        				get(column-1).getStartPos()+"\n";
				        		description=description+"Last Transition ID:"+generalDatakeeper.getGlobalDataKeeper().getPhaseCollectors().get(0).getPhases().
				        				get(column-1).getEndPos()+"\n\n";
				        		description=description+"Total Changes For This Phase:"+tmpValue+"\n";
				        		
			        		}
			        		else{
			        			description=table.getColumnName(column)+"\n";
				        		description=description+"First Transition ID:"+generalDatakeeper.getGlobalDataKeeper().getPhaseCollectors().get(0).getPhases().
				        				get(column-1).getStartPos()+"\n";
				        		description=description+"Last Transition ID:"+generalDatakeeper.getGlobalDataKeeper().getPhaseCollectors().get(0).getPhases().
				        				get(column-1).getEndPos()+"\n\n";
			        			description=description+"Table:"+configuration.getFinalRows()[row][0]+"\n";
				        		description=description+"Birth Version Name:"+generalDatakeeper.getGlobalDataKeeper().getAllPPLTables().get(configuration.getFinalRows()[row][0]).getBirth()+"\n";
				        		description=description+"Birth Version ID:"+generalDatakeeper.getGlobalDataKeeper().getAllPPLTables().get(configuration.getFinalRows()[row][0]).getBirthVersionID()+"\n";
				        		description=description+"Death Version Name:"+generalDatakeeper.getGlobalDataKeeper().getAllPPLTables().get(configuration.getFinalRows()[row][0]).getDeath()+"\n";
				        		description=description+"Death Version ID:"+generalDatakeeper.getGlobalDataKeeper().getAllPPLTables().get(configuration.getFinalRows()[row][0]).getDeathVersionID()+"\n";
				        		description=description+"Total Changes For This Phase:"+tmpValue+"\n";
				        		
			        		}
			        		
			        		descriptionText.setText(description);

		        		}
		        		
		        		Color cl = new Color(255,69,0,100);
		        		
		        		c.setBackground(cl);
		        		return c;
			        }
		        	
		        }


		        try{
		        	int numericValue=Integer.parseInt(tmpValue);
		        	Color insersionColor=null;
					setToolTipText(Integer.toString(numericValue));

		        	
	        		if(numericValue==0){
	        			insersionColor=new Color(154,205,50,200);
	        		}
	        		else if(numericValue> 0&& numericValue<=configuration.getSegmentSize()[3]){
	        			
	        			insersionColor=new Color(176,226,255);
		        	}
	        		else if(numericValue>configuration.getSegmentSize()[3] && numericValue<=2*configuration.getSegmentSize()[3]){
	        			insersionColor=new Color(92,172,238);
	        		}
	        		else if(numericValue>2*configuration.getSegmentSize()[3] && numericValue<=3*configuration.getSegmentSize()[3]){
	        			
	        			insersionColor=new Color(28,134,238);
	        		}
	        		else{
	        			insersionColor=new Color(16,78,139);
	        		}
	        		c.setBackground(insersionColor);
		        	
		        	return c;
		        }
		        catch(Exception e){
		        		

		        	
	        		if(tmpValue.equals("")){
	        			c.setBackground(Color.gray);
	        			return c; 
	        		}
	        		else{
	        			if(columnName.contains("v")){
	        				c.setBackground(Color.lightGray);
	        				setToolTipText(columnName);
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
		
		generalTable.addMouseListener(new MouseAdapter() {
			@Override
			   public void mouseClicked(MouseEvent e) {
				
				if (e.getClickCount() == 1) {
					JTable target = (JTable)e.getSource();
			         
			         selectedRowsFromMouse = target.getSelectedRows();
			         selectedColumn = target.getSelectedColumn();
			         LifeTimeTable.repaint();
				}

			   }
		});
		
		generalTable.addMouseListener(new MouseAdapter() {
			@Override
			   public void mouseReleased(MouseEvent e) {
					if(e.getButton() == MouseEvent.BUTTON3){
						System.out.println("Right Click");

						JTable target1 = (JTable)e.getSource();
						selectedColumn=target1.getSelectedColumn();
						selectedRowsFromMouse=new int[target1.getSelectedRows().length];
						selectedRowsFromMouse=target1.getSelectedRows();
						
						final String sSelectedRow = (String) generalTable.getValueAt(target1.getSelectedRow(),0);
						tablesSelected = new ArrayList<String>();

						for(int rowsSelected=0; rowsSelected<selectedRowsFromMouse.length; rowsSelected++){
							tablesSelected.add((String) generalTable.getValueAt(selectedRowsFromMouse[rowsSelected], 0));
						}
						
						JPopupMenu popupMenu = new JPopupMenu();
				        JMenuItem showDetailsItem = new JMenuItem("Show Details for the selection");
				        showDetailsItem.addActionListener(new ActionListener() {

				            @Override
				            public void actionPerformed(ActionEvent le) {
				            	if(sSelectedRow.contains("Cluster ")){
				            		showClusterSelectionToZoomArea(selectedColumn,sSelectedRow);

				            	}
				            	else{
				            		showSelectionToZoomArea(selectedColumn);
				            	}
				            }
				        });
				        popupMenu.add(showDetailsItem);
				        JMenuItem clearSelectionItem = new JMenuItem("Clear Selection");
				        clearSelectionItem.addActionListener(new ActionListener() {

				            @Override
				            public void actionPerformed(ActionEvent le) {
				            	
				            	selectedFromTree=new ArrayList<String>();
				            	LifeTimeTable.repaint();
				            }
				        });
				        popupMenu.add(clearSelectionItem);
				        popupMenu.show(generalTable, e.getX(),e.getY());
						      
					}
				
			   }
		});
		
		generalTable.getTableHeader().addMouseListener(new MouseAdapter() {
		    @Override
		    public void mouseClicked(MouseEvent e) {
		    	configuration.setWholeCol(generalTable.columnAtPoint(e.getPoint()));
		        String name = generalTable.getColumnName(configuration.getWholeCol());
		        System.out.println("Column index selected " + configuration.getWholeCol() + " " + name);
		        generalTable.repaint();
		        if (showingPld) {
			        makeGeneralTableIDU();
				}
		    }
		});
		
		generalTable.getTableHeader().addMouseListener(new MouseAdapter() {
		    @Override
		    public void mouseReleased(MouseEvent e) {
		    	if(SwingUtilities.isRightMouseButton(e)){
					System.out.println("Right Click");
					
							final JPopupMenu popupMenu = new JPopupMenu();
					        JMenuItem clearColumnSelectionItem = new JMenuItem("Clear Column Selection");
					        clearColumnSelectionItem.addActionListener(new ActionListener() {

					            @Override
					            public void actionPerformed(ActionEvent e) {
					            	configuration.setWholeCol(-1);
					            	generalTable.repaint();
					            	if(showingPld){
					            		makeGeneralTableIDU();
					            	}
					            }
					        });
					        popupMenu.add(clearColumnSelectionItem);
					        JMenuItem showDetailsItem = new JMenuItem("Show Details for this Phase");
					        showDetailsItem.addActionListener(new ActionListener() {

					            @Override
					            public void actionPerformed(ActionEvent e) {
									String sSelectedRow=configuration.getFinalRows()[0][0];
									System.out.println("?"+sSelectedRow);
					            	tablesSelected=new ArrayList<String>();
					            	for(int i=0; i<configuration.getFinalRows().length; i++)
					            		tablesSelected.add((String) generalTable.getValueAt(i, 0));

					            	if(!sSelectedRow.contains("Cluster ")){
					            		
					            		showSelectionToZoomArea(configuration.getWholeCol());	
					            	}
					            	else{
					            		showClusterSelectionToZoomArea(configuration.getWholeCol(), "");
					            	}
					            	
					            }
					        });
					        popupMenu.add(showDetailsItem);
					        popupMenu.show(generalTable, e.getX(),e.getY());
					    
				}
			
		   }
		    
		});
		
		
		LifeTimeTable=generalTable;
		
		tmpScrollPane.setViewportView(LifeTimeTable);
		tmpScrollPane.setAlignmentX(0);
		tmpScrollPane.setAlignmentY(0);
	    tmpScrollPane.setBounds(300,30,950,265);
	    tmpScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	    tmpScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
	    
		lifeTimePanel.setCursor(getCursor());
		lifeTimePanel.add(tmpScrollPane);
	}
	public void fillTree(){
		
		 TreeConstructionGeneral treeConstruction=new TreeConstructionGeneral(generalDatakeeper.getGlobalDataKeeper().getAllPPLSchemas());
		 tablesTree=new JTree();
		 tablesTree=treeConstruction.constructTree();
		 tablesTree.addTreeSelectionListener(new TreeSelectionListener () {
			    public void valueChanged(TreeSelectionEvent ae) { 
			    	TreePath selection = ae.getPath();
			    	selectedFromTree.add(selection.getLastPathComponent().toString());
			    	System.out.println(selection.getLastPathComponent().toString()+" is selected");
			    	
			    }
		 });
		 
		 tablesTree.addMouseListener(new MouseAdapter() {
				@Override
				   public void mouseReleased(MouseEvent e) {
					
						if(SwingUtilities.isRightMouseButton(e)){
							System.out.println("Right Click Tree");
								
									final JPopupMenu popupMenu = new JPopupMenu();
							        JMenuItem showDetailsItem = new JMenuItem("Show This into the Table");
							        showDetailsItem.addActionListener(new ActionListener() {
		
							            @Override
							            public void actionPerformed(ActionEvent e) {
							          
							                LifeTimeTable.repaint();
							            	
							            }
							        });
							        popupMenu.add(showDetailsItem);
							        popupMenu.show(tablesTree, e.getX(),e.getY());
							        							        
								//}
							//}
						}
					
				   }
			});
		 
		 treeScrollPane.setViewportView(tablesTree);
		 
		 treeScrollPane.setBounds(5, 5, 250, 170);
		 treeScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		 treeScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		 tablesTreePanel.add(treeScrollPane);
		 
		 treeLabel.setText("General Tree");

		 sideMenu.revalidate();
		 sideMenu.repaint();		
		
	}



	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	
}