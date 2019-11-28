package org.asura.core.cjf;

public interface ChineseJF {
	public static final int cashSize = 2000;

	public boolean initialized();

	public void init();

	public void free();

	public String chineseFan2Jan(String paramString);
}
