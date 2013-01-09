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

import java.util.HashMap;
import java.util.Map;
/**
 * This controller gives access to {@link TalendFlowModel} where {@link TalendFlow}
 * elements are stored.<br />
 * Altough the controller is unique and accessed using a singleton instance,
 * multiple flow models can be used simultaneously, providing a context-aware mechanism
 * useful, for example, if stateful or CODA properties ar needed.<br />
 * Different flow models are uniquely identifies by a key {@link TalendContext}.<br />
 * <pre>
 * {@code
 * TalendFlowController tcontroller = TalendFlowController.getInstance(); // get the singleton instance of the controller
 * TalendFlowModel tmodel = tcontroller.getModel(new TalendContext()); // get the flow model passing a default (empty) context
 *
 * // Get the factory interfaces to buld flows and rows
 * TalendFlowFactory tablefactory = tmodel.getFlowFactory();
 * TalendRowFactory rowFactory = tmodel.getRowFactory();
 * }
 * </pre>
 * 
 * @author Gabriele Baldassarre
 *
 */
public class TalendFlowController { 

	private static Map<TalendContext, TalendFlowModel> contextCollection;

	private TalendFlowController() {
		contextCollection = new  HashMap<TalendContext, TalendFlowModel>();

	}

	private static class TalendFlowControllerHolder { 
		private final static TalendFlowController INSTANCE = new TalendFlowController();

	}
	
	/**
	 * Return a concrete instance of the controller
	 * 
	 * @return the singleton instance of the controller
	 */
	public static TalendFlowController getInstance() {
		return TalendFlowControllerHolder.INSTANCE;
	}

	/**
	 * Return the flow model identified by a key {@link TalendContext}, if exists. If not, create
	 * a new instance of the flow model and link the context on it.<br />
	 * A default empty TalendContext can be safely used in normal operation. It's a standard way in task
	 * when only a flow model is needed (most common cases).
	 * 
	 * @param context a Talend context used to identify the model from this moment on.
	 * @return the flow model container associated to the provided context. 
	 */
	public TalendFlowModel getModel(TalendContext context){
		if(contextCollection.containsKey(context)){
			return contextCollection.get(context);
		} else {
			TalendFlowModel newModel = new TalendFlowModelImpl();
			contextCollection.put(context, newModel);
			return newModel;
		}
	}

}