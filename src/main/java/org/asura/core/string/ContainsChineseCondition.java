package org.asura.core.string;

import org.asura.core.util.StringUtil;

public class ContainsChineseCondition implements IStringCondition {
	private static final long serialVersionUID = 7280462409795365661L;

	public boolean meet(String string) {
		string = StringUtil.getStandardString(string);
		return StringUtil.containsChinese(string);
	}
}
