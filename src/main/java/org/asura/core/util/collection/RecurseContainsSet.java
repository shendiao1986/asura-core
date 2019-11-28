package org.asura.core.util.collection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

public class RecurseContainsSet<T> {
	private ConcurrentHashMap<T, Set<T>> map;

	public RecurseContainsSet() {
		this.map = new ConcurrentHashMap<T, Set<T>>();
	}

	public void addContains(T big, T small) {
		if (!(this.map.containsKey(small))) {
			this.map.put(small, new ConcurrentSkipListSet<T>());
		}

		this.map.get(small).add(big);
	}

	public void addContains(Set<T> bigs, T small) {
		if (!(this.map.containsKey(small))) {
			this.map.put(small, new ConcurrentSkipListSet<T>());
		}

		this.map.get(small).addAll(bigs);
	}

	public void removeContains(T big, T small) {
		this.map.get(small).remove(big);
		if (this.map.get(small).size() == 0)
			this.map.remove(small);
	}

	public Set<T> getParents(T t) {
		if (this.map.containsKey(t)) {
			return this.map.get(t);
		}
		return new ConcurrentSkipListSet<T>();
	}

	public List<T> getSortedParents(T t, Comparator<T> comparator) {
		List<T> result = new ArrayList<T>();
		for (T tt : getParents(t)) {
			result.add(tt);
		}

		Collections.sort(result, comparator);

		return result;
	}

	public boolean contains(T t) {
		return this.map.containsKey(t);
	}

	public Set<T> getAllContaineds() {
		return this.map.keySet();
	}

	public Map<T, Set<T>> getContainsMap() {
		return this.map;
	}

	public Map<T, Set<T>> getParentMap() {
		Map<T, Set<T>> result = new ConcurrentHashMap<T, Set<T>>();
		for (T t : this.map.keySet()) {
			for (T st : this.map.get(t)) {
				if (!(result.containsKey(st))) {
					result.put(st, new ConcurrentSkipListSet<T>());
				}
				result.get(st).add(t);
			}
		}

		return result;
	}

	public void removeAllContains(Map<T, Set<T>> map) {
		HashSet<T> removes = new HashSet<T>();
		List<Set<T>> alls = new ArrayList<Set<T>>();
		for (T small : map.keySet()) {
			Set<T> parents = map.get(small);
			HashSet<T> all = new HashSet<T>();
			all.addAll(parents);
			all.add(small);
			alls.add(all);
		}

		for (T small : map.keySet()) {
			Set<T> parents = (Set<T>) map.get(small);
			Set<T> all = new HashSet<T>();
			all.addAll(parents);
			all.add(small);

			for (Set<T> set : alls) {
				if ((set.containsAll(all)) && (all.size() < set.size())) {
					removes.add(small);
				}
			}
		}

		for (Object small : removes)
			map.remove(small);
	}

	public T getRootParent(T t) {
		if (this.map.containsKey(t)) {
			Set<T> set = this.map.get(t);
			boolean hasNoParent = false;
			T parent = null;
			Iterator<T> it = set.iterator();
			if (!it.hasNext()) {
				return parent;
			}
			while (true) {
				T pt = it.next();
				if (!this.map.containsKey(pt)) {
					if (hasNoParent) {
						return null;
					}
					hasNoParent = true;
					parent = pt;
				}
				if (!it.hasNext()) {
					return parent;
				}
			}
		}
		return null;
	}
}
