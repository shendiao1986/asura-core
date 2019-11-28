package org.asura.core.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class DataServer implements Runnable, DataSender {

	private static final Logger logger = LoggerFactory.getLogger(DataServer.class);

	private static Map<String, DataServer> serverMap = new HashMap<>();

	private Thread theThread = null;

	private String threadName = null;

	private static final int DEFAULT_BACKLOG = 10;

	private int backlog = DEFAULT_BACKLOG;

	private Map<String, SocketDetail> connections = new HashMap<>();

	private final Set<DataServerListener> listeners = new LinkedHashSet<>();

	private ServerSocketChannel master = null;

	private static final int DEFAULT_POLLTIME = 5000;
	private int pollTime = DEFAULT_POLLTIME;

	private Object runLock = new Object();

	private Object initializedLock = new Object();

	private boolean running = false;

	private boolean initialized = true;

	private Selector sel = null;

	private int serverPort = 0;

	private DataServer(final String name) {
		threadName = name;
	}

	public static synchronized DataServer getReference(final String name) {
		if (!serverMap.containsKey(name)) {
			serverMap.put(name, new DataServer(name));
		}

		return serverMap.get(name);
	}

	public static synchronized DataServer removeReference(final String name) {
		final DataServer retval = getReference(name);
		serverMap.remove(name);
		return retval;
	}

	public static synchronized void runServer(final String name) {
		getReference(name).thread().start();
	}

	public static synchronized void runServerAwaitInitialization(final String name) {
		final DataServer objectServer = getReference(name);
		objectServer.setAsUninitialized();
		objectServer.thread().start();
	}

	public String getName() {
		return threadName;
	}

	public boolean isRunning() {
		synchronized (runLock) {
			return running;
		}
	}

	private boolean isInitialized() {
		synchronized (this.initializedLock) {
			return this.initialized;
		}
	}

	public void kill() {
		logger.info("DataServer '" + threadName + "' killed");
		synchronized (runLock) {
			running = false;
		}
	}

	public void setAsInitialized() {
		logger.info("DataServer '" + threadName + "' initialized");
		synchronized (this.initializedLock) {
			this.initialized = true;
		}
	}

	private void setAsUninitialized() {
		logger.info("DataServer '" + threadName + "' uninitialized");
		synchronized (this.initializedLock) {
			this.initialized = false;
		}
	}

	public void manageSocket(final SocketChannel sc) {
		if (sc != null) {
			final String id = sc.socket().getInetAddress().toString() + ":" + sc.socket().getPort();
			try {
				synchronized (this) {
					sc.register(sel, SelectionKey.OP_READ);
					final SocketDetail detail = new SocketDetail(sc);
					connections.put(id, detail);
				}
				onConnect(id);
			} catch (final IOException ex) {
				logger.error("Could not monitor socket " + id, ex);
				try {
					sc.close();
				} catch (final IOException ex2) {
					logger.error("Error closing " + id, ex2);
				}
			}
		}
	}

	private void onAccept(final ServerSocketChannel ssc) {
		try {
			final SocketChannel conn = ssc.accept();
			conn.configureBlocking(false);
			conn.socket().setSoLinger(true, 0);
			manageSocket(conn);
		} catch (final IOException iox) {
			logger.error("Failed to accept incoming connection", iox);
		}
	}

	private void onConnect(final String id) {
		for (final Iterator<DataServerListener> it = getListeners().iterator(); it.hasNext();) {
			it.next().connectionEstablished(id, this);
		}
	}

	private synchronized void onData(final SocketChannel sc) {
		final String id = sc.socket().getInetAddress().toString() + ":" + sc.socket().getPort();
		final SocketDetail detail = connections.get(id);
		final DataProtocol proto = detail.getProtocol();

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
					if (obj != null) {
						onObjectRead(obj, id);
					} else {
						unmanageSocket(id);
					}
				}
			} else {
				logger.warn("No bytes read from stream " + id);
			}
		} catch (final IOException iox) {
			unmanageSocket(id);
		} catch (final ClassNotFoundException cnfx) {
			logger.error("Data received that is not in classpath", cnfx);
		}
	}

	private void onDisconnect(final String id) {
		for (final Iterator<DataServerListener> it = getListeners().iterator(); it.hasNext();) {
			it.next().connectionTerminated(id, this);
		}
	}

	private void onFatalError(final String msg) {
		logger.error("A fatal error occured in DataServer '" + threadName + "': " + msg);

		for (final Iterator<DataServerListener> it = getListeners().iterator(); it.hasNext();) {
			it.next().fatalError(msg, this);
		}
		kill();
		removeReference(threadName);
		shutdown();
	}

	private void onObjectRead(final Object obj, final String id) {
		for (final Iterator<DataServerListener> it = getListeners().iterator(); it.hasNext();) {
			it.next().dataReceived(obj, this, id);
		}
	}

	/**
	 * Callback performed when select is completed
	 */
	private void onIdle() {
		for (final Iterator<DataServerListener> it = getListeners().iterator(); it.hasNext();) {
			it.next().idleProcessing(this);
		}
	}

	private boolean pollConnections() throws IOException {
		int active = 0;
		try {
			active = sel.select(pollTime);
		} catch (final IOException iox) {
			logger.error("Failed to select on connections", iox);
			onFatalError("Server IO error: " + iox.toString());
		}

		if (active != 0) {
			final Set<SelectionKey> activeChannels = sel.selectedKeys();
			for (final Iterator<SelectionKey> i = activeChannels.iterator(); i.hasNext();) {
				final SelectionKey sk = (SelectionKey) i.next();
				i.remove();
				if (sk.isAcceptable()) {
					final ServerSocketChannel srv = (ServerSocketChannel) sk.channel();
					onAccept(srv);
				} else if (sk.isReadable()) {
					final SocketChannel sc = (SocketChannel) sk.channel();
					onData(sc);
				}
			}
			return true;
		} else {
			onIdle();
			return false;
		}
	}

	public void removeServerListener(final DataServerListener lst) {
		synchronized (listeners) {
			if (lst != null) {
				listeners.remove(lst);
			}
		}
	}

	@Override
	public synchronized void sendData(String receiver, Object data) {
		try {
			final SocketDetail detail = (SocketDetail) connections.get(receiver);
			if (detail == null) {
				throw new IOException("Socket not found: " + receiver);
			}

			final SocketChannel sock = detail.getSocketChannel();

			if (sock != null) {
				final ByteBuffer buf = ByteBuffer.wrap(DataProtocol.convertObject(data));
				while (buf.hasRemaining()) {
					sock.write(buf);
				}
			}
		} catch (final IOException ex) {
			logger.warn("Failed to send to socket " + receiver, ex);
			unmanageSocket(receiver);
		}

	}

	@Override
	public void run() {
		synchronized (runLock) {
			running = true;
		}
		try {
			activateServer();
			// But don't service the queue until the main thread is initialized.
			while (isInitialized() == false) {
				awaitInitialization();
			}
			while (isRunning()) {
				pollConnections();
			}
		} catch (final Exception ex) {
			logger.error("Fatal unhandled exception", ex);
			onFatalError("Server Handler Error: " + ex.toString());
		}
		synchronized (runLock) {
			running = false;
		}
		shutdown();

	}

	private void awaitInitialization() {
		try {
			Thread.sleep(1000);
		} catch (Exception e) {
			// do nothing
		}
	}

	public synchronized void broadcastObject(final Object obj) {
		for (final Iterator<String> it = connections.keySet().iterator(); it.hasNext();) {
			sendData(it.next(), obj);
		}
	}

	public void setBacklog(final int back) {
		if (back > 0) {
			backlog = back;
		}
	}

	public void setPollTime(final int time) {
		if (time > 0) {
			pollTime = time;
		}
	}

	public void setPort(final int port) {
		if (port >= 0) {
			serverPort = port;
		}
	}

	private synchronized void shutdown() {

		try {
			master.close();
		} catch (final IOException iox) {
			logger.error("Failed to shut down master socket", iox);
		}

		for (final Iterator<String> it = connections.keySet().iterator(); it.hasNext();) {
			unmanageSocket(it.next(), false);
		}

		synchronized (listeners) {
			listeners.clear();
		}
	}

	public synchronized Thread thread() {
		if (theThread == null) {
			theThread = new Thread(this, "DataServer(" + threadName + ")");
		}
		return theThread;
	}

	public synchronized void unmanageSocket(final String id) {
		unmanageSocket(id, true);
	}

	public synchronized void unmanageSocket(final String id, final boolean alertListeners) {
		logger.info("Disconnecting from " + id);
		final SocketDetail details = connections.get(id);
		final SocketChannel sc = details.getSocketChannel();
		if (sc != null) {
			try {
				sc.close();
			} catch (final IOException iox) {
				logger.warn("Failed to close connection " + id, iox);
			}
		}
		connections.remove(id);
		if (alertListeners) {
			onDisconnect(id);
		}
	}

	private void activateServer() throws IOException {
		sel = SelectorProvider.provider().openSelector();
		master = ServerSocketChannel.open();
		master.configureBlocking(false);
		final InetSocketAddress addr = new InetSocketAddress(serverPort);
		master.socket().bind(addr, backlog);
		master.register(sel, SelectionKey.OP_ACCEPT);
		if (serverPort == 0) {
			serverPort = master.socket().getLocalPort();
		}
		logger.info("Server socket is registered on port " + serverPort
				+ (isInitialized() ? " and ready for action" : " and waiting final initialization of process"));
	}

	public void addServerListener(final DataServerListener lst) {
		synchronized (listeners) {
			if (lst != null) {
				listeners.add(lst);
			}
		}
	}

	private List<DataServerListener> getListeners() {
		synchronized (listeners) {
			if (listeners.size() == 0) {
				logger.warn("No listeners for DataServer '" + threadName + "'");
			}
			return new LinkedList<>(listeners);
		}
	}

	private static final class SocketDetail {
		private final DataProtocol protocol;
		private final SocketChannel socketChannel;

		public SocketDetail(final SocketChannel theSocket) {
			this.socketChannel = theSocket;
			this.protocol = new DataProtocol();
		}

		public SocketChannel getSocketChannel() {
			return socketChannel;
		}

		public DataProtocol getProtocol() {
			return protocol;
		}
	}
}
