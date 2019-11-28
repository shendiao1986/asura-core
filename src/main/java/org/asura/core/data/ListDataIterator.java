package org.asura.core.data;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class ListDataIterator <T> implements DataIterator<T> {
	private Iterator<T> it;
	private Collection<T> list;

	public ListDataIterator(List<T> list) {
		this.list = list;
		this.it = list.iterator();
	}

	public ListDataIterator(Collection<T> list) {
		this.list = list;
		this.it = list.iterator();
	}

	public ListDataIterator(Iterator<T> iterator) {
		this.it = iterator;
	}

	public void close() {
	}

	public boolean hasNext() {
		return this.it.hasNext();
	}

	public T next() {
		return this.it.next();
	}

	public void reset() {
		if (this.list != null)
			this.it = this.list.iterator();
	}
}
