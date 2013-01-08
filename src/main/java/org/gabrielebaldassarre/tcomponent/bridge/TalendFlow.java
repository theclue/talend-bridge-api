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
 * Flows hosts columns and permits all manipulation on them.
 * They are like tables in a RDBMS
 * 
 * @author Gabriele Baldassarre
 *
 */
public interface TalendFlow {
	/**
	 * Return the name of the current flow
	 * 
	 * @return the name of the flow
	 */
	public String getName();
	
	/**
	 * Check if the current flow has at least one column
	 * 
	 * @return true if the flow has one or more columns, false otherwhise
	 */
	public boolean hasColumns();
	
	/**
	 * Add a column to current flow
	 * 
	 * @param name the name of the column; must be unique
	 * @param type the type for the column, from supported types
	 * @return a reference to the flow itself
	 */
	public TalendFlow addColumn(String name, TalendType type);
	
	/**
	 * Add a column to current flow, with a default value
	 * 
	 * @param name the name of the column; must be unique
	 * @param type the type for the column, from supported types
	 * @param defaultValue default type for rows of that column; must be of the same type (or parseable, if a string) of the column
	 * @return a reference to the flow itself
	 */
	public TalendFlow addColumn(String name, TalendType type, Object defaultValue);
	
	/**
	 * Add a column to current flow, with a default value and a comment
	 * 
	 * @param name the name of the column; must be unique
	 * @param type the type for the column, from supported types
	 * @param defaultValue default type for rows of that column; must be of the same type (or parseable, if a string) of the column
	 * @param isKey true if the column to add is part of the primary key for this flow
	 * @return a reference to the flow itself
	 */	
	public TalendFlow addColumn(String name, TalendType type, Object defaultValue, boolean isKey);
	
	/**
	 * Check if the column with the given name exists in the flow
	 * 
	 * @param column the column name to check for
	 * @return true if the column exists, false otherwise
	 */
	public boolean hasColumn(String column);
	
	/**
	 * Check if the column exists in the flow
	 * 
	 * @param column the column to check for
	 * @return true if the column exists, false otherwise
	 */
	public boolean hasColumn(TalendColumn column);
	
	/**
	 * Return an array to references to the whole set of columns of the current flow
	 * 
	 * @return an array containing references to all columns
	 */
	public TalendColumn[] getColumns();
	
	/**
	 * Delete a column from the current flow; all rows data for that column are removed, too
	 * 
	 * @param column the column to remove
	 */
	public void removeColumn(TalendColumn column);
	
	/**
	 * Get the column with the given name
	 * 
	 * @param name the name of the column
	 * @return a reference to column or null if a column with given name doesn't exists
	 */
	public TalendColumn getColumn(String name);

	/**
	 * Get the column at given index
	 * 
	 * @param index the index of the column
	 * @return a reference to column or null if a column at given index doesn't exists
	 */
	public TalendColumn getColumn(int index);
	
	/**
	 * Return the number of columns of current flow
	 * 
	 * @return the number of columns
	 */
	public int countColumns();

	/**
	 * Return the number of rows of current flow
	 * 
	 * @return the number of rows
	 */
	public int countRows();
	
	/**
	 * Return an array with the whole set of rows contained in the flow
	 * 
	 * @return the array with rows
	 */
	public TalendRow[] getRows();
	
	/**
	 * Get the row at specified position
	 * 
	 * @param rownum the row position to get
	 * @return a reference to the row
	 * @throws IndexOutOfBoundsException if there's no element at that position
	 */
	public TalendRow getRow(int rownum) throws IndexOutOfBoundsException;
	
	/**
	 * Return an array with references to all key columns of the table
	 * 
	 * @return an array with references to columns
	 */
	public TalendColumn[] getKeyColumns();
	
	/**
	 * Slice the flow and get only values from the specified column
	 * 
	 * @param column the column to get values from
	 * @return an array containing the values from the specified column taken from each not-null row
	 */
	public TalendValue[] sliceRows(TalendColumn column);
	
	/**
	 * Check if the flow support transactions and, consequently, {@link commit} operations
	 */
	public boolean supportsTransactions();
	
	/**
	 * Commit changes to flow; if current table doesn't support transactions, the method has no effect
	 */
	public void commit();
	
}
