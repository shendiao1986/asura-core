package org.asura.core.util.machine;

public class MachineResource {
	private double total;
	private double used;
	private String desc;
	private String unit;

	public double getTotal() {
		return this.total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public double getUsed() {
		return this.used;
	}

	public void setUsed(double used) {
		this.used = used;
	}

	public String getDesc() {
		return this.desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getUnit() {
		return this.unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String toString() {
		return this.desc + ": use " + this.used + this.unit + " of " + this.total + this.unit;
	}
}
