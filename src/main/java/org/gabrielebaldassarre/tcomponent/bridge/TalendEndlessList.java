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

import java.util.List;


/**
 * This list has actually the same features of a regular generic decorable List
 * 
 * @author Gabriele Baldassarre
 *
 * @param <T> the type of elements held in this collection
 */
public class TalendEndlessList<T> extends TalendList<T>{
	
	public TalendEndlessList(List<T> delegate){
		this.delegate = delegate;
	}
	
}

