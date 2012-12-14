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

/**
 * This class decorates an existing java List, adding a size limit to it. If the maximum Size of the collection
 * is met, then the oldest elements are kicked out, in a very simple FIFO strategy.<br />
 * Please note that <strong>this is a decorating class</strong>, nor a cloning or proxying one.
 * This means that you are very likely to encounter very unpredictable side effects if you continue to
 * use the delegate collection (ie. adding new elements on it) after having decorated it using this class' instences<br />
 * This happens because delegate list won't know of herself being constrained, while decorated one ignores of delegate still being used.<br />
 * <br />
 * This class should never be used directly.<br />
 * The best way to build instances of this type is to call the provided {@link TalendListFactory} set of methods, which choose the best concrete implementation of {@link TalendList}, instead.
 * 
 * <pre>
 * {@code
 * 
 * TalendListFactory<String> factory = TalendListFactory.getInstance(String.class);
 * List<String> decoratedList = factory.newTalendList(new ArrayList(), 2);

 * }
 * </pre>
 * 
 * @author Gabriele Baldassarre
 * @param <T> the type of elements held in this collection
 * @see TalendListFactory
 */
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
	 * Create a new collection decorating an existing one and put a size limit on it. If this limit is met, oldest elements
	 * are kicked out from the collection.
	 * 
	 * @param delegate the collection to decorate
	 * @param maximumSize the maximum number of elements that can be held by the collection at any time
	 * @throws IndexOutOfBoundsException if maximumSize is minus or equal to zero
	 * @throws IllegalArgumentException if the delegate collection has already too many elements to bo contrained to maximumSize
	 */
	public TalendLimitedList(List<T> delegate, int maximumSize){
		ResourceBundle rb = ResourceBundle.getBundle("TalendBridge", Locale.getDefault());
		if(maximumSize <= 0){
			throw new IndexOutOfBoundsException(String.format(Locale.getDefault(), rb.getString("exception.invalidBufferSize"), maximumSize));
		}
		
		if(delegate.size() > maximumSize){
			throw new IllegalArgumentException(String.format(Locale.getDefault(), rb.getString("exception.bufferTooSmall"), delegate.size(), maximumSize));
		}

		this.delegate = delegate;
		this.maximumSize = maximumSize;

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
		if(c.size() > maximumSize){
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
