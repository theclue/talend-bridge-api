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
 * This factory is used to build instances of concrete implementation of TalendValue interface, which
 * represents atomic values stored in cells in various data structures of data model.<br />
 * Although in most cases, values are directly build using {@see TalendRow} setValue, the factory is usable
 * by the user, since a value can exists outside of a flow (and used to compare, clone and other various operations).
 *  
 * @author Gabriele Baldassarre
 * @see TalendValue
 * @see TalendRow
 *
 */
public interface TalendValueFactory {
	
	/**
	 * Build a new TalendValue for the given column and with the given value.
	 * 
	 * @param column a reference to column, used to determine the kind and the properties of the just build value
	 * @param value the assuming value. This value is assumed to be final
	 * @return a reference to newly created value
	 */
	public TalendValue newValue(TalendColumn column, Object value);
	

}
