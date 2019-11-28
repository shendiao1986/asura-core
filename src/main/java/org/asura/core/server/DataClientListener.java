package org.asura.core.server;

public interface DataClientListener {

	void dataReceived(Object obj, DataClient source, String sourceConn);
	
	void fatalError(String errorMessage, DataClient source);
	
    void connectionTerminated(String id, DataClient source);
    
    void connectionEstablished(String id, DataClient source);

    void idleProcessing(DataClient source);
}
