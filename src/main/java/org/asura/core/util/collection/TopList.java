package org.asura.core.util.collection;

import java.util.ArrayList;
import java.util.List;

public class TopList<T extends IWeightable> {
	private List<T> list;
	private int capacity;
	private double min;

	public TopList() {
		this.capacity = 2147483647;
		this.list = new ArrayList<>();
	}

	public TopList(int capacity) {
		this.capacity = capacity;
		this.list = new ArrayList<>();
	}

	public void clear() {
		this.list.clear();
	}

	public void add(T t) {
		if (this.list.size() < this.capacity) {
			insertToList(t);
			this.min = ((IWeightable) this.list.get(this.list.size() - 1)).getWeight();
		} else if (t.getWeight() > this.min) {
			insertToList(t);
			this.list.remove(this.list.size() - 1);
			this.min = ((IWeightable) this.list.get(this.list.size() - 1)).getWeight();
		}
	}

	public List<T> getList() {
		return this.list;
	}

	public int size() {
		return this.list.size();
	}

	public void setList(List<T> list) {
		this.list = list;
	}

	private void insertToList(T weightable) {
		int start = 0;
		int end = this.list.size();
		if (this.list.size() == 0) {
			this.list.add(weightable);
		} else {
			int middle;
			do
				while (true) {
					middle = (start + end) / 2;
					if (((IWeightable) this.list.get(middle)).getWeight() > weightable.getWeight()) {
						if (start == middle) {
							this.list.add(end, weightable);
							return;
						}
						start = middle;
					}

					if (((IWeightable) this.list.get(middle)).getWeight() >= weightable.getWeight())
						break;
					if (end == middle) {
						this.list.add(start, weightable);
						return;
					}
					end = middle;
				}

			while (((IWeightable) this.list.get(middle)).getWeight() != weightable.getWeight());
			for (int i = middle + 1; i < this.list.size(); ++i) {
				if (((IWeightable) this.list.get(i)).getWeight() < weightable.getWeight()) {
					if (i > 0) {
						this.list.add(i - 1, weightable);
						return;
					}
					this.list.add(weightable);

					return;
				}
			}
			this.list.add(weightable);
		}
	}
}
