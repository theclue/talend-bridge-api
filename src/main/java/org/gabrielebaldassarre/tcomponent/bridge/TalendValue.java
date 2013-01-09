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
 * This interfaces provide accessors to concrete implementation of values, which
 * represents atomic data elements stored in cells in various data structures of data model.<br />
 * Although in most cases, values are directly build using {@see TalendRow} setValue, a factory is usable
 * by the user, since a value can exists outside of a flow (and used to compare, clone and other various operations).
 *  
 * @author Gabriele Baldassarre
 * @see TalendValueFactory
 * @see TalendRow
 *
 */
public interface TalendValue {
	
	/**
	 * Get a reference to column from which the value was build from.
	 * 
	 * @return a reference to column
	 */
	public TalendColumn getColumn();
	
	/**
	 * Get the value expressed as java type
	 * 
	 * @return a reference to value contained by this cell, in java type form
	 */
	public Object getValue();
	
	/**
	 * Check if the value contained by this cell is equal to column default
	 * 
	 * @return true if it's equal to column default, false otherwise
	 */
	public boolean isColumnDefault();

}
