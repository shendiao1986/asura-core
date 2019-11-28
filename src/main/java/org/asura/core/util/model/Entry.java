package org.asura.core.util.model;

public class Entry<T> {
	private T object;
	private int position;

	public Entry(T object, int position) {
		this.object = object;
		this.position = position;
	}

	public T getObject() {
		return this.object;
	}

	public void setObject(T object) {
		this.object = object;
	}

	public int getPosition() {
		return this.position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	@SuppressWarnings("unchecked")
	public boolean eqauls(Object obj) {
		Entry<T> entry = (Entry<T>) obj;
		return ((entry.getObject().equals(this.object)) && (entry.position == this.position));
	}

	public int hashCode() {
		return this.position;
	}

	public String toString() {
		return "[" + this.object + ", " + this.position + "]";
	}
}
