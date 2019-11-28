package org.asura.core.util;

import org.asura.core.util.cache.SimpleCache;
import org.junit.Test;

public class TestSimpleCache {

	@Test
	public void test() {
		SimpleCache<String, String> cache = new SimpleCache<>(3);
		cache.cache("a", "a");
		System.out.println(cache.get("a"));
		cache.cache("b", "b");
		System.out.println(cache.get("a"));
		cache.cache("c", "c");
		System.out.println(cache.get("a"));
		cache.cache("d", "d");
		System.out.println(cache.get("a"));
		System.out.println(cache.get("b"));
		System.out.println(cache.get("c"));
		System.out.println(cache.get("d"));
	}

}
