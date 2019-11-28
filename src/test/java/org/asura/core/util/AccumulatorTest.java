package org.asura.core.util;

import org.asura.core.util.collection.Accumulator;
import org.junit.Test;

public class AccumulatorTest {

	@Test
	public void test() {
		System.out.println("2017-01-09".substring("2017-01-09".lastIndexOf("-") + 1));

		for (String s : StringUtil.split("asss", ",")) {
			System.out.println(s);
		}

		Accumulator<String> acc = new Accumulator<>();
		acc.addKey("a", 3);
		acc.addKey("b", 4);
		acc.addKey("c", 5);
		acc.addKey("d", 6);
		acc.addKey("e", 8);
		acc.addKey("f", 8);
		acc.addKey("g", 9);
		for (String key : acc.keysSortedByValue()) {
			System.out.println(key + "-" + acc.getCount(key));
		}

		System.out.println("-----------");

		for (String key : acc.keysSortedByValue(3)) {
			System.out.println(key + "-" + acc.getCount(key));
		}
	}

}
