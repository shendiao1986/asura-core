package org.asura.core.string;

public class NotStringCondition implements IStringCondition {
	private static final long serialVersionUID = -8509986932625311477L;
	private IStringCondition condition;

	public boolean meet(String string) {
		return (!(this.condition.meet(string)));
	}

	public IStringCondition getCondition() {
		return this.condition;
	}

	public void setCondition(IStringCondition condition) {
		this.condition = condition;
	}
}