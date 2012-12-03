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
 * Return a data model, like a small in-memory RDBMS that has flows (tables), columns, rows and properties.
 * The model provide the accessors to product factories, used to build objects belonging to the model itself.
 * 
 * @author Gabriele Baldassarre
 *
 */
public interface TalendFlowModel{

	/**
	 * Return the row factory, used to add rows to flows belonging to the model
	 * 
	 * @return the row factory
	 */
	public TalendRowFactory getRowFactory();

	/**
	 * Return the value factory, used to add new values to existing rows and columns
	 * 
	 * @return the value factory
	 */	
	public TalendValueFactory getValueFactory();

	/**
	 * Return the flow factory, used to add tables to the model
	 * 
	 * @return the flow factory
	 */	
	public TalendFlowFactory getFlowFactory();

	/**
	 * Get the {@link TalendFlow} with the provided name, if it exists in the model, or null otherwise
	 * 
	 * @param name the name of the flow to retrieve 
	 * @return a reference to the desired flow or null if a flow with that name doesn't exist
	 */
	public TalendFlow getFlow(String name);

	/**
	 * Return an array of the whole set of TalendFlows belonging to the model. The array is empty if no TalendFlows have
	 * been registered to the current model.
	 * 
	 * @return an array of flows
	 */
	public TalendFlow[] getTalendFlows();

	/**
	 * Check to see if the flow with the given name has been registered to the module
	 * 
	 * @param name the name of the flow to check about
	 * @return true if the flow exists, false otherwise
	 */
	public boolean hasFlow(String name);

	/**
	 * TODO
	 * 
	 * @param model
	 */
	public void mergeModel(TalendFlowModel model);

	/**
	 * Count the number of flows of the model
	 * 
	 * @return the number of flows.
	 */
	public int countFlows();

	//public void removeFlow(TalendFlow flow);

}