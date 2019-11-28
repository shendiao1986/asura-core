package org.asura.core.util;

import java.util.Comparator;

public class PositionComparator implements Comparator<Position> {
	public int compare(Position o1, Position o2) {
		if (o1.getStart() > o2.getStart())
			return 1;
		if (o1.getStart() == o2.getStart()) {
			if (o1.getEnd() > o2.getEnd())
				return 1;
			if (o1.getEnd() == o2.getEnd()) {
				return 0;
			}
			return -1;
		}

		return -1;
	}
}
