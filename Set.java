package set;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JOptionPane;

public class Set {
	public static final int INITIAL_NUM_CARDS = 12;
	private static final int TOTAL_NUM_CARDS = 81;
	private static final int NUM_CARDS_DRAWN = 3;
	private static final int NUM_SETS_FOUND = 1;

	protected Card[] deck = new Card[TOTAL_NUM_CARDS];
	private static boolean[] used = new boolean[TOTAL_NUM_CARDS];

	private SetFrame frame;
	private int numSetsFound = 0;
	private int cardsLeft = TOTAL_NUM_CARDS;
	private int selected = 0;
	private Card card1 = null;
	private Card card2 = null;
	private Card card3 = null;
	private int card1ID = -1;
	private int card2ID = -1;
	private int card3ID = -1;
	private MouseListener listener = new MouseAdapter() {
		public void mousePressed(MouseEvent e) {
			CardPanel button = ((CardPanel)(e.getSource()));
			if(!button.getSelected()){
				button.select();
				selected++;
				if(selected == 1){
					card1 = deck[button.getDeckID()];
					card1ID = button.getDeckID();
				} else if (selected == 2){
					card2 = deck[button.getDeckID()];
					card2ID = button.getDeckID();
				} else if (selected == 3){
					card3 = deck[button.getDeckID()];
					card3ID = button.getDeckID();
					if(check()){
						frame.removeCards();
						addCards();
						numSetsFound = numSetsFound + NUM_SETS_FOUND;
						frame.updateStatusPanel(true);
						frame.repaint();
						frame.getCardsPanel().repaint();
						reset();
					} else {
						frame.updateStatusPanel(false);
						reset();
					}
				}  
			} else {
				button.deselect();
				selected--;
				if(card1 == deck[button.getDeckID()]){
					card1 = card2;
					card1ID = card2ID;
					card2 = null;
					card2ID = -1;
				} else {
					card2 = null;
					card2ID = -1;
				}
			}
		}
	};

	public static void main(String[] args) {
		new Set();
	}

	public Set(){
		makeDeck();
		init();
	}

	/* 
	 * One deck:
	 * 81 cards
	 * 3 shapes 27 cards of each shape
	 * 3 colors 27 cards of each color
	 * 3 numbers 27 1's 27 2's 27 3's
	 */
	public void makeDeck(){
		int count = 0;
		for(int i = 0; i < 3; i++){
			for(int j = 0; j < 3; j++){
				for(int k = 0; k < 3; k++){
					for(int l = 0; l < 3; l++){
						deck[count] = new Card(i, j, k, l);
						count++;
					}
				}
			}
		}
	}

	private void init() {
		frame = new SetFrame(this);
		frame.displayCards(INITIAL_NUM_CARDS);
		frame.pack();		
	}

	public boolean hasEnoughCards(int num) {
		return cardsLeft >= num;
	}

	public void addCards() {
		if(cardsLeft > NUM_CARDS_DRAWN){
			cardsLeft = cardsLeft - NUM_CARDS_DRAWN;
		} else {
			cardsLeft = 0;
			frame.deactivateAddCardsButton();
		}
		frame.displayCards(NUM_CARDS_DRAWN);

	}

	public void reset(){
		card1 = null;
		card2 = null;
		card3 = null;
		card1ID = -1;
		card2ID = -1;
		card3ID = -1;
		selected = 0;
		frame.deselectAll();
	}

	public void newGame(){
		for(int i = 0; i < used.length; i++){
			used[i] = false;
		}
		numSetsFound = 0;
		cardsLeft = TOTAL_NUM_CARDS;
		selected = 0;
		reset();
		frame.setVisible(false);
		init();
	}

	/*
	 * There is a symbol, color, shading, and number associated with each card.
	 * Three cards are a set when each of the attributes, individually, are the same or different for all three cards.
	 */
	public boolean check(){
		if (selected == 3){	
			if((sameSymbols() || differentSymbols()) &&
					(sameColors() || differentColors())	&&
					(sameNumbers() || differentNumbers()) &&
					(sameShadings() || differentShadings())){
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	public boolean sameSymbols(){
		return card1.getSymbol() == card2.getSymbol() && card1.getSymbol() == card3.getSymbol();
	}

	public boolean differentSymbols(){
		return card1.getSymbol() != card2.getSymbol() && card1.getSymbol() != card3.getSymbol() && card2.getSymbol() != card3.getSymbol();
	}

	public boolean sameColors(){
		return card1.getColor() == card2.getColor() && card1.getColor() == card3.getColor();
	}

	public boolean differentColors(){
		return card1.getColor() != card2.getColor() && card1.getColor() != card3.getColor() && card2.getColor() != card3.getColor();
	}

	public boolean sameNumbers(){
		return card1.getNumber() == card2.getNumber() && card1.getNumber() == card3.getNumber();
	}

	public boolean differentNumbers(){
		return card1.getNumber() != card2.getNumber() && card1.getNumber() != card3.getNumber() && card2.getNumber() != card3.getNumber();
	}

	public boolean sameShadings(){
		return card1.getShade() == card2.getShade() && card1.getShade() == card3.getShade();
	}

	public boolean differentShadings(){
		return card1.getShade() != card2.getShade() && card1.getShade() != card3.getShade() && card2.getShade() != card3.getShade();
	}

	public boolean hasRemainingSets() {
		if(frame.getCardsPanel() != null){
			Component[] components = frame.getCardsPanel().getComponents();
			for(int i = 0; i < components.length; i++){
				card1 = deck[((CardPanel) components[i]).getDeckID()];
				for(int j = 0; j < components.length; j++){
					card2 = deck[((CardPanel) components[j]).getDeckID()];
					for (int k = 0; k < components.length; k++){
						card3 = deck[((CardPanel) components[k]).getDeckID()];
						boolean allThreeSame = i == j && j == k;
						selected = 3;
						if(!allThreeSame && check()){
							reset();
							return true;
						}
					}
				}
				reset();
			}
		}
		return false;
	}

	public void gameOver() {
		int reply = JOptionPane.showConfirmDialog(frame, (Object)"The game is over as there are no more cards and no more sets. Play again?", "Game Over", JOptionPane.YES_NO_OPTION);
		if(reply == JOptionPane.YES_OPTION){
			for(int i = 0; i < used.length; i++){
				used[i] = false;
			}
			numSetsFound = 0;
			cardsLeft = TOTAL_NUM_CARDS;
			selected = 0;
			reset();
			frame.setVisible(false);
			init();
		} else {
			System.exit(0);
		}
	}

	public Card[] getDeck() {
		return deck;
	}

	public boolean[] getUsed() {
		return used;
	}

	public int getNumSetsFound() {
		return numSetsFound;
	}

	public int getCardsLeft() {
		return cardsLeft;
	}

	public int getCard1ID() {
		return card1ID;
	}

	public int getCard2ID() {
		return card2ID;
	}

	public int getCard3ID() {
		return card3ID;
	}

	public MouseListener getListener() {
		return listener;
	}

	public void setNumSetsFound(int numSetsFound) {
		this.numSetsFound = numSetsFound;
	}

	public void setCardsLeft(int cardsLeft) {
		this.cardsLeft = cardsLeft;
	}

}
