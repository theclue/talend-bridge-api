package org.gabrielebaldassarre.tcomponent.bridge;

import java.io.Serializable;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;

public class TalendRowImpl implements Serializable, TalendRow, TalendBehaviourableRow {

	private static final long serialVersionUID = 3990672272324520026L;
	private TalendFlowImpl table;
	private Map<TalendColumnImpl, TalendValue> valueMap;
	private Map<TalendColumnImpl, TalendValue> valueDraft;
	
	public TalendRowImpl(TalendFlowImpl table){
		this.table = table;
		this.valueMap = new ConcurrentHashMap<TalendColumnImpl, TalendValue>(table.countColumns());
		this.valueDraft = new ConcurrentHashMap<TalendColumnImpl, TalendValue>(table.countColumns());
		init();
	}

	public TalendFlow getTable() {
		return table;
	}

	public TalendValue getTalendValue(int index) throws IllegalArgumentException {
		ResourceBundle rb = ResourceBundle.getBundle("TalendBridge", Locale.getDefault());
		if(table.countColumns() < index){
			throw new IllegalArgumentException(String.format(Locale.getDefault(), rb.getString("exception.invalidIndex"), table.getName(), index));
		}
		return valueMap.get(table.getColumn(index));
	}

	public TalendValue getTalendValue(String column) throws IllegalArgumentException {
		for(Map.Entry<TalendColumnImpl, TalendValue> item : valueMap.entrySet()){
			if(item.getKey().getName().equals(column)) return item.getValue();
		}
		return null;
	}

	public TalendValue[] getTalendValues() {
		return valueMap.values().toArray(new TalendValue[valueMap.size()]);
	}

	public Object getValue(int index) throws IllegalArgumentException {
		ResourceBundle rb = ResourceBundle.getBundle("TalendBridge", Locale.getDefault());
		TalendColumn col = table.getColumn(index);
		if(col == null){
			throw new IllegalArgumentException(String.format(Locale.getDefault(), rb.getString("exception.invalidIndex"), table.getName(), index));
		}
		TalendValue val = getTalendValue(index);
		return (val == null ? null : val.getValue());
	}

	public Object getValue(String column) throws IllegalArgumentException {
		ResourceBundle rb = ResourceBundle.getBundle("TalendBridge", Locale.getDefault());
		if(table.getColumn(column) == null){
			throw new IllegalArgumentException(String.format(Locale.getDefault(), rb.getString("exception.invalidColumn"), column, table.getName()));
		}
		TalendValue val = getTalendValue(column);
		return (val == null ? null : val.getValue());
	}

	public TalendRow setValue(String column, Object value) {
		setValue(table.getColumn(column), value);
		return this;
	}

	public TalendRow setValue(int index, Object value) throws IllegalArgumentException {
		ResourceBundle rb = ResourceBundle.getBundle("TalendBridge", Locale.getDefault());
		TalendColumn col = table.getColumn(index);
		if(col == null){
			throw new IllegalArgumentException(String.format(Locale.getDefault(), rb.getString("exception.invalidIndex"), table.getName(), index));
		}
		setValue(col, value);
		return this;

	}

	public TalendRow setValue(TalendColumn column, Object value) {
		ResourceBundle rb = ResourceBundle.getBundle("TalendBridge", Locale.getDefault());
		if(table.getColumn(column) == null){
			throw new IllegalArgumentException(String.format(Locale.getDefault(), rb.getString("exception.invalidColumn"), column.getName(), table.getName()));
		}
		TalendValue val = table.getFactory().newValue(table.getColumn(column), value);
		
		setValue(val);

		return this;
	}

	public int countValues() {
		return valueMap.size();
	}

	public boolean isEmpty() {
		return valueMap.isEmpty();
	}

	public void truncate() {
		init();
	}

	public synchronized TalendRow setValue(TalendValue value) throws IllegalStateException {
		ResourceBundle rb = ResourceBundle.getBundle("TalendBridge", Locale.getDefault());
		if(table.getColumn(value.getColumn()) == null){
			throw new IllegalArgumentException(String.format(Locale.getDefault(), rb.getString("exception.invalidColumn"), value.getColumn(), table.getName()));
		}
		
		TalendColumnImpl col = table.getColumn(value.getColumn());
		
		TalendValueImpl val = new TalendValueImpl(col, value.getValue());
		valueDraft.put(col, val);
		
		return this;
	}

	private void init(){
		for(TalendColumnImpl col : table.getColumns()){
			setValue(new TalendValueImpl(col, col.getDefaultValue()));
		}
	}

	public String toString(){
		String values = "{TalendRow flow=" + table.getName();

		for(Map.Entry<TalendColumnImpl, TalendValue> item : valueMap.entrySet()){
			values += ", " + item.getKey().getName() + "=" + (item.getValue().getValue() != null && item.getKey().getType() == TalendType.STRING ? "\'" : "") + item.getValue().getValue() + (item.getValue().getValue() != null && item.getKey().getType() == TalendType.STRING ? "\'" : "") + " (" + item.getKey().getType() + ")";
		}
		values += "}";
		return values;

	}

	public void removeColumn(TalendColumnImpl column){
		valueMap.remove(column);
	}

	public TalendRow addBehaviour(TalendRowBehaviour b) {
		b.visit(this);
		return this;
	}

	public TalendRow save() {
		for(Map.Entry<TalendColumnImpl, TalendValue> item : valueDraft.entrySet()){
			if(item.getValue().getValue() == null){
				valueMap.remove(item.getKey());
			} else {
				valueMap.put(item.getKey(), item.getValue());
			}
		}
		valueDraft.clear();

		return this;
	}

	public boolean isChanged() {
		return valueDraft.size() > 0 ? true : false;
	}

	public void discardChanges() {
		valueDraft.clear();
		
	}

}
