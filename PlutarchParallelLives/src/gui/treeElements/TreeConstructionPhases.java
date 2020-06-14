package gui.treeElements;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import phaseAnalyzer.commons.Phase;
import data.dataKeeper.GlobalDataKeeper;
import data.dataPPL.pplSQLSchema.PPLSchema;
import data.dataPPL.pplTransition.PPLTransition;
import data.dataPPL.pplTransition.TableChange;

public class TreeConstructionPhases implements TreeConstruction {
	private DefaultMutableTreeNode top = null;
	private TreeMap<String, PPLSchema> schemas = null;
	private GlobalDataKeeper dataKeeper=null;
	public TreeConstructionPhases(GlobalDataKeeper dataKeeper) {
		this.dataKeeper=dataKeeper;
	}
	
	@Override
	public JTree constructTree() {
		this.top=new DefaultMutableTreeNode("Phases");
		this.schemas=new TreeMap<String, PPLSchema>();
		ArrayList<Phase> phases=dataKeeper.getPhaseCollectors().get(0).getPhases();
		constructTreeNodes(phases);
		JTree treeToConstruct = new JTree(top);
		return treeToConstruct;
	}
	
	private void constructTreeNodes(ArrayList<Phase> phases){
		for(int i=0; i<phases.size(); i++){
			DefaultMutableTreeNode phaseTreeNode=new DefaultMutableTreeNode("Phase "+i);
			top.add(phaseTreeNode);
			TreeMap<Integer,PPLTransition> transitions=phases.get(i).getPhasePPLTransitions();
			for(Map.Entry<Integer, PPLTransition> transition:transitions.entrySet()){
				DefaultMutableTreeNode transitionTreeNode=new DefaultMutableTreeNode(transition.getKey());
				ArrayList<TableChange> tableChanges=transition.getValue().getTableChanges();
				for(int j=0; j<tableChanges.size(); j++){
					DefaultMutableTreeNode affectedTableTreeNode=new DefaultMutableTreeNode(tableChanges.get(j).getAffectedTableName());
					transitionTreeNode.add(affectedTableTreeNode);
				}
				phaseTreeNode.add(transitionTreeNode);
				schemas.put(transition.getValue().getOldVersionName(),dataKeeper.getAllPPLSchemas().get(transition.getValue().getOldVersionName()));
				schemas.put(transition.getValue().getNewVersionName(),dataKeeper.getAllPPLSchemas().get(transition.getValue().getNewVersionName()));
			}
		}
	}
}
