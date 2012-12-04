package org.gabrielebaldassarre.tcomponent.bridge;

import java.util.Locale;
import java.util.ResourceBundle;

public class TalendValueImpl implements TalendValue {
	
	private TalendColumnImpl column;
	private Object value;

	public TalendValueImpl(TalendColumnImpl column, Object value) throws IllegalArgumentException{
		ResourceBundle rb = ResourceBundle.getBundle("TalendBridge", Locale.getDefault());
		this.column = column;	
		if (value != null) {
            if (value.getClass() != column.getType().getType()) {
                if (value.getClass() == String.class) {
                    value = column.getType().parse((String) value);
                } else {
                	throw new IllegalArgumentException(String.format(Locale.getDefault(), rb.getString("exception.invalidTalendParameter"), value, column.getType().getType().getSimpleName()));
                }
            }
		}
		
		this.value = value;
	}

	public TalendColumn getColumn() {
		return column;
	}

	public Object getValue() {
		return value;
	}

	public boolean isColumnDefault() {
		return(value == null ? false : (value.equals(column.getDefaultValue())));
	}
	
	public String toString(){
		return (value != null ? value.toString() : null);
	}

}
