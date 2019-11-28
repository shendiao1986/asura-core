package org.asura.core.util.math;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class NumberUtil {
	//private static final int NUM_MIN_ASC = 48;
	//private static final int NUM_MAX_ASC = 57;
	//private static final int DOT = 46;

	public static double getLenedDoubleValue(double d, int len) {
		BigDecimal bg = new BigDecimal(d);
		double result = bg.setScale(len, BigDecimal.ROUND_HALF_UP).doubleValue();
		return result;
	}

	public static String getLenedDouble(double d, int len) {
		if ((Double.isNaN(d)) || (Double.isInfinite(d))) {
			return "N/A";
		}
		NumberFormat format = NumberFormat.getInstance();
		format.setMaximumFractionDigits(len);

		return format.format(d);
	}

	public static int getMax(int[] ints) {
		int max = -2147483648;
		for (int in : ints) {
			if (in > max) {
				max = in;
			}
		}

		return max;
	}

	public static int getMin(int[] ints) {
		int min = 2147483647;
		for (int in : ints) {
			if (in < min) {
				min = in;
			}
		}

		return min;
	}

	public static Double[] getAllDoubles(String value) {
		List<Double> set = new ArrayList<>();
		int start = -1;
		int end = -1;
		value = value.replace("。", ".");
		char[] chars = value.toCharArray();
		for (int i = 0; i < chars.length; ++i) {
			if (((chars[i] >= '0') && (chars[i] <= '9')) || (chars[i] == '.')) {
				if (start == -1) {
					start = i;
				}
				if (i == chars.length - 1) {
					end = i + 1;
				}
			} else if ((end == -1) && (start != -1)) {
				end = i;
			}

			if ((start != -1) && (end != -1)) {
				set.add(Double.valueOf(getDouble(value.substring(start, end))));
				start = -1;
				end = -1;
			}
		}

		return ((Double[]) set.toArray(new Double[0]));
	}

	public static int getInt(String string) {
		Double d;
		try {
			return Integer.parseInt(string);
		} catch (Exception e) {
			d = Double.valueOf(getDouble(string));
		}
		return d.intValue();
	}

	public static long getLong(String string) {
		Double d;
		try {
			return Integer.parseInt(string);
		} catch (Exception e) {
			d = Double.valueOf(getDouble(string));
		}
		return d.longValue();
	}

	public static double getDouble(String string) {
		try {
			return Double.parseDouble(string);
		} catch (Exception e) {
			try {
				if (string != null) {
					char[] chars = string.toCharArray();
					boolean start = false;
					int len = 0;
					for (int i = 0; i < chars.length; ++i) {
						if (chars[i] == 12290)
							chars[i] = '.';
						if ((((chars[i] < '0') || (chars[i] > '9'))) && (chars[i] != '.') && (chars[i] != ',')
								&& (chars[i] != 65292)) {
							chars[i] = ' ';
							if (!(start))
								continue;
							len = i;
							break;
						}

						start = true;
					}

					string = new String(chars);
					if (len != 0) {
						string = string.substring(0, len);
					}
					string = string.replace(" ", "");
					string = string.replace(",", "");
					string = string.replace("，", "");
					if (string.equals("")) {
						return 0.0D;
					}
					return Double.valueOf(string).doubleValue();
				}
				return 0.0D;
			} catch (Exception e1) {
			}
		}
		return 0.0D;
	}
}
