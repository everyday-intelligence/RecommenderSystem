package com.recsys.matrix;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IndexedSimpleMatrix extends AbstractMatrix implements Serializable{
	protected Double[][] matrix;   // rows-by-columns array
	private Map<Long,Integer> rowLabelMatrixRowMapping;
	private Map<Long,Integer> colLabelMatrixColMapping;
	private List<Long> rowsLabels = new ArrayList<Long>();
	private List<Long> colsLabels = new ArrayList<Long>();
	
	public IndexedSimpleMatrix(List<Long>rowsLabels, List<Long>colsLabels) {
		super(rowsLabels.size(), colsLabels.size());
		matrix = new Double[this.rowsNumber][this.columnsNumber];
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

	public Double get(long rowLabel, long colLabel) {
		Double rij = get(fromRowLabelToMatrixRow(rowLabel), fromColLabelToMatrixCol(colLabel));
		if(rij == null){
			System.out.println("rating "+rowLabel+" , "+colLabel+" dans la case "+fromRowLabelToMatrixRow(rowLabel)+" , "+fromColLabelToMatrixCol(colLabel)+" is null");
		}
		return rij;
	}


    // create M-by-N matrix of 0's
    public IndexedSimpleMatrix(int rows,int columns) {
        super(rows,columns);
        matrix = new Double[rows][columns];
        for(int i=0;i<getRowsNumber();i++){
			for(int j=0;j<getColumnsNumber();j++){
				set(i,j,0d);
			}
		}
    }
    
    
    
    
    public Double get(int row, int col){
    	if(matrix[row][col]==null){System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!"+row+" , "+col);}
    	if((row<0) || (row>=this.rowsNumber) || (col<0) || (col>=this.columnsNumber)){
			throw(new IndexOutOfBoundsException("coordonn�es hors de la matrice"));
		}else{
    		return matrix[row][col];
		}
    }
    
    // Insert a value in the matrix
    public void set(int row,int col,Double vals){
    	if((row<0) || (row>=this.rowsNumber) || (col<0) || (col>=this.columnsNumber)){
			throw(new IndexOutOfBoundsException("coordonn�es hors de la matrice"));
		}else{
    		matrix[row][col]=vals;
		}
    }
    
    
	@Override
	public AbstractVector getRow(int rowNumber) {
		return new SimpleVector(matrix[rowNumber]);
	}

	@Override
	public AbstractVector getColumn(int colNumber) {
		Double[] v = new Double[this.rowsNumber];
		for(int i=0;i<this.rowsNumber;i++){
			v[i]=matrix[i][colNumber];
		}
		return new SimpleVector(v);
	}

	
	public void set(long rowLabel, long colLabel, Double vals) {
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
