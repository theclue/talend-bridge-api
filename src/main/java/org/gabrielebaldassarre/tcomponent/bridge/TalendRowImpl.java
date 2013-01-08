package org.gabrielebaldassarre.tcomponent.bridge;

import java.io.Serializable;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;

public class TalendRowImpl implements Serializable, TalendRow, TalendBehaviourableRow, Cloneable {

	private static final long serialVersionUID = 3990672272324520026L;
	private TalendFlowImpl table;
	private Map<TalendColumnImpl, TalendValue> valueMap;
	private Map<TalendColumnImpl, TalendValue> valueDraft;
	private boolean autosave;
	public boolean presentInTable;
	
	public TalendRowImpl(TalendFlowImpl table, boolean autosave){
		this.table = table;
		this.valueMap = new ConcurrentHashMap<TalendColumnImpl, TalendValue>(table.countColumns());
		this.valueDraft = new ConcurrentHashMap<TalendColumnImpl, TalendValue>(table.countColumns());
		this.autosave = autosave;
		this.presentInTable = !table.supportsTransactions();
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

	public TalendRow setValue(String column, Object value, boolean save) {
		ResourceBundle rb = ResourceBundle.getBundle("TalendBridge", Locale.getDefault());
		if(table.getColumn(column) == null){
			throw new IllegalArgumentException(String.format(Locale.getDefault(), rb.getString("exception.invalidColumn"), column, table.getName()));
		}
		setValue(table.getColumn(column), value,save);
		return this;
	}

	public TalendRow setValue(int index, Object value, boolean save) throws IllegalArgumentException {
		ResourceBundle rb = ResourceBundle.getBundle("TalendBridge", Locale.getDefault());
		TalendColumn col = table.getColumn(index);
		if(col == null){
			throw new IllegalArgumentException(String.format(Locale.getDefault(), rb.getString("exception.invalidIndex"), table.getName(), index));
		}
		setValue(col, value, save);
		return this;

	}

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

	public int countValues() {
		return valueMap.size();
	}

	public boolean isEmpty() {
		return valueMap.isEmpty();
	}

	public void truncate() {
		init();
	}

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

	private void init(){
		for(TalendColumnImpl col : table.getColumns()){
			setValue(new TalendValueImpl(col, col.getDefaultValue()), true);
		}
	}

	public String toString(){
		String values = "{TalendRow flow=" + table.getName();

		for(Map.Entry<TalendColumnImpl, TalendValue> item : valueMap.entrySet()){
			values += ", " + item.getKey().getName() + "=" + (item.getValue().getValue() != null && item.getKey().getType() == TalendType.STRING ? "\'" : "") + item.getValue().getValue() + (item.getValue().getValue() != null && item.getKey().getType() == TalendType.STRING ? "\'" : "") + " (" + item.getKey().getType() + (item.getKey().isKey() == true ? " - PK" : "") + ")";
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

	public boolean isChanged() {
		return autosave == true ? false : (valueDraft.size() > 0 ? true : false);
	}

	public void discardChanges() {
		valueDraft.clear();
		
	}

	@Override
	public TalendRow setValue(String column, Object value) {
		return setValue(column, value, this.autosave);
	}

	@Override
	public TalendRow setValue(TalendColumn column, Object value) {
		return setValue(column, value, this.autosave);
	}

	@Override
	public TalendRow setValue(int index, Object value) {
		return setValue(index, value, this.autosave);
	}

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

	@Override
	public boolean supportTransactions() {
		return !autosave;
	}
	
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
