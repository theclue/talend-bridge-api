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
 * Flows are data structures that can hold data in tabular-format, like tables in typical
 * RDBMS, but whit some neat additional features.<br />
 * First of all, data is organized in OOP fashion (developers that uses ORM systems or graph-based databases
 * would find some familiar elements), where columns, rows and even single cells are objects.<br />
 * Flows can be used like regular tables - an endeless pit to store and retrieve unlimited amount of rows - or buffers
 * - limited in size and organized with a FIFO strategy. This is useful when planned data manipulations doesn't need the entire data
 * set (ie. a group by transformation) but only a limited buffer of newest rows (ie. a t-7 moving average calculation) and you don't
 * want to waste memory.<br />
 * Aside that, a compact and efficient subset of table-like features, like defaultvalue, primary key and index is provided.<br />
 * Please not that method to <strong>remove</strong>rows are not provided, as the intended use of flow is as temporary area to hold data for data manipulations ans
 * <strong>not</strong> as a persistent DB-like are to store data. That's why many typical data operations are limited and many others are complitely disallowed.<br >
 * The deep of the buffer is set using the maximumSize parameter when a new flow is built using methods provided by {@link TalendFlowFactory}.<br />
 * Here's some examples on how to build and how to use flows:<br />
 * <pre>
 * {@code
 * // providing you have a valid flow model instance in tmodel
 * 
 * TalendFlowFactory tablefactory = tmodel.getFlowFactory();
 * 
 * // Create a new flow named 'singleRow' with maximumSize=0 (without buffer, only the current row is stored) and without transaction support
 * TalendFlow singleRow = tablefactory.newFlow("buffer1", 0, false);
 * 
 * // Create a new flow 'movingAverage' holding 6 rows behind the current row and transaction support
 * TalendFlow movingAverage = tablefactory.newFlow("movingA", 6, true);
 * 
 * // Create a new flow 'pit' unlimited in size and with transaction support
 * TalendFlow endlessPit = tablefactory.newFlow("pit", null, true);
 * 
 * // Create a new column in 'pit' of type String and default value 'foo'
 * endlessPit.addColumn("col1", TalendType.STRING, "foo");
 * }
 * </pre>
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
	 * Add a column to current flow with no default value<br />
	 * The column will be identified by its name, that must be unique in the flow.<br />
	 * This method is designed using the builder pattern; it means you can concat several calls to <em>build</em> a set of column in a very compact way.<br />
	 * Example of use:<br>
	 * <pre>
	 * {@code
	 * // Assuming a valid TalendFlowFactory instance in tablefactory
	 *  
	 * // Create a new endless flow supporting transactions
	 * TalendFlow table = tablefactory.newFlow("table1", null, true);
	 * 
	 * // Add a String and a Integer column
	 * table.addColumn("col1", TalendType.STRING).addColumn("col2", TalendType.INTEGER);
	 * }
	 * </pre>
	 * 
	 * @param name the name of the column; must be unique
	 * @param type the type for the column, from supported ones
	 * @return a reference to the flow itself
	 * @throws IllegalArgumentException if name is null, empty, invalid or already present in the flow
	 */
	public TalendFlow addColumn(String name, TalendType type);

	/**
	 * Add a column to current flow, specifying the default value<br />
	 * The column will be identified by its name, that must be unique in the flow.<br />
	 * This method is designed using the builder pattern; it means you can concat several calls to <em>build</em> a set of column in a very compact way.<br />
	 * Example of use:<br>
	 * <pre>
	 * {@code
	 * // Assuming a valid TalendFlowFactory instance in tablefactory
	 *  
	 * // Create a new endless flow supporting transactions
	 * TalendFlow table = tablefactory.newFlow("table1", null, true);
	 * 
	 * // Add a String column with default value 'foo'
	 * table.addColumn("col1", TalendType.STRING, "foo");
	 * 
	 * // Add a new Integer column with no default value
	 * table.addColumn("col2", TalendType.INTEGER, null);
	 * }
	 * </pre>
	 * 
	 * @param name the name of the column; must be unique
	 * @param type the type for the column, from supported types
	 * @param defaultValue default value for rows of that column; must be of the same type (or parseable, if String) of the column
	 * @return a reference to the flow itself
	 * @throws IllegalArgumentException if name is null, empty, invalid or already present in the flow
	 */
	public TalendFlow addColumn(String name, TalendType type, Object defaultValue);

	/**
	 * Add a column to current flow, specifying the default value and if it will be part of the primary key of the flow<br />
	 * The column will be identified by its name, that must be unique in the flow.<br />
	 * Like any RDBMS table, no duplicate in the primary key is allowed, but since flows can be set to be limited in size, PKs are rolled-out too and could become available again.
	 * Please note that only flows that support transactions can hold key columns.<br />
	 * This method is designed using the builder pattern; it means you can concat several calls to <em>build</em> a set of column in a very compact way.<br />
	 * Example of use:<br>
	 * <pre>
	 * {@code
	 * // Assuming a valid TalendFlowFactory instance in tablefactory and a valid TalendRowFactory in rowFactory
	 *  
	 * // Create a 1- step buffer flow supporting transactions
	 * TalendFlow table = tablefactory.newFlow("table1", 1, true);
	 * 
	 * // Add a String column with default value 'foo' that is part of the primary key
	 * table.addColumn("col1", TalendType.STRING, "foo", true);
	 * 
	 * // Add a new Integer column with no default value and that is not part of the primary key
	 * table.addColumn("col2", TalendType.INTEGER, null);
	 * 
	 * // Add a new row leaving columns to default value
	 * rowFactory.newRow(table);
	 * 
	 * // Add a new row and set col1 to a new value, to avoid duplicates in key
	 * rowFactory.newRow(table).setValue("col1", "bar");
	 * 
	 * // Commit changes to table
	 * table.commit();
	 * 
	 * Add a new row leaving columns to default value; since table can only hold back 1 row (aside the incoming one) col1="foo" key is newly available
	 * rowFactory.newRow(table);
	 * 
	 * // Commit changes to table. No exception is triggered for duplicate key
	 * table.commit();
	 * }
	 * </pre> 
	 * 
	 * @param name the name of the column; must be unique
	 * @param type the type for the column, from supported types
	 * @param defaultValue default type for rows of that column; must be of the same type (or parseable, if a string) of the column
	 * @param isKey true if the column will be part of the primary key for this flow
	 * @return a reference to the flow itself
	 * @throws IllegalArgumentException if name is null, empty, invalid or already present in the flow
	 * @throws IllegalArgumentException if trying to add a key column to a flow not supporting transactions
	 * @throws IllegalArgumentException if trying to add a key column to a non-empty flow
	 */	
	public TalendFlow addColumn(String name, TalendType type, Object defaultValue, boolean isKey);

	/**
	 * Check if the column with the given name exists in the flow.
	 * 
	 * @param column the column name to check for
	 * @return true if the column exists, false otherwise
	 */
	public boolean hasColumn(String column);

	/**
	 * Check if the column exists in the flow.
	 * 
	 * @param column the column to check for
	 * @return true if the column exists, false otherwise
	 */
	public boolean hasColumn(TalendColumn column);

	/**
	 * Return an array to references to the whole set of columns of the current flow.
	 * 
	 * @return an array containing references to all columns
	 */
	public TalendColumn[] getColumns();

	/**
	 * Delete a column from the current flow; all rows data cells for that column are removed, too
	 * 
	 * @param column the column to remove
	 * @throws IllegalStateException if trying to emove a column that is part of the primary key and the flow is not empty
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
	 * @throws IllegalArgumentException if the given column is not valid or part of the flow
	 */
	public TalendValue[] sliceRows(TalendColumn column);

	/**
	 * Check if the flow support transactions and, consequently, {@link commit} operations
	 */
	public boolean supportsTransactions();

	/**
	 * Commit changes to flow.<br>
	 * If a flow support transactions, changes in data are commited to table only when this method is called.<br />
	 * Please note that only changes in data are handled using commit/rollback mechanism. Changes in DDL (ie. add or remove a column) are commited immidiately, even on supporting-transactions flows.<br />
	 * Commit/rollback features are mandatory if a flow need to define a key because of rows' setValue lazyness (save to row made by commit() rather than by setValue itself: this avoid key duplication exceptions while adding new rows).<br />
	 * If flow doesn't support transactions, the method has no effect
	 */
	public void commit();
	
	/**
	 * Return the maximum size of the current flow.
	 */
	public Integer getMaximumSize();
	
	/**
	 * Get the model which the flow belongs
	 */
	public TalendFlowModel getModel();

	/**
	 * Discard all data changes made from the last commit.
	 */
	public void rollback();
	
	/**
	 * Truncate the current data flow
	 */
	public void truncate();
}
