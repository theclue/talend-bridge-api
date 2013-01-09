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
 * A flow model is a two purposes container used in data manipulation tasks.<br/>
 * First of all, it contains the entire DDL for the data model. It means it contains accessors
 * to the whole sets of data structures (tables, columns, rows...) and methods and factories to manipulate it
 * using.<br />
 * Then, it contains the data itself like it would be organized in a RDBMS, providing methods and structure
 * to access stored data in OOP fashion, in a way not much different from modern graph-based databases.<br />
 * Elements in data model are usually built using model's factories, to ensure ownership and mutual linking with the
 * model itself.<br />
 * Altought multiple model can be used and combined at same time, a data manipulation task usually uses just one data model.
 * This means:
 * <ul>
 * <li>Get an instance of the flow controller</li>
 * <li>Get a new flow model, that acts as container for the needed data structures
 * <li>Use model factories to define and populate the data model</li>
 * </ul>
 * Here's a typical example of use:<br />
 * <pre>
 * {@code
 * TalendFlowController tcontroller = TalendFlowController.getInstance(); // get the singleton instance of the controller
 * TalendFlowModel tmodel = tcontroller.getModel(new TalendContext()); // get the flow model passing a default (empty) context
 *
 * // Get the factory interfaces to buld flows and rows
 * TalendFlowFactory tablefactory = tmodel.getFlowFactory();
 * TalendRowFactory rowFactory = tmodel.getRowFactory();
 * 
 * // Build a new flow
 * TalendFlow table = tablefactory.newFlow("table1", null, true);
 * }
 * </pre>
 * 
 * @author Gabriele Baldassarre
 * @see TalendFlow
 *
 */
public interface TalendFlowModel{

	/**
	 * Return the row factory, used to add rows to flows belonging to model
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
	 * Return the flow factory, used to add table-like structures to model.<br />
	 * 
	 * @return the flow factory
	 */	
	public TalendFlowFactory getFlowFactory();

	/**
	 * Get the {@link TalendFlow} with given name, if it exists in the model, or null otherwise
	 * 
	 * @param name the name of the flow to retrieve 
	 * @return a reference to the flow or null if a flow with that name doesn't exist
	 */
	public TalendFlow getFlow(String name);

	/**
	 * Return an array of the whole set of TalendFlows belonging to model.
	 * The array is empty if no TalendFlows have
	 * been registered to the model, yet.
	 * 
	 * @return an array of flows belonging to current model
	 */
	public TalendFlow[] getTalendFlows();

	/**
	 * Check if the flow with given name belongs to module.
	 * 
	 * @param name the name of the flow to check about
	 * @return true if the flow exists, false otherwise
	 */
	public boolean hasFlow(String name);

	/**
	 * Count the number of flows belonging to model
	 * 
	 * @return the number of flows.
	 */
	public int countFlows();

	//public void removeFlow(TalendFlow flow);

}