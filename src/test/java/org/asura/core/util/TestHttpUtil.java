package org.asura.core.util;

import org.junit.Test;

public class TestHttpUtil {

	@Test
	public void test() {
		System.out.println(HttpUtil.fetchConent("http://www.baidu.com", "utf-8"));
	}

}
