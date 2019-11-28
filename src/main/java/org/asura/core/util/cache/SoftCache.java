package org.asura.core.util.cache;

import org.asura.core.util.SoftHashMap;

/**
 * 采用soft hashmap实现的cache，在OOM之前会将cache中的value进行释放，以降低内存占用
 * @author shendiao
 *
 * @param <K>
 *            the type of keys maintained by this cache
 * @param <V>
 *            the type of values maintained by this cache
 */
public class SoftCache<K, V> implements ICache<K, V> {

	private static final long serialVersionUID = -442694402313203253L;

	private SoftHashMap<K, V> table;

	public SoftCache() {
		this.table = new SoftHashMap<>();
	}

	@Override
	public V get(K key) {
		return this.table.get(key);
	}

	@Override
	public void cache(K key, V value) {
		this.table.put(key, value);
	}

	@Override
	public void remove(K key) {
		this.table.remove(key);
	}

	@Override
	public int size() {
		return this.table.size();
	}

	@Override
	public void cache(K key, V value, int seconds) {
		this.cache(key, value);
	}

	@Override
	public void clear() {
		this.table.clear();
	}

}
