package org.asura.core.data;

public class EmptyDataIterator<T> implements DataIterator<T> {

	@Override
	public boolean hasNext() {
		return false;
	}

	@Override
	public T next() {
		return null;
	}

	@Override
	public void close() {
		
	}

	@Override
	public void reset() {
		
	}

}
