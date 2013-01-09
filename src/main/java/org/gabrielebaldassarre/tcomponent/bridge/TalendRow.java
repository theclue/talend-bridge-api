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
 * TalendRows are row-like structures that belong to TalendFlow elements and act as container of cell-like objects like TalendValue instances.<br />
 * This means that they are organized in OOP fashion (developers that uses ORM systems or graph-based databases
 * would find some familiar elements).<br />
 * If the belonging flow support transactions, this features is inherited by rows that become <em>lazy</em>. This means that operations made using setValue calls are not
 * immediately pushed to rows, but only after a TalendFlow's commit() call.<br /> 
 * Since rows have no sense outside of a flow, they cannot be built directly, but a factory model is provided by the data model for this purpose.<br />
 * For the same reason, a row cannot be detached from its flow.<br />
 * Example of use:<br />
 * <pre>
 * {@code
 * // providing you have a valid flow model instance in tmodel
 * 
 * TalendFlowFactory tablefactory = tmodel.getFlowFactory();
 * TalendRowFactory rowFactory = tmodel.getRowFactory();
 * 
 * // Create a new flow 'pit' unlimited in size and with transaction support
 * TalendFlow endlessPit = tablefactory.newFlow("pit", null, true);
 * 
 * // Create a new column in 'pit' of type String and default value 'foo'
 * endlessPit.addColumn("col1", TalendType.STRING, "foo");
 * 
 * // Create a new row in 'pit' and set the value of 'col1' to 'bar'
 * TalendRow row = rowFactory.newRow(endlessPit).setValue("col1", "bar");
 * 
 * // Since the changes are still not commited to row, col1 has still the default value of 'foo'
 * System.out.println(row.getValue("col1"); // foo
 * 
 * // Commit changes
 * endlessPit.commit();
 * 
 * System.out.println(row.getValue("col1"); // bar
 * }
 * </pre>
 * 
 * @author Gabriele Baldassarre
 *
 */
public interface TalendRow {
	/**
	 * Return a reference to the flow which the row belongs.
	 * 
	 * @return a reference to flow
	 */
	TalendFlow getTable();

	/**
	 * Return the value of the column at the given index
	 * 
	 * @param index the colum index from which retrieve the desired value
	 * @return the value or null if not value is  defined for the column at given index
	 * @throws IllegalArgumentException if no valid column is found in providing flow at given index
	 */
	TalendValue getTalendValue(int index);

	/**
	 * Return the value of the column with given column name
	 * 
	 * @param column the colum index from which retrieve the desired value
	 * @return the value or null if not value is the defined for the column with given name
	 * @throws IllegalArgumentException if no valid column is found in providing flow with given name
	 */
	TalendValue getTalendValue(String column);

	/**
	 * Return an array with the whole set of values from the current row
	 * 
	 * @return an arrayset with values
	 */
	TalendValue[] getTalendValues();

	/**
	 * Return the value of the column at the given index
	 * 
	 * @param index the colum index from which retrieve the desired value
	 * @return the value as java type or null if not value is  defined for the column at given index
	 * @throws IllegalArgumentException if no valid column is found in providing flow at given index
	 */	
	Object getValue(int index);

	/**
	 * Return the value of the column with given column name
	 * 
	 * @param column the colum index from which retrieve the desired value
	 * @return the value as java type or null if not value is the defined for the column with given name
	 * @throws IllegalArgumentException if no valid column is found in providing flow with given name
	 */	
	Object getValue(String column);

	/**
	 * Set the value for the column with the given name.<br />
	 * This method implements the builder design pattern: it returns a reference to current row so it's
	 * possible to concatenate several to <em>build</em> a complete row.<br />
	 * If belonging flow support transactions, changes in rows are not immediately pushed to row. They are delayed
	 * until a call to TalendFlow's commit().<br />
	 * Examples:</br>
	 * <pre>
	 * {@code
	 * // providing you have a valid flow instance in table
	 * 
	 * // Add two columns to flow
	 * table.addColumn("col1", TalendType.STRING, "foo").addColumn("col2", TalendType.INTEGER, 1);
	 * 
	 * // Add two rows to flow
	 * rowFactory.newRow(table).setValue("col2", -1);
	 * rowFactory.newRow(table).setValue("col1", "bar").setValue("col2", 0); 
	 *  }
	 * </pre>
	 * @param column the column to set
	 * @param value the value to set, expressed as java type
	 * @return a reference to current row
	 * @throws IllegalArgumentException if no valid column is found in providing flow with given name
	 * @throws IllegalStateException if trying to alter the value of a column which is part of the primary key and the row was already saved to flow
	 */
	TalendRow setValue(String column, Object value);

	/**
	 * Set the value for the given column.<br />
	 * This method implements the builder design pattern: it returns a reference to current row so it's
	 * possible to concatenate several to <em>build</em> a complete row.<br />
	 * If belonging flow support transactions, changes in rows are not immediately pushed to row. They are delayed
	 * until a call to TalendFlow's commit().
	 * 
	 * @param column the column to set
	 * @param value the value to set, expressed as java type
	 * @return a reference to current row
	 * @throws IllegalArgumentException if column doesn't belong to flow which the row belongs
	 * @throws IllegalStateException if trying to alter the value of a column which is part of the primary key and the row was already saved to flow
	 */
	TalendRow setValue(TalendColumn column, Object value);

	/**
	 * Set the value for the column at given index.<br />
	 * This method implements the builder design pattern: it returns a reference to current row so it's
	 * possible to concatenate several to <em>build</em> a complete row.<br />
	 * If belonging flow support transactions, changes in rows are not immediately pushed to row. They are delayed
	 * until a call to TalendFlow's commit().
	 * 
	 * @param column the column to set
	 * @param value the value to set, expressed as java type
	 * @return a reference to current row
	 * @throws IllegalArgumentException if no valid column is found in providing flow at given index
	 * @throws IllegalStateException if trying to alter the value of a column which is part of the primary key and the row was already saved to flow
	 */
	TalendRow setValue(int index, Object value);

	/**
	 * Set the value for the column which the value itself is associated.<br />
	 * This method implements the builder design pattern: it returns a reference to current row so it's
	 * possible to concatenate several to <em>build</em> a complete row.<br />
	 * If belonging flow support transactions, changes in rows are not immediately pushed to row. They are delayed
	 * until a call to TalendFlow's commit().
	 * 
	 * @param column the column to set
	 * @param value the value to set, expressed as java type
	 * @return a reference to current row
	 * @throws IllegalArgumentException if no valid column is found in providing flow at given index
	 * @throws IllegalStateException if trying to alter the value of a column which is part of the primary key and the row was already saved to flow
	 */
	TalendRow setValue(TalendValue value);

	/**
	 * Check if the row support transactions
	 * 
	 * @return true if belonging flow (and, consequantely, the row) supports transactions, false otherwise
	 */
	boolean supportTransactions();

	/**
	 * Count the number of non-null values in row
	 * 
	 * @return the number of non-null values
	 */
	int countValues();

	/**
	 * Check if current row is empty, ie all its values points to null<br />
	 * Please note that if columns admit a non-null default value, rows can't be empty by definition.
	 * 
	 * @return true if row is empty, false otherwise
	 */
	boolean isEmpty();

	/**
	 * Truncate the current row<br />
	 * If some columns admit default non-null values, values for these columns is reverted to defaults.
	 * 
	 */
	void truncate();

	//TalendRow save();

	//void discardChanges();

	/**
	 * Check if row has pending changes from the last flow's commit<br />
	 * 
	 * @return true if there are changes still not commited to row, false otherwise or if parent flow doesn't support transactions.
	 */
	boolean isChanged();

	/**
	 * Clone the current row making a new rows with values equals to current one.<br />
	 * Please note that if parent flow support transactions, the cloned row is not automatically committed.
	 * This mean that it's still possible to make setValue to cloned row (even on key columns).<br >
	 * @return a reference to cloned row
	 */
	TalendRow clone();

}
