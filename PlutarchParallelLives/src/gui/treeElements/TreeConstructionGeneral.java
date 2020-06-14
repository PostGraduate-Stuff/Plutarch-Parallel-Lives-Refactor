package gui.treeElements;

import java.util.Map;
import java.util.TreeMap;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import data.dataKeeper.GlobalDataKeeper;
import data.dataPPL.pplSQLSchema.PPLSchema;
import data.dataPPL.pplSQLSchema.PPLTable;

public class TreeConstructionGeneral {

	private TreeMap<String,PPLSchema> schemas=null;
	
	public TreeConstructionGeneral(TreeMap<String,PPLSchema> allPPLSchemas){
		this.schemas=allPPLSchemas;
	}
	
	public JTree constructTree(){
		DefaultMutableTreeNode top=new DefaultMutableTreeNode("Versions");
		for (Map.Entry<String,PPLSchema> pplSchema : schemas.entrySet()) {
			DefaultMutableTreeNode schemaTreeNode=new DefaultMutableTreeNode(pplSchema.getKey());
		    top.add(schemaTreeNode);
		    TreeMap<String, PPLTable> tables=pplSchema.getValue().getTables();
			for (Map.Entry<String,PPLTable> pplTable : tables.entrySet()) {
				DefaultMutableTreeNode tableTreeNode=new DefaultMutableTreeNode(pplTable.getKey());
				schemaTreeNode.add(tableTreeNode);
			}
		}
		JTree treeToConstruct = new JTree(top);
		return treeToConstruct;
	}
}
