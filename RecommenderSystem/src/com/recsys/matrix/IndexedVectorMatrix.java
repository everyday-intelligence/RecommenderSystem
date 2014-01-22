package com.recsys.matrix;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IndexedVectorMatrix extends AbstractMatrix implements Serializable{
	protected VectorMatrix matrix;   // rows-by-columns array
	private Map<Long,Integer> rowLabelMatrixRowMapping;
	private Map<Long,Integer> colLabelMatrixColMapping;
	private List<Long> rowsLabels = new ArrayList<Long>();
	private List<Long> colsLabels = new ArrayList<Long>();
	
	public IndexedVectorMatrix(List<Long>rowsLabels, List<Long>colsLabels) {
		super(rowsLabels.size(), colsLabels.size());
		matrix = new VectorMatrix(rowsLabels.size(), colsLabels.size());
        for(int i=0;i<getRowsNumber();i++){
			for(int j=0;j<getColumnsNumber();j++){
				set(i,j,0d);
			}
		}
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
	}

	public double get(long rowLabel, long colLabel) {
		return get(fromRowLabelToMatrixRow(rowLabel), fromColLabelToMatrixCol(colLabel));
		
	}


    // create M-by-N matrix of 0's
    public IndexedVectorMatrix(int rows,int columns) {
        super(rows,columns);
        matrix = new VectorMatrix(rows, columns);
        for(int i=0;i<getRowsNumber();i++){
			for(int j=0;j<getColumnsNumber();j++){
				set(i,j,0d);
			}
		}
    }
    
    
    
    
    public double get(int row, int col){
    	if((row<0) || (row>=this.rowsNumber) || (col<0) || (col>=this.columnsNumber)){
			throw(new IndexOutOfBoundsException("coordonn�es hors de la matrice"));
		}else{
    		return matrix.get(row, col);
		}
    }
    
    // Insert a value in the matrix
    public void set(int row,int col,double vals){
    	if((row<0) || (row>=this.rowsNumber) || (col<0) || (col>=this.columnsNumber)){
			throw(new IndexOutOfBoundsException("coordonn�es hors de la matrice"));
		}else{
    		matrix.set(row, col, vals);
		}
    }
    
    
	@Override
	public AbstractVector getRow(int rowNumber) {
		return matrix.getRow(rowNumber);
	}

	@Override
	public AbstractVector getColumn(int colNumber) {
		return matrix.getColumn(colNumber);
	}

	
	public void set(long rowLabel, long colLabel, double vals) {
		set(fromRowLabelToMatrixRow(rowLabel), fromColLabelToMatrixCol(colLabel), vals);
	}
	
	public AbstractVector getRow(long rowLabel) {
		return getRow(rowLabelMatrixRowMapping.get(rowLabel));
	}

	public AbstractVector getColumn(long colLabel) {
		return getColumn(colLabelMatrixColMapping.get(colLabel));
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
