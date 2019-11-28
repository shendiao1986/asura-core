package org.asura.core.util.math.matrix;

import java.util.HashMap;

public class UnitValue {
	private HashMap<String, Double> map;
	private HashMap<String, Double> lastMap;
	private int count;
	private int maxCount;
	private HashMap<String, Double> min;
	private double minAv;

	public UnitValue() {
		this(10000);
	}

	public UnitValue(int maxCount) {
		this.count = 0;
		this.maxCount = maxCount;

		this.map = new HashMap<>();
		this.lastMap = new HashMap<>();
		this.min = new HashMap<>();
		this.minAv = 2147483647.0D;
	}

	public double getValue(String id) {
		if (this.map.containsKey(id)) {
			return ((Double) this.map.get(id)).doubleValue();
		}
		return -1.0D;
	}

	public double getLastValue(String id) {
		if (this.map.containsKey(id))
			return ((Double) this.map.get(id)).doubleValue();
		if (this.lastMap.containsKey(id)) {
			return ((Double) this.lastMap.get(id)).doubleValue();
		}
		return -1.0D;
	}

	public void addValue(String id, double value) {
		this.map.put(id, Double.valueOf(value));
	}

	@SuppressWarnings("unchecked")
	public void startNewRound() {
		this.lastMap = ((HashMap<String, Double>) this.map.clone());

		this.map.clear();
	}

	public String[] getIds() {
		return ((String[]) this.map.keySet().toArray(new String[0]));
	}

	@SuppressWarnings("unchecked")
	public boolean isReady(double diff) {
		if (this.count > this.maxCount) {
			return true;
		}

		this.count += 1;

		double total = 0.0D;

		boolean r = true;
		for (String key : this.map.keySet()) {
			Double lastValue = (Double) this.lastMap.get(key);
			if ((lastValue == null) || (lastValue.doubleValue() < 0.0D)) {
				lastValue = Double.valueOf(0.0D);
			}
			double value = Math.abs(lastValue.doubleValue() - ((Double) this.map.get(key)).doubleValue());
			total += value;

			if (value > diff) {
				r = false;
			}

			if (value > 10000000.0D) {
				this.map.clear();
				this.min = null;

				System.out.println("compute failed.");
				return true;
			}
		}

		if (r) {
			this.min = null;

			return true;
		}
		double av = total / Double.valueOf(this.map.size()).doubleValue();
		if (this.minAv > av) {
			this.minAv = av;
			this.min = ((HashMap<String, Double>) this.map.clone());
		}

		return false;
	}

	public String getDiff() {
		double total = 0.0D;
		double max = 0.0D;
		double maxLast = 0.0D;
		for (String key : this.map.keySet()) {
			Double lastValue = (Double) this.lastMap.get(key);
			if ((lastValue == null) || (lastValue.doubleValue() < 0.0D)) {
				lastValue = Double.valueOf(0.0D);
			}
			double value = Math.abs(lastValue.doubleValue() - ((Double) this.map.get(key)).doubleValue());
			total += value;
			if (value > max) {
				max = value;
				maxLast = lastValue.doubleValue();
			}
		}

		return "max:" + max + ", lastValue:" + maxLast + ", average:"
				+ (total / Double.valueOf(this.map.size()).doubleValue());
	}

	public String toString() {
		if (this.min == null) {
			return this.map.toString();
		}
		return this.min.toString();
	}
}
