package org.asura.core.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class IPUtil {
	
	public static String getLocalIp() {
		List<String> ips = new ArrayList<String>();
		NetworkInterface inter;
		try {
			Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces();
			while (e.hasMoreElements()) {
				inter = e.nextElement();
				Enumeration<InetAddress> en = inter.getInetAddresses();
				while (en.hasMoreElements()) {
					InetAddress dress = en.nextElement();
					String ip = dress.getHostAddress();
					if (StringUtil.isNumberString(ip)) {
						ips.add(ip);
					}
				}
			}
		} catch (SocketException e1) {
			e1.printStackTrace();
		}

		if (ips.size() > 0) {
			for (String ip : ips) {
				if ((!(ip.equals("127.0.0.1"))) && (!(ip.startsWith("192.168")))) {
					return ip;
				}
			}

			for (String ip : ips) {
				if (!(ip.equals("127.0.0.1"))) {
					return ip;
				}
			}
		}

		return "";
	}
}
