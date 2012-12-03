package org.gabrielebaldassarre.tcomponent.bridge;

public interface TalendColumn {

	public String getName();
	
	public Object getDefaultValue();
	
	public TalendType getType();
	
	public int getIndex();
	
	public TalendFlow getFlow();
	
	public String getComment();
}
