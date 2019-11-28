package org.asura.core.util;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class TestSoftHashMap {

	@Test
	public void test() throws Exception {
		testMap(new HashMap<String, Integer>());
		testMap(new SoftHashMap<String, Integer>());
	}

	private static void testMap(Map<String, Integer> map) throws InterruptedException {
		System.out.println("Testing " + map.getClass());
		map.put("One", new Integer(1));
		map.put("Two", new Integer(2));
		map.put("Three", new Integer(3));
		map.put("Four", new Integer(4));
		map.put("Five", new Integer(5));
		print(map);
		Thread.sleep(2000);
		print(map);
		try {
			byte[] block = new byte[2000 * 1024 * 1024];
			System.out.println(block.length);
		} catch (OutOfMemoryError ex) {
			//ex.printStackTrace();
		}
		print(map);
	}

	private static void print(Map<String, Integer> map) {
		System.out.println("One=" + map.get("One"));
		System.out.println("Two=" + map.get("Two"));
		System.out.println("Three=" + map.get("Three"));
		System.out.println("Four=" + map.get("Four"));
		System.out.println("Five=" + map.get("Five"));
	}

}
