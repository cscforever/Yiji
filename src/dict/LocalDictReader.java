package dict;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.R.string;
import android.content.res.AssetManager;
import android.webkit.WebView;

public class LocalDictReader {
	public static AssetManager ASSET_MANAGER = null;
	private static final String ENCODING = "utf-8";
	private static final String HEAD_FILE = "dict.conf";
	private static final int MAX_LENGTH = 12288;
	private static final int QUERY_TYPE_C2E = 1;
	// private static final int QUERY_TYPE_E2C;
	private static final String[] QUERY_TYPE_NAME;
	private HeaderFile conf;
	private String path;

	static {
		String[] arrayOfString = new String[2];
		arrayOfString[0] = "e2c_";
		arrayOfString[1] = "c2e_";
		QUERY_TYPE_NAME = arrayOfString;

	}

	public LocalDictReader(AssetManager paramAssetManager, String paramString)
			throws IOException {
		ASSET_MANAGER = paramAssetManager;
		this.path = paramString;
		if (!this.path.endsWith("/"))
			this.path += "/";
		// System.out.println( new File( this.path + "dict.conf" ).exists());
		this.conf = new HeaderFile(this.path + "dict.conf");
	}

	private static int doBinarySearch(String[] paramArrayOfString,
			String paramString) {
		int i = -1;

		for (i = paramArrayOfString.length - 1; i >= 0; i--) {
			if (paramArrayOfString[i].compareTo(paramString) <= 0) {
//				System.out.println("this dict start with="
//						+ paramArrayOfString[i]);
				break;
			}
		}
		return i;
	}

	private DefinitionFile getDefinitionFile(int type, int index)
			throws IOException {
		// System.out.println("definition file path=" + path);
		return new DefinitionFile(this.path, type, index);
	}

