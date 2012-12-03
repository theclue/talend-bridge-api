package org.gabrielebaldassarre.tcomponent.bridge;

public interface TalendRowFactory {

	public TalendRow newRow(String table);
	
	public TalendRow newRow(TalendFlow table);
}
