package org.asura.core.cjf.entity;

import java.io.Serializable;

public class Char implements Serializable {
	private static final long serialVersionUID = -3712732188408051103L;
	public int jId = 0;

	public char jChar = '\0';

	public int fId = 0;

	public char fChar = '\0';

	public Char(char jChar, char fChar) {
		this.jId = jChar;
		this.jChar = jChar;
		this.fId = fChar;
		this.fChar = fChar;
	}

	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		return ((obj instanceof Char) && (((Char) obj).jId == this.jId) && (((Char) obj).fId == this.fId));
	}

	public int hashCode() {
		return (this.jId + this.fId);
	}

	public String toString() {
		return this.jChar + ":" + this.fChar;
	}
}
