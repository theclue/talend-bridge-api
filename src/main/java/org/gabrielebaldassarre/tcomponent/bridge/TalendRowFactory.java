package org.gabrielebaldassarre.tcomponent.bridge;

public interface TalendRowFactory {

	public TalendRow newRow(String table);
	
	public TalendRow newRow(TalendFlow table);
	
	public TalendRow newRow(String table, Object values);
	
	public TalendRow newRow(TalendFlow table, Object values);	
}
