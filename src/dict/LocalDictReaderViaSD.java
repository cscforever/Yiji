package dict;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class LocalDictReaderViaSD {
	private static final String ENCODING = "utf-8";
	private static final String HEAD_FILE = "dict.conf";
	private static final int MAX_LENGTH = 12288;
	private static final int QUERY_TYPE_C2E = 1;
	private static final int QUERY_TYPE_E2C = 2;
	private static final String[] QUERY_TYPE_NAME;
	private HeaderFile conf;
	private String path;

	static {
		String[] arrayOfString = new String[2];
		arrayOfString[0] = "e2c_";
		arrayOfString[1] = "c2e_";
		QUERY_TYPE_NAME = arrayOfString;
	}

	public LocalDictReaderViaSD(String paramString) throws IOException {
		this.path = paramString;
		if (!this.path.endsWith("/"))
			this.path += "/";
		this.conf = new HeaderFile(this.path + "dict.conf");
	}

	private static int doBinarySearch(String[] paramArrayOfString,
			String paramString) {
		int i;
		for (i = 0; i < paramArrayOfString.length; i++) {

			if (paramArrayOfString[i].equals(paramArrayOfString)) {
				return i;
			}

		}
		return -1;

	}

	private DefinitionFile getDefinitionFile(int paramInt1, int paramInt2)
			throws IOException {
		return new DefinitionFile(this.path, paramInt1, paramInt2);
	}

	private static String getFileName(String paramString, int paramInt1,
			int paramInt2, boolean paramBoolean) {
		StringBuffer localStringBuffer = new StringBuffer();
		if (paramBoolean) {
			localStringBuffer.append(paramString);
			localStringBuffer.append(QUERY_TYPE_NAME[paramInt1]);
			localStringBuffer.append(paramInt2);
			localStringBuffer.append(".idx");
		} else {
			localStringBuffer.append(paramString);
			localStringBuffer.append(QUERY_TYPE_NAME[paramInt1]);
			localStringBuffer.append(paramInt2);
			localStringBuffer.append(".def");
		}
		return localStringBuffer.toString();

	}

	private IndexFile getIndexFile(int paramInt1, int paramInt2)
			throws IOException {
		return new IndexFile(this.path, paramInt1, paramInt2);
	}

	private static int getQueryType(String paramString) {
		if (paramString.charAt(0) < 'Ā')
			;
		for (int i = 0;; i = 1)
			return i;
	}

	public int getC2EWordNumber() {
		return this.conf.c2eWordNumber;
	}

	public String getDefinition(String paramString) throws IOException {
		String str1 = paramString.toLowerCase();
		int i = getQueryType(str1);
		int j = this.conf.getIndexNumber(i, str1);
		String localObject2;
		if (j == -1)
			localObject2 = null;
		int k = getIndexFile(i, j).getWordNumber(str1);
		if (k == -1) {
			localObject2 = null;
		} else {
			DefinitionFile localDefinitionFile = getDefinitionFile(i, j);
			try {
				String str2 = localDefinitionFile.getDefinition(str1, k);
				localDefinitionFile.close();
				localObject2 = str2;
			} finally {
				localDefinitionFile.close();
			}
		}
		return localObject2;

	}

	public int getE2CWordNumber() {
		return this.conf.e2cWordNumber;
	}

	public String[] getSuggest(String paramString, int paramInt)
			throws IOException {
		String str = paramString.toLowerCase();
		int i = getQueryType(str);
		int j = this.conf.getIndexNumber(i, str);
		String[] arrayOfString1;
		if (j == -1) {
			arrayOfString1 = new String[1];
			arrayOfString1[0] = str;
		}

		do {
			LocalDictReaderViaSD.IndexFile.SuggestResult localSuggestResult;

			while (true) {
				localSuggestResult = getIndexFile(i, j).getSuggest(str,
						paramInt);
				if (localSuggestResult != null)
					break;
				arrayOfString1 = new String[1];
				arrayOfString1[0] = str;
			}
			String[] arrayOfString2 = {};
			if ((localSuggestResult.continued)
					&& (this.conf.fileExist(i, j + 1)))
				arrayOfString2 = getIndexFile(i, j + 1).getWords(
						paramInt - localSuggestResult.number);
			for (int m = 0; m < arrayOfString2.length; m++) {
				localSuggestResult.suggest[(m + localSuggestResult.number)] = arrayOfString2[m];
			}
			arrayOfString1 = localSuggestResult.suggest;
			if ((arrayOfString1 != null) && (arrayOfString1.length != 0)
					&& (arrayOfString1[0] != null)) {
				break;
			}
			arrayOfString1 = new String[1];
			arrayOfString1[0] = str;
		} while (arrayOfString1[0].equalsIgnoreCase(str));

		for (int k = arrayOfString1.length - 1; k > 0; k--) {
			arrayOfString1[k] = arrayOfString1[(k - 1)];
		}
		arrayOfString1[0] = str;
		return arrayOfString1;

	}

	public int getWordSize() {
		return this.conf.wordNumber;
	}

	public static class DefinitionFile extends LocalDictReaderViaSD.MobileFile {
		public DefinitionFile(String paramString, int paramInt1, int paramInt2)
				throws IOException {
			super(paramString);
		}

		public String getDefinition(String paramString, int paramInt)
				throws IOException {
			DataInput localDataInput = getInput();
			byte[] arrayOfByte = new byte[1024];
			String str = null;

			for (int i = 0; i <= paramInt; i++) {
				int j = localDataInput.readShort();
				if (i == paramInt) {
					localDataInput.readFully(arrayOfByte, 0, j);
					str = new String(arrayOfByte, 0, j, "utf-8");
				}
				localDataInput.skipBytes(j);
			}
			return str;

		}
	}

	public static class HeaderFile {
		public String author;
		public int c2eFileNumber;
		private String[] c2eIndex;
		private int c2eWordNumber;
		public String date;
		public String description;
		public String dictName;
		public int e2cFileNumber;
		private String[] e2cIndex;
		private int e2cWordNumber;
		public String version;
		public int wordNumber;

		public HeaderFile(String paramString) throws IOException {
			read(new DataInputStream(new DataInputStream(new FileInputStream(
					new File(paramString)))));
		}

		private void read(DataInput paramDataInput) throws IOException {
			byte[] arrayOfByte = new byte[256];
			this.dictName = LocalDictReaderViaSD.MobileFile.readAscii(
					paramDataInput, 24, arrayOfByte);
			this.version = LocalDictReaderViaSD.MobileFile.readAscii(
					paramDataInput, 12, arrayOfByte);
			this.wordNumber = LocalDictReaderViaSD.MobileFile.readInt(
					paramDataInput, 6, arrayOfByte);
			this.e2cWordNumber = LocalDictReaderViaSD.MobileFile.readInt(
					paramDataInput, 6, arrayOfByte);
			this.c2eWordNumber = LocalDictReaderViaSD.MobileFile.readInt(
					paramDataInput, 6, arrayOfByte);
			this.author = LocalDictReaderViaSD.MobileFile.readAscii(
					paramDataInput, 12, arrayOfByte);
			this.date = LocalDictReaderViaSD.MobileFile.readAscii(
					paramDataInput, 8, arrayOfByte);
			this.description = LocalDictReaderViaSD.MobileFile.readAscii(
					paramDataInput, 32, arrayOfByte);
			this.c2eFileNumber = paramDataInput.readInt();
			this.e2cFileNumber = paramDataInput.readInt();
			this.c2eIndex = new String[this.c2eFileNumber];
			this.e2cIndex = new String[this.e2cFileNumber];

			for (int i = 0; i < this.c2eFileNumber; i++) {
				for (int j = 0; j < this.e2cFileNumber; j++) {
					this.e2cIndex[j] = LocalDictReaderViaSD.MobileFile
							.readString(paramDataInput, arrayOfByte);
				}
				this.c2eIndex[i] = LocalDictReaderViaSD.MobileFile.readString(
						paramDataInput, arrayOfByte);
			}

		}

		public boolean fileExist(int paramInt1, int paramInt2) {
			boolean i;
			if (paramInt1 == 0)
				if (this.e2cIndex.length > paramInt2)
					i = true;
				else {
					i = false;

				}
			if (this.c2eIndex.length > paramInt2) {
				i = true;
			} else
				i = false;
			return i;
		}

		public int getIndexNumber(int paramInt, String paramString) {
			int i = 0;
			if (paramInt != 0) {
				i = LocalDictReaderViaSD.doBinarySearch(this.e2cIndex,
						paramString);
			} else {
				i = LocalDictReaderViaSD.doBinarySearch(this.c2eIndex,
						paramString);
			}
			return i;
		}
	}

	public static class IndexFile {
		private byte[] data;
		private int number;
		private int realLength;

		public IndexFile(String paramString, int paramInt1, int paramInt2)
				throws IOException {
			String str = LocalDictReaderViaSD.getFileName(paramString,
					paramInt1, paramInt2, true);
			this.data = new byte[12288];
			DataInputStream localDataInputStream = new DataInputStream(
					new FileInputStream(new File(str)));
			try {
				this.number = new DataInputStream(localDataInputStream)
						.readInt();
				this.realLength = localDataInputStream
						.read(this.data, 0, 12288);
			} catch (Exception e) {
				// TODO: handle exception
			} finally {
				localDataInputStream.close();
			}
		}

		public SuggestResult getSuggest(String paramString, int paramInt)
				throws IOException {
			// ===========================
			return null;

			// ===============================

		}

		public int getWordNumber(String paramString) throws IOException {
			byte[] arrayOfByte = paramString.getBytes("utf-8");
			int i = 0;
			int i1 = -1;

			for (int j = 0; j < this.number; j++) {

				int k = this.data[i];
				int m = 1;

				if (k == arrayOfByte.length) {
					for (int n = 0; n < k; n++) {
						i1 = j;
						if (this.data[(n + (i + 1))] == arrayOfByte[n]) {
							m = 0;
							break;

						}
					}
				}
				if (m != 0) {
					i += k + 1;
				}
			}
			return i1;

		}

		public String[] getWords(int paramInt) throws IOException {
			String[] arrayOfString = new String[paramInt];
			int i = 0;
			for (int j = 0;; j++) {
				if (j >= paramInt)
					return arrayOfString;
				int k = this.data[i];
				arrayOfString[j] = new String(this.data, i + 1, k, "utf-8");
				i += k + 1;
			}
		}

		private static class SuggestResult {
			private boolean continued;
			private int number;
			private String[] suggest;
		}
	}

	private static class MobileFile {
		protected byte[] data = null;
		protected InputStream is;

		public MobileFile(String paramString) throws IOException {
			this.is = new DataInputStream(new FileInputStream(new File(
					paramString)));
		}

		public static String readAscii(DataInput paramDataInput, int paramInt,
				byte[] paramArrayOfByte) throws IOException {
			paramDataInput.readFully(paramArrayOfByte, 0, paramInt);
			return new String(paramArrayOfByte, 0, paramInt, "utf-8");
		}

		public static int readInt(DataInput paramDataInput, int paramInt,
				byte[] paramArrayOfByte) throws IOException {
			return Integer.parseInt(readAscii(paramDataInput, paramInt,
					paramArrayOfByte).trim(), 10);
		}

		public static String readString(DataInput paramDataInput,
				byte[] paramArrayOfByte) throws IOException {
			return readAscii(paramDataInput, paramDataInput.readByte(),
					paramArrayOfByte);
		}

		public void close() throws IOException {
			this.is.close();
		}

		public DataInput getInput() {
			return new DataInputStream(this.is);
		}
	}
}

/*
 * Location: D:\fanbianyi\youdao.jar Qualified Name:
 * com.youdao.dict.LocalDictReaderViaSD JD-Core Version: 0.6.0
 */