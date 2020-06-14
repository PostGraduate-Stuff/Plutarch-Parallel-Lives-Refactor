package gui.configurations;

import java.awt.Color;

import java.util.ArrayList;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.GroupLayout.Alignment;
import javax.swing.border.EmptyBorder;

import gui.tableElements.commons.JvTable;

public class GuiConfiguration extends JFrame
{
	private static final long serialVersionUID = 1L;
	
	private int rowHeight=1;
	private int columnWidth=1;
	private int wholeCol=-1;
	private JPanel contentPane;
	private JPanel lifeTimePanel = new JPanel();
	private JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
	private JScrollPane tmpScrollPane =new JScrollPane();
	private JScrollPane treeScrollPane= new JScrollPane();
	private JScrollPane tmpScrollPaneZoomArea =new JScrollPane();
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
	private JButton undoButton;
	private JMenu mnProject;
	private JMenuItem mntmInfo;
	
	private boolean showingPld=false;
	
	private ArrayList<Integer> selectedRows=new ArrayList<Integer>();
	private ArrayList<String> tablesSelected = new ArrayList<String>();
	private ArrayList<String> selectedFromTree=new ArrayList<String>();
	private int[] selectedRowsFromMouse;
	private int selectedColumn=-1;
	private int selectedColumnZoomArea=-1;
	private int wholeColZoomArea=-1;
	
	public void setSideMenu(){
		setSideMenuConfig();
		setTablesTreePanelConfig();
		setDescriptionPanelConfig();
		setDescriptionPanelConfig();
		addToSideMenu();
	}
	
	private void setSideMenuConfig(){
		sideMenu.setBounds(0, 0, 280, 600);
		sideMenu.setBackground(Color.DARK_GRAY);
		
		GroupLayout gl_sideMenu = new GroupLayout(sideMenu);
		gl_sideMenu.setHorizontalGroup(gl_sideMenu.createParallelGroup(Alignment.LEADING));
		gl_sideMenu.setVerticalGroup(gl_sideMenu.createParallelGroup(Alignment.LEADING));
		sideMenu.setLayout(gl_sideMenu);
	}
	
	private void setTablesTreePanelConfig(){
		tablesTreePanel.setBounds(10, 400, 260, 180);
		tablesTreePanel.setBackground(Color.LIGHT_GRAY);
		
		GroupLayout gl_tablesTreePanel = new GroupLayout(tablesTreePanel);
		gl_tablesTreePanel.setHorizontalGroup(gl_tablesTreePanel.createParallelGroup(Alignment.LEADING));
		gl_tablesTreePanel.setVerticalGroup(gl_tablesTreePanel.createParallelGroup(Alignment.LEADING));
		tablesTreePanel.setLayout(gl_tablesTreePanel);
		
		treeLabel = new JLabel();
		treeLabel.setBounds(10, 370, 260, 40);
		treeLabel.setForeground(Color.WHITE);
		treeLabel.setText("Tree");
	}

	private void setDescriptionPanelConfig(){
		descriptionPanel.setBounds(10, 190, 260, 180);
		descriptionPanel.setBackground(Color.LIGHT_GRAY);
		
		GroupLayout gl_descriptionPanel = new GroupLayout(descriptionPanel);
		gl_descriptionPanel.setHorizontalGroup(gl_descriptionPanel.createParallelGroup(Alignment.LEADING));
		gl_descriptionPanel.setVerticalGroup(gl_descriptionPanel.createParallelGroup(Alignment.LEADING));
		
		descriptionPanel.setLayout(gl_descriptionPanel);
		
		descriptionText = new JTextArea();
		descriptionText.setBounds(5, 5, 250, 170);
		descriptionText.setForeground(Color.BLACK);
		descriptionText.setText("");
		descriptionText.setBackground(Color.LIGHT_GRAY);
		
		descriptionPanel.add(descriptionText);
		
		descriptionLabel = new JLabel();
		descriptionLabel.setBounds(10, 160, 260, 40);
		descriptionLabel.setForeground(Color.WHITE);
		descriptionLabel.setText("Description");
	}

