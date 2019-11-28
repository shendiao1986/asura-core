package org.asura.core.server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class DataProtocol {

	private static final Logger logger = LoggerFactory.getLogger(DataProtocol.class);

	private static final int HEADER_SIZE = 8;
	private static final int PROTOCOL_TYPE = 0x2566;

	private static final int INCREMENT_SIZE = 1024;
	private static final int INITIAL_SIZE = 1024;

	private byte[] buffer = new byte[INITIAL_SIZE];
	private int count = 0;

	public DataProtocol() {

	}

	public static byte[] convertObject(final Object obj) throws IOException {
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();

		final ObjectOutputStream os = new ObjectOutputStream(baos);
		os.writeObject(obj);

		final byte[] ary = baos.toByteArray();
		final int len = ary.length;

		final ByteArrayOutputStream los = new ByteArrayOutputStream();
		final DataOutputStream dos = new DataOutputStream(los);
		dos.writeInt(PROTOCOL_TYPE);
		dos.writeInt(len);
		final byte[] lenbuf = los.toByteArray();

		final byte[] retval = new byte[len + HEADER_SIZE];
		System.arraycopy(lenbuf, 0, retval, 0, HEADER_SIZE);
		System.arraycopy(ary, 0, retval, HEADER_SIZE, len);

		return retval;
	}

	public synchronized int bytesNeeded() throws IOException {
		int retval = HEADER_SIZE - count;
		if (hasHeader()) {
			retval = getLength() - (count - HEADER_SIZE);
		}
		return retval;
	}

	public synchronized boolean bytesRead(final byte[] data) throws IOException {
		return bytesRead(data, 0, data.length);
	}

	public synchronized boolean bytesRead(final byte[] data, final int offset, final int length) throws IOException {
		ensureBuffer(count + length);
		System.arraycopy(data, offset, buffer, count, length);
		count += length;
		return completePacket();
	}

	private synchronized boolean hasHeader() {
		return count >= HEADER_SIZE;
	}

	private synchronized void ensureBuffer(final int bytesRequired) {
		while (buffer.length < bytesRequired) {
			increaseBuffer();
		}
	}

	private synchronized int getLength() throws IOException {
		int len = -1;
		if (hasHeader()) {
			final DataInputStream str = new DataInputStream(new ByteArrayInputStream(buffer));
			int protocolType = str.readInt();
			if (protocolType == PROTOCOL_TYPE) {
				len = str.readInt();
			} else {
				logger.error("Not supported protocol type");
				reset();
				throw new IOException("Protocol failure - protocol type violation");
			}
		}
		return len;
	}

	public synchronized boolean completePacket() throws IOException {
		boolean retval = false;
		if (hasHeader()) {
			retval = count >= (getLength() + HEADER_SIZE);
		}
		return retval;
	}

	public synchronized Object getObject() throws IOException, ClassNotFoundException {
		Object rv = null;
		if (completePacket()) {
			final ObjectInputStream str = new ObjectInputStream(new ByteArrayInputStream(getPacket()));
			rv = str.readObject();
		}
		return rv;
	}

	private synchronized byte[] getPacket() throws IOException {
		byte[] retval = null;
		try {
			if (completePacket()) {
				retval = new byte[getLength()];
				System.arraycopy(buffer, HEADER_SIZE, retval, 0, getLength());
				removePacket();
			}
		} catch (final NegativeArraySizeException nasex) {
			reset();
			throw new IOException("Protocol violation - negative packet size");
		}
		return retval;
	}

	private synchronized void increaseBuffer() {
		final byte[] tmp = new byte[buffer.length + INCREMENT_SIZE];
		System.arraycopy(buffer, 0, tmp, 0, buffer.length);
		buffer = tmp;
	}

	private synchronized void removePacket() throws IOException {
		if (completePacket()) {
			try {
				final int offset = HEADER_SIZE + getLength();
				final byte[] tmp = new byte[buffer.length];
				System.arraycopy(buffer, offset, tmp, 0, buffer.length - offset);
				buffer = tmp;
				count -= offset;
			} catch (final ArrayIndexOutOfBoundsException ex) {
				logger.error("Failure removing packet");
				reset();
				throw new IOException("Protocol failure - array violation");
			}
		}
	}

	public synchronized void reset() {
		buffer = new byte[INITIAL_SIZE];
		count = 0;
	}

}
