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
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This is the concrete implementation of a row and therefore should not be used in normal circumnstances.
 * You should build instances using {@link TalendRowFactory} instead and interface with flow using {@link TalendRow} interface.
 * 
 * @author Gabriele Baldassarre
 * @see TalendRowFactory
 * @see TalendRow
 */
public class TalendRowImpl implements Serializable, TalendRow, TalendBehaviourableRow, Cloneable {

	private static final long serialVersionUID = 3990672272324520026L;
	private TalendFlowImpl table;
	private Map<TalendColumnImpl, TalendValue> valueMap;
	private Map<TalendColumnImpl, TalendValue> valueDraft;
	private boolean autosave;
	public boolean presentInTable;
	
	/**
	 * {@inheritDoc}
	 */
	public TalendRowImpl(TalendFlowImpl table, boolean autosave){
		this.table = table;
		this.valueMap = new ConcurrentHashMap<TalendColumnImpl, TalendValue>(table.countColumns());
		this.valueDraft = new ConcurrentHashMap<TalendColumnImpl, TalendValue>(table.countColumns());
		this.autosave = autosave;
		this.presentInTable = !table.supportsTransactions();
		init();
	}

	/**
	 * {@inheritDoc}
	 */
	public TalendFlow getTable() {
		return table;
	}

	/**
	 * {@inheritDoc}
	 */
	public TalendValue getTalendValue(int index) throws IllegalArgumentException {
		ResourceBundle rb = ResourceBundle.getBundle("TalendBridge", Locale.getDefault());
		if(table.countColumns() < index){
			throw new IllegalArgumentException(String.format(Locale.getDefault(), rb.getString("exception.invalidIndex"), table.getName(), index));
		}
		return valueMap.get(table.getColumn(index));
	}

