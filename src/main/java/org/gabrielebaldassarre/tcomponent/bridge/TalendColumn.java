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
 * This interface rapresents a column of a given type which belongs to a {@see TalendFlow}.
 * Each column in a table has unique name and index and can have a default value of the same type
 * of the column and a literal column.
 * 
 * @author Gabriele Baldassarre
 *
 */
public interface TalendColumn {

	/**
	 * Get the name of current column
	 * 
	 * @return the name of the column
	 */
	public String getName();
	
	/**
	 * Get the default value of the current column, if any
	 * 
	 * @return the default value or null if no default value was assigned when the column was built
	 */
	public Object getDefaultValue();
	
	/**
	 * Get the type of the current column
	 * 
	 * @return type of the column
	 */
	public TalendType getType();
	
	/**
	 * Get the numerical index of the column, as the position inside the TalendFlow which the column
	 * belongs. Indexes start from zero.
	 * 
	 * @return
	 */
	public int getIndex();
	
	/**
	 * Retrieve the flow which the column belongs.
	 * 
	 * @return a reference to belonging table
	 */
	public TalendFlow getFlow();
	
	/**
	 * Check if the column is part of the primary key for the associated table
	 * 
	 * @return true if the column is part of the key, false otherwise.
	 */
	public boolean isKey();

}
