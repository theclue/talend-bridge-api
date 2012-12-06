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
 * This is the factory used to build new flows in the model. Since this factory has no sense outside
 * of a model, use model's {@link getFlowFactory} to
 * get an instance of the current factory.
 * 
 * @author Gabriele Baldassarre
 *
 */
public interface TalendFlowFactory {
	
	/**
	 * Build a new flow with the given name in the model and return a reference to it.
	 * 
	 * @param name the name of the new flow
	 * @return a reference to flow
	 */
	public TalendFlow newFlow(String name);
	
	/**
	 * Build a new flow with the given names and add columns on it using the public
	 * fields of template class.
	 * 
	 * @param name the name of the new flow
	 * @param template the struct to get the column list from. Only public fields are used.
	 * @return a reference to flow
	 */
	public TalendFlow newFlow(String name, Class template);

}
