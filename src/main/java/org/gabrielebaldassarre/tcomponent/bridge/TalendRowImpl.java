package org.gabrielebaldassarre.tcomponent.bridge;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;

public class TalendRowImpl implements TalendRow, TalendBehaviourableRow {

	private TalendFlowImpl table;
	private Map<TalendColumnImpl, TalendValue> valueMap;
	private Map<String, TalendValue> columnvalueMap;

	public TalendRowImpl(TalendFlowImpl table){
		this.table = table;
		this.valueMap = new ConcurrentHashMap<TalendColumnImpl, TalendValue>(table.countColumns());
		this.columnvalueMap = new ConcurrentHashMap<String, TalendValue>(table.countColumns());

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
		//TalendValueImpl val = new TalendValueImpl(table.getColumn(column), value);
		columnvalueMap.put(column.getName(), val);
		valueMap.put(table.getColumn(column), val);
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

		columnvalueMap.put(col.getName(), val);
		valueMap.put(col, val);
		
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
		columnvalueMap.remove(column.getName());
	}

	public TalendRow setValues(Object rowStruct) {
		ResourceBundle rb = ResourceBundle.getBundle("TalendBridge", Locale.getDefault());

		Class<? extends Object> struct = rowStruct.getClass();
		Field[] fields = struct.getFields();
		for(Field f : fields){
			if(!Modifier.isStatic(f.getModifiers())){
				Object value = null;
				String column = null;
				try {
					value = f.get(rowStruct);
					column = f.getName();
				} catch (IllegalArgumentException e) {
					throw new IllegalArgumentException(e);
				} catch (SecurityException e) {
					throw new SecurityException(e);
				} catch (IllegalAccessException e) {}

				if(table.hasColumn(column)){
					TalendType columnClass = table.getColumn(column).getType();
					if(columnClass.equals(TalendType.buildFrom(f.getType()))){
						setValue(column, value);
					} else throw new IllegalArgumentException(String.format(Locale.getDefault(), rb.getString("exception.columnOfWrongType"), column, table.getName(), columnClass.getType().getSimpleName(), f.getType().getSimpleName()));

				}
			}

		}
		
		return this;

	}

	public TalendRow addBehaviour(TalendRowBehaviour b) {
		b.visit(this);
		return this;
	}

}
