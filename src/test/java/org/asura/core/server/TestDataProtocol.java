package org.asura.core.server;

import org.junit.Test;

public class TestDataProtocol {

	@Test
	public void test() throws Exception {
		DataProtocol protocol=new DataProtocol();
		System.out.println(protocol.bytesNeeded());
		byte[] bytes=DataProtocol.convertObject(new String("shendiao"));
		protocol.bytesRead(bytes);
		System.out.println(protocol.bytesNeeded());
		System.out.println(protocol.getObject());
		System.out.println(protocol.bytesNeeded());
	}

}
