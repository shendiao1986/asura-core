package org.asura.core.server;

import org.junit.Test;

public class TestDataServer {
	
	private String sendId;

	@Test
	public void test() {
		DataServer server=DataServer.getReference("shendiao-server");
		server.setBacklog(3);
		server.setPollTime(1000);
		server.setPort(22038);
		server.addServerListener(new DataServerListener() {
			
			@Override
			public void idleProcessing(DataServer source) {
                System.out.println("idleProcessing"+source.getName());
			}
			
			@Override
			public void fatalError(String errorMessage, DataServer source) {
				System.out.println("fatalError"+source.getName());
			}
			
			@Override
			public void dataReceived(Object obj, DataServer source, String sourceConn) {
                System.out.println("object received from "+source.getName() +": "+ obj.toString()+ " the source connection is :"+sourceConn);

				
			}
			
			@Override
			public void connectionTerminated(String id, DataServer source) {
				System.out.println("connectionTerminated"+source.getName());
			}
			
			@Override
			public void connectionEstablished(String id, DataServer source) {
				System.out.println("connectionEstablished"+source.getName());
				sendId=id;
			}
			
			
		});

		server.setAsInitialized();
		DataServer.runServer("shendiao-server");
		server.sendData(sendId, new String("this is from server"));
		try {
			Thread.sleep(3600);
		} catch (InterruptedException e) {

		}
	}

}
