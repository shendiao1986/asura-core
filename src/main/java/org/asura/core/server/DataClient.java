package org.asura.core.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.util.concurrent.Uninterruptibles;

public final class DataClient implements Runnable, DataSender {

	private static final Logger logger = LoggerFactory.getLogger(DataClient.class);

	private static Timer reconnectTimer = new Timer();

	private static Map<String, DataClient> clientMap = new HashMap<>();

	private final Set<DataClientListener> listeners = new LinkedHashSet<>();

	private String threadName = null;

	private Thread theThread = null;

	private final Map<String, SocketDetail> connections = new HashMap<>();

	private final Map<SocketChannel, String> connectionIds = new IdentityHashMap<>();

	private Selector sel = null;

	private static final int RECONNECT_TIME = 2000;

	private static final int DEFAULT_POLL_TIME = 5000;

	private int pollTime = DEFAULT_POLL_TIME;

	private final Object runLock = new Object();

	private final Object selectorLockUpdate = new Object();

	private final Object selectorLockContinue = new Object();

	private boolean running = false;

	private DataClient(final String name) {
		threadName = name;
	}

	public static synchronized DataClient getReference(final String name) {
		if (!clientMap.containsKey(name)) {
			clientMap.put(name, new DataClient(name));
		}
		return (DataClient) clientMap.get(name);
	}

	public static synchronized void runClient(final String name) {
		getReference(name).thread().start();
	}

	public static synchronized DataClient removeReference(final String name) {
		final DataClient retval = getReference(name);
		clientMap.remove(name);
		return retval;
	}

	public void beginConnection(final String hostname, final int port, final String id) throws IOException {
		if (id == null) {
			throw new IOException("No ID specified for connection");
		}

		while (!isRunning()) {
			Uninterruptibles.sleepUninterruptibly(200, TimeUnit.MILLISECONDS);
		}

		final SocketChannel sc = SocketChannel.open();
		sc.configureBlocking(false);
		try {
			final InetSocketAddress addr = new InetSocketAddress(hostname, port);

			if (connections.containsKey(id)) {
				unmanageSocket(id, false);
			}

			final SocketDetail details = new SocketDetail(sc, addr);
			connections.put(id, details);
			connectionIds.put(sc, id);

			synchronized (this) {
				synchronized (selectorLockContinue) {
					sel.wakeup();
					synchronized (selectorLockUpdate) {
						if (sc.connect(addr)) {
							onConnect(sc, true);
						} else {
							sc.register(sel, SelectionKey.OP_CONNECT);
						}
					}
				}
			}

		} catch (final IOException ex) {
			logger.error("Could not open socket " + id, ex);
			try {
				connections.remove(id);
				connectionIds.remove(sc);
				sc.close();
			} catch (final IOException ex2) {
				logger.error("Error closing " + id, ex2);
			}
			throw ex;
		}
	}

	public synchronized void unmanageSocket(final String id) {
		unmanageSocket(id, true);
	}

	public synchronized void unmanageSocket(final String id, final boolean alertListeners) {
		final SocketDetail details = (SocketDetail) connections.get(id);
		if (details == null) {
			logger.error("Failed to get SocketDetails object");
		} else {
			final SocketChannel sc = details.getSocketChannel();
			if (sc == null) {
				logger.error("Failed to get SocketChannel object");
			} else {
				try {
					connectionIds.remove(sc);
					sc.close();
				} catch (final IOException iox) {
					logger.warn("Failed to close connection " + id, iox);
				}
			}
		}
		connections.remove(id);
		if (alertListeners) {
			onDisconnect(id);
		}
	}

