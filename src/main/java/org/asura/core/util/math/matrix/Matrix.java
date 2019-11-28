package org.asura.core.util.math.matrix;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Matrix {
	private HashMap<String, List<Vote>> map;

	public Matrix() {
		this.map = new HashMap<>();
	}

	public void addVote(Vote vote) {
		if (!(this.map.containsKey(vote.getId2()))) {
			this.map.put(vote.getId2(), new ArrayList<Vote>());
		}

		this.map.get(vote.getId2()).add(vote);
	}

	public UnitValue compute() {
		return compute(1.0E-007D);
	}

	public UnitValue compute(double diff) {
		return compute(diff, 100000);
	}

	public UnitValue compute(double diff, int maxCount) {
		UnitValue uv = new UnitValue();

		int count = 0;
		while (true) {
			for (String key : this.map.keySet()) {
				List<Vote> list = this.map.get(key);
				double total = 0.0D;
				for (Vote v : list) {
					double value = uv.getLastValue(v.getId1());
					if (value < 0.0D) {
						value = 1.0D;
					}

					total += value * v.getPercent();
				}

				total += 1.0D;

				uv.addValue(key, total);
			}

			System.out.println("compute:" + (count++) + "  " + uv.getDiff());

			if (uv.isReady(diff)) {
				break;
			}
			uv.startNewRound();
		}

		return uv;
	}
}
