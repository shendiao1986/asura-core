package org.asura.core.util.model;

public class WordDistance {
	private String s1;
	private String s2;
	private String sentence;
	private int position1;
	private int position2;
	private int distance;

	public String getS1() {
		return this.s1;
	}

	public void setS1(String s1) {
		this.s1 = s1;
	}

	public String getS2() {
		return this.s2;
	}

	public void setS2(String s2) {
		this.s2 = s2;
	}

	public String getSentence() {
		return this.sentence;
	}

	public void setSentence(String sentence) {
		this.sentence = sentence;
	}

	public int getPosition1() {
		return this.position1;
	}

	public void setPosition1(int position1) {
		this.position1 = position1;
	}

	public int getPosition2() {
		return this.position2;
	}

	public void setPosition2(int position2) {
		this.position2 = position2;
	}

	public int getDistance() {
		return this.distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public String getFrontWord() {
		if (this.position1 <= this.position2) {
			return this.s1;
		}
		return this.s2;
	}
}
