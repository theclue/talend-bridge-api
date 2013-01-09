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
 * This is the factory used to build new rows in flows belonging to data model.<br />
 * Since this factory has no sense outside
 * of a model, use model's {@link getRowFactory} to
 * get an instance of the current factory.<br />
 * See {@see TalendRow} for some examples on how to use this factory.
 * 
 * @author Gabriele Baldassarre
 * @see TalendFlowModel
 *
 */
public interface TalendRowFactory {

	/**
	 * Add a new row to flow with the given name<br />
	 * Please note that if parent flow support transactions, rows are not added immediately, but only after
	 * a call to TalendFlow's commit().
	 * 
	 * @param table the flow name to add the row into.
	 * @return a reference to newly created row
	 * @throws IllegalArgumentException if a flow with given name doesn't exists in data model
	 */
	public TalendRow newRow(String table);

	/**
	 * Add a new row to provided flow<br />
	 * Please note that if parent flow support transactions, rows are not added immediately, but only after
	 * a call to TalendFlow's commit().
	 * 
	 * @param table the flow to add the row into.
	 * @return a reference to newly created row
	 * @throws IllegalArgumentException if the given flow doesn't exists in data model
	 */	
	public TalendRow newRow(TalendFlow table);

	/**
	 * Add a new row to flow with the given name and initialize its values from a given behaviour<br />
	 * Please note that if parent flow support transactions, rows are not added immediately, but only after
	 * a call to TalendFlow's commit().<br />
	 * Example:<br>
	 * <pre>
	 * {@code
	 * // Assuming row1Struct a valid Talend Open Studio vector that carries data
	 * 
	 * TalendRowBridgeBehaviour bridge = new TalendRowBridgeBehaviour();
	 * bridge.setFromStruct(row1Struct);
	 * 
	 * // row is pre-initialized with values from bridges - only matching values are added
	 * TalendRow row = rowFactory.newRow(tabella, bridge);
	 * }
	 * </pre>
	 * 
	 * @param table the flow name to add the row into.
	 * @param values values taken from a valid bridge behaviour visitor
	 * @return a reference to newly created row
	 * @throws IllegalArgumentException if a flow with given name doesn't exists in data model
	 */
	public TalendRow newRow(String table, TalendRowBridgeBehaviour values);

	/**
	 * Add a new row to provided flow and initialize its values from a given behaviour<br />
	 * Please note that if parent flow support transactions, rows are not added immediately, but only after
	 * a call to TalendFlow's commit().
	 * 
	 * @param table the flow to add the row into.
	 * @param values values taken from a valid bridge behaviour visitor
	 * @return a reference to newly created row
	 * @throws IllegalArgumentException if the given flow doesn't exists in data model
	 */	
	public TalendRow newRow(TalendFlow table, TalendRowBridgeBehaviour values);	
}
