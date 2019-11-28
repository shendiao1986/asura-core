package org.asura.core.util;

import java.io.Serializable;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * 采用soft reference实现的hashmap，在OOM之前会将map中的value进行释放，以降低内存占用
 * 
 * @author shendiao
 *
 * @param <K>
 *            the type of keys maintained by this map
 * @param <V>
 *            the type of mapped values
 */
public class SoftHashMap<K, V> extends AbstractMap<K, V> implements Serializable {

	private static final long serialVersionUID = 5675784935582207417L;

	private final Map<K, SoftReference<V>> hash = new HashMap<K, SoftReference<V>>();

	private final Map<SoftReference<V>, K> reverseLookup = new HashMap<SoftReference<V>, K>();

	private final ReferenceQueue<V> queue = new ReferenceQueue<V>();

	public V put(K key, V value) {
		clearStaleEntries();
		SoftReference<V> soft_ref = new SoftReference<V>(value, queue);
		reverseLookup.put(soft_ref, key);
		SoftReference<V> result = hash.put(key, soft_ref);
		if (result == null) {
			return null;
		}
		reverseLookup.remove(result);
		return result.get();
	}

	public V remove(Object key) {
		clearStaleEntries();
		SoftReference<V> result = hash.remove(key);
		if (result == null) {
			return null;
		}
		return result.get();
	}

	public void clear() {
		hash.clear();
		reverseLookup.clear();
	}

	public int size() {
		clearStaleEntries();
		return hash.size();
	}

	@Override
	public Set<Entry<K, V>> entrySet() {
		clearStaleEntries();
		Set<Entry<K, V>> result = new LinkedHashSet<Entry<K, V>>();
		for (final Entry<K, SoftReference<V>> entry : hash.entrySet()) {
			final V value = entry.getValue().get();
			if (value != null) {
				result.add(new Entry<K, V>() {
					public K getKey() {
						return entry.getKey();
					}

					public V getValue() {
						return value;
					}

					public V setValue(V v) {
						entry.setValue(new SoftReference<V>(v, queue));
						return value;
					}
				});
			}
		}
		return result;
	}

	private void clearStaleEntries() {
		Reference<? extends V> sv;
		while ((sv = queue.poll()) != null) {
			hash.remove(reverseLookup.remove(sv));
		}
	}

}
