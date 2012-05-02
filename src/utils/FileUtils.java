package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import android.os.Environment;

public class FileUtils {
	static public final String SDPATH;
	static public final String SDPATH_zzq;
	static public final String SDPATH_YIJI_IMPORT;

	static {
		SDPATH = Environment.getExternalStorageDirectory() + "/";
		SDPATH_zzq = SDPATH + "zzq/";
		SDPATH_YIJI_IMPORT = SDPATH + "YIJI/import/";
	}

	public String getSDPATH() {
		return SDPATH;
	}

	public FileUtils() {
	}

	public String readFileFrom_Yiji_import(String filename) {
		System.out.println("reading file");
		File file = new File(FileUtils.SDPATH_YIJI_IMPORT + filename);
		return readFileFrom(file);
	}

	
	public String readFileFrom(File file) {
		// ======================
		if (!file.exists()) {
			System.out.println("not exists");
			return null;
		}
		FileInputStream is;
		BufferedReader br;
		String result = "";
		try {
			is = new FileInputStream(file);
			br = new BufferedReader(new InputStreamReader(is, "utf-8"));
			String line;
			while ((line = br.readLine()) != null) {
				if (line.length() > 0) {
					result += (line+"\n");
					System.out.println(line);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("--------->reading file end");
		return result;
	}
	
	
	/**
	 * 
	 * @throws IOException
	 */
	public File creatSDFile(String fileName) throws IOException {
		File file = new File(SDPATH + fileName);
		file.createNewFile();
		return file;
	}

	/**
	 * ��SD���ϴ���Ŀ¼
	 * 
	 * @param dirName
	 */
	public File creatSDDir(String dirName) {
		File dir = new File(SDPATH + dirName);
		dir.mkdirs();
		return dir;
	}

	/**
	 * �ж�SD���ϵ��ļ����Ƿ����
	 */
	public boolean isFileExist(String fileName) {
		File file = new File(SDPATH + fileName);
		return file.exists();
	}

	/**
	 * ��һ��InputStream��������д�뵽SD����
	 */
	public File write2SDFromInput(String path, String fileName,
			InputStream input) {
		File file = null;
		OutputStream output = null;
		try {
			creatSDDir(path);
			file = creatSDFile(path + fileName);
			output = new FileOutputStream(file);
			byte buffer[] = new byte[4 * 1024];
			int len;
			while ((len = input.read(buffer)) != -1) {
				output.write(buffer, 0, len);
			}
			output.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				output.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return file;
	}

}