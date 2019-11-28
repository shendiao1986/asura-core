package org.asura.core.util;

public class Position {
	private int start;
	private int end;

	public Position() {
	}

	public Position(int start, int end) {
		this.start = start;
		this.end = end;
	}

	public int getStart() {
		return this.start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getEnd() {
		return this.end;
	}

	public void setEnd(int end) {
		this.end = end;
	}

	public boolean beforePosition(Position pos) {
		return (this.start < pos.start);
	}

	public boolean isIncludePosition(Position pos) {
		return ((this.start <= pos.start) && (this.end >= pos.end));
	}

	public boolean isIncludeByPosition(Position pos) {
		return ((this.start >= pos.start) && (this.end <= pos.end));
	}

	public boolean isInclusivePosition(Position pos) {
		return (((this.start <= pos.start) && (this.end >= pos.start))
				|| ((pos.start <= this.start) && (pos.end >= this.start)));
	}

	public int hashCode() {
		return (String.valueOf(this.start) + String.valueOf(this.end)).hashCode();
	}

	public boolean equals(Object obj) {
		Position pos = (Position) obj;
		return ((this.start == pos.start) && (this.end == pos.end));
	}

	public String toString() {
		return "start:" + this.start + "end:" + this.end;
	}
}
