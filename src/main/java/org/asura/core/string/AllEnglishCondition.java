package org.asura.core.string;

import org.asura.core.util.StringUtil;

public class AllEnglishCondition implements IStringCondition {
	private static final long serialVersionUID = 8508817617639353984L;

	public boolean meet(String string) {
		string = StringUtil.getStandardString(string);
		return StringUtil.isEnglishString(string);
	}
}
