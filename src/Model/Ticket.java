package Model;

public class Ticket {
	private int wordCursor;
	private ReciteMode reciteMode = ReciteMode.ChnToEng;
	public int getWordCursor() {
		return wordCursor;
	}
	public void setWordCursor(int wordCursor) {
		this.wordCursor = wordCursor;
	}
	public ReciteMode getReciteMode() {
		return reciteMode;
	}
	public void setReciteMode(ReciteMode reciteMode) {
		this.reciteMode = reciteMode;
	}
	public Ticket(int wordCursor, ReciteMode reciteMode) {
		super();
		this.wordCursor = wordCursor;
		this.reciteMode = reciteMode;
	}
	
	@Override
	public String toString() {
		return "Ticket [wordCursor=" + wordCursor + ", reciteMode="
				+ reciteMode + "]";
	}
	

	

	
	
	

}
