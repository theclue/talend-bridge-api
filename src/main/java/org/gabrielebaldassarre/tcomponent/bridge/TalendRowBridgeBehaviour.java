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


public class TalendRowBridgeBehaviour implements TalendRowBehaviour{
	
	private TalendRow row = null;
	private Object rowStruct = null;
	
	public TalendRowBridgeBehaviour(){
		
	}
	
	public TalendRowBridgeBehaviour(Object rowStruct){
		setFromStruct(rowStruct);
	}
	
	public void setFromStruct(Object rowStruct){
		this.rowStruct = rowStruct;

	}

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

	public Boolean isValid() {
		return row != null;
	}
	
	public TalendRow getRow(){
		return row;
	}
	
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