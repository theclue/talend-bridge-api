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
 * This abstract class provides the needed declarations of methods used to decorate
 * any standard java List. This class implements the decorator pattern, allowing subclasses to
 * override methods from List or to delegate to original List methods if not in need to override.<br />
 * Please note that <strong>this is a decorating class</strong>, nor a cloning or proxying one.
 * This means that you are very likely to encounter very unpredictable side effects if you continue to
 * use the delegate collection after having decorated it using this class' concrete implementations.
 * 
 * @author Gabriele Baldassarre
 * @param <T> the type of elements held in this collection
 * @see <a href="https://code.google.com/p/guava-libraries/wiki/CollectionHelpersExplained#Forwarding_Decorators">Forwarding Decorators</a>
 */
public abstract class TalendList<T> extends ForwardingList<T>{

	protected List<T> delegate;

	/**
	 * Return a reference to delegated list, allowing to call original List methods
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
