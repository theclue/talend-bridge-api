/*
	This file is part of Talend Bridge Component API

    Talend Bridge is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Talend Bridge is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Nome-Programma.  If not, see <http://www.gnu.org/licenses/>.
*/
package org.gabrielebaldassarre.tcomponent.bridge;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * This is the concrete implementation of a flow and therefore should not be used in normal circumnstances.
 * You should build instances using {@link TalendFlowFactory} instead.
 * 
 * @author Gabriele Baldassarre
 *
 */
public class TalendFlowImpl implements TalendFlow, TalendBehaviourableFlow {
	
	protected TalendFlowModelImpl model;
	protected Map<String, TalendColumnImpl> columns;
	protected Map<TalendColumnImpl, TalendColumnImpl> columnImpls;
	protected List<TalendColumnImpl> columnsList;
	protected final String name;
	protected List<TalendRowImpl> rowList;
	protected final Integer maximumSize; 
	
	/**
	 * {@inheritDoc}
	 */
	public TalendFlowImpl(TalendFlowModelImpl model, final String name, final Integer maximumSize){
				
		this.model = model;
		this.name = name;
		this.columns = new ConcurrentHashMap<String, TalendColumnImpl>();
		this.columnImpls = new ConcurrentHashMap<TalendColumnImpl, TalendColumnImpl>();
		this.columnsList = TalendListFactory.getInstance(TalendColumnImpl.class).newTalendList(new ArrayList<TalendColumnImpl>());
		this.maximumSize = maximumSize;
		if(maximumSize == null){
			this.rowList = TalendListFactory.getInstance(TalendRowImpl.class).newTalendList(new LinkedList<TalendRowImpl>());
		} else {
			this.rowList = TalendListFactory.getInstance(TalendRowImpl.class).newTalendList(new LinkedList<TalendRowImpl>(), maximumSize);
		}
		

	}
	
	/**
	 * {@inheritDoc}
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean hasColumns() {
		return (columns.size() > 0 ? true : false);
	}

	/**
	 * {@inheritDoc}
	 */
	public TalendFlow addColumn(String name, TalendType type) {
		addColumn(name, type, null, null);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	public TalendFlow addColumn(String name, TalendType type, Object defaultValue, String comment) throws IllegalArgumentException {
		
		ResourceBundle rb = ResourceBundle.getBundle("TalendBridge", Locale.getDefault());
		if (name == null || name.isEmpty() || hasColumn(name)) {
            throw new IllegalArgumentException(String.format(Locale.getDefault(), rb.getString("exception.invalidColumnName"), name));
        }
		
		if (hasColumn(name)) {
            throw new IllegalArgumentException(String.format(Locale.getDefault(), rb.getString("exception.columnAlreadyUsed"), name, this.getName()));
        }

		if (defaultValue != null) {
            if (defaultValue.getClass() != type.getType()) {
                if (defaultValue.getClass() == String.class) {
                    defaultValue = type.parse((String) defaultValue);
                } else {
                	throw new IllegalArgumentException(String.format(Locale.getDefault(), rb.getString("exception.invalidColumnDefault"), defaultValue));
                }
            }
		}
 
        TalendColumnImpl col = new TalendColumnImpl(this, columns.size(), name, type, defaultValue, comment);
        
        columns.put(name, col);
        columnImpls.put(col, col);
        columnsList.add(col);
        
        return this;
 	}

	/**
	 * {@inheritDoc}
	 */
	public boolean hasColumn(String column) {
		return (columns.containsKey(column) || columns.containsKey(column.toLowerCase()));
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean hasColumn(TalendColumn column) {
		return (columns.containsKey((TalendColumnImpl) column));
	}

	/**
	 * {@inheritDoc}
	 */
	public TalendColumnImpl[] getColumns() {
		return columns.values().toArray(new TalendColumnImpl[columns.size()]);
	}

	/**
	 * {@inheritDoc}
	 */
	public synchronized void removeColumn(TalendColumn column) {
		int index = columnsList.indexOf(column);
        if (index == -1) {
            return;
        }
 
        TalendColumnImpl c;
        for (index = index + 1; index < columnsList.size(); index++) {
            c = columnsList.get(index);
            c.index--;
        }

        for(TalendRowImpl row : rowList){
        	row.removeColumn((TalendColumnImpl) column);
        }
        
        columnsList.remove((TalendColumnImpl) column);
        columns.remove(column.getName());
        columnImpls.remove(column);
        
	}

	/**
	 * {@inheritDoc}
	 */
	public TalendColumnImpl getColumn(String name) {
		if(!columns.containsKey(name)) return null;
		return columns.get(name);
	}

	/**
	 * {@inheritDoc}
	 */
	public TalendColumnImpl getColumn(TalendColumn column) {
		if(!columnImpls.containsKey(column)) return null;
		return columnImpls.get(column);
	}

	/**
	 * {@inheritDoc}
	 */
	public TalendColumnImpl getColumn(int index) {
		if(index > columnsList.size()) return null;
		return columnImpls.get(columnsList.get(index));
	}

	/**
	 * {@inheritDoc}
	 */
	public int countColumns(){
		return columns.size();
	}

	/**
	 * {@inheritDoc}
	 */
	public TalendFactoryImpl getFactory(){
		return model.getFactory();
	}

	/**
	 * {@inheritDoc}
	 */
	public void addRow(TalendRowImpl row){
		rowList.add(row);
	}

	/**
	 * {@inheritDoc}
	 */
	public int countRows() {
		return rowList.size();
	}

	/**
	 * {@inheritDoc}
	 */
	public TalendFlow addColumn(String name, TalendType type, Object defaultValue) {
		addColumn(name, type, defaultValue, null);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */	
	public TalendRow[] getRows() {
		return rowList.toArray(new TalendRow[rowList.size()]);
	}

	/**
	 * {@inheritDoc}
	 * @return 
	 */	
	public  TalendFlow addBehaviour(TalendFlowBehaviour b) {
		b.visit(this);
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 */	
	public TalendValue[] sliceRows(TalendColumn column) {
		ResourceBundle rb = ResourceBundle.getBundle("TalendBridge", Locale.getDefault());
		if (column == null || hasColumn(column)) {
            throw new IllegalArgumentException(String.format(Locale.getDefault(), rb.getString("exception.invalidColumnName"), name));
        }
		List<TalendValue> slicedValues = new ArrayList<TalendValue>(countRows());
		for(TalendRowImpl row : rowList){
			slicedValues.add(row.getTalendValue(column.getName()));
		}
		
		return slicedValues.toArray(new TalendValue[countRows()]);
	}

	/**
	 * {@inheritDoc}
	 */	
	public TalendRow getRow(int rownum) throws IndexOutOfBoundsException {
		ResourceBundle rb = ResourceBundle.getBundle("TalendBridge", Locale.getDefault());
		if(rowList.size() < rownum+1) throw new IndexOutOfBoundsException(String.format(Locale.getDefault(), rb.getString("exception.invalidRowNum"), name, rownum));
		return rowList.get(rownum);
	}

}
