package org.asura.core.util.model;

public class WordPair {
	private String part_1;
	private String part_2;

	public WordPair(String p1, String p2) {
		this.part_1 = p1;
		this.part_2 = p2;
	}

	public String getPart_1() {
		return this.part_1;
	}

	public void setPart_1(String part_1) {
		this.part_1 = part_1;
	}

	public String getPart_2() {
		return this.part_2;
	}

	public void setPart_2(String part_2) {
		this.part_2 = part_2;
	}
}
