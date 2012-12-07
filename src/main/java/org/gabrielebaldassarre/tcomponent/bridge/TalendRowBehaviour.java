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
 * Classes implementing this interface can visit one or more TalendFlow and add
 * a particular behaviour to them (ie. convert data into a new form). This behaviour is
 * then used by the concrete instance of the visitor itself.
 * 
 * @author Gabriele Balsassarre
 *
 */
public interface TalendRowBehaviour {
	/**
	 * Visit a target table and eventually perform an action on it. The action
	 * that is made is implemented in classes implementing this interface
	 * 
	 * @param row the row to visit
	 */
	public void visit(TalendRow row);
	
	/**
	 * Based upon the concrete implementation of the class implementing this
	 * interface, return true if the table just visited is valid for the purpose
	 * of the behaviour/visitor. If not table was already visited, it return null
	 * 
	 * @return true if the already-visited table is valid for the purpose of the visitor, false otherwise and null if no table was visited already
	 */
	public Boolean isValid();
}
