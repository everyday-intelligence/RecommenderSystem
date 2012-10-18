package com.recsys.matrix;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IndexedSimpleMatrix extends SimpleMatrix {

	private Map<Long,Integer> rowLabelMatrixRowMapping;
	private Map<Long,Integer> colLabelMatrixColMapping;
	private List<Long> rowsLabels = new ArrayList<Long>();
	private List<Long> colsLabels = new ArrayList<Long>();
	
	public IndexedSimpleMatrix(List<Long>rowsLabels, List<Long>colsLabels) {
		super(rowsLabels.size(), colsLabels.size());

		rowLabelMatrixRowMapping = new HashMap<Long,Integer>(rowsLabels.size());
		for(int i=0;i<rowsLabels.size();i++){
			rowLabelMatrixRowMapping.put(rowsLabels.get(i), i);
			this.rowsLabels.add(rowsLabels.get(i));
		}
		colLabelMatrixColMapping = new HashMap<Long,Integer>(colsLabels.size());
		for(int i=0;i<colsLabels.size();i++){
			colLabelMatrixColMapping.put(colsLabels.get(i), i);
			this.colsLabels.add(colsLabels.get(i));
		}
		System.out.println("rows = "+rowLabelMatrixRowMapping.size()+" = "+rowsLabels.size());
		System.out.println("cols = "+colLabelMatrixColMapping.size()+" = "+colsLabels.size());
	}

	public Double get(long rowLabel, long colLabel) {
		Double rij = super.get(fromRowLabelToMatrixRow(rowLabel), fromColLabelToMatrixCol(colLabel));
		if(rij == null){
			System.out.println("rating "+rowLabel+" , "+colLabel+" dans la case "+fromRowLabelToMatrixRow(rowLabel)+" , "+fromColLabelToMatrixCol(colLabel)+" is null");
		}
		return rij;
	}

	public void set(long rowLabel, long colLabel, Double vals) {
		super.set(fromRowLabelToMatrixRow(rowLabel), fromColLabelToMatrixCol(colLabel), vals);
	}
	
	public AbstractVector getRow(long rowLabel) {
		return super.getRow(rowLabelMatrixRowMapping.get(rowLabel));
	}

	public AbstractVector getColumn(long colLabel) {
		return super.getColumn(colLabelMatrixColMapping.get(colLabel));
	}
	
	
	private int fromRowLabelToMatrixRow(long idUser){
		return rowLabelMatrixRowMapping.get(idUser);		
	}
	private long fromMatrixRowToRowLabel(int userRowIndex){
		return rowsLabels.get((int) userRowIndex);		
	}
	private int fromColLabelToMatrixCol(long idItem){
		return colLabelMatrixColMapping.get(idItem);		
	}
	private long fromMatrixColToColLabel(int itemColIndex){
		return colsLabels.get((int) itemColIndex);		
	}
}