package set;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

public class CardPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private static final Border selectedBorder = BorderFactory.createLineBorder(Color.black);
	private static final Border noBorder = new EmptyBorder(0,0,0,0);
	private int deckID;
	private Set set;
	private BufferedImage img;
	private boolean selected;

	public CardPanel(Set set, int deckID) {
		super();
		this.deckID = deckID;
		this.set = set;
		img = null;
		try {
			String fileName = set.deck[deckID].getImageFileName();
			URL url = getClass().getResource("images/" + fileName + ".png");
		    img = ImageIO.read(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.setMaximumSize(new Dimension(50, 100));
		this.setMinimumSize(new Dimension(50, 100));
		this.setSize(new Dimension(50, 100));
	}
	

	public int getDeckID() {
		return deckID;
	}

	public void setDeckID(int deckID) {
		this.deckID = deckID;
	}
	
	public void select(){
		this.setBorder(selectedBorder);
		this.selected = true;
	}
	
	public void deselect(){
		this.setBorder(noBorder);
		this.selected = false;
	}
	
	public boolean getSelected(){
		return selected;
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		int xAxisCoord = (int) Math.floor(this.getWidth()/2) - 50;
		int yAxisCoord = (int) Math.floor(this.getHeight()/2);
		int numSymbols = set.deck[deckID].getNumber();	
		if(numSymbols == 1){
			g.drawImage(img, xAxisCoord, yAxisCoord - 25, null);
		} else if (numSymbols == 2){
			g.drawImage(img, xAxisCoord, yAxisCoord - 50, null);
			g.drawImage(img, xAxisCoord, yAxisCoord, null);
		} else {
			g.drawImage(img, xAxisCoord, yAxisCoord - 75, null);
			g.drawImage(img, xAxisCoord, yAxisCoord - 25, null);
			g.drawImage(img, xAxisCoord, yAxisCoord + 25, null);
		}
	}

}