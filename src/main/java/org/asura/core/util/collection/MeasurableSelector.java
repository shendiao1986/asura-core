package org.asura.core.util.collection;

import java.util.Collection;

public class MeasurableSelector {
	public static int getMaxValue(Collection<? extends IMeasurable> measurables) {
		int max = -2147483648;
		for (IMeasurable measurable : measurables) {
			if (measurable.getMeasureValue() > max) {
				max = measurable.getMeasureValue();
			}
		}

		return max;
	}

	public static int getMinValue(Collection<? extends IMeasurable> measurables) {
		int min = 2147483647;
		for (IMeasurable measurable : measurables) {
			if (measurable.getMeasureValue() < min) {
				min = measurable.getMeasureValue();
			}
		}

		return min;
	}

	public static IMeasurable getMax(Collection<? extends IMeasurable> measurables) {
		int max = -2147483648;
		IMeasurable result = null;
		for (IMeasurable measurable : measurables) {
			if (measurable.getMeasureValue() > max) {
				max = measurable.getMeasureValue();
				result = measurable;
			}
		}

		return result;
	}

	public static IMeasurable getMin(Collection<? extends IMeasurable> measurables) {
		int min = 2147483647;
		IMeasurable result = null;
		for (IMeasurable measurable : measurables) {
			if (measurable.getMeasureValue() < min) {
				min = measurable.getMeasureValue();
				result = measurable;
			}
		}

		return result;
	}
}
