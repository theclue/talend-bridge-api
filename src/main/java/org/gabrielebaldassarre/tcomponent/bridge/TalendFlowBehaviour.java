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
 * then used by the concrete instance of the visitor itself<br />
 * The mechanism is particularly useful while building Talend components that usually needs to perform operations on data
 * contained in the flow model but cannot extend the TalendFlow class at runtime. The visitor design pattern is
 * perfect to handle such situations as concrete visitors can <em>visit</em> target data structures and perform operations on them.<br />
 * 
 * Here's an basic (abstract) example of use:<br />
 * 
 * <pre>
 * {@code
 * // Assuming "table" as valid TalendFlow name of a concrete flow belonging to tmodel and that implements TalendFlowBehaviour interface
 * TalendFlow table = tmodel.getFlow("tab");
 * 
 * // 'averageNullBehave' is a concrete instance of a flow behaviour that can visit a flow and
 * // do some stuff on it (ie. calculate the average number of null values for the whole set of rows)
 * TalendFlowBehaviour averageNullBehave = new TalendFlowAverageNullBehaviour();
 * 
 * // the behaviour is added to the flow by visiting it 
 * averageNullBehave.visit(table);
 * 
 * // TalendFlowAverageNullBehaviour class has concrete implementation of methods to, ie., return the computed value
 * int value = averageNullBehave.getComputedValue();
 * }
 * </pre>
 * 
 * @author Gabriele Balsassarre
 *
 */
public interface TalendFlowBehaviour {
	/**
	 * Visit a target table and eventually perform an action on it. The action
	 * that is made is implemented in classes implementing this interface
	 * 
	 * @param table the TalendFlow to visit
	 */
	public void visit(TalendFlow table);
	
	/**
	 * Based upon the concrete implementation of the class implementing this
	 * interface, return true if the table just visited is valid for the purpose
	 * of the behaviour/visitor. False otherwise
	 * 
	 * @return true if the already-visited table is valid for the purpose of the visitor, false otherwise
	 */
	public Boolean isValid();
}
