package org.asura.core.server;

public interface DataSender {
	
	interface Factory{
		
		DataSender getServerFor(String destination);
		
		DataSender getClientFor(String destination);
	}
	
	void sendData(String receiver, Object data);

}
