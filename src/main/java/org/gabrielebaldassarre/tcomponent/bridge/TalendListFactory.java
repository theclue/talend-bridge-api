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
 * This generic factory is used to decorate java Lists, adding different 
 * custom properties on it.<br />
 * Please note that this factory it's not a collection himself! It doesn't <strong>clone</strong> collections, but it <strong>decorates</strong>
 * them, adding new features and delegating unchanging behaviours to encapsulated collection.<br />
 * To use it, first get the factory for the proper type, than ask for an instance of the desired decorated collection:<br />
 *
 * <pre>
 * {@code
 * TalendListFactory factory = TalendListFactory.getInstance(TalendRow.class);
 * List<TalendRow> rowList = factory.newTalendList(new LinkedList<TalendRow>());
 * }
 * </pre>
 * 
 * @author Gabriele Baldassarre
 *
 */
public class TalendListFactory<T> {
	/**
	 * Get a concrete instance of the factory, suitable to build collections of the given type
	 * 
	 * @param type the type of lists the factory will be able to build
	 * @return a concrete instance of the factory
	 */
	public static <T> TalendListFactory<T> getInstance(Class<T> type){
		return new TalendListFactory<T>();

	}

	/**
	 * Build a decorated TalendList which refers to a given collection.<br />
	 * The decorated collection is not limited in size, at least if no such limits exists in the original delegate list.
	 * 
	 * @param delegate the existing collection to which a reference will be encapsulated in the decorated instance
	 * @return a concrete implementation of a TalendList
	 */
	public TalendList<T> newTalendList(List<T> delegate){
		return new TalendEndlessList<T>(delegate);
	}

	/**
	 * Build a decorated TalendList which refers to a given collection.<br />
	 * This TalendList <strong>is</strong> limited in size and it adds a thresold to delegate, rolling-out oldest
	 * elements from it if maximum size is met.<br />
	 * 
	 * <pre>
	 * {@code
	 * List<String> originalList = new ArrayList<String>();
	 * originalList.add("foo");
	 * originalList.add("bar");
	 * System.out.println(originalList); // [foo, bar]
	 * 
	 * TalendListFactory<String> factory = TalendListFactory.getInstance(String.class);
	 * List<String> decoratedList = factory.newTalendList(originalList, 2);
	 * 
	 * decoratedList.add("beer");
	 * 
	 * System.out.println(decoratedList); // [bar, beer]
	 * System.out.println(originalList); // [bar, beer]
	 * }
	 * </pre>
	 * 
	 * @param delegate the existing collection to which a reference will be encapsulated in the decorated instance
	 * @param maximumSize the maximum number of elements that can take place in delegate
	 * @return a decorated class implementing a threshold control
	 * @throws IndexOutOfBoundsException if the initial size of delegate collection exceeds maximum size
	 */
	public TalendList<T> newTalendList(List<T> delegate, int maximumSize){
		return new TalendLimitedList<T>(delegate, maximumSize);
	}

}


