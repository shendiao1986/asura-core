package org.asura.core.util;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class CombinationUtilTest {

	@Test
	public void testGetCountOfCombinationByWeight() {
		System.out.println(CombinationUtil.getCountOfCombinationByWeight("ab"));
		//System.out.println(CombinationUtil2.getCountOfCombinationByWeight("ab"));
	}

	@Test
	public void testGetLimitedCombination2() {
		List<List<String>> ll = new ArrayList<>();
		List<String> l1 = new ArrayList<>();
		List<String> l2 = new ArrayList<>();
		List<String> l3 = new ArrayList<>();
		l1.add("a");
		l1.add("b");
		l1.add("c");
		l2.add("1");
		l2.add("2");
		l2.add("3");
		l3.add("*");
		l3.add("#");
		l3.add("@");
		ll.add(l1);
		ll.add(l2);
		ll.add(l3);

		ll = CombinationUtil.getLimitedCombination(ll, 5);
		for (List<String> l : ll) {
			System.out.println("=============");
			for (String s : l) {
				System.out.println(s);
			}
			System.out.println("-----------");
		}
	}

	@Test
	public void testGetLimitedCombinationWithinMax2() {
		List<List<String>> ll = new ArrayList<>();
		List<String> l1 = new ArrayList<>();
		List<String> l2 = new ArrayList<>();
		List<String> l3 = new ArrayList<>();
		l1.add("a");
		l1.add("b");
		l1.add("c");
		l2.add("1");
		l2.add("2");
		l2.add("3");
		l3.add("*");
		l3.add("#");
		l3.add("@");
		ll.add(l1);
		ll.add(l2);
		ll.add(l3);

		ll = CombinationUtil.getLimitedCombinationWithinMax(ll, 5);
		for (List<String> l : ll) {
			System.out.println("=============");
			for (String s : l) {
				System.out.println(s);
			}
			System.out.println("-----------");
		}
	}

	@Test
	public void testGetPermutation2() {
		List<List<String>> ll = new ArrayList<>();
		List<String> l1 = new ArrayList<>();
		List<String> l2 = new ArrayList<>();
		List<String> l3 = new ArrayList<>();
		l1.add("a");
		l1.add("b");

		l2.add("1");
		l2.add("2");

		l3.add("*");
		l3.add("#");

		ll.add(l1);
		ll.add(l2);
		ll.add(l3);

		List<List<List<String>>> ll2 = CombinationUtil.getPermutation(ll);
		for (List<List<String>> l : ll2) {
			System.out.println("=============");
			for (List<String> s : l) {
				System.out.println(s.toString() + s.size());
			}
			System.out.println("-----------");
		}
	}

	@Test
	public void testGetCombinationListInt2() {

		List<List<String>> ll = new ArrayList<>();
		List<String> l1 = new ArrayList<>();
		List<String> l2 = new ArrayList<>();
		List<String> l3 = new ArrayList<>();
		l1.add("a");
		l1.add("b");

		l2.add("b");

		l3.add("c");

		ll.add(l1);
		ll.add(l2);
		ll.add(l3);

		List<List<List<String>>> la = CombinationUtil.getCombination(ll, 5);

		List<List<String>> lb = CombinationUtil.getCombination(ll);

		for (List<?> l : la) {
			System.out.println("=============");
			for (Object s : l) {
				System.out.println(s);
			}
			System.out.println("-----------");
		}

		for (List<?> l : lb) {
			System.out.println("=============");
			for (Object s : l) {
				System.out.println(s);
			}
			System.out.println("-----------");
		}
	}

	@Test
	public void testGetCombinationListInt3() {

		List<String> l1 = new ArrayList<>();
		l1.add("a");
		l1.add("b");
		l1.add("c");
		l1.add("d");

		List<List<String>> la = CombinationUtil.getCombination(l1, 2);

		//List<String> lb = CombinationUtil.getCombination(ll);

		for (List<?> l : la) {
			System.out.println("=============");
			for (Object s : l) {
				System.out.println(s);
			}
			System.out.println("-----------");
		}

		/*for (List<?> l : lb) {
			System.out.println("=============");
			for (Object s : l) {
				System.out.println(s);
			}
			System.out.println("-----------");
		}*/
	}
}
