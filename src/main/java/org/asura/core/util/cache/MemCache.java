package org.asura.core.util.cache;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

import net.spy.memcached.MemcachedClient;

public class MemCache<K, V> implements ICache<String, V> {

	private static final long serialVersionUID = 1L;

	private MemcachedClient client;

	public int expTime = 3600;

	public MemCache(MemHost... hosts) {
		List<InetSocketAddress> list = new ArrayList<InetSocketAddress>();
		for (MemHost host : hosts) {
			list.add(new InetSocketAddress(host.getHost(), host.getPort()));
		}
		try {
			this.client = new MemcachedClient(list);
		} catch (IOException e) {
			throw new RuntimeException("memcache initialized failed", e);
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public V get(String key) {
		return (V) client.get(key);
	}

	@Override
	public void cache(String key, V value) {
		client.delete(key);
		client.add(key, this.expTime, value);
	}

	@Override
	public void cache(String key, V value, int seconds) {
		client.delete(key);
		client.add(key, seconds, value);
	}

	@Override
	public void remove(String key) {
		client.delete(key);
	}

	@Override
	public int size() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void clear() {
		throw new UnsupportedOperationException();
	}

}
