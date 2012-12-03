/*
	This file is part of Talend Bridge Component API

    Talend Bridge is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Talend Bridge is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Nome-Programma.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.gabrielebaldassarre.tcomponent.bridge;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import java.util.Locale;
import java.util.ResourceBundle;
/**
 * This enum rapresents the whole set of supported datatypes of a column.
 * It's used to bridge between Talend types taxonomy, java primitives
 * and eventually other taxonomies and to build instance of given types.
 * 
 * TODO: Lists are not so well supported yet...
 * 
 * @author Gabriele Baldassarre
 *
 */
public enum TalendType {

	STRING(String.class,  null),
	BOOLEAN(Boolean.class, boolean.class),
	BYTE(Byte.class, byte.class),
	BYTE_ARRAY(byte[].class, null),
	CHARACTER(Character.class, char.class),
	DATE(Date.class, null),
	DOUBLE(Double.class, double.class),
	FLOAT(Float.class, float.class),
	INTEGER(Integer.class, int.class),
	LONG(Long.class, long.class),
	SHORT(Short.class, short.class),
	OBJECT(Object.class, null),
	BIGDECIMAL(BigDecimal.class, null),
	LIST(List.class, null);

	@SuppressWarnings("rawtypes")
	private final Class java;
	@SuppressWarnings("rawtypes")
	private final Class primitive;
	private final String id;

	/**
	 * Build a type from a pair of java nullable type and its
	 * optional primitive wrapper class. The Talend id is build accordingly.
	 * 
	 * @param java the nullable java class of the type
	 * @param primitive the non-nullable primitive wrapper class of the type
	 */
	@SuppressWarnings("rawtypes")
	private TalendType(Class java, Class primitive) {
		this.java = java;
		this.primitive = primitive;
		this.id = "id_" + (java.equals(ArrayList.class) ? "List" : java.getSimpleName());
	}

	/**
	 * Build an instance of TalendType from a given object.
	 * 
	 * @param obj a generic object from which type a TalendType instance is build from
	 * @return a TalendType instance of the right type or null if object is not a supported datatype
	 */
	public static TalendType buildFrom(Object obj) {
		if (obj == null) {
			return null;
		}
		Class<?> c = obj.getClass();

		for (TalendType talendType : TalendType.values()) {
			if (c.equals(talendType.getType())) {
				return talendType;
			}
		}
		return null;
	}

	/**
	 * Return the nullable java Class for current TalendType
	 * 
	 * @return a reference to nullable java Class
	 */
	@SuppressWarnings("rawtypes")
	public Class getType(){
		return java;
	}

	/**
	 * Return the alphanumeric Talend type ID name for the current type
	 * 
	 * @return a Talend type id string
	 */
	public String getId(){
		return id;
	}

	/**
	 * Return the primitive java Class for current TalendType
	 * 
	 * @return a reference to primitive java wrapper Class
	 */    
	@SuppressWarnings("rawtypes")
	public Class getPrimitiveType(){
		return primitive;
	}
	/**
	 * Build a TalendType instance from a given Talend type ID literal (ie. "id_BigDecimal")
	 * 
	 * @param id a literal in the form of Talend type ID
	 * @return a new TalendType instance of the correct type
	 * @throws IllegalArgumentException if the given ID is not valid or unsupported
	 */
	public static TalendType getInstanceFromTalendId(String id) throws IllegalArgumentException {

		ResourceBundle rb = ResourceBundle.getBundle("TalendBridge", Locale.getDefault());

		if(id != null && id.startsWith("id_")){
			if(id.equals("id_String")) return TalendType.STRING;
			if(id.equals("id_Boolean")) return TalendType.BOOLEAN;
			if(id.equals("id_Byte")) return TalendType.BYTE;
			if(id.equals("id_byte[]")) return TalendType.BYTE_ARRAY;
			if(id.equals("id_Character")) return TalendType.CHARACTER;
			if(id.equals("id_Date")) return TalendType.DATE;
			if(id.equals("id_Double")) return TalendType.DOUBLE;
			if(id.equals("id_Float")) return TalendType.FLOAT;
			if(id.equals("id_Integer")) return TalendType.INTEGER;
			if(id.equals("id_Long")) return TalendType.LONG;
			if(id.equals("id_Short")) return TalendType.SHORT;
			if(id.equals("id_Object")) return TalendType.OBJECT;
			if(id.equals("id_List")) return TalendType.LIST;
			if(id.equals("id_BigDecimal")) return TalendType.BIGDECIMAL;

		} 
		throw new IllegalArgumentException(String.format(Locale.getDefault(), rb.getString("exception.invalidTalendType"), id));
	}

	/**
	 * Parse a literal from a given TalendType enum to the correct type
	 * 
	 * 
	 * @param str the value to be parsed
	 * @return the value parsed to target type
	 * @throws IllegalArgumentException if parsing is not possible for the given type
	 */
	public Object parse(String str) throws IllegalArgumentException {

		ResourceBundle rb = ResourceBundle.getBundle("TalendBridge", Locale.getDefault());
		try {
			switch (this) {
			case STRING:
				return str;
			case BYTE:
				return new Byte(removeDecimalDigitsFromString(str));
			case SHORT:
				return new Short(removeDecimalDigitsFromString(str));
			case INTEGER:
				return new Integer(removeDecimalDigitsFromString(str));
			case LONG:
				return new Long(removeDecimalDigitsFromString(str));
			case FLOAT:
				return new Float(str);
			case DOUBLE:
				return new Double(str);
			case BOOLEAN:
				return new Boolean(str);
			case CHARACTER:
				return new Character(str.charAt(0));
			case BIGDECIMAL:
				return new BigDecimal(str);
			default:
				throw new IllegalArgumentException(String.format(Locale.getDefault(), rb.getString("exception.invalidTalendParameter"), str, this.getType().getSimpleName()));
			}
		} catch (NumberFormatException e){
			throw new IllegalArgumentException(String.format(Locale.getDefault(), rb.getString("exception.invalidTalendParameter"), str, this.getType().getSimpleName()));
		}
	}

	/**
	 * Get a list of instances of the given type from a list of strings.
	 * Unparseable elements are stripped down
	 * 
	 * @param elements the elements to parse
	 * @return a list of parsed elements, converted to given TalendType
	 */
	public List<Object> parse(List<String> elements) {
		ArrayList<Object> el = new ArrayList<Object>();
		
		for(String element : elements){
			try {
				el.add(parse(element));
			} catch (NumberFormatException e){}
		}
		el.removeAll(Collections.singleton(null));
		return el;
	}
	/**
	 * Parse a literal from a given TalendType enum to the correct type, considering
	 * input as a single string with all elements separated by a separator.
	 * Unparseable values are removed
	 * 
	 * @param str the input list of values to be parsed
	 * @param separator the separator string
	 * @return a list of value of correct java type
	 */
	public List<Object> parse(String str, String separator) {
		return parse(Arrays.asList(str.split(separator)));
	}
	
	@Override
	public String toString() {
		return java.getSimpleName();
	}

	/**
	 * The name of the enum constant.
	 *
	 * @return the name of the enum constant
	 */
	public String getTypeString() {
		return super.toString();
	}
	
	private String removeDecimalDigitsFromString(String s) {
		return removeDecimalDigitsFromStringPattern.matcher(s).replaceAll("");
	}
	private static final Pattern removeDecimalDigitsFromStringPattern = Pattern.compile("\\.[0-9]*");


}
