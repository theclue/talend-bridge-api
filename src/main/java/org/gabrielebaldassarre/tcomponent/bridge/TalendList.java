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

import java.util.Iterator;
import java.util.List;

import com.google.common.collect.ForwardingIterator;
import com.google.common.collect.ForwardingList;

/**
 * TalendList is a decoration of any valid java Collection. Although its behaviour is
 * similar to delegate in most circumnstances, it support an optional parameter to keep
 * the maximum size of the collection not exceeded a given limit, rolling out older data
 * in a FIFO strategy.
 * 
 * @author Gabriele Baldassarre
 * 
 * @param <T> the type of elements held in this collection
 */
public abstract class TalendList<T> extends ForwardingList<T>{

	protected List<T> delegate;

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected List<T> delegate() {
		return delegate;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<T> iterator() {
		final Iterator<T> iter = super.iterator();
		return new ForwardingIterator<T>() {
			@Override protected Iterator<T> delegate() {
				return iter;
			}
			@Override public T next() {
				T v = super.next();
				return (v); 
			}
		};
	}

}
