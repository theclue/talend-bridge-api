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
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * This simple visitor acts as a <em>bridge</em> between this framework's data model rows and standard Talend
 * Open Studio vectors (made in the form of structs with public attribute rapresenting column values ie row1Struct, out1Struct....).<br />
 * In other words, it provide a compact way to:
 * <ul>
 * <li>Set values from an incoming TOS struct to a row of a given flow</li>
 * <li>Set values from a row of a given flow to a output TOS struct, to be used for ex. in a outgoing component connection</li>
 * </ul>
 * Please note that, althought quite handy and very concise, for the very primitive nature of TOS Struct, this bridge is quite slow.
 * So, its use is not reccomended for huge amount of data or in performance-critical tasks.<br />
 * 
 * Examples of use:<br>
 * <pre>
 * {@code
 * // 1. From TOS Struct to TalendRow  
 * // Assuming row1Struct a valid Talend Open Studio vector that carries data
 * 
 * TalendRowBridgeBehaviour bridge = new TalendRowBridgeBehaviour();
 * bridge.setFromStruct(row1Struct);
 * 
 * // row is pre-initialized with values from bridges - only matching values are added
 * TalendRow row = rowFactory.newRow(tabella, bridge);
 * 
 * // 2. From TalendRow to TOS Struct
 * // Assuming row1Struct is an empty instance of a valid Talend Open Studio vector that carries data
 * // Assuming row3 is a valid TalendRow
 * 
 * TalendRowBridgeBehaviour bridge2 = new TalendRowBridgeBehaviour();
 * 
 * // Visit the tow
 * bridge2.visit(row3);
 * 
 * row1Struct vector = null;
 * 
 * // Feed the TOS vector
 * vector = (row1Struct) bridge.createStruct(row1Struct.class);
 * }
 * </pre>
 * 
 * @author Gabriele Baldassarre
 *
 */
public class TalendRowBridgeBehaviour implements TalendRowBehaviour{

	private TalendRow row = null;
	private Object rowStruct = null;

	/**
	 * Build an empty instance of the visitor.
	 * 
	 */
	public TalendRowBridgeBehaviour(){

	}

	/**
	 * Build a instance of the visitor and pre-initialize data from the given TOS vector
	 * 
	 * @param rowStruct the Talend Open Studio vector to retrieve values from
	 */
	public TalendRowBridgeBehaviour(Object rowStruct){
		setFromStruct(rowStruct);
	}

	/**
	 * Pre-initialize data from the given TOS vector
	 * 
	 * @param rowStruct the Talend Open Studio vector to retrieve values from
	 */
	public void setFromStruct(Object rowStruct){
		this.rowStruct = rowStruct;

	}

	
	/**
	 * Visit a row.<br />
	 * If the visitor was pre-initialized with values from TOS vector, set matching column values of visiting row with these values.
	 * 
	 * @param row the row to visit
	 * @throws IllegalArgumentException if a column type mismatch occurred between TOS vector and the visiting row
	 */
	public void visit(TalendRow row) {
		ResourceBundle rb = ResourceBundle.getBundle("TalendBridge", Locale.getDefault());
		this.row = row;

		if(rowStruct == null) return;

		Class<? extends Object> struct = rowStruct.getClass();
		Field[] fields = struct.getFields();
		for(Field f : fields){
			if(!Modifier.isStatic(f.getModifiers())){
				Object value = null;
				String column = null;
				try {
					value = f.get(rowStruct);
					column = f.getName();
				} catch (IllegalArgumentException e) {
					throw new IllegalArgumentException(e);
				} catch (SecurityException e) {
					throw new SecurityException(e);
				} catch (IllegalAccessException e) {}

				if(row.getTable().hasColumn(column)){
					TalendType columnClass = row.getTable().getColumn(column).getType();
					if(columnClass.equals(TalendType.buildFrom(f.getType()))){
						row.setValue(column, value);
					} else throw new IllegalArgumentException(String.format(Locale.getDefault(), rb.getString("exception.columnOfWrongType"), column, row.getTable().getName(), columnClass.getType().getSimpleName(), f.getType().getSimpleName()));

				}
			}

		}

	}

	/**
	 * Check if the visitor is valid, ie. if it visited at least on row since it's creation
	 * 
	 * @return true if the visitor is valid, false otherwise
	 */
	public Boolean isValid() {
		return row != null;
	}

	/**
	 * Get a reference to the last visited row
	 * 
	 * @return a reference to last visited row or null if no row was still visited
	 */
	public TalendRow getRow(){
		return row;
	}

	/**
	 * Feed a TOS vector with data coming from last visited row
	 * 
	 * @param rowClass a static reference to class of which target vector is instance of
	 * @return a newly create instance of class rowClass, pre-initialized with values coming from the last visited TalendRow
	 * @throws InstantiationException if it was not possible to build an instance of rowClass by reflection
	 * @throws IllegalAccessException if visiting row refers to a column corresponding to a private field in target vector
	 */
	public Object createStruct(Class<? extends Object> rowClass) throws InstantiationException, IllegalAccessException{
		ResourceBundle rb = ResourceBundle.getBundle("TalendBridge", Locale.getDefault());

		if(!isValid()){
			throw new IllegalArgumentException(String.format(Locale.getDefault(), rb.getString("exception.rowNotVisited")));
		}
		if(rowClass == null) return null;

		Field[] fields = rowClass.getFields();

		Object newStruct = rowClass.newInstance();

		for(Field f : fields){
			if(!Modifier.isStatic(f.getModifiers())){
				String column = null;

				column = f.getName();
				if(row.getTable().hasColumn(column)){
					TalendType columnClass = row.getTable().getColumn(column).getType();
					if(columnClass.equals(TalendType.buildFrom(f.getType()))){
						f.set(newStruct, row.getValue(column));
					} else throw new IllegalArgumentException(String.format(Locale.getDefault(), rb.getString("exception.columnOfWrongType"), column, row.getTable().getName(), columnClass.getType().getSimpleName(), f.getType().getSimpleName()));

				}

			}

		}

		return newStruct;
	}

}