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
 * This controller allows to gain access to {@link TalendFlowModel} where flows
 * (columns, rows and properties) are stored like a set of in-memory RDBMS
 * tables.
 * 
 * Models are context-aware; multiple models can coexists in the same job execution, 
 * providing different {@link TalendContext}
 * 
 * @author Gabriele Baldassarre
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
	 * Return the flow controller instance
	 * 
	 * @return the singleton instance of the flow controller
	 */
	public static TalendFlowController getInstance() {
		return TalendFlowControllerHolder.INSTANCE;
	}
	
	/**
	 * Return the flow model identified by a {@link TalendContext}, if exists. If not, create
	 * a new model
	 * 
	 * @param context
	 * @return the flow model linked to the 
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