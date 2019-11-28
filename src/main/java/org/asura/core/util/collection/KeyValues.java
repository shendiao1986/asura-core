package org.asura.core.util.collection;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class KeyValues<K, V> {
	private HashMap<K, Set<V>> keyValues;

	public KeyValues() {
		this.keyValues = new HashMap<K,Set<V>>();
	}

	public void add(K k, V v) {
		if (!(this.keyValues.containsKey(k))) {
			this.keyValues.put(k, new HashSet<V>());
		}

		this.keyValues.get(k).add(v);
	}

	public int getKeyCount() {
		return this.keyValues.size();
	}

	public Set<K> getKeys() {
		return this.keyValues.keySet();
	}

	public Set<V> getValues(K k) {
		return this.keyValues.get(k);
	}
}