	@Override
	public void sendData(String receiver, Object data) {
		SocketChannel sock = null;
		SocketDetail details = null;

		try {
			details = (SocketDetail) connections.get(receiver);
			if (details == null) {
				throw new IOException("No connection registered to name " + receiver);
			}
			sock = details.getSocketChannel();

			if (sock != null) {
				final ByteBuffer buf = ByteBuffer.wrap(DataProtocol.convertObject(data));
				while (buf.hasRemaining()) {
					sock.write(buf);
				}
			}
		} catch (final IOException ex) {
			logger.warn("Failed to send to socket " + receiver, ex);
			unmanageSocket(receiver);
		} catch (final NullPointerException ex) {
			logger.error(receiver);
			unmanageSocket(receiver);
		}
	}

	public synchronized boolean isConnected(final String id) {
		return connections.containsKey(id);
	}

	public void setPollTime(final int time) {
		if (time > 0) {
			pollTime = time;
		}
	}

	public synchronized Thread thread() {
		if (theThread == null) {
			theThread = new Thread(this, "DataClient(" + threadName + ")");
		}

		return theThread;
	}

	public boolean isRunning() {
		synchronized (runLock) {
			return running;
		}
	}

	public void kill() {
		synchronized (runLock) {
			running = false;
		}
	}

	public void removeClientListener(final DataClientListener lst) {
		synchronized (listeners) {
			if (lst != null) {
				listeners.remove(lst);
			}
		}
	}

	public void addClientListener(final DataClientListener lst) {
		synchronized (listeners) {
			if (lst != null) {
				listeners.add(lst);
			}
		}
	}

	public String getName() {
		return threadName;
	}

	private void scheduleReconnect(final String id) {
		final ReConnect rc = new ReConnect(id);
		reconnectTimer.schedule(rc, RECONNECT_TIME);
	}

	private void activateClient() throws IOException {
		sel = SelectorProvider.provider().openSelector();
	}

	private void onConnect(final SocketChannel sc, final boolean alreadyConnected) {
		final String id = (String) connectionIds.get(sc);
		if (id != null) {
			try {
				if (alreadyConnected || sc.finishConnect()) {
					sc.register(sel, SelectionKey.OP_READ);
					sc.socket().setSoLinger(true, 0);
					onConnect(id);
				} else {
					logger.warn("Failed to complete connection");
					scheduleReconnect(id);
				}
			} catch (final IOException iox) {
				scheduleReconnect(id);
			} catch (final NullPointerException ex) {
				logger.error("Failed to get ID for socket", ex);
				try {
					sc.close();
				} catch (final IOException ioex) {
					logger.error("IOException", ex);
				}
			}
		}
	}

	private synchronized void onData(final SocketChannel sc) {

		final String id = connectionIds.get(sc);
		if (id == null) {
			logger.error("Data received on unmanaged socket");
			return;
		}

		final SocketDetail details = connections.get(id);
		if (details == null) {
			logger.error("Missing connection details for " + id);
			connectionIds.remove(sc);
			return;
		}

		final DataProtocol proto = details.getProtocol();

		if (proto == null) {
			logger.error("Failed to get protocol handler for " + id);
			unmanageSocket(id);
			return;
		}

		try {
			final int bufsize = proto.bytesNeeded();
			final ByteBuffer buf = ByteBuffer.allocate(bufsize);
			final int count = sc.read(buf);

			if (count < 0) {
				throw new IOException("Socket closed");
			} else if (count > 0) {
				buf.flip();
				if (proto.bytesRead(buf.array(), 0, count)) {
					final Object obj = proto.getObject();
					if (obj == null) {
						unmanageSocket(id);
					} else {
						onDataRead(obj, id);
					}
				}
			} else {
				logger.warn("No bytes read from stream " + id);
			}

		} catch (final IOException iox) {
			unmanageSocket(id);
		} catch (final ClassNotFoundException cnfx) {
			logger.error("Object received that is not in classpath", cnfx);
		}
	}

