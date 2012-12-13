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

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class TalendLimitedList<T> extends TalendList<T>{

	protected List<T> delegate;
	protected Integer maximumSize = null;

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected List<T> delegate() {
		return delegate;
	}


	/**
	 * Create a new collection decorating an existing one and put a size limit on it.
	 * If the delegate collection is too big to fit into the new collection, an exception is thrown.
	 * 
	 * @param delegate the collection to decorate
	 * @param maximumSize the maximum number of elements that can be held by the collection
	 */
	public TalendLimitedList(List<T> delegate, int maximumSize){
		ResourceBundle rb = ResourceBundle.getBundle("TalendBridge", Locale.getDefault());
		if(maximumSize <= 0){
			throw new IndexOutOfBoundsException(String.format(Locale.getDefault(), rb.getString("exception.invalidBufferSize"), maximumSize));
		}

		this.delegate = delegate;
		this.maximumSize = maximumSize;

		//addAll(delegate);

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean add(T e){
		if(maximumSize != null && this.size() == maximumSize){
			this.remove(0);
		}
		return super.add(e);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean addAll(Collection<? extends T> c){
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

}
