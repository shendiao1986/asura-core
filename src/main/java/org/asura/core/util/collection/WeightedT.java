package org.asura.core.util.collection;

public class WeightedT<T> {
	private T weight;
	private double value;

	public T getWeight() {
		return this.weight;
	}

	public void setWeight(T weight) {
		this.weight = weight;
	}

	public double getValue() {
		return this.value;
	}

	public void setValue(double value) {
		this.value = value;
	}
}
