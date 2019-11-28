package org.asura.core.data;

import java.io.Serializable;
import java.util.HashMap;

import org.apache.commons.lang.StringUtils;

public class DataRecord implements Serializable {
	private static final long serialVersionUID = -1070543050853190777L;
	private HashMap<String, Object> dataMap;
	private DataAction dataAction;

	public DataRecord() {
		this.dataMap = new HashMap<>();
		this.dataAction = DataAction.Add;
	}

	public String[] getAllFields() {
		return ((String[]) this.dataMap.keySet().toArray(new String[0]));
	}

	public String getFieldValue(String field) {
		if ((!(this.dataMap.containsKey(field))) || (this.dataMap.get(field) == null)) {
			return "";
		}
		return this.dataMap.get(field).toString();
	}

	public Object getFieldObject(String field) {
		return this.dataMap.get(field);
	}

	public void deleteField(String field) {
		this.dataMap.remove(field);
	}

	public boolean containsField(String field) {
		return this.dataMap.containsKey(field);
	}

	public void setAction(DataAction action) {
		this.dataAction = action;
	}

	public void AddField(String field, String value) {
		this.dataMap.put(field, value);
	}

	public void AddField(String field, Object value) {
		this.dataMap.put(field, value);
	}

	public DataAction getDataAction() {
		String value = getFieldValue("action");
		if (value != null)
			try {
				value = StringUtils.capitalize(value);
				setAction(DataAction.valueOf(value));
			} catch (Exception localException) {
			}
		return this.dataAction;
	}

	public String toString() {
		return this.dataMap.toString();
	}

	@SuppressWarnings("unchecked")
	public DataRecord clone() {
		DataRecord dr = new DataRecord();
		dr.dataMap = ((HashMap<String, Object>) this.dataMap.clone());
		dr.dataAction = this.dataAction;

		return dr;
	}

	public static enum DataAction {
		Add, Update, Delete;
	}
}
