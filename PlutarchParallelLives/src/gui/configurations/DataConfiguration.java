package gui.configurations;

public class DataConfiguration
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
	private Integer[] segmentSize=new Integer[4];
	private String[] finalColumns=null;
	private String[][] finalRows=null;
	private String[] finalColumnsZoomArea = null; 
	private String[][] finalRowsZoomArea =null;
	private String[] firstLevelUndoColumnsZoomArea=null;
	private String[][] firstLevelUndoRowsZoomArea=null;
	private Integer[] segmentSizeZoomArea=new Integer[4];
	private Integer[] segmentSizeDetailedTable=new Integer[3];
	
	public DataConfiguration(Float timeWeight,
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

	public DataConfiguration(){}
	
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
}
