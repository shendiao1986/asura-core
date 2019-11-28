package org.asura.core.util.cache;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * 采用liked hashmap实现的cache，可以指定缓存时间，以及容量，超过容量时以LRU的方式清除缓存内容
 * @author shendiao
 *
 * @param <K>
 *            the type of keys maintained by this cache
 * @param <V>
 *            the type of values maintained by this cache
 */
public class SimpleCache<K, V> implements ICache<K, V> {

	private static final long serialVersionUID = 1L;
	private LinkedHashMap<K, V> table;
	private HashMap<K, Long> startTime;
	private HashMap<K, Integer> cacheTime;
	private long capacity;

	public SimpleCache(int capacity) {
		this.table = new LinkedHashMap<>(capacity, 0.75F, true);
		this.capacity = capacity;
		this.startTime = new HashMap<>();
		this.cacheTime = new HashMap<>();
	}

	@Override
	public synchronized V get(K key) {
		clearStaleEntry(key);
		return this.table.get(key);
	}

	@Override
	public synchronized void cache(K key, V value, int seconds) {
		if (this.table.size() >= this.capacity) {
			this.table.remove(this.table.keySet().iterator().next());
		}
		this.startTime.put(key, Long.valueOf(new Date().getTime()));
		this.cacheTime.put(key, Integer.valueOf(seconds));
		this.table.put(key, value);
	}

	@Override
	public synchronized void remove(K key) {
		this.table.remove(key);
	}

	@Override
	public int size() {
		return this.table.size();
	}

	@Override
	public void cache(K key, V value) {
		if (this.table.size() >= this.capacity) {
			this.table.remove(this.table.keySet().iterator().next());
		}
		this.table.put(key, value);
	}

	@Override
	public void clear() {
		this.startTime.clear();
		this.cacheTime.clear();
		this.capacity = 0L;
		this.table.clear();
	}

	private void clearStaleEntry(K key) {
		if (this.startTime.containsKey(key)) {
			long now = new Date().getTime();
			if (now - ((Long) this.startTime.get(key)).longValue() > ((Integer) this.cacheTime.get(key)).intValue()
					* 1000) {
				remove(key);
				this.startTime.remove(key);
				this.cacheTime.remove(key);
			}
		}
	}
}
