package gui.configurations;

import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTree;

import data.dataKeeper.GlobalDataKeeper;
import data.dataKeeper.PPLFile;
import gui.tableElements.commons.JvTable;
import gui.tableElements.commons.MyTableModel;
import gui.tableElements.tableConstructors.TableConstructionIDU;
import gui.tableElements.tableConstructors.TableConstructionWithClusters;
import services.DataService;

public class Configuration 
{
	private Float timeWeight=null;
	private Float changeWeight=null;
	private Double birthWeight=null;
	private Double deathWeight=null;
	private Double changeWeightCl=null;
	private Integer numberOfPhases=null;
	private Integer numberOfClusters=null;
	private Boolean preProcessingTime=null;
	private Boolean preProcessingChange=null;
	private int rowHeight=1;
	private int columnWidth=1;
	private int wholeCol=-1;
	private Integer[] segmentSize=new Integer[4];
	
	private String[] finalColumns=null;
	private String[][] finalRows=null;
	private String[] finalColumnsZoomArea = null; 
	private String[][] finalRowsZoomArea =null;
	
	private ArrayList<Integer> selectedRows=new ArrayList<Integer>();
	
	
	//~~
	
	private JPanel contentPane;
	private JPanel lifeTimePanel = new JPanel();
	private JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
	
	private MyTableModel detailedModel = null;
	private MyTableModel generalModel = null;
	private MyTableModel zoomModel = null;

	private JvTable LifeTimeTable=null;
	private JvTable zoomAreaTable=null;
	
	
	
	private JScrollPane tmpScrollPane =new JScrollPane();
	private JScrollPane treeScrollPane= new JScrollPane();
	private JScrollPane tmpScrollPaneZoomArea =new JScrollPane();

	
	
	//public ArrayList<Integer> selectedRows=new ArrayList<Integer>();
	//public globalDataKeeper globalDataKeeper=null;

	
	
	
	
	
	private String[] firstLevelUndoColumnsZoomArea=null;
	private String[][] firstLevelUndoRowsZoomArea=null;
	//public String currentProject=null; == getFileContent
	//public String project=null;
	
	
	
	private Integer[] segmentSizeZoomArea=new Integer[4];
	private Integer[] segmentSizeDetailedTable=new Integer[3];

	private ArrayList<String> selectedFromTree=new ArrayList<String>();
	
	private JTree tablesTree=new JTree();
	private JPanel sideMenu=new JPanel();
	private JPanel tablesTreePanel=new JPanel();
	private JPanel descriptionPanel=new JPanel();
	private JLabel treeLabel;
	private JLabel generalTableLabel;
	private JLabel zoomAreaLabel;
	private JLabel descriptionLabel;
	private JTextArea descriptionText;
	private JButton zoomInButton;
	private JButton zoomOutButton;
	private JButton uniformlyDistributedButton;
	private JButton notUniformlyDistributedButton;
	private JButton showThisToPopup;

	
	private int[] selectedRowsFromMouse;
	private int selectedColumn=-1;
	private int selectedColumnZoomArea=-1;

	private int wholeColZoomArea=-1;
	
	

	private ArrayList<String> tablesSelected = new ArrayList<String>();

	private boolean showingPld=false;
	
	private JButton undoButton;
	private JMenu mnProject;
	private JMenuItem mntmInfo;
	
	//~~

	public Configuration(Float timeWeight,
						 Float changeWeight,
						 Double birthWeight,
						 Double deathWeight,
						 Double changeWeightCl)
	{
		this.timeWeight=timeWeight;
		this.changeWeight=changeWeight;
		this.birthWeight=birthWeight;
		this.deathWeight=deathWeight;
		this.changeWeightCl=changeWeightCl;
	}
	
	public Configuration(){}

	public Float getTimeWeight() {
		return timeWeight;
	}

	public void setTimeWeight(Float timeWeight) {
		this.timeWeight = timeWeight;
	}

