package org.asura.core.util.math;

import java.util.Arrays;
import java.util.Iterator;

import org.asura.core.util.collection.Accumulator;

public class Probability {
	public static double getNormalDistribution(double expectation, double svariance, double value) {
		if (svariance == 0.0D) {
			if (value == expectation) {
				return 1.0D;
			}
			return 0.0D;
		}

		double pow = Math.pow(value - expectation, 2.0D) / 2.0D * Math.pow(svariance, 2.0D);
		return (1.0D / Math.sqrt(6.283185307179586D) * svariance * Math.pow(2.718281828459045D, -pow));
	}

	public static double getStanrdVariance(double[] doubles) {
		return Math.sqrt(getVariance(doubles));
	}

	public static double getVariance(double[] doubles) {
		if (doubles.length > 0) {
			double total = 0.0D;
			double average = getExpectation(doubles);
			double[] arrayOfDouble;
			int j = (arrayOfDouble = doubles).length;
			int i = 0;
			while (true) {
				double d = arrayOfDouble[i];
				total += (average - d) * (average - d);

				++i;
				if (i >= j) {
					return (total / Double.valueOf(doubles.length).doubleValue());
				}
			}
		}
		return -1.0D;
	}

	public static double getMid(double[] doubles) {
		Arrays.sort(doubles);

		int l = doubles.length;
		if (l > 0) {
			if (l % 2 == 0) {
				return ((doubles[(l / 2 - 1)] + doubles[(l / 2)]) / 2.0D);
			}
			return doubles[(l / 2)];
		}

		return 0.0D;
	}

	public static double getExpectation(double[] doubles) {
		if (doubles.length > 0) {
			Accumulator<Double> map = new Accumulator<>();
			for (double d : doubles) {
				map.addKey(Double.valueOf(d));
			}
			double total = 0.0D;
			Iterator<Double> it = map.getKeys().iterator();
			while (true) {
				double d = ((Double) (it).next()).doubleValue();

				total = total + d * Double.valueOf(map.getCount(Double.valueOf(d))).doubleValue()
						/ Double.valueOf(map.getAllCount()).doubleValue();

				if (!it.hasNext()) {
					return total;
				}
			}
		}
		return -1.0D;
	}
}
