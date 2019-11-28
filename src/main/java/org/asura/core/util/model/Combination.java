package org.asura.core.util.model;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

public class Combination<T> {
	public List<List<T>> getCombination(T[] array, int n) {
		List<List<T>> combList = new ArrayList<>();
		int m = array.length;
		if (m < n) {
			throw new IllegalArgumentException("Error   m   <   n");
		}
		BitSet bs = new BitSet(m);
		for (int i = 0; i < n; ++i) {
			bs.set(i, true);
		}
		do {
			printAll(array, bs, combList);
		} while (moveNext(bs, m));

		return combList;
	}

	private boolean moveNext(BitSet bs, int m) {
		int start = -1;
		while (start < m) {
			if (bs.get(++start)) {
				break;
			}
		}
		if (start >= m) {
			return false;
		}
		int end = start;
		while (end < m) {
			if (!(bs.get(++end))) {
				break;
			}
		}

		if (end >= m) {
			return false;
		}
		for (int i = start; i < end; ++i) {
			bs.set(i, false);
		}

		for (int i = 0; i < end - start - 1; ++i) {
			bs.set(i);
		}
		bs.set(end);
		return true;
	}

	private void printAll(T[] array, BitSet bs, List<List<T>> combList) {
		List<T> list = new ArrayList<>();
		for (int i = 0; i < array.length; ++i) {
			if (bs.get(i)) {
				list.add(array[i]);
			}
		}
		combList.add(list);
	}
}