	private boolean pollConnections() throws IOException {
		boolean wasActive = false;
		int active = 0;

		synchronized (selectorLockContinue) {
		}

		synchronized (selectorLockUpdate) {
			try {
				active = sel.select(pollTime);
			} catch (final IOException iox) {
				logger.error("Failed to select on connections", iox);
				onFatalError("Client IO error: " + iox.toString());
			}
			if (active == 0) {
				onIdle();
			} else {
				final Set<SelectionKey> activeChannels = sel.selectedKeys();

				for (final Iterator<SelectionKey> i = activeChannels.iterator(); i.hasNext();) {
					final SelectionKey sk = (SelectionKey) i.next();
					i.remove();
					if (sk.isConnectable()) {
						final SocketChannel sc = (SocketChannel) sk.channel();
						onConnect(sc, false);
					} else if (sk.isReadable()) {
						final SocketChannel sc = (SocketChannel) sk.channel();
						onData(sc);
					}
				}
				wasActive = true;
			}
		}
		return wasActive;
	}

	@Override
	public void run() {
		try {
			activateClient();
			synchronized (runLock) {
				running = true;
			}
			while (isRunning()) {
				pollConnections();
			}
		} catch (final Exception ex) {
			logger.error("Fatal unhandled exception", ex);
			onFatalError("Client Handler Error: " + ex.toString());
		}
		synchronized (runLock) {
			running = false;
		}
		shutdown();

	}

	private List<DataClientListener> getListeners() {
		synchronized (listeners) {
			if (listeners.size() == 0) {
				logger.warn("No listeners for DataClient '" + threadName + "'");
			}
			return new LinkedList<>(listeners);
		}
	}

	private void onDataRead(final Object data, final String id) {
		for (final Iterator<DataClientListener> it = getListeners().iterator(); it.hasNext();) {
			it.next().dataReceived(data, this, id);
		}
	}

	private synchronized void shutdown() {
		for (final Iterator<String> it = connections.keySet().iterator(); it.hasNext();) {
			unmanageSocket(it.next(), false);
		}
		synchronized (listeners) {
			listeners.clear();
		}
	}

	private void onFatalError(final String msg) {
		logger.error("A fatal error occured in DataClient '" + threadName + "': " + msg);
		for (final Iterator<DataClientListener> it = getListeners().iterator(); it.hasNext();) {
			it.next().fatalError(msg, this);
		}
		kill();
		removeReference(threadName);
		shutdown();
	}

	private void onDisconnect(final String id) {
		for (final Iterator<DataClientListener> it = getListeners().iterator(); it.hasNext();) {
			it.next().connectionTerminated(id, this);
		}
	}

	private void onConnect(final String id) {
		for (final Iterator<DataClientListener> it = getListeners().iterator(); it.hasNext();) {
			it.next().connectionEstablished(id, this);
		}
	}

	private void onIdle() {
		for (final Iterator<DataClientListener> it = getListeners().iterator(); it.hasNext();) {
			it.next().idleProcessing(this);
		}
	}

	private class ReConnect extends TimerTask {
		private final String id;

		public ReConnect(final String id) {
			this.id = id;
		}

		public void run() {
			try {
				final SocketDetail details = (SocketDetail) connections.get(id);
				final InetSocketAddress addr = details.getAddress();
				unmanageSocket(id, false);
				beginConnection(addr.getHostName(), addr.getPort(), id);
			} catch (final IOException ex) {
				logger.error("Failed to attempt reconnect", ex);
			}
		}
	}

	private static class SocketDetail {
		private final SocketChannel socketChannel;
		private final InetSocketAddress address;
		private final DataProtocol protocol;

		public SocketDetail(final SocketChannel theSocket, final InetSocketAddress theAddress) {
			this.socketChannel = theSocket;
			this.address = theAddress;
			this.protocol = new DataProtocol();
		}

		public SocketChannel getSocketChannel() {
			return socketChannel;
		}

		public InetSocketAddress getAddress() {
			return address;
		}

		public DataProtocol getProtocol() {
			return protocol;
		}
	}

}
