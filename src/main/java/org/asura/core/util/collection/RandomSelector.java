package org.asura.core.util.collection;

import java.util.ArrayList;
import java.util.List;

import org.asura.core.util.math.RandomUtil;

public class RandomSelector<T> {
	public List<T> select(List<T> list, int count) {
		List<T> result = new ArrayList<T>();

		List<Integer> ids = new ArrayList<Integer>();
		for (int i = 0; i < list.size(); ++i) {
			ids.add(Integer.valueOf(i));
		}

		while (result.size() < Math.min(count, list.size())) {
			int i = ((Integer) ids.get(RandomUtil.random(0, ids.size() - 1))).intValue();
			result.add(list.get(i));

			ids.remove(Integer.valueOf(i));
		}

		return result;
	}
}