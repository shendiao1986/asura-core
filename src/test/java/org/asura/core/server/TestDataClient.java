package org.asura.core.server;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

public class TestDataClient {

	@Test
	public void test() {
		DataClient client=DataClient.getReference("test-aaa");
		client.addClientListener(new DataClientListener() {
			
			@Override
			public void idleProcessing(DataClient source) {
				System.out.println("idleProcessing"+source.getName());
			}
			
			@Override
			public void fatalError(String errorMessage, DataClient source) {
				 System.out.println("fatalError"+source.getName());
			}
			
			@Override
			public void dataReceived(Object obj, DataClient source, String sourceConn) {
				System.out.println("object received from "+source.getName() +": "+ obj.toString());
			}
			
			@Override
			public void connectionTerminated(String id, DataClient source) {
                System.out.println("connectionTerminated"+source.getName());
			}
			
			@Override
			public void connectionEstablished(String id, DataClient source) {
				System.out.println("connectionEstablished"+source.getName());				
			}
		});
		
		DataClient.runClient("test-aaa");
		
		try {
			client.beginConnection("localhost", 22038, "a1");
		} catch (IOException e) {
			e.printStackTrace();
		}
		client.sendData("a1", new String("test1"));
		try {
            Thread.sleep(500000);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
	}

}
