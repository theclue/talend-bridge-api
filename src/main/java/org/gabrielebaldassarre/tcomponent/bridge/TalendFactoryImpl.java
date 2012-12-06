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

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * This is a concrete implementation for the whole set of factories used by {@link TalendFlowModel}
 * and should never be instantiated in normal conditions. Factory model methods should be used instead.
 * 
 * @author Gabriele Baldassarre
 *
 */
public class TalendFactoryImpl implements TalendFlowFactory, TalendRowFactory, TalendValueFactory{

	private TalendFlowModelImpl model;

	/**
	 * {@inheritDoc}
	 */
	public TalendFactoryImpl(TalendFlowModelImpl model){
		this.model = model;
	}
	
	/**
	 * {@inheritDoc}
	 */	
	public void setModel(TalendFlowModelImpl model){
		this.model = model;
	}

	/**
	 * {@inheritDoc}
	 */
	public TalendFlow newFlow(String name, Integer maximumSize) {
		TalendFlowImpl table = new TalendFlowImpl(model, name, maximumSize);
		model.addFlow(name, table);
		return table;
		
	}

	/**
	 * {@inheritDoc}
	 */
	public TalendRow newRow(String table) {
		TalendRowImpl row = new TalendRowImpl(model.getFlow(table));
		model.getFlow(table).addRow(row);
		return row;
	}

	/**
	 * {@inheritDoc}
	 */
	public TalendRow newRow(TalendFlow table) {
		return newRow(table.getName());
	}

	/**
	 * {@inheritDoc}
	 */
	public TalendValue newValue(TalendColumn column, Object value) {
		return new TalendValueImpl((TalendColumnImpl) column, value);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public TalendRow newRow(String table, Object values) {
		TalendRow row = newRow(table);
		row.setValues(values);
		return row;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public TalendRow newRow(TalendFlow table, Object values) {
		return newRow(table.getName(), values);
	}

	/**
	 * {@inheritDoc}
	 */
	public TalendFlow newFlow(String name, Class<?> template, Integer maximumSize) {
		TalendFlow table = newFlow(name, maximumSize);
		
		Field[] fields = template.getFields();
		for(Field f : fields){
			if(!Modifier.isStatic(f.getModifiers())){
				String column = null;
				try {
					column = f.getName();			
				} catch (SecurityException e) {
					throw new SecurityException(e);
				} 

				table.addColumn(column, TalendType.buildFrom(f.getType()));
				
			}

		}
		
		return table;
		
	}

}