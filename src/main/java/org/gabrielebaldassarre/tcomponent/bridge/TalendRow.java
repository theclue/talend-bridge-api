package org.gabrielebaldassarre.tcomponent.bridge;

public interface TalendRow {

	TalendFlow getTable();
	
	TalendValue getTalendValue(int index);
	
	TalendValue getTalendValue(String column);
	
	TalendValue[] getTalendValues();
	
	Object getValue(int index);
	
	Object getValue(String column);
	
	void setValue(String column, Object value);
	
	void setValue(TalendColumn column, Object value);
	
	void setValue(int index, Object value);
	
	void setValue(TalendValue value);
	
	int countValues();
	
	boolean isEmpty();
	
	void truncate();
	
	
}
