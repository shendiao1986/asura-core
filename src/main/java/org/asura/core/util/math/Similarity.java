package org.asura.core.util.math;

public class Similarity {
	private int min(int one, int two, int three) {
		int min = one;

		if (two < min) {
			min = two;
		}

		if (three < min) {
			min = three;
		}

		return min;
	}

	public int ld(String str1, String str2) {
		int n = str1.length();
		int m = str2.length();

		if (n == 0) {
			return m;
		}

		if (m == 0) {
			return n;
		}

		int[][] d = new int[n + 1][m + 1];

		for (int i = 0; i <= n; ++i) {
			d[i][0] = i;
		}

		for (int j = 0; j <= m; ++j) {
			d[0][j] = j;
		}

		for (int i = 1; i <= n; ++i) {
			char ch1 = str1.charAt(i - 1);

			for (int j = 1; j <= m; ++j) {
				char ch2 = str2.charAt(j - 1);
				int temp;
				if (ch1 == ch2)
					temp = 0;
				else {
					temp = 1;
				}

				d[i][j] = min(d[(i - 1)][j] + 1, d[i][(j - 1)] + 1, d[(i - 1)][(j - 1)] + temp);
			}
		}

		return d[n][m];
	}

	public double sim(String str1, String str2) {
		int ld = ld(str1, str2);

		return (1.0D - (ld / Math.max(str1.length(), str2.length())));
	}

	public static void main(String[] args) {
		Similarity s = new Similarity();

		String str1 = "isusxing";

		String str2 = "ixusxing";

		System.out.println("ld=" + s.ld(str1, str2));

		System.out.println("sim=" + s.sim(str1, str2));
	}
}