	/**
	 * {@inheritDoc}
	 */
	public TalendValue getTalendValue(String column) throws IllegalArgumentException {
		ResourceBundle rb = ResourceBundle.getBundle("TalendBridge", Locale.getDefault());
		if(table.getColumn(column) == null){
			throw new IllegalArgumentException(String.format(Locale.getDefault(), rb.getString("exception.invalidColumn"), column, table.getName()));
		}
		for(Map.Entry<TalendColumnImpl, TalendValue> item : valueMap.entrySet()){
			if(item.getKey().getName().equals(column)) return item.getValue();
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public TalendValue[] getTalendValues() {
		return valueMap.values().toArray(new TalendValue[valueMap.size()]);
	}

	/**
	 * {@inheritDoc}
	 */
	public Object getValue(int index) throws IllegalArgumentException {
		ResourceBundle rb = ResourceBundle.getBundle("TalendBridge", Locale.getDefault());
		TalendColumn col = table.getColumn(index);
		if(col == null){
			throw new IllegalArgumentException(String.format(Locale.getDefault(), rb.getString("exception.invalidIndex"), table.getName(), index));
		}
		TalendValue val = getTalendValue(index);
		return (val == null ? null : val.getValue());
	}

	/**
	 * {@inheritDoc}
	 */
	public Object getValue(String column) throws IllegalArgumentException {
		ResourceBundle rb = ResourceBundle.getBundle("TalendBridge", Locale.getDefault());
		if(table.getColumn(column) == null){
			throw new IllegalArgumentException(String.format(Locale.getDefault(), rb.getString("exception.invalidColumn"), column, table.getName()));
		}
		TalendValue val = getTalendValue(column);
		return (val == null ? null : val.getValue());
	}

	/**
	 * {@inheritDoc}
	 */
	public TalendRow setValue(String column, Object value, boolean save) {
		ResourceBundle rb = ResourceBundle.getBundle("TalendBridge", Locale.getDefault());
		if(table.getColumn(column) == null){
			throw new IllegalArgumentException(String.format(Locale.getDefault(), rb.getString("exception.invalidColumn"), column, table.getName()));
		}
		setValue(table.getColumn(column), value,save);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	public TalendRow setValue(int index, Object value, boolean save) throws IllegalArgumentException {
		ResourceBundle rb = ResourceBundle.getBundle("TalendBridge", Locale.getDefault());
		TalendColumn col = table.getColumn(index);
		if(col == null){
			throw new IllegalArgumentException(String.format(Locale.getDefault(), rb.getString("exception.invalidIndex"), table.getName(), index));
		}
		setValue(col, value, save);
		return this;

	}

	/**
	 * {@inheritDoc}
	 */
	public TalendRow setValue(TalendColumn column, Object value, boolean save) {
		ResourceBundle rb = ResourceBundle.getBundle("TalendBridge", Locale.getDefault());
		if(table.getColumn(column) == null || !column.getFlow().equals(table)){
			throw new IllegalArgumentException(String.format(Locale.getDefault(), rb.getString("exception.invalidColumn"), column.getName(), table.getName()));
		}
		
		if(presentInTable == true && column.isKey()){
			throw new IllegalStateException(String.format(Locale.getDefault(), rb.getString("exception.cannotAlterKeyValues"), column.getName(), table.getName()));
		}
		
		TalendValue val = table.getFactory().newValue(table.getColumn(column), value);
		
		setValue(val, save);

		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	public int countValues() {
		return valueMap.size();
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isEmpty() {
		return valueMap.isEmpty();
	}

	/**
	 * {@inheritDoc}
	 */
	public void truncate() {
		init();
	}

	/**
	 * {@inheritDoc}
	 */
	public synchronized TalendRow setValue(TalendValue value, boolean save) throws IllegalStateException {
		ResourceBundle rb = ResourceBundle.getBundle("TalendBridge", Locale.getDefault());
		if(table.getColumn(value.getColumn()) == null){
			throw new IllegalArgumentException(String.format(Locale.getDefault(), rb.getString("exception.invalidColumn"), value.getColumn(), table.getName()));
		}
		
		TalendColumnImpl col = table.getColumn(value.getColumn());
		
		TalendValueImpl val = new TalendValueImpl(col, value.getValue());
		if(save == true){
			mapValues(valueMap, col, val);
		} else {
			mapValues(valueDraft, col, val);
		}
		
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	private void init(){
		for(TalendColumnImpl col : table.getColumns()){
			setValue(new TalendValueImpl(col, col.getDefaultValue()), true);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public String toString(){
		String values = "{TalendRow flow=" + table.getName();

		for(Map.Entry<TalendColumnImpl, TalendValue> item : valueMap.entrySet()){
			values += ", " + item.getKey().getName() + "=" + (item.getValue().getValue() != null && item.getKey().getType() == TalendType.STRING ? "\'" : "") + item.getValue().getValue() + (item.getValue().getValue() != null && item.getKey().getType() == TalendType.STRING ? "\'" : "") + " (" + item.getKey().getType() + (item.getKey().isKey() == true ? " - PK" : "") + ")";
		}
		values += "}";
		return values;

	}

	/**
	 * {@inheritDoc}
	 */
	public void removeColumn(TalendColumnImpl column){
		valueMap.remove(column);
	}

	/**
	 * {@inheritDoc}
	 */
	public TalendRow addBehaviour(TalendRowBehaviour b) {
		b.visit(this);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	public TalendRow save() {
		if(autosave == true) return this;

		ResourceBundle rb = ResourceBundle.getBundle("TalendBridge", Locale.getDefault());
		
		if(getKeySet().size() < table.getKeyColumns().length){
			throw new IllegalStateException(String.format(Locale.getDefault(), rb.getString("exception.cannotSaveRow"), table.getName()));
		}
		
		for(Map.Entry<TalendColumnImpl, TalendValue> item : valueDraft.entrySet()){
			mapValues(valueMap, item.getKey(), item.getValue());
		}
		valueDraft.clear();
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isChanged() {
		return autosave == true ? false : (valueDraft.size() > 0 ? true : false);
	}

	/**
	 * {@inheritDoc}
	 */
	public void discardChanges() {
		valueDraft.clear();
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TalendRow setValue(String column, Object value) {
		return setValue(column, value, this.autosave);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TalendRow setValue(TalendColumn column, Object value) {
		return setValue(column, value, this.autosave);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TalendRow setValue(int index, Object value) {
		return setValue(index, value, this.autosave);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TalendRow setValue(TalendValue value) {
		return setValue(value, this.autosave);
	}
	
	private void mapValues(Map<TalendColumnImpl, TalendValue> valueMap, TalendColumnImpl key, TalendValue value){
		if(value.getValue() == null){
			valueMap.remove(key);
		} else {
			valueMap.put(key,  value);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean supportTransactions() {
		return !autosave;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Map<TalendColumn, TalendValue> getKeySet(){
		
		Map<TalendColumn, TalendValue> keycolumnbuffer = new ConcurrentHashMap<TalendColumn, TalendValue>();
		
		for(Map.Entry<TalendColumnImpl, TalendValue> item : valueMap.entrySet()){
			if(item.getKey().isKey() && item.getValue().getValue() != null) keycolumnbuffer.put(item.getKey(), item.getValue());
		}

		for(Map.Entry<TalendColumnImpl, TalendValue> item : valueDraft.entrySet()){
			if(item.getKey().isKey() && item.getValue().getValue() != null) keycolumnbuffer.put(item.getKey(), item.getValue());
		}		

		return keycolumnbuffer;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public TalendRow clone(){
		TalendRow cloned = table.getFactory().newRow(table);
		
		Map<TalendColumnImpl, TalendValue> clonedValues = new ConcurrentHashMap<TalendColumnImpl, TalendValue>(valueMap);
		clonedValues.putAll(valueDraft);
		
		for(Map.Entry<TalendColumnImpl, TalendValue> item : clonedValues.entrySet()){
			cloned.setValue(item.getValue());
		}
		return cloned;
	}

}
