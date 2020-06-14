package gui.treeElements;

import java.util.ArrayList;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import tableClustering.clusterExtractor.commons.Cluster;
import data.dataKeeper.GlobalDataKeeper;

public class TreeConstructionPhasesWithClusters implements TreeConstruction {
	
	private ArrayList<Cluster> clusters = null;

	public TreeConstructionPhasesWithClusters(ArrayList<Cluster> clusters) {
		this.clusters=clusters;
	}
	
	@Override
	public JTree constructTree() {
		DefaultMutableTreeNode top=new DefaultMutableTreeNode("Clusters");		
		for(int i=0; i<clusters.size(); i++){
			DefaultMutableTreeNode clusterTreeNode=new DefaultMutableTreeNode("Cluster "+i);
			top.add(clusterTreeNode);
			ArrayList<String> tables=clusters.get(i).getNamesOfTables();
			for(String tableName:tables){
				DefaultMutableTreeNode tableNameTreeNode=new DefaultMutableTreeNode(tableName);
				clusterTreeNode.add(tableNameTreeNode);
			}
		}
		JTree treeToConstruct = new JTree(top);
		return treeToConstruct;
	}
}
