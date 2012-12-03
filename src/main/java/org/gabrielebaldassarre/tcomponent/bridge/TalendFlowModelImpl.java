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

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * This is the concrete implementation of a {@link TalendFlowModel} and should never
 * be used. Use the provided interface from the {@link TalendFlowController} builder instead.
 * 
 * @author Gabriele Baldassarre
 *
 */
public class TalendFlowModelImpl implements TalendFlowModel{

	private ConcurrentMap<String, TalendFlowImpl> tables;
	TalendFactoryImpl factory;

	/**
	 * Boot-strap a model and relative factories
	 */
	public TalendFlowModelImpl(){
		factory = new TalendFactoryImpl(this);

	}
	
	/**
	 * {@inheritDoc}
	 */
	public TalendRowFactory getRowFactory() {
		return factory;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public TalendValueFactory getValueFactory() {
		return factory;
	}
	
	/**
	 * {@inheritDoc}
	 */	
	public TalendFlowFactory getFlowFactory() {
		return factory;
	}
	/**
	 * Return the concrete implementation of the model factory
	 * 
	 * @return the concrete factory
	 */
	public TalendFactoryImpl getFactory(){
		return factory;
	}

	/**
	 * {@inheritDoc}
	 */	
	public TalendFlowImpl getFlow(String name) {
		return (tables.containsKey(name) ? tables.get(name) : null);
	}
	
	/**
	 * {@inheritDoc}
	 */	
	public TalendFlowImpl[] getTalendFlows() {
		return tables.values().toArray(new TalendFlowImpl[tables.size()]);
	}

	/**
	 * {@inheritDoc}
	 */	
	public boolean hasFlow(String name) {
		return (tables.containsKey(name) ? Boolean.TRUE : Boolean.FALSE);
	}

	/**
	 * {@inheritDoc}
	 */	
	public void mergeModel(TalendFlowModel model) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * {@inheritDoc}
	 */	
	public int countFlows() {
		return tables.size();
	}
	
	/**
	 * {@inheritDoc}
	 */	
	public void addFlow(String name, TalendFlowImpl table) {
		if(tables == null) tables = new ConcurrentHashMap<String, TalendFlowImpl>();
		tables.put(name, table);

	}
}