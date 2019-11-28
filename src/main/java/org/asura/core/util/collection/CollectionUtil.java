package org.asura.core.util.collection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CollectionUtil {
	public static <T> void sort(List<T> list, Comparator<T> comparator) {
		Collections.sort(list, comparator);
	}

	public static <T extends Comparable<? super T>> void sort(List<T> list) {
		Collections.sort(list);
	}

	public static <T extends IWeightable> List<T> getTop(List<T> list, int size) {
		double minMax = 0.0D;
		IWeightable minMaxT = null;
		List<T> result = new ArrayList<>();
		for (T t : list) {
			boolean bigger = false;
			if (minMax < t.getWeight()) {
				minMax = t.getWeight();
				bigger = true;
			}
			if (result.size() < size) {
				result.add(t);
				WeightedT<T> wt = getMin(result);
				minMaxT = (IWeightable) wt.getWeight();
				minMax = wt.getValue();
			} else if (bigger) {
				result.remove(minMaxT);
				result.add(t);
				WeightedT<T> wt = getMin(result);
				minMaxT = (IWeightable) wt.getWeight();
				minMax = wt.getValue();
			}

		}

		return result;
	}

	private static <T extends IWeightable> WeightedT<T> getMin(List<T> list) {
		double min = 1.7976931348623157E+308D;
		WeightedT<T> minT = null;
		for (T t : list) {
			if (t.getWeight() < min) {
				min = t.getWeight();
				minT = new WeightedT<T>();
				minT.setValue(min);
				minT.setWeight(t);
			}
		}

		return minT;
	}

	public static <T extends IWeightable> List<T> getButtom(List<T> list, int size) {
		double maxMin = 0.0D;
		IWeightable maxMinT = null;
		List<T> result = new ArrayList<>();
		for (T t : list) {
			boolean bigger = false;
			if (maxMin > t.getWeight()) {
				maxMin = t.getWeight();
				bigger = true;
			}
			if (result.size() < size) {
				result.add(t);
				WeightedT<T> wt = getMax(result);
				maxMinT = (IWeightable) wt.getWeight();
				maxMin = wt.getValue();
			} else if (bigger) {
				result.remove(maxMinT);
				result.add(t);
				WeightedT<T> wt = getMax(result);
				maxMinT = (IWeightable) wt.getWeight();
				maxMin = wt.getValue();
			}

		}

		return result;
	}

	private static <T extends IWeightable> WeightedT<T> getMax(List<T> list) {
		double max = 4.9E-324D;
		WeightedT<T> maxT = null;
		for (T t : list) {
			if (t.getWeight() > max) {
				max = t.getWeight();
				maxT = new WeightedT<T>();
				maxT.setValue(max);
				maxT.setWeight(t);
			}
		}

		return maxT;
	}
}
