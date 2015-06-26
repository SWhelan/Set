package set;

public class Card {

	private int color = 0;
	private int number = 0;
	private int symbol = 0;
	private int shade = 0;
	
	public Card(){
		this(0, 0, 0, 0);
	}
	
	public Card(int color, int number, int symbol, int shade) {
		this.color = color;
		this.number = number;
		this.symbol = symbol;
		this.shade = shade;
	}
	
	public int getColor() {
		return color;
	}
	public String getColorName() {
		switch(color){
		case 0:
			return "purple";
		case 1:
			return "green";
		default:
			return "red";
		}
	}
	public void setColor(int color) {
		this.color = color;
	}
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public int getSymbol() {
		return symbol;
	}
	public String getSymbolName() {
		switch(symbol){
		case 0:
			return "diamond";
		case 1:
			return "squiggly";
		default:
			return "oval";
		}
	}
	public void setSymbol(int symbol) {
		this.symbol = symbol;
	}

	public int getShade() {
		return shade;
	}

	public String getShadeName() {
		switch(shade){
		case 0:
			return "lined";
		case 1:
			return "empty";
		default:
			return "filled";
		}
	}
	public void setShade(int shade) {
		this.shade = shade;
	}

	public String getImageFileName() {
		return this.getColorName() + "_" + this.getSymbolName() + "_" + this.getShadeName();
	}
	
}
