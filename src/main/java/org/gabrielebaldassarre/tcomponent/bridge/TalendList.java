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


import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

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
 * @param <E> the type of elements held in this collection
 */
public class TalendList<E extends Serializable> extends ForwardingList<E>{

	protected List<E> delegate;
	protected Integer maximumSize = null;

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected List<E> delegate() {
		return delegate;
	}

	/**
	 * Create a new collection decorating an existing one
	 * 
	 * @param delegate the collection to decorate
	 */
	public TalendList(List<E> delegate){
		this.delegate = delegate;
	}

	/**
	 * Create a new collection decorating an existing one and put a size limit on it.
	 * If the delegate collection is too big to fit into the new collection, an exception is thrown.
	 * 
	 * @param delegate the collection to decorate
	 * @param maximumSize the maximum number of elements that can be held by the collection
	 */
	public TalendList(List<E> delegate, Integer maximumSize){
		ResourceBundle rb = ResourceBundle.getBundle("TalendBridge", Locale.getDefault());
		if(maximumSize != null && maximumSize <= 0){
			throw new IndexOutOfBoundsException(String.format(Locale.getDefault(), rb.getString("exception.invalidBufferSize"), maximumSize));
		}

		this.delegate = delegate;
		this.maximumSize = maximumSize;

		addAll(delegate);

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean add(E e){
		if(maximumSize != null && this.size() == maximumSize){
			this.remove(0);
		}
		return super.add(e);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean addAll(Collection<? extends E> c){
		ResourceBundle rb = ResourceBundle.getBundle("TalendBridge", Locale.getDefault());
		if(maximumSize != null && c.size() > maximumSize){
			throw new IllegalArgumentException(String.format(Locale.getDefault(), rb.getString("exception.bufferTooSmall"), c.size(), maximumSize));
		}

		if(maximumSize != null && (this.size() + c.size()) > maximumSize){
			for(int i=0; i < c.size(); i++){
				this.remove(0);
			}
		}
		return super.addAll(c);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<E> iterator() {
		final Iterator<E> iter = super.iterator();
		return new ForwardingIterator<E>() {
			@Override protected Iterator<E> delegate() {
				return iter;
			}
			@Override public E next() {
				E v = super.next();
				return (v); 
			}
		};
	}

}
