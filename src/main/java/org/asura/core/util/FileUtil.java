package org.asura.core.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.asura.core.data.DataIterator;
import org.asura.core.data.EmptyDataIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileUtil {

	private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);

	public static String[] getContentByLine(String fileName) {
		return getContentByLine(fileName, null);
	}

	public static String[] getContentByLine(String fileName, String charsetName) {
		try {
			return getContentByLine(new FileInputStream(fileName), charsetName);
		} catch (FileNotFoundException e) {
			logger.error("exception occurred", e);
		}

		return new String[0];
	}

	public static String[] getContentByLine(InputStream in, String charsetName) {
		ArrayList<String> result = new ArrayList<>();
		try {
			BufferedReader reader = null;
			if (charsetName != null) {
				reader = new BufferedReader(new InputStreamReader(in, charsetName));
			} else {
				reader = new BufferedReader(new InputStreamReader(in));
			}

			while (reader.ready()) {
				result.add(reader.readLine());
			}
			in.close();
			reader.close();
		} catch (IOException e) {
			logger.error("exception occurred", e);
		}

		return result.toArray(new String[0]);
	}

	public static void output(String fileName, byte[] bs) {
		try {
			FileUtils.writeByteArrayToFile(new File(fileName), bs);
		} catch (IOException e) {
			logger.error("exception occurred", e);
		}
	}

	public static byte[] getBytes(String fileName) {
		try {
			return FileUtils.readFileToByteArray(new File(fileName));
		} catch (IOException e) {
			logger.error("exception occurred", e);
		}

		return new byte[0];
	}

	public static String getUrlFileName(String url) {
		try {
			URL u = new URL(url);
			return getChar(u.getHost()) + "/" + getChar(u.getFile());
		} catch (MalformedURLException e) {
			logger.error("exception occurred", e);
		}

		return url;
	}

	private static String getChar(String str) {
		char[] cs = str.toCharArray();
		List<String> result = new ArrayList<>();
		for (char c : cs) {
			if ((StringUtil.isEnglishOrNumberCharacter(c)) || (c == '.') || (c == '&') || (c == '=') || (c == '/')
					|| (c == '%'))
				result.add(String.valueOf(c));
			else if (c == '?') {
				result.add("?");
			}
		}

		return StringUtil.getStringFromStrings(result, "");
	}

	public static DataIterator<String> getContent(String fileName, String charsetName) {
		try {
			return getContent(new FileInputStream(fileName), charsetName);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return new EmptyDataIterator<String>();
	}

	public static DataIterator<String> getContent(final FileInputStream stream, final String charsetName) {
		return new DataIterator<String>() {

			private BufferedReader reader;

			{
				try {
					if (charsetName != null) {
						reader = new BufferedReader(new InputStreamReader(stream, charsetName));
					} else {
						reader = new BufferedReader(new InputStreamReader(stream));
					}
				} catch (UnsupportedEncodingException e) {
					logger.error("exception occurred", e);
				}
			}

			@Override
			public boolean hasNext() {
				if (this.reader != null) {
					try {
						boolean has = this.reader.ready();
						if (!has) {
							close();
						}
						return has;
					} catch (IOException e) {
						return false;
					}
				}
				return false;
			}

			@Override
			public String next() {
				try {
					return this.reader.readLine();
				} catch (IOException e) {
					logger.error("exception occurred", e);
				}

				return "";
			}

			@Override
			public void close() {
				try {
					stream.close();
					this.reader.close();
				} catch (IOException e) {
					logger.error("exception occurred", e);
				}
			}

			@Override
			public void reset() {
				try {
					this.reader.reset();
				} catch (IOException e) {
					logger.error("exception occurred", e);
				}
			}
		};
	}

	public static void moveFiles(String sourceDic, String targetDic) {
		String[] files = new File(sourceDic).list();
		for (String file : files) {
			File oldFile = new File(sourceDic + "//" + file);
			File newFile = new File(targetDic + "//" + file);
			if (oldFile.exists()) {
				oldFile.renameTo(newFile);
			}
		}
	}

	public static void deleteFile(String path) {
		File file = new File(path);
		if (file.exists()) {
			file.delete();
		}
	}

	public static void createFolder(String path) {
		File file = new File(path);
		if (!(file.exists())) {
			file.mkdirs();
		}
	}

	public static void copyFile(String sourcePath, String targetPath) throws IOException {
		FileUtils.copyFile(new File(sourcePath), new File(targetPath));
	}

	public static boolean clearFolder(String path) {
		boolean flag = false;
		File file = new File(path);
		if (!(file.exists())) {
			return flag;
		}
		if (!(file.isDirectory())) {
			return flag;
		}
		String[] tempList = file.list();
		if ((tempList == null) || (tempList.length <= 0)) {
			return true;
		}
		File temp = null;
		for (int i = 0; i < tempList.length; ++i) {
			if (path.endsWith(File.separator))
				temp = new File(path + tempList[i]);
			else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				clearFolder(path + "/" + tempList[i]);
				delFolder(path + "/" + tempList[i]);
				flag = true;
			}
		}
		return flag;
	}

	public static boolean clearFolder(String path, String exceptFileName) {
		File file = new File(path);
		if (!(file.exists())) {
			return true;
		}
		if (!(file.isDirectory())) {
			return true;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; ++i) {
			if (path.endsWith(File.separator))
				temp = new File(path + tempList[i]);
			else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if ((temp.isFile()) && (!(temp.getName().equals(exceptFileName))) && (!(temp.delete()))) {
				return false;
			}

			if (temp.isDirectory()) {
				if (!(clearFolder(path + "/" + tempList[i]))) {
					return false;
				}
				if (!(delFolder(path + "/" + tempList[i]))) {
					return false;
				}
			}
		}

		return true;
	}

	public static boolean delFolder(String folderPath) {
		try {
			clearFolder(folderPath);
			String filePath = folderPath;
			filePath = filePath.toString();
			File myFilePath = new File(filePath);
			myFilePath.delete();
		} catch (Exception e) {
			return false;
		}

		return true;
	}

	public static void createFile(String filename) {
		File file = new File(filename);
		if (file.exists())
			return;
		try {
			file.createNewFile();
		} catch (IOException e) {
			logger.error("exception occurred", e);
		}
	}

	public static void createFile(String dic, String filename) {
		File file = new File(dic, filename);
		if (file.exists())
			return;
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static boolean isFileExisted(String dic, String fileName) {
		File file = new File(dic, fileName);
		return file.exists();
	}

	public static List<String> getAllFolderPath(String path) {
		List<String> list = new ArrayList<>();
		getAllFolderPath(list, path);
		return list;
	}

	public static void getAllFolderPath(List<String> list, String path) {
		File file = new File(path);

		File[] files = file.listFiles();

		for (File one : files) {
			if (one.isDirectory()) {
				list.add(one.getAbsolutePath());
				getAllFolderPath(list, one.getAbsolutePath());
			}
		}
	}

	public static int getAllFilesCount(String folder) {
		return getAllFileNames(folder).length;
	}

	public static String[] getAllFileNames(String folder) {
		LinkedList<File> list = new LinkedList<>();
		LinkedHashSet<String> files = new LinkedHashSet<>();
		File dir = new File(folder);

		File[] file = dir.listFiles();
		if (file == null) {
			return ((String[]) files.toArray(new String[0]));
		}
		for (int i = 0; i < file.length; ++i) {
			if (file[i].isDirectory())
				list.add(file[i]);
			else {
				files.add(file[i].getAbsolutePath());
			}
		}

		while (!(list.isEmpty())) {
			File tmp = (File) list.removeFirst();
			if (tmp.isDirectory()) {
				file = tmp.listFiles();
				if (file == null) {
					continue;
				}
				for (int i = 0; i < file.length; ++i) {
					if (file[i].isDirectory()) {
						list.add(file[i]);
					} else {
						files.add(file[i].getAbsolutePath());
					}
				}
			} else {
				files.add(tmp.getAbsolutePath());
			}
		}
		return files.toArray(new String[0]);
	}

	public static boolean deleteFile(String dic, String fileName) {
		File file = new File(dic, fileName);
		if (file.exists()) {
			return file.delete();
		}
		return true;
	}

	public static boolean copyFiles(String sourceDic, String targetDic) {
		try {
			FileUtils.copyDirectory(new File(sourceDic), new File(targetDic));
		} catch (IOException e) {
			logger.error("exception occurred", e);
		}
		return true;
	}

	public static String[] getFilesUnderFolder(String folder) {
		List<String> list = new ArrayList<>();
		File f = new File(folder);
		File[] fs = f.listFiles();
		if (fs != null) {
			for (File file : fs) {
				if (!(file.isDirectory())) {
					list.add(file.getAbsolutePath());
				}
			}
		}

		return list.toArray(new String[0]);
	}

	public static String[] getFoldersUnderFolder(String folder) {
		List<String> list = new ArrayList<>();
		File f = new File(folder);
		File[] fs = f.listFiles();
		if (fs != null) {
			for (File file : fs) {
				if ((!(file.isDirectory())) || (file.getAbsolutePath().equals(f.getAbsolutePath())))
					continue;
				list.add(file.getAbsolutePath());
			}

		}

		return ((String[]) list.toArray(new String[0]));
	}

	public static long getLastModifiedTime(String folder) {
		File f = new File(folder);
		File[] fileArray = f.listFiles();
		long lastmodified = -1L;
		if (fileArray != null) {
			for (int i = 0; i < fileArray.length; ++i) {
				if (lastmodified < fileArray[i].lastModified()) {
					lastmodified = fileArray[i].lastModified();
				}
			}
		}
		return lastmodified;
	}

	public static void output(Hashtable<String, Integer> list, int condition, String filePath) {
		output(list, condition, false, filePath);
	}

	public static void output(Hashtable<String, Integer> list, int condition, boolean append, String filePath) {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File(filePath), append));
			Iterator<String> it = list.keySet().iterator();
			while (it.hasNext()) {
				String key = it.next();
				if (((Integer) list.get(key)).intValue() > condition) {
					bw.write(key + "," + list.get(key));
					bw.write("\n");
				}
			}
			bw.close();
		} catch (Exception e) {
			logger.error("exception occurred", e);
		}
	}

	public static String getFileContent(String fileName, String charsetName) {
		return StringUtil.getStringFromStrings(getContentByLine(fileName, charsetName), "\n").trim();
	}

	public static void output(Collection<String> list, String filePath, String charsetName) {
		output((String[]) list.toArray(new String[0]), filePath, charsetName);
	}

	public static void output(String[] list, String filePath, String charsetName) {
		try {
			BufferedWriter bw = getWriter(filePath, charsetName);
			if (bw != null) {
				if (list.length > 0) {
					bw.write(list[0]);
					for (int i = 1; i < list.length; ++i) {
						if (list[i] != null) {
							bw.write("\n");
							bw.write(list[i]);
						}
					}
				}
				bw.close();
			}
		} catch (Exception localException) {
			logger.error("exception occurred", localException);
		}

	}

	public static BufferedWriter getWriter(String filePath, String charsetName) {
		try {
			return new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(new File(filePath), false), charsetName));
		} catch (UnsupportedEncodingException e) {
			logger.error("exception occurred", e);
		} catch (FileNotFoundException localFileNotFoundException) {
			logger.error("exception occurred", localFileNotFoundException);
		}
		return null;
	}

	public static BufferedReader getReader(String filePath, String charsetName) {
		try {
			return new BufferedReader(new InputStreamReader(new FileInputStream(filePath), charsetName));
		} catch (UnsupportedEncodingException e) {
			logger.error("exception occurred", e);
		} catch (FileNotFoundException e) {
			logger.error("exception occurred", e);
		}

		return null;
	}

}