	public Float getChangeWeight() {
		return changeWeight;
	}

	public void setChangeWeight(Float changeWeight) {
		this.changeWeight = changeWeight;
	}

	public Double getBirthWeight() {
		return birthWeight;
	}

	public void setBirthWeight(Double birthWeight) {
		this.birthWeight = birthWeight;
	}

	public Double getDeathWeight() {
		return deathWeight;
	}

	public void setDeathWeight(Double deathWeight) {
		this.deathWeight = deathWeight;
	}

	public Double getChangeWeightCl() {
		return changeWeightCl;
	}

	public void setChangeWeightCl(Double changeWeightCl) {
		this.changeWeightCl = changeWeightCl;
	}

	public Integer getNumberOfPhases() {
		return numberOfPhases;
	}

	public void setNumberOfPhases(Integer numberOfPhases) {
		this.numberOfPhases = numberOfPhases;
	}

	public Integer getNumberOfClusters() {
		return numberOfClusters;
	}

	public void setNumberOfClusters(Integer numberOfClusters) {
		this.numberOfClusters = numberOfClusters;
	}

	public Boolean getPreProcessingTime() {
		return preProcessingTime;
	}

	public void setPreProcessingTime(Boolean preProcessingTime) {
		this.preProcessingTime = preProcessingTime;
	}

	public Boolean getPreProcessingChange() {
		return preProcessingChange;
	}

	public void setPreProcessingChange(Boolean preProcessingChange) {
		this.preProcessingChange = preProcessingChange;
	}

	public int getRowHeight() {
		return rowHeight;
	}

	public void setRowHeight(int rowHeight) {
		this.rowHeight = rowHeight;
	}

	public int getColumnWidth() {
		return columnWidth;
	}

	public void setColumnWidth(int columnWidth) {
		this.columnWidth = columnWidth;
	}
	
	public int getWholeCol() {
		return wholeCol;
	}

	public void setWholeCol(int wholeCol) {
		this.wholeCol = wholeCol;
	}

	public Integer[] getSegmentSize() {
		return segmentSize;
	}

	public void setSegmentSize(Integer[] segmentSize) {
		this.segmentSize = segmentSize;
	}

	public String[] getFinalColumns() {
		return finalColumns;
	}

	public void setFinalColumns(String[] finalColumns) {
		this.finalColumns = finalColumns;
	}

	public String[][] getFinalRows() {
		return finalRows;
	}

	public void setFinalRows(String[][] finalRows) {
		this.finalRows = finalRows;
	}

	public String[] getFinalColumnsZoomArea() {
		return finalColumnsZoomArea;
	}

	public void setFinalColumnsZoomArea(String[] finalColumnsZoomArea) {
		this.finalColumnsZoomArea = finalColumnsZoomArea;
	}

	public String[][] getFinalRowsZoomArea() {
		return finalRowsZoomArea;
	}

	public void setFinalRowsZoomArea(String[][] finalRowsZoomArea) {
		this.finalRowsZoomArea = finalRowsZoomArea;
	}
	
	public void setSelectedRows(ArrayList<Integer> selectedRows) {
		this.selectedRows = selectedRows;
	}

	public ArrayList<Integer> getSelectedRows() {
		return this.selectedRows;
	}

	public JPanel getContentPane() {
		return contentPane;
	}

	public void setContentPane(JPanel contentPane) {
		this.contentPane = contentPane;
	}

	public JPanel getLifeTimePanel() {
		return lifeTimePanel;
	}

	public void setLifeTimePanel(JPanel lifeTimePanel) {
		this.lifeTimePanel = lifeTimePanel;
	}

	public JTabbedPane getTabbedPane() {
		return tabbedPane;
	}

	public void setTabbedPane(JTabbedPane tabbedPane) {
		this.tabbedPane = tabbedPane;
	}

	public MyTableModel getDetailedModel() {
		return detailedModel;
	}

	public void setDetailedModel(MyTableModel detailedModel) {
		this.detailedModel = detailedModel;
	}

	public MyTableModel getGeneralModel() {
		return generalModel;
	}

	public void setGeneralModel(MyTableModel generalModel) {
		this.generalModel = generalModel;
	}

	public MyTableModel getZoomModel() {
		return zoomModel;
	}

	public void setZoomModel(MyTableModel zoomModel) {
		this.zoomModel = zoomModel;
	}

	public JvTable getLifeTimeTable() {
		return LifeTimeTable;
	}

	public void setLifeTimeTable(JvTable lifeTimeTable) {
		LifeTimeTable = lifeTimeTable;
	}

	public JvTable getZoomAreaTable() {
		return zoomAreaTable;
	}

	public void setZoomAreaTable(JvTable zoomAreaTable) {
		this.zoomAreaTable = zoomAreaTable;
	}

	public JScrollPane getTmpScrollPane() {
		return tmpScrollPane;
	}

	public void setTmpScrollPane(JScrollPane tmpScrollPane) {
		this.tmpScrollPane = tmpScrollPane;
	}

	public JScrollPane getTreeScrollPane() {
		return treeScrollPane;
	}

	public void setTreeScrollPane(JScrollPane treeScrollPane) {
		this.treeScrollPane = treeScrollPane;
	}

	public JScrollPane getTmpScrollPaneZoomArea() {
		return tmpScrollPaneZoomArea;
	}

	public void setTmpScrollPaneZoomArea(JScrollPane tmpScrollPaneZoomArea) {
		this.tmpScrollPaneZoomArea = tmpScrollPaneZoomArea;
	}

	

	public String[] getFirstLevelUndoColumnsZoomArea() {
		return firstLevelUndoColumnsZoomArea;
	}

	public void setFirstLevelUndoColumnsZoomArea(String[] firstLevelUndoColumnsZoomArea) {
		this.firstLevelUndoColumnsZoomArea = firstLevelUndoColumnsZoomArea;
	}

	public String[][] getFirstLevelUndoRowsZoomArea() {
		return firstLevelUndoRowsZoomArea;
	}

	public void setFirstLevelUndoRowsZoomArea(String[][] firstLevelUndoRowsZoomArea) {
		this.firstLevelUndoRowsZoomArea = firstLevelUndoRowsZoomArea;
	}

	public Integer[] getSegmentSizeZoomArea() {
		return segmentSizeZoomArea;
	}

	public void setSegmentSizeZoomArea(Integer[] segmentSizeZoomArea) {
		this.segmentSizeZoomArea = segmentSizeZoomArea;
	}

	public Integer[] getSegmentSizeDetailedTable() {
		return segmentSizeDetailedTable;
	}

	public void setSegmentSizeDetailedTable(Integer[] segmentSizeDetailedTable) {
		this.segmentSizeDetailedTable = segmentSizeDetailedTable;
	}

	public ArrayList<String> getSelectedFromTree() {
		return selectedFromTree;
	}

	public void setSelectedFromTree(ArrayList<String> selectedFromTree) {
		this.selectedFromTree = selectedFromTree;
	}

	public JTree getTablesTree() {
		return tablesTree;
	}

	public void setTablesTree(JTree tablesTree) {
		this.tablesTree = tablesTree;
	}

	public JPanel getSideMenu() {
		return sideMenu;
	}

	public void setSideMenu(JPanel sideMenu) {
		this.sideMenu = sideMenu;
	}

	public JPanel getTablesTreePanel() {
		return tablesTreePanel;
	}

	public void setTablesTreePanel(JPanel tablesTreePanel) {
		this.tablesTreePanel = tablesTreePanel;
	}

	public JPanel getDescriptionPanel() {
		return descriptionPanel;
	}

	public void setDescriptionPanel(JPanel descriptionPanel) {
		this.descriptionPanel = descriptionPanel;
	}

