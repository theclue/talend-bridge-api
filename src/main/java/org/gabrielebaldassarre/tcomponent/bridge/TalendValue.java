package org.gabrielebaldassarre.tcomponent.bridge;

public interface TalendValue {
	
	public TalendColumn getColumn();
	
	public Object getValue();
	
	public boolean isColumnDefault();

}
