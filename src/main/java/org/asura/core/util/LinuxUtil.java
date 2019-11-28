package org.asura.core.util;

import org.asura.core.util.machine.MachineResource;
import org.asura.core.util.math.NumberUtil;

public class LinuxUtil {
	public static String replace(String source, String target, String fileName) {
		String replace = "sed -i 's/" + source + "/" + target + "/g' " + fileName;
		String start = "/tmp/tmp.sh";
		String result = execute(replace, start);
		FileUtil.deleteFile(start);

		return result;
	}

	public static void createSh(String[] cmds, String fileName) {
		createSh(StringUtil.getStringFromStrings(cmds, "\n"), fileName);
	}

	public static void createSh(String content, String fileName) {
		FileUtil.output(new String[] { content }, fileName, "utf8");
		CommandUtil.execute("chmod 777 " + fileName);
	}

	public static String getStatus(String name) {
		String exe = "ps -ef|grep '" + name + "'|grep -v grep |awk '{print $2}'|xargs top -b -n 1 -p";
		return execute(exe);
	}

	public static String getStatusFormated(String name) {
		String status = getStatus(name);
		String memory = getMemory(status);
		String cpu = getCpu(status);

		if (memory.equals("N/A")) {
			return "";
		}
		return "mem:" + memory + ", cpu:" + cpu;
	}

	public static String getMemory(String top) {
		String[] lines = StringUtil.split(top.trim(), "\n");
		String lastLine = lines[(lines.length - 1)];

		if (StringUtil.containsNumber(lastLine)) {
			String[] ss = StringUtil.splitWithoutBlank(lastLine, " ");
			if (ss.length > 5) {
				return ss[5];
			}
		}

		return "N/A";
	}

	public static String getCpu(String top) {
		String[] lines = StringUtil.split(top.trim(), "\n");
		String lastLine = lines[(lines.length - 1)];
		if (StringUtil.containsNumber(lastLine)) {
			String[] ss = StringUtil.splitWithoutBlank(lastLine, " ");
			if (ss.length > 5) {
				return ss[8];
			}
		}

		return "N/A";
	}

	public static String getCpuPCount() {
		return execute("cat /proc/cpuinfo |grep \"physical id\"|sort |uniq|wc -l");
	}

	public static String getCpuLCount() {
		return execute("cat /proc/cpuinfo |grep \"processor\"|wc -l");
	}

	public static String getCpuLoad() {
		String string = execute("top -b -n 1 -p 111111");
		String[] ss = RegularExpressionUtil.getPatternValue(string, "load average:(.*)");
		if (ss.length > 0) {
			return ss[0];
		}
		return "";
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
				used = Integer.valueOf(ss[2]).intValue() / 1048576.0D;
			} catch (Exception localException) {
			}
		total = NumberUtil.getDouble(NumberUtil.getLenedDouble(total, 1));
		used = NumberUtil.getDouble(NumberUtil.getLenedDouble(used, 1));

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

		total = NumberUtil.getDouble(NumberUtil.getLenedDouble(total, 1));
		used = NumberUtil.getDouble(NumberUtil.getLenedDouble(used, 1));

		mr.setTotal(total);
		mr.setUsed(used);

		return mr;
	}

	public static String execute(String cmds) {
		long id = Thread.currentThread().getId();
		String name = "/tmp/tmp" + id + ".sh";
		FileUtil.output(new String[] { cmds }, name, "utf8");
		CommandUtil.execute("chmod 777 " + name);
		String result = CommandUtil.execute(name);
		FileUtil.deleteFile(name);

		return result;
	}

	public static String execute(String cmds, String fileName) {
		FileUtil.output(new String[] { cmds }, fileName, "utf8");
		CommandUtil.execute("chmod 777 " + fileName);
		String result = CommandUtil.execute(fileName);

		return result;
	}
}
