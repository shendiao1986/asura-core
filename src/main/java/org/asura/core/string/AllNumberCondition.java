package org.asura.core.string;

import org.asura.core.util.StringUtil;

public class AllNumberCondition implements IStringCondition {
	private static final long serialVersionUID = 2845407160413310741L;

	public boolean meet(String string) {
		string = StringUtil.getStandardString(string);
		return StringUtil.isNumberString(string);
	}
}
