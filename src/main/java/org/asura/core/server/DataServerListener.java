package org.asura.core.server;

public interface DataServerListener {

	void dataReceived(Object obj, DataServer source, String sourceConn);
	
    void fatalError(String errorMessage, DataServer source);

    void connectionTerminated(String id, DataServer source);

    void connectionEstablished(String id, DataServer source);

    void idleProcessing(DataServer source);

}
