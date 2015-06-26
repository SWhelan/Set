package set;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class SetFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	private static final String SET_FRAME_TITLE = "Set";
	private static final String CARDS_LEFT_INFO = "Cards Left: ";
	private static final String NUM_SETS_FOUND_INFO = "Number of Sets Found: ";
	private static final String SET_FOUND_INFO = "A set!";
	private static final String SET_NOT_FOUND_INFO = "Not a set.";
	private static final String ADD_CARDS_BUTTON_TEXT = "Add Cards";
	private static final String NEW_GAME_BUTTON_TEXT = "New Game";
	private static final String CHECK_SETS_BUTTON_TEXT = "Check for Sets";
	private static final int TOTAL_NUM_CARDS = 81;
	private static final int INITIAL_WIDTH = 620;
	private static final int INITIAL_HEIGHT = 520;
	private JPanel cardsPanel = new JPanel(new GridLayout(3, 4));
	private JPanel statusPanel = new JPanel();
	private boolean lastWasASet = false;
	private boolean userAskedForCards = false;
	private JButton addCardsButton;
	private JButton newGameButton;
	private JButton checkForSets;
	private ImageIcon img;
	private Set game;

	public SetFrame(final Set game){
		this.game = game;
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		} catch (InstantiationException e1) {
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		} catch (UnsupportedLookAndFeelException e1) {
			e1.printStackTrace();
		}
		img = new ImageIcon(getClass().getResource("images/purple_diamond_filled.png"));
		this.setIconImage(img.getImage());
		this.setSize(INITIAL_WIDTH, INITIAL_HEIGHT);
		this.setMinimumSize(new Dimension(INITIAL_WIDTH, INITIAL_HEIGHT));
		
		JPanel contentPanel = new JPanel(new BorderLayout());
		contentPanel.add(cardsPanel, BorderLayout.CENTER);
		statusPanel.add(new JLabel(CARDS_LEFT_INFO + game.getCardsLeft()));
		statusPanel.add(new JLabel(NUM_SETS_FOUND_INFO + game.getNumSetsFound()));
		
		addCardsButton = new JButton(ADD_CARDS_BUTTON_TEXT);
		addCardsButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				userAskedForCards = true;
				game.addCards();
				userAskedForCards = false;
			}
		});
		
		newGameButton = new JButton(NEW_GAME_BUTTON_TEXT);
		newGameButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				game.newGame();
			}
		});
		
		checkForSets = new JButton(CHECK_SETS_BUTTON_TEXT);
		checkForSets.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Component parentFrame = ((Component)((Component)e.getSource()).getParent().getParent());
				if(game.hasRemainingSets()){
					JOptionPane.showMessageDialog(parentFrame, (Object)"There is at least one remaining set.", "Sets Left", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(img.getImage()));
				} else {
					JOptionPane.showMessageDialog(parentFrame, (Object)"There are not any remaining sets.", "No Sets Left", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(img.getImage()));
				}
			}
		});
		
		statusPanel.add(addCardsButton);
		statusPanel.add(newGameButton);
		statusPanel.add(checkForSets);
		
		contentPanel.add(statusPanel, BorderLayout.PAGE_END);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.add(contentPanel);
		this.setTitle(SET_FRAME_TITLE);
		this.setVisible(true);
	}
	
	public JPanel getCardsPanel() {
		return cardsPanel;
	}

	public JPanel getStatusPanel() {
		return statusPanel;
	}
	
	public void removeCards(){
		Component[] components = cardsPanel.getComponents();
		for(int i = 0; i < components.length; i++){
			CardPanel button = (CardPanel)components[i];
			if(button.getDeckID() == game.getCard1ID() || button.getDeckID() == game.getCard2ID() || button.getDeckID() == game.getCard3ID()){
				cardsPanel.remove(components[i]);
			}
		}
		cardsPanel.repaint();
	}

	public void updateStatusPanel(boolean b) {
		statusPanel.removeAll();
		statusPanel.add(new JLabel(CARDS_LEFT_INFO + Integer.toString(game.getCardsLeft())));
		statusPanel.add(new JLabel(NUM_SETS_FOUND_INFO + Integer.toString(game.getNumSetsFound())));
		if(b){
			statusPanel.add(new JLabel(SET_FOUND_INFO));
			lastWasASet = true;
		} else {
			statusPanel.add(new JLabel(SET_NOT_FOUND_INFO));
			lastWasASet = false;
		}
		
		statusPanel.add(addCardsButton);
		statusPanel.add(newGameButton);
		statusPanel.add(checkForSets);
		statusPanel.repaint();
	}
	
	public void displayCards(int num){
		int rand;
		int numBoardCards = cardsPanel.getComponents().length;
		if(numBoardCards < Set.INITIAL_NUM_CARDS || userAskedForCards) {
			if(!game.hasEnoughCards(num)){
				num = game.getCardsLeft();
			}
			for(int i = 0; i < num; i++){
				do{
					rand = (int) Math.floor(Math.random()*TOTAL_NUM_CARDS);
				} while (game.getUsed()[rand]);			
				displayCard(rand);
				game.getUsed()[rand] = true;
			}
			if(game.getCardsLeft() < 1){
				if(!game.hasRemainingSets()){
					game.gameOver();
				}
			}
			this.updateStatusPanel(this.getLastWasASet());
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			double maxWidth = screenSize.getWidth();
			double maxHeight = screenSize.getHeight();
			int numColumns = this.getCardsPanel().getComponents().length/3;
			if(this.getWidth() < maxWidth && this.getHeight() < maxHeight){
				this.setMinimumSize(new Dimension(this.getWidth(), this.getHeight()));
				this.pack();
				this.setMinimumSize(new Dimension((numColumns * 100) + 20, INITIAL_HEIGHT));
			} else {
				Rectangle bounds = getMaximizedBounds(); 
				this.setMaximizedBounds(bounds);
				this.setExtendedState(JFrame.MAXIMIZED_BOTH);
				this.validate();
			}
		}
	}
	
	public void displayCard(int i){
		CardPanel card = new CardPanel(game, i);
		card.addMouseListener(game.getListener());
		cardsPanel.add(card);
	}

	public void deselectAll() {
		Component[] components = cardsPanel.getComponents();
		for(int i = 0; i < components.length; i++){
			CardPanel button = (CardPanel)components[i];
			if(button.getSelected()){
				button.deselect();
			}
		}
	}

	public boolean getLastWasASet() {
		return lastWasASet;
	}

	public void deactivateAddCardsButton() {
		addCardsButton.setEnabled(false);
	}

}
