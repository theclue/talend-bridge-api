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
 * This interface is implemented by {@link TalendRow} and give row instances the possibility
 * to be visited by visitor implementing {@link TalendRowBehaviour} which add behaviour and extra capabilities
 * to them, giving the possibility to expose their data.
 * 
 * @author Gabriele Baldassarre
 *
 */
public interface TalendBehaviourableRow {
	/**
	 * Add the specific behaviour to this row.
	 * 
	 * @param b is the behaviour that visits the row, carrying an action to be applied to the row
	 * @return a reference to current row
	 */
	public TalendRow addBehaviour(TalendRowBehaviour b);
}