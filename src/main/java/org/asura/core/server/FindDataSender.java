package org.asura.core.server;

public enum FindDataSender implements DataSender.Factory{
	
	INSTANCE;

	@Override
	public DataSender getServerFor(String destination) {
		return DataServer.getReference(destination);
	}

	@Override
	public DataSender getClientFor(String destination) {
		return DataClient.getReference(destination);
	}

}