	private void addToSideMenu(){
		sideMenu.add(treeLabel);
		sideMenu.add(tablesTreePanel);
		
		sideMenu.add(descriptionLabel);
		sideMenu.add(descriptionPanel);

		lifeTimePanel.add(sideMenu);
	}
	

	
	public void addLifeTimePanel(){
		
		lifeTimePanel.add(zoomInButton);
		lifeTimePanel.add(undoButton);
		lifeTimePanel.add(zoomOutButton);
		lifeTimePanel.add(uniformlyDistributedButton);
		lifeTimePanel.add(notUniformlyDistributedButton);
		lifeTimePanel.add(showThisToPopup);
	
		lifeTimePanel.add(zoomAreaLabel);
		
		lifeTimePanel.add(generalTableLabel);
		
		
	}
	
	public void addLabels(){
		generalTableLabel = new JLabel("Parallel Lives Diagram");
		generalTableLabel.setBounds(300, 0, 150, 30);
		generalTableLabel.setForeground(Color.BLACK);
		
		zoomAreaLabel = new JLabel();
		zoomAreaLabel.setText("<HTML>Z<br>o<br>o<br>m<br><br>A<br>r<br>e<br>a</HTML>");
		zoomAreaLabel.setBounds(1255, 325, 15, 300);
		zoomAreaLabel.setForeground(Color.BLACK);
		
		
	}
	
	public void addContentPane() {
		contentPane = new JPanel();
		
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addComponent(tabbedPane, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 1474, Short.MAX_VALUE));
		gl_contentPane.setVerticalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addComponent(tabbedPane, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 771, Short.MAX_VALUE));
		contentPane.setLayout(gl_contentPane);
		
	}
	
	public void addTabbedPane(){
		tabbedPane.addTab("LifeTime Table", null, lifeTimePanel, null);
		
		GroupLayout gl_lifeTimePanel = new GroupLayout(lifeTimePanel);
		gl_lifeTimePanel.setHorizontalGroup(gl_lifeTimePanel.createParallelGroup(Alignment.LEADING)
				.addGap(0, 1469, Short.MAX_VALUE));
		gl_lifeTimePanel.setVerticalGroup(gl_lifeTimePanel.createParallelGroup(Alignment.LEADING)
				.addGap(0, 743, Short.MAX_VALUE)
		);
		lifeTimePanel.setLayout(gl_lifeTimePanel);
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

	public void setSelectedRows(ArrayList<Integer> selectedRows) {
		this.selectedRows = selectedRows;
	}

	public ArrayList<Integer> getSelectedRows() {
		return this.selectedRows;
	}

	public JPanel getContentPane() {
		return contentPane;
	}

	public void setContentPaneConfig(JPanel contentPane) {
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

	public void setDistributedButton() {
		tabbedPane.setSelectedIndex(0);
		uniformlyDistributedButton.setVisible(true);
		notUniformlyDistributedButton.setVisible(true);
		
	}
	
	public void setPLDVisibility(boolean action)
	{
		showingPld = action;
		zoomInButton.setVisible(action);
		zoomOutButton.setVisible(action);
		showThisToPopup.setVisible(action); 
	}

	public void setScrollPaneSettings(JvTable table) {
		tmpScrollPaneZoomArea.setViewportView(table);
		tmpScrollPaneZoomArea.setAlignmentX(0);
		tmpScrollPaneZoomArea.setAlignmentY(0);
		tmpScrollPaneZoomArea.setBounds(300,300,950,250);
		tmpScrollPaneZoomArea.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		tmpScrollPaneZoomArea.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		lifeTimePanel.add(tmpScrollPaneZoomArea);		
	}

	public void setTreeScrollSettings(String text) {
		treeScrollPane.setViewportView(tablesTree);
		treeScrollPane.setBounds(5, 5, 250, 170);
		treeScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		treeScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		tablesTreePanel.add(treeScrollPane);
		 
		treeLabel.setText(text);

		sideMenu.revalidate();
		sideMenu.repaint();				
	}
}
