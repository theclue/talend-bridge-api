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

/**
 * This is a memory-based concrete implementation of a {@link TalendColumn} and should never be used
 * in normal conditions. A TalendColumn, built using {@link TalendFlow} methods should be build instead.
 * 
 * @author Gabriele Baldassarre
 *
 */
public class TalendColumnImpl implements TalendColumn{

	private TalendFlowImpl table;
	private String name;
	private String comment;
	private TalendValueImpl defaultValue;
	int index;
	private TalendType type;
	
	public TalendColumnImpl(TalendFlowImpl table, int index, String name, TalendType type, Object defaultValue, String comment){
		this.table = table;
		this.name = name;
		this.index = index;
		this.type = type;
		this.comment = comment;
		if(defaultValue != null){
			this.defaultValue = new TalendValueImpl(this, defaultValue);
		}
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Object getDefaultValue() {
		return(defaultValue != null ? defaultValue.getValue() : null);
	}

	/**
	 * {@inheritDoc}
	 */
	public TalendType getType() {
		return type;
	}

	/**
	 * {@inheritDoc}
	 */
	public int getIndex() {
		return index;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public TalendFlow getFlow(){
		return table;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getComment() {
		return comment;
	}
}
