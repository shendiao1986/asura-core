package org.asura.core.util.math.matrix;

import java.util.Date;

public class Vote {
	private String id1;
	private String id2;
	private double percent;
	private Date date;

	public Vote(String id1, String id2) {
		this.id1 = id1;
		this.id2 = id2;
		this.percent = 1.0D;
		this.date = new Date();
	}

	public Vote(String id1, String id2, double percent) {
		this.id1 = id1;
		this.id2 = id2;
		this.percent = percent;
		this.date = new Date();
	}

	public Vote(String id1, String id2, double percent, Date date) {
		this.id1 = id1;
		this.id2 = id2;
		this.percent = percent;
		this.date = date;
	}

	public String getId1() {
		return this.id1;
	}

	public void setId1(String id1) {
		this.id1 = id1;
	}

	public String getId2() {
		return this.id2;
	}

	public void setId2(String id2) {
		this.id2 = id2;
	}

	public double getPercent() {
		return this.percent;
	}

	public void setPercent(double percent) {
		this.percent = percent;
	}

	public Date getDate() {
		return this.date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String toString() {
		return this.id1 + "->" + this.id2 + ":" + this.percent;
	}
}
