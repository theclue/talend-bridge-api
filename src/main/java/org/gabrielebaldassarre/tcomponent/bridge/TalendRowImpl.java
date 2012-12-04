package org.gabrielebaldassarre.tcomponent.bridge;

import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;

public class TalendRowImpl implements TalendRow {

	private TalendFlowImpl table;
	private Map<TalendColumnImpl, TalendValueImpl> valueMap;
	private Map<String, TalendValueImpl> columnvalueMap;
	
	public TalendRowImpl(TalendFlowImpl table){
		this.table = table;
		this.valueMap = new ConcurrentHashMap<TalendColumnImpl, TalendValueImpl>(table.countColumns());
		this.columnvalueMap = new ConcurrentHashMap<String, TalendValueImpl>(table.countColumns());
		
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
		return columnvalueMap.get(column);
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

	public void setValue(String column, Object value) {
		setValue(table.getColumn(column), value);
	}

	public void setValue(int index, Object value) throws IllegalArgumentException {
		ResourceBundle rb = ResourceBundle.getBundle("TalendBridge", Locale.getDefault());
		TalendColumn col = table.getColumn(index);
		if(col == null){
			throw new IllegalArgumentException(String.format(Locale.getDefault(), rb.getString("exception.invalidIndex"), table.getName(), index));
		}
		setValue(col, value);

	}

	public void setValue(TalendColumn column, Object value) {
		ResourceBundle rb = ResourceBundle.getBundle("TalendBridge", Locale.getDefault());
		if(table.getColumn(column) == null){
			throw new IllegalArgumentException(String.format(Locale.getDefault(), rb.getString("exception.invalidColumn"), column, table.getName()));
		}
		
		TalendValueImpl val = new TalendValueImpl(table.getColumn(column), value);
		columnvalueMap.put(column.getName(), val);
		valueMap.put(table.getColumn(column), val);
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

	public synchronized void setValue(TalendValue value) throws IllegalStateException {
		ResourceBundle rb = ResourceBundle.getBundle("TalendBridge", Locale.getDefault());
		if(table.getColumn(value.getColumn()) == null){
			throw new IllegalArgumentException(String.format(Locale.getDefault(), rb.getString("exception.invalidColumn"), value.getColumn(), table.getName()));
		}
		TalendColumnImpl col = table.getColumn(value.getColumn());
		TalendValueImpl val = new TalendValueImpl(col, value.getValue());
		
		columnvalueMap.put(col.getName(), val);
		valueMap.put(col, val);

	}
	
	private void init(){
		for(TalendColumnImpl col : table.getColumns()){
				setValue(new TalendValueImpl(col, col.getDefaultValue()));
		}
	}

}
