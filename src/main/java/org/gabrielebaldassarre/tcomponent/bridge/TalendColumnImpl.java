package org.gabrielebaldassarre.tcomponent.bridge;

public class TalendColumnImpl implements TalendColumn{

	private TalendFlowImpl table;
	private String name;
	private String comment;
	private TalendValueImpl defaultValue;
	int index;
	private TalendType type;
	
	
	public TalendColumnImpl(TalendFlowImpl table, int index, String name, TalendType type, Object defaultValue, String comment){
		this.table = table;
		this.name = name;
		this.index = index;
		this.type = type;
		this.comment = comment;
		if(defaultValue != null){
			this.defaultValue = new TalendValueImpl(this, defaultValue);
		}
		
	}
	
	public String getName(){
		return name;
	}

	public Object getDefaultValue() {
		return(defaultValue != null ? defaultValue.getValue() : null);
	}

	public TalendType getType() {
		return type;
	}

	public int getIndex() {
		return index;
	}
	
	public TalendFlow getFlow(){
		return table;
	}

	public String getComment() {
		return comment;
	}
	
}
