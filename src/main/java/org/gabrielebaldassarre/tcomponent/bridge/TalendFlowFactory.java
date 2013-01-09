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
 * This is the factory used to build new flows in data model.<br />
 * Think of flows like tables in traditional RDBMS, with columns, rows, cells, primary keys and indexes.
 * Since this factory has no sense outside
 * of a model, use model's {@link getFlowFactory} to
 * get an instance of the current factory.<br />
 * See {@see TalendFlow} for some examples on how to use this factory.
 * 
 * @author Gabriele Baldassarre
 * @see TalendFlowModel
 *
 */
public interface TalendFlowFactory {
	
	/**
	 * Build a new flow with the given name in the model and return a reference to it.
	 * If you set a maximum size for the flow, only a limited number of rows can be
	 * allocated in the flow on a date time; eldest rows are discarded as a FIFO stack.
	 * 
	 * @param name the name of the new flow
	 * @param maximumSize the maximum number of rows to retain; null for unlimited size
	 * @param supportTransactions set to true to save updated to flow only using a {@link commit()} call, false to save updates immediately
	 * @return a reference to flow
	 * @throws IllegalArgumentException if maximumSize is less than zero
	 * @throws IllegalArgumentException if the name for the flow is empty, null or invalid
	 */
	public TalendFlow newFlow(String name, Integer maximumSize, boolean supportTransactions);
	
	/**
	 * Build a new flow with the given names and add columns on it using the public
	 * fields of a provided template class.
	 * 
	 * @param name the name of the new flow
	 * @param template the struct to get the column list from. Only public fields are used.
	 * @param maximumSize the maximum number of rows to retain; null for unlimited size
	 * @param supportTransactions true to save updates to flow only using a {@link commit()} call, false to save updates immediately
	 * @return a reference to flow
	 * @throws IllegalArgumentException if the name for the flow is empty, null or invalid
	 */
	public TalendFlow newFlow(String name, Class<?> template, Integer maximumSize, boolean supportTransactions);

}
