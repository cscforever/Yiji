package dict;

import android.content.res.AssetManager;
import android.util.Log;
import java.io.IOException;

public class LocalDictReaderContainer {
	private static final String TAG = "LocalDictReaderContainer";
	String dictDir;
	String dictSDdir;
	LocalDictReader ldr;
	LocalDictReaderViaSD ldrvsd;
	AssetManager manager;

	public LocalDictReaderContainer(AssetManager paramAssetManager,
			String paramString1, String paramString2) throws IOException {
		this.dictDir = paramString1;
		this.dictSDdir = paramString2;
		this.manager = paramAssetManager;
		try {
			this.ldrvsd = new LocalDictReaderViaSD(paramString2);
//			Log.d("LocalDictReaderContainer", "got ldrvsd");
		} catch (Exception localException) {
//			Log.d("LocalDictReaderContainer", "we have to use lib in assets");
			this.ldr = new LocalDictReader(paramAssetManager, paramString1);
		}
	}

	public String getDefinition(String paramString) throws IOException {
		try {
//			Log.d("LocalDictReaderContainer", "try ldrvsd");
			String str2 = this.ldrvsd.getDefinition(paramString);
			return str2;
		} catch (Exception localException) {
//			Log.d("LocalDictReaderContainer",
//					"something wrong with the lib in sd");
			if (this.ldr == null)
				this.ldr = new LocalDictReader(this.manager, this.dictDir);
			String str1 = this.ldr.getDefinition(paramString);
			return str1;
		}
	}

	public String[] getSuggest(String paramString, int paramInt)
			throws IOException {
		try {
			String[] arrayOfString2 = this.ldrvsd.getSuggest(paramString,
					paramInt);
			return arrayOfString2;
		} catch (Exception localException) {
			if (this.ldr == null)
				this.ldr = new LocalDictReader(this.manager, this.dictDir);
			String[] arrayOfString1 = this.ldr
					.getSuggest(paramString, paramInt);
			return arrayOfString1;
		}
	}
}

