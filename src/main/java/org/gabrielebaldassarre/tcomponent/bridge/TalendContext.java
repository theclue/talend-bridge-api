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
 * A TalendContext is built from a Talend job globalMap, which encapsulate all the components
 * global variables, the status of job flows and the whole set of user's global variables.
 * Basically it summarize the internal status of a Talend execution at the time it's instantiated and provide
 * a set of accessors to use those variables by the model.
 * 
 * @author Gabriele Baldassarre
 *
 */
public class TalendContext {
	
	private Map<String, Object> contextMap;
	/**
	 * Build a generic context with an empty status; no information from Talend job status 
	 * is set to the TalendContext and to the model.
	 */
	public TalendContext(){
		contextMap = null;
	}
	/**
	 * Build a context passing a Talend job execution status through a globalMap object.
	 * The globalMap content will be available from inside the model.
	 * 
	 * @param globalMap a Talend globalMap rapresenting the execution status of a Talend job.
	 */
	public TalendContext(Map<String, Object> globalMap){
		contextMap = new HashMap<String, Object>(globalMap);
	}
	
    /**
     * Overwrite the context status of a TalendContext with a new globalMap from Talend job execution.
     * The new map will replace the existing one for the current context instance.
     *     
     * @param globalMap the new Talend status
     * @return a reference to the updated instance
     */
    public TalendContext updateContextMap(Map<String, Object> globalMap){
    	if(contextMap == null){
    		contextMap = new HashMap<String, Object>(globalMap);
    	} else {
    		contextMap.clear();
    		contextMap.putAll(globalMap);
    	}
    	return this;
    }
    /**
     * Get the up-to-date Talend context map, or null if TalendContext has not a Talend job context linked to.
     * 
     * @return the current context map or null if not defined
     */
    public Map<String, Object> getContextMap(){
    	return contextMap;
    }
   
    /**
     * Merge the context map from another TalendContext instance.
     * 
     * @param context the TalendContext to merge with
     * @param overwrite true to overwrite existing context keys with new ones, false otherwise
     */
    public void mergeContext(TalendContext context, boolean overwrite){
    	if(contextMap == null){
    		contextMap = new HashMap<String, Object>(context.getContextMap());
    		return;
    	}
    	Map<String, Object> tmp;
    	if(!overwrite) {
    		tmp = new HashMap<String, Object>(context.getContextMap());
    		tmp.keySet().removeAll(contextMap.keySet());
    	} else {
    		tmp = context.getContextMap();
    	}
    	contextMap.putAll(tmp);
    }
    /**
     * Add a new key-value pair to TalendContext status.
     * 
     * @param key the keyname for the new parameter
     * @param value the value for the new parameter
     * @param overwrite true to overwrite existing context keys with new ones, false otherwise
     */
    public void addToContext(String key, Object value, boolean overwrite){
    	if(contextMap == null){
    		contextMap = new HashMap<String, Object>();
    	}
    	if(overwrite || !contextMap.containsKey(key)) {
        	contextMap.put(key, value);
    	}
    }
    
    public boolean equals(TalendContext context){
    	return(contextMap != null ? contextMap.equals(context.getContextMap()) : context.getContextMap() == null);
    }
    
    public String toString(){
    	return "{context=" + (contextMap != null ? contextMap.toString() + "}" : "null");
    }
	
}