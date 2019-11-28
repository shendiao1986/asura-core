package org.asura.core.util;

public class DebugUtil {

	private static ThreadLocal<Integer> count = new ThreadLocal<Integer>() {
		@Override
		protected Integer initialValue() {
			return 0;
		}

	};

	public static void resetPoint() {
		count.set(0);
	}

	public static int getPoint() {
		return count.get().intValue();
	}

	public static boolean triggerred(int cnt) {
		count.set(count.get().intValue() + 1);
		if (count.get().intValue() % cnt == 0) {
			return true;
		} else {
			return false;
		}
	}

}