	private static String getFileName(String dir, int type, int index,
			boolean is_Idx) {
		StringBuffer localStringBuffer = new StringBuffer();
		if (is_Idx) {
			localStringBuffer.append(dir);
			localStringBuffer.append(QUERY_TYPE_NAME[type]);
			localStringBuffer.append(index);
			localStringBuffer.append(".idx");
		} else {
			localStringBuffer.append(dir);
			localStringBuffer.append(QUERY_TYPE_NAME[type]);
			localStringBuffer.append(index);
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
			return 0;
		return 1;
	}

	public int getC2EWordNumber() {
		return this.conf.c2eWordNumber;
	}

	public String getDefinition(String paramString) throws IOException {
		String str1 = paramString.toLowerCase();
		int queryType = getQueryType(str1);
//		System.out.println("querytpe=" + queryType);
		// 0--英文
		// 1--中文
		// ============================
		int indexNum = this.conf.getIndexNumber(queryType, str1);
//		System.out.println("indexnum=" + indexNum);
		String word = "";
		if (indexNum == -1)
			word = null;
		else {
			// 得到单词的ID
			int wordNum = getIndexFile(queryType, indexNum)
					.getWordNumberTestVersion(str1);

//			System.out.println("wordNum-->" + wordNum);
			if (wordNum == -1) {
				word = null;
			}
			// 得到释义的文件
			DefinitionFile localDefinitionFile = getDefinitionFile(queryType,
					indexNum);
			try {
				// 获得释义---using 获得的单词ID 和 单词
				String str2 = localDefinitionFile.getDefinition(str1, wordNum);
				word = str2;
			} catch (Exception e) {
				word = null;
			} finally {
				localDefinitionFile.close();
			}
		}
		return word;
	}

	public int getE2CWordNumber() {
		return this.conf.e2cWordNumber;
	}

	public String[] getSuggest(String paramString, int suggestNumber)
			throws IOException {
		String str1 = paramString.toLowerCase();
		int queryType = getQueryType(str1);
//		System.out.println("querytpe=" + queryType);
		// 0--英文
		// 1--中文
		// ============================
		int indexNum = this.conf.getIndexNumber(queryType, str1);
//		System.out.println("indexnum=" + indexNum);
		String[] suggests = new String[suggestNumber];
		if (indexNum == -1) {
			suggests = new String[1];
			suggests[0] = str1;
			return suggests;
		} else {
			// 得到单词的ID

			LocalDictReader.IndexFile.SuggestResult localSuggestResult;
			localSuggestResult = getIndexFile(queryType, indexNum).getSuggest(
					str1, suggestNumber);
			if (localSuggestResult == null) {
				suggests = new String[1];
				suggests[0] = str1;
				return suggests;
			} else {
				String[] arrayOfString2 = localSuggestResult.suggest;
				return arrayOfString2;
			}
		}
	}

	public int getWordSize() {
		return this.conf.wordNumber;
	}

	public static class DefinitionFile extends LocalDictReader.MobileFile {

		public DefinitionFile(String path, int type, int index)
				throws IOException {
			// false--def
			// true--index
			super(path, false, type, index);
		}

		public String getDefinition(String paramString, int wordNum)
				throws IOException {
			DataInput localDataInput = getInput();
			byte[] arrayOfByte = new byte[1024];
			String str = null;
			for (int i = 0; i <= wordNum; i++) {
				int j = localDataInput.readShort();

				if (i == wordNum) {
//					System.out.println("i== wordnum");
					localDataInput.readFully(arrayOfByte, 0, j);
					str = new String(arrayOfByte, 0, j, "utf-8");
				}
				localDataInput.skipBytes(j);
			}
//			System.out.println("最后获得的str=" + str);
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

			DataInputStream dis = new DataInputStream(
					LocalDictReader.ASSET_MANAGER.open(paramString));
			// byte[] arrayOfByte = new byte[256];
			//
			// dis.readFully(arrayOfByte, 0, 24);
			// System.out.println( new String(arrayOfByte, 0, 24, "utf-8"));

			read(dis);
		}

		private void read(DataInput paramDataInput) throws IOException {
			byte[] arrayOfByte = new byte[256];
			this.dictName = LocalDictReader.MobileFile.readAscii(
					paramDataInput, 24, arrayOfByte);
			this.version = LocalDictReader.MobileFile.readAscii(paramDataInput,
					12, arrayOfByte);
			this.wordNumber = LocalDictReader.MobileFile.readInt(
					paramDataInput, 6, arrayOfByte);
			this.e2cWordNumber = LocalDictReader.MobileFile.readInt(
					paramDataInput, 6, arrayOfByte);
			this.c2eWordNumber = LocalDictReader.MobileFile.readInt(
					paramDataInput, 6, arrayOfByte);
			this.author = LocalDictReader.MobileFile.readAscii(paramDataInput,
					12, arrayOfByte);
			this.date = LocalDictReader.MobileFile.readAscii(paramDataInput, 8,
					arrayOfByte);
			this.description = LocalDictReader.MobileFile.readAscii(
					paramDataInput, 32, arrayOfByte);
			this.c2eFileNumber = paramDataInput.readInt();
			this.e2cFileNumber = paramDataInput.readInt();
			this.c2eIndex = new String[this.c2eFileNumber];
			this.e2cIndex = new String[this.e2cFileNumber];

			for (int i = 0; i < this.c2eFileNumber; i++) {
				this.c2eIndex[i] = LocalDictReader.MobileFile.readString(
						paramDataInput, arrayOfByte);

			}
			// System.out.println("===============");
			for (int j = 0; j < this.e2cFileNumber; j++) {
				this.e2cIndex[j] = LocalDictReader.MobileFile.readString(
						paramDataInput, arrayOfByte);
				// System.out.println(this.e2cIndex[j]);
			}
			// System.out.println("===============");

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
			if (paramInt == 0) {
				i = LocalDictReader.doBinarySearch(this.e2cIndex, paramString);
			} else {
				i = LocalDictReader.doBinarySearch(this.c2eIndex, paramString);
			}
			return i;
		}
	}

	public static class IndexFile {
		private byte[] data;
		private int number;
		private int realLength;

		public IndexFile(String dir, int type, int index) throws IOException {
			String str = LocalDictReader.getFileName(dir, type, index, true);
//			System.out.println("file name-->" + str);
			this.data = new byte[12288];
			// DataInputStream localDataInputStream = new DataInputStream(
			// new FileInputStream(new File(str)));

			DataInputStream localDataInputStream = new DataInputStream(
					LocalDictReader.ASSET_MANAGER.open(str));
			try {
				this.number = new DataInputStream(localDataInputStream)
						.readInt();
//				System.out.println("word number = " + number);

				this.realLength = localDataInputStream
						.read(this.data, 0, 12288);
//				System.out.println("data length==" + realLength);

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				localDataInputStream.close();
			}
		}

		public SuggestResult getSuggest(String word, int paramInt)
				throws IOException {
			byte[] arrayOfByte = word.getBytes("utf-8");
			int i = 0;
			int j;
			boolean getIt = true;
			j = 0;
			int count = 0;
			SuggestResult suggestResult = new SuggestResult();
			String resultString[] = new String[10];
			while (j < number && count<10) {
				int wordLength = this.data[i];
				// System.out.println("k-->"+wordLength);
				if (wordLength >= arrayOfByte.length) {
					// 如果单词的长度和单词的长度相同
//					System.out.println("bigger or same length");
					String suggest = new String(data, i + 1, wordLength);

//					System.out.println(suggest);
					for (int n = 0; n < arrayOfByte.length; n++) {
						// System.out.println((char)data[(n + (i + 1))] + ","
						// + (char)arrayOfByte[n]);
						if (this.data[(n + (i + 1))] != arrayOfByte[n]) {
							getIt = false;
//							System.out.println(i);
							break;
						}
					}
					if (getIt) {
//						System.out.println("success");
						resultString[count] = suggest;
						count++;
						// break;
					}
					getIt = true;
				}
				i += wordLength + 1;
				j++;
			}
			if (count != 0 && resultString != null && resultString[0] != null
					&& resultString[0].length() != 0) {
				int length = Math.min(paramInt, count);
				suggestResult.number = length;
				suggestResult.suggest = new String[length];
				for (int k = 0; k <length; k++) {
					suggestResult.suggest[k] = resultString[k];
				}
				suggestResult.continued = false;
			} else {
				suggestResult.number = 1;
				suggestResult.suggest = new String[1];
				suggestResult.suggest[0] = word;
				suggestResult.continued = false;
			}
			

			return suggestResult;

		}

		public int getWordNumberTestVersion(String word) throws IOException {
			byte[] arrayOfByte = word.getBytes("utf-8");
			int i = 0;
			int result = -1;
			int j;
			boolean getIt = true;
			j = 0;

			while (j < number) {
				int wordLength = this.data[i];
				// System.out.println("k-->"+wordLength);

				if (wordLength == arrayOfByte.length) {
					// 如果单词的长度和单词的长度相同
//					System.out.println("same length");
//					System.out.println(new String(data, i + 1, wordLength));
					for (int n = 0; n < wordLength; n++) {
						// System.out.println((char)data[(n + (i + 1))] + ","
						// + (char)arrayOfByte[n]);
						if (this.data[(n + (i + 1))] != arrayOfByte[n]) {
							getIt = false;
//							System.out.println(i);
							break;
						}

					}

					if (getIt) {
//						System.out.println("success");
						result = j;
						break;
					}
					getIt = true;
				}
				i += wordLength + 1;
				j++;
			}

			if (j >= this.number) {
				result = -1;
				return result;
			}
			return result;
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
		protected boolean isIndexFile;
		protected InputStream is;

		public MobileFile(String path, boolean isIndexFile, int type, int index)
				throws IOException {
			this.isIndexFile = isIndexFile;
			String desPath = LocalDictReader.getFileName(path, type, index,
					isIndexFile);
			this.is = LocalDictReader.ASSET_MANAGER.open(desPath);
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
			if (!this.isIndexFile)
				this.is.close();
		}

		public DataInput getInput() {
			if (this.isIndexFile) {
				return new DataInputStream(new ByteArrayInputStream(this.data));
			}
			return new DataInputStream(this.is);
		}
	}
}

/*
 * Location: D:\fanbianyi\youdao.jar Qualified Name:
 * com.youdao.dict.LocalDictReader JD-Core Version: 0.6.0
 */