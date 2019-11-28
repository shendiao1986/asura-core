package org.asura.core.util;

import org.asura.core.util.math.NumberUtil;
import org.junit.Test;

public class NumberUtilTest {

	@Test
	public void test() {
		System.out.println(NumberUtil.getLenedDoubleValue(2.1544d, 2));
		System.out.println(NumberUtil.getLenedDoubleValue(2.1562d, 2));
		System.out.println(NumberUtil.getLenedDoubleValue(2.1552d, 2));
	}

}
