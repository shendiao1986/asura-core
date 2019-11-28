package org.asura.core.data;

import java.util.ArrayList;
import java.util.List;

public class MultiDataIterator <T> implements DataIterator<T> {
	private List<DataIterator<T>> iterators;
	private DataIterator<T> currentIt;
	private int pos;
	private IDataPreHandler<T> preHandler;
	private boolean interrupt;

	public IDataPreHandler<T> getPreHandler() {
		return this.preHandler;
	}

	public void setPreHandler(IDataPreHandler<T> preHandler) {
		this.preHandler = preHandler;
	}

	public MultiDataIterator() {
		this.iterators = new ArrayList<>();
		this.pos = 0;
	}

	public void addDataIterator(DataIterator<T> it) {
		this.iterators.add(it);
	}

	public void interrupt() {
		this.interrupt = true;
	}

	public void close() {
		if (this.currentIt != null)
			this.currentIt.close();
	}

	public boolean hasNext() {
		if (this.interrupt) {
			return false;
		}
		if (this.iterators.size() == 0) {
			return false;
		}
		if ((this.pos == 0) && (this.currentIt == null)) {
			this.currentIt = (this.iterators.get(0));
		}
		if (this.currentIt.hasNext()) {
			return this.currentIt.hasNext();
		}
		if (this.pos < this.iterators.size() - 1) {
			this.pos += 1;
			this.currentIt.close();
			this.currentIt = (this.iterators.get(this.pos));

			return hasNext();
		}
		this.currentIt.close();
		return false;
	}

	public T next() {
		if (this.currentIt != null) {
			T t = this.currentIt.next();
			if (this.preHandler != null) {
				t = this.preHandler.prehandle(t);
			}
			return t;
		}
		return null;
	}

	public void reset() {
		this.pos = 0;
		if (this.currentIt != null) {
			this.currentIt.close();
			this.currentIt = null;
		}
		for (DataIterator<T> it : this.iterators) {
			it.reset();
		}
	}
}
