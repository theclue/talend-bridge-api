package org.gabrielebaldassarre.tcomponent.bridge;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public final class TalendValueImpl implements TalendValue, Serializable {
	
	private static final long serialVersionUID = 2968836897105515941L;
	private final TalendColumnImpl column;
	private final Object value;

	/**
	 * {@inheritDoc}
	 */
	public TalendValueImpl(TalendColumnImpl column, Object value) throws IllegalArgumentException{
		ResourceBundle rb = ResourceBundle.getBundle("TalendBridge", Locale.getDefault());
		this.column = column;	
		if (value != null) {
            if (!column.getType().equals(TalendType.OBJECT) && value.getClass() != column.getType().getType()) {
                if (value.getClass().equals(String.class)) {
                    value = column.getType().parse((String) value);
                } else {
                	if(column.getType().equals(TalendType.LIST) && !implementsInterface(value, List.class))
                		throw new IllegalArgumentException(String.format(Locale.getDefault(), rb.getString("exception.invalidTalendParameter"), value, column.getType().getType().getSimpleName()));
                }
            }
		}
		
		this.value = value;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public TalendColumn getColumn() {
		return column;
	}
	
	private boolean implementsInterface(final Object object, final Class interf){
	    for (Class<?> c : object.getClass().getInterfaces()) {
	        if (c.equals(interf)) {
	            return true;
	        }
	    }
	    return false;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Object getValue() {
		return value;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean isColumnDefault() {
		return(value == null ? false : (value.equals(column.getDefaultValue())));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString(){
		return (value != null ? value.toString() : null);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode(){
		return ((new Long(serialVersionUID)).hashCode() * 23) + value.hashCode();

	}
	
	/**
	 * {@inheritDoc}
	 */	
	@Override
	public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (obj == this)
            return true;
        if (obj.getClass() != getClass())
            return false;
	    return  value.equals(((TalendValueImpl) obj).getValue());
	  }
	
}
