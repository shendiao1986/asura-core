package org.asura.core.string;

public class StartEndCondition implements IStringCondition {
	private static final long serialVersionUID = 636299047393449058L;
	private IStringCondition start;
	private IStringCondition end;

	public boolean meet(String string) {
		for (int i = 1; i < string.length(); ++i) {
			String s1 = string.substring(0, i);
			String s2 = string.substring(i);

			if ((this.start.meet(s1)) && (this.end.meet(s2))) {
				return true;
			}
		}

		return false;
	}

	public IStringCondition getStart() {
		return this.start;
	}

	public void setStart(IStringCondition start) {
		this.start = start;
	}

	public IStringCondition getEnd() {
		return this.end;
	}

	public void setEnd(IStringCondition end) {
		this.end = end;
	}
}