	public JLabel getTreeLabel() {
		return treeLabel;
	}

	public void setTreeLabel(JLabel treeLabel) {
		this.treeLabel = treeLabel;
	}

	public JLabel getGeneralTableLabel() {
		return generalTableLabel;
	}

	public void setGeneralTableLabel(JLabel generalTableLabel) {
		this.generalTableLabel = generalTableLabel;
	}

	public JLabel getZoomAreaLabel() {
		return zoomAreaLabel;
	}

	public void setZoomAreaLabel(JLabel zoomAreaLabel) {
		this.zoomAreaLabel = zoomAreaLabel;
	}

	public JLabel getDescriptionLabel() {
		return descriptionLabel;
	}

	public void setDescriptionLabel(JLabel descriptionLabel) {
		this.descriptionLabel = descriptionLabel;
	}

	public JTextArea getDescriptionText() {
		return descriptionText;
	}

	public void setDescriptionText(JTextArea descriptionText) {
		this.descriptionText = descriptionText;
	}

	public JButton getZoomInButton() {
		return zoomInButton;
	}

	public void setZoomInButton(JButton zoomInButton) {
		this.zoomInButton = zoomInButton;
	}

	public JButton getZoomOutButton() {
		return zoomOutButton;
	}

	public void setZoomOutButton(JButton zoomOutButton) {
		this.zoomOutButton = zoomOutButton;
	}

	public JButton getUniformlyDistributedButton() {
		return uniformlyDistributedButton;
	}

	public void setUniformlyDistributedButton(JButton uniformlyDistributedButton) {
		this.uniformlyDistributedButton = uniformlyDistributedButton;
	}

	public JButton getNotUniformlyDistributedButton() {
		return notUniformlyDistributedButton;
	}

	public void setNotUniformlyDistributedButton(JButton notUniformlyDistributedButton) {
		this.notUniformlyDistributedButton = notUniformlyDistributedButton;
	}

	public JButton getShowThisToPopup() {
		return showThisToPopup;
	}

	public void setShowThisToPopup(JButton showThisToPopup) {
		this.showThisToPopup = showThisToPopup;
	}

	public int[] getSelectedRowsFromMouse() {
		return selectedRowsFromMouse;
	}

	public void setSelectedRowsFromMouse(int[] selectedRowsFromMouse) {
		this.selectedRowsFromMouse = selectedRowsFromMouse;
	}

	public int getSelectedColumn() {
		return selectedColumn;
	}

	public void setSelectedColumn(int selectedColumn) {
		this.selectedColumn = selectedColumn;
	}

	public int getSelectedColumnZoomArea() {
		return selectedColumnZoomArea;
	}

	public void setSelectedColumnZoomArea(int selectedColumnZoomArea) {
		this.selectedColumnZoomArea = selectedColumnZoomArea;
	}

	public int getWholeColZoomArea() {
		return wholeColZoomArea;
	}

	public void setWholeColZoomArea(int wholeColZoomArea) {
		this.wholeColZoomArea = wholeColZoomArea;
	}

	public ArrayList<String> getTablesSelected() {
		return tablesSelected;
	}

	public void setTablesSelected(ArrayList<String> tablesSelected) {
		this.tablesSelected = tablesSelected;
	}

	public boolean isShowingPld() {
		return showingPld;
	}

	public void setShowingPld(boolean showingPld) {
		this.showingPld = showingPld;
	}

	public JButton getUndoButton() {
		return undoButton;
	}

	public void setUndoButton(JButton undoButton) {
		this.undoButton = undoButton;
	}

	public JMenu getMnProject() {
		return mnProject;
	}

	public void setMnProject(JMenu mnProject) {
		this.mnProject = mnProject;
	}

	public JMenuItem getMntmInfo() {
		return mntmInfo;
	}

	public void setMntmInfo(JMenuItem mntmInfo) {
		this.mntmInfo = mntmInfo;
	}
	
}
