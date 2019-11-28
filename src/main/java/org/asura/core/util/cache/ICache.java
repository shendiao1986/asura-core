package org.asura.core.util.cache;

import java.io.Serializable;

public interface ICache<K, V> extends Serializable {

	public V get(K key);

	public void cache(K key, V value);
	
	public void cache(K key, V value, int seconds);

	public void remove(K key);

	public int size();
	
	public void clear();

}
