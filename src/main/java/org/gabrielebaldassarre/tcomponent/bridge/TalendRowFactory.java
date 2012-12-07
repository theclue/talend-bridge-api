package org.gabrielebaldassarre.tcomponent.bridge;

public interface TalendRowFactory {

	public TalendRow newRow(String table);
	
	public TalendRow newRow(TalendFlow table);
	
	public TalendRow newRow(String table, TalendRowBridgeBehaviour values);
	
	public TalendRow newRow(TalendFlow table, TalendRowBridgeBehaviour values);	
}
