package org.asura.core.cjf;

import org.asura.core.cjf.imp.ChineseJFImpl;

public class CJFBeanFactory {
	private static ChineseJF chineseJF = new ChineseJFImpl();

	public static ChineseJF getChineseJF() {
		return chineseJF;
	}
}
