package org.asura.core.util.machine;

import org.asura.core.util.CommandUtil;
import org.asura.core.util.StringUtil;
import org.asura.core.util.math.NumberUtil;

public class ResourceUtil {
	public static void main(String[] args) {
		System.out.println(getDisc());
	}

	public static MachineResource getCPU() {
		MachineResource mr = new MachineResource();
		mr.setDesc("CPU");
		mr.setUnit("G");

		return mr;
	}

	public static MachineResource getMemory() {
		MachineResource mr = new MachineResource();
		mr.setDesc("内存信息");
		mr.setUnit("G");

		String report = CommandUtil.execute("free");
		String[] lines = report.split("\n");

		double total = 0.0D;
		double used = 0.0D;

		if (lines.length > 2)
			try {
				String[] ss = StringUtil.splitWithoutBlank(lines[1], " ");
				total = Integer.valueOf(ss[1]).intValue() / 1048576.0D;

				ss = StringUtil.splitWithoutBlank(lines[2], " ");
				used = Integer.valueOf(ss[1]).intValue() / 1048576.0D;
			} catch (Exception localException) {
			}
		mr.setTotal(total);
		mr.setUsed(used);

		return mr;
	}

	public static MachineResource getDisc() {
		MachineResource mr = new MachineResource();
		mr.setDesc("硬盘信息");
		mr.setUnit("G");

		String report = CommandUtil.execute("df -h");
		String[] lines = report.split("\n");

		double total = 0.0D;
		double used = 0.0D;
		for (int i = 1; i < lines.length; ++i) {
			String[] vs = StringUtil.splitWithoutBlank(lines[i], " ");
			if (vs.length == 6) {
				if (vs[1].endsWith("G")) {
					total += NumberUtil.getDouble(vs[1]);
				}

				if (vs[2].endsWith("G")) {
					used += NumberUtil.getDouble(vs[2]);
				}
			}
		}

		mr.setTotal(total);
		mr.setUsed(used);

		return mr;
	}
}
