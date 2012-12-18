package org.gabrielebaldassarre.tcomponent.bridge;

public interface TalendRow {

	TalendFlow getTable();
	
	TalendValue getTalendValue(int index);
	
	TalendValue getTalendValue(String column);
	
	TalendValue[] getTalendValues();
	
	Object getValue(int index);
	
	Object getValue(String column);
	
	TalendRow setValue(String column, Object value);
	
	TalendRow setValue(TalendColumn column, Object value);
	
	TalendRow setValue(int index, Object value);
	
	TalendRow setValue(TalendValue value);
	
	int countValues();
	
	boolean isEmpty();
	
	void truncate();
	
	TalendRow save();
	
	void discardChanges();
	
	boolean isChanged();
	
	
}
