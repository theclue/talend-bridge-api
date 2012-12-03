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
 * This interface adds to TalendFlow implementing it the capabilites to be visited
 * by visitor implementing {@link TalendBehaviour} which add behaviour and extra capabilities
 * on it, giving the possibility to expose their data. Only one behaviour is implemented atm.
 * 
 * @author Gabriele Baldassarre
 *
 */
public interface TalendBehaviourableFlow {
	/**
	 * Add the specific behaviour to this flow.
	 * 
	 * @param b is the visitor who visit the current instance and add a behaviour to.
	 */
	public void addBehaviour(TalendBehaviour b);
}