package Model;

public class WordStructure {
	private String nameString;
	private String meanString;

	
	public WordStructure() {
		
	}

	public WordStructure(String nameString, String meanString) {
		this.nameString = nameString;
		this.meanString = meanString;
	}

	public String getNameString() {
		return nameString;
	}

	public void setNameString(String nameString) {
		this.nameString = nameString;
	}

	public String getMeanString() {
		return meanString;
	}

	public void setMeanString(String meanString) {
		this.meanString = meanString;
	}

	@Override
	public String toString() {
		return "WordStructure [nameString=" + nameString + ", meanString="
				+ meanString + "]";
	}

	
	
	
}
