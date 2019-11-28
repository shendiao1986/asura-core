package org.asura.core.util.math;

import java.util.HashSet;

public class RandomUtil {
	public static int random(int min, int max) {
		int delta = max - min + 1;
		double step = 1.0D / Double.valueOf(delta).doubleValue();
		double ran = Math.random();
		if (ran == 1.0D) {
			ran = 0.99999999D;
		}

		return (Double.valueOf(ran / step).intValue() + min);
	}

	public static int[] random(int min, int max, int count) {
		HashSet<Integer> set = new HashSet<>();
		for (int i = 0; i < count * 2; ++i) {
			int r = random(min, max);
			set.add(Integer.valueOf(r));
			if (set.size() >= count) {
				break;
			}
		}
		int[] is = new int[set.size()];
		for (int i = 0; i < is.length; ++i) {
			is[i] = ((Integer[]) set.toArray(new Integer[0]))[i].intValue();
		}

		return is;
	}
}
