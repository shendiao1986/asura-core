package org.asura.core.string;

import org.asura.core.util.StringUtil;

public class AllChineseCondition implements IStringCondition {
	private static final long serialVersionUID = 4597582104415823885L;

	public boolean meet(String string) {
		string = StringUtil.getStandardString(string);
		return StringUtil.isAllChineseCharacter(string);
	}
}
