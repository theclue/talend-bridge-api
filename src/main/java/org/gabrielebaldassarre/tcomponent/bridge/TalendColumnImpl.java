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

import java.io.Serializable;
import java.util.Arrays;

/**
 * This is a memory-based concrete implementation of a {@link TalendColumn} and should never be used
 * in normal conditions. A TalendColumn, built using {@link TalendFlow} methods should be build instead.
 * 
 * @author Gabriele Baldassarre
 *
 */
public final class TalendColumnImpl implements Serializable, TalendColumn{

	private static final long serialVersionUID = 8383916163449740885L;
	private TalendFlowImpl table;
	private String name;
	private TalendValueImpl defaultValue;
	int index;
	private TalendType type;

	public TalendColumnImpl(TalendFlowImpl table, int index, String name, TalendType type, Object defaultValue){
		this.table = table;
		this.name = name;
		this.index = index;
		this.type = type;
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
	public boolean isKey(){
		if(table.getKeyColumns() == null) return false;
		return Arrays.asList(table.getKeyColumns()).contains(this);
	}

	/**
	 * {@inheritDoc}
	 */	
	public String toString(){
		return "{TalendColumn flow=" + table.getName() + ", index=" + index + ", name=" + name + ", type=" + type.getType().getSimpleName() + "}";

	}

	/**
	 * {@inheritDoc}
	 */
	public int hashCode(){
		int hash = (index == 0 ? 1 : index);
		hash = hash * 11 + table.getName().hashCode();
		hash = hash * 7 + name.hashCode();
		hash = hash * 3 + type.hashCode();
		hash = hash * 13 + (defaultValue == null ? 0 : defaultValue.hashCode());
		return hash;

	}

	/**
	 * {@inheritDoc}
	 */	
	public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof TalendColumnImpl)) {
            return false;
        }
        TalendColumnImpl other = (TalendColumnImpl) obj;
        return this.getType().equals(other.getType()) && this.getName().equals(other.getName()) && this.getFlow().equals(other.getFlow());
    }
}
