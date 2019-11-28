package org.asura.core.util.collection;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class RecurseSameSet<T> {
	private HashMap<T, HashSet<T>> map;
	private HashMap<T, T> findMap;

	public RecurseSameSet() {
		this.map = new HashMap<T, HashSet<T>>();
		this.findMap = new HashMap<T, T>();
	}

	public void addSame(T t1, T t2) {
		if ((contains(t1)) && (contains(t2))) {
			T key2 = getMapKey(t2);
			T key1 = getMapKey(t1);
			if (!(key2.equals(key1))) {
				this.map.get(key1).add(key2);
				this.map.get(key1).addAll(this.map.get(key2));
				for (T t : this.map.get(key2)) {
					this.findMap.put(t, key1);
				}
				this.map.remove(key2);
			}
			this.findMap.put(t1, key1);
			this.findMap.put(t2, key1);
		} else if (contains(t1)) {
			T key = getMapKey(t1);
			this.map.get(key).add(t2);
			this.findMap.put(t2, key);
		} else if (contains(t2)) {
			T key = getMapKey(t2);
			this.map.get(key).add(t1);
			this.findMap.put(t1, key);
		} else {
			this.map.put(t1, new HashSet<T>());
			this.map.get(t1).add(t1);
			this.map.get(t1).add(t2);
			this.findMap.put(t1, t1);
			this.findMap.put(t2, t1);
		}
	}

	private T getMapKey(T t) {
		if (this.findMap.containsKey(t)) {
			return this.findMap.get(t);
		}
		return null;
	}

	public Set<T> getUniques() {
		return this.map.keySet();
	}

	public Set<T> getSame(T t) {
		if (contains(t)) {
			return this.map.get(this.findMap.get(t));
		}

		return new HashSet<T>();
	}

	public boolean contains(T t) {
		return this.findMap.containsKey(t);
	}

	public Map<T, T> getOriginalStandardMap() {
		Map<T, T> result = new HashMap<T, T>();
		for (T t : this.map.keySet()) {
			for (T key : this.map.get(t)) {
				result.put(key, t);
			}
		}
		return result;
	}

	public String toString() {
		return this.map.toString();
	}

}
