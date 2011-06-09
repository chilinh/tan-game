/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gametan;

import java.awt.Graphics;
import java.util.ArrayList;

/**
 *
 * @author linh
 */

public class Player {

    public static final int W_NAME = 100;
    public static final int H_NAME = 100;
    
    public Player(String name) {
        this.name = new NameView(name);
        this.type = PlayerType.PERSON;
        this.level = Level.EASY;
	this.baseAI = new ArrayList<ArrayCards>();
        for (int i = 0; i < 3; i++) {
            this.baseAI.add(new ArrayCards());
        }
    }
    
    public Player(String name, Level level) {
        this.name = new NameView(name);
        this.type =  Player.PlayerType.BOT;
	this.level = level;
	this.baseAI = new ArrayList<ArrayCards>();
        for (int i = 0; i < 3; i++) {
            this.baseAI.add(new ArrayCards());
        }
    }

    // Get set method
    public SessionType getSessionType() {
        return sessionType;
    }

    public void setSessionType(SessionType sessionType) {
        this.state = Player.PlayerState.NO_STATE;
        this.sessionType = sessionType;
    }

    public PlayerState getState() {
        return state;
    }

    public void setState(PlayerState state) {
        this.state = state;
    }

    public boolean isShowHelp() {
        return cards.isShowHelp();
    }

    public void setShowHelp(boolean showHelp) {
        cards.setShowHelp(showHelp);
	cards.sort();
    }
    
    public PlayerType getType() {
        return type;
    }

    public String getName() {
	return name.getName();
    }

    public void setShow(boolean show) {
	cards.setShow(show);
    }
    
    // Addition method    
    public int getNumCards() {
        return cards.getNumCards();
    }
    
    public int getNumCardsToTake() {
	return cards.getNumCardToTake();
    }
    
    public void sortCards() {
	cards.sort();
    }
    
    public void receiveCard(Card card) {
	cards.addCard(card);
    }
    
    public Card PlayCards() {
        for (Card card : cardsCanPlay) {
            card.setMark(false);
        }
        cardsCanPlay.clear();
        return cards.takeCard();
    }
    
    public boolean isVertical() {
        return cards.isVertical();
    }
    
    public void CheckAvailable(ArrayCards cardsInTurn, int numCardsOfDefender, boolean deckEmpty) {
	if (type == PlayerType.BOT) cards.sort();
	
	// Normal rule: have no card
	if (cards.isEmpty()) {
	    if (!deckEmpty) state = Player.PlayerState.SKIP;
	    else state = Player.PlayerState.WIN;
	    return;
	}
	
	// Addition rule: have only one normal card, only play special cards
	boolean onlySpecial = false;
	if (cards.getNumCards() > 1 && cards.getNumNormCards() == 1)
	    onlySpecial = true;
	
	// Check available cards for attacker or supporter
	if (sessionType == Player.SessionType.ACTIVE || sessionType == Player.SessionType.SUPPORT) {
	    // Addition rule: defender have only one card, can not play Ace cards or special cards
	    boolean noAce = false;
	    if (numCardsOfDefender == 1) {
		if (onlySpecial) {
		    state = Player.PlayerState.SKIP;
		    return;
		} else noAce = true;
	    }
	    // Check if not a new session
	    if (cardsInTurn.isEmpty()) {
		for (int j = 0; j < cards.getNumCards(); j++) {
		    Card card = cards.getCard(j);
		    if (noAce && card.getSize() == Card.ACE) continue;
		    if (!onlySpecial || card.isSpecial()) {
                        cardsCanPlay.add(card);
                        card.setMark(true);
                    }
		}
	    } else {
                int sz = cardsInTurn.getNumCards();
		for (int i = 0; i < sz; i++) {
		    Card card = cardsInTurn.getCard(i);
		    if (noAce && card.getSize() == Card.ACE) continue;
		    for (int j = 0; j < cards.getNumCards(); j++) {
			Card card2 = cards.getCard(j);
			if (card.isSameSize(card2) && (!onlySpecial || card2.isSpecial())) {
                            cardsCanPlay.add(card2);
                            card2.setMark(true);
			}
		    }
		}
	    }
	    // Change stage of player
	    if (cardsCanPlay.size() > 0) state = Player.PlayerState.CHOOSE;
	    else state = Player.PlayerState.SKIP;
	    return;
	}
	// Check available cards for defender
	if (sessionType == Player.SessionType.PASSIVE) {
	    Card card = cardsInTurn.getCard(cardsInTurn.getNumCards()-1);
            int sz = cards.getNumCards();
	    for (int j = 0; j < sz; j++) {
		Card card2 = cards.getCard(j);
		// If card is not special, all special card can be added
		if (!card.isSpecial() && card2.isSpecial()) {
		    cardsCanPlay.add(card2);
                    card2.setMark(true);
		    continue;
		}
		if (card.isSameType(card2) && (card.getSize() < card2.getSize()) && (!onlySpecial || card2.isSpecial())) {
                    cardsCanPlay.add(card2);
                    card2.setMark(true);
		}
	    }
	    // Change stage of player
	    if (cardsCanPlay.size() > 0) state = Player.PlayerState.CHOOSE;
	    else state = Player.PlayerState.TAKE;
	}
    }
    
    // For AI
    public void UpdateAI(Object obj, boolean sidePlay, PlayerState state) {
        if (obj == null)
            return;
        if (obj instanceof Card) {
            Card card = (Card) obj;
        }
    }
    
    public void ChooseCard() {
        cards.setChooseCard(cardsCanPlay.get(0));
    }
    
    // Drawing method
    public void setDrawing(int pnWidth, int pnHeight, int pnX, int pnY, boolean vertical) {
        cards.setVertical(vertical);
        int d = 0;
        if (vertical) {
            d = pnHeight - H_NAME;
            if (pnX == 0) {
                // Left
                cards.setDrawing(pnWidth, d, pnX, pnY);
                name.setDrawing(pnWidth, H_NAME, pnX, pnY + d);
            } else {
                // Right
                cards.setDrawing(pnWidth, d, pnX, pnY + H_NAME);
                name.setDrawing(pnWidth, H_NAME, pnX, pnY);
            }
        } else {
            d = pnWidth - W_NAME;
            if (pnY == 0) {
                // Top
                cards.setDrawing(d, pnHeight, pnX + W_NAME, pnY);
                name.setDrawing(W_NAME, pnHeight, pnX, pnY);
            } else {
                // Bottom
                cards.setDrawing(d, pnHeight, pnX, pnY);
                name.setDrawing(W_NAME, pnHeight, pnX + d, pnY);
            }
        }
    }
    
    public void draw(Graphics g) {
        name.draw(g);
        cards.draw(g);
    }
    
    public boolean checkCor(int x, int y) {
        return cards.checkCor(x, y);
    }
    
    public int getX() {
        return cards.getX();
    }
    
    public int getY() {
        return cards.getY();
    }
    
    private PlayerType type;
    private PlayerState state = PlayerState.NO_STATE;
    private SessionType sessionType = SessionType.DEACTIVE;
    private NameView name;
    private ArrayCardsPlayer cards = new ArrayCardsPlayer();
    private ArrayList<Card> cardsCanPlay = new ArrayList<Card>();
    
    private Level level;
    private int kindCard = -1;
    private ArrayList<Integer> kindRepBySpecial = new ArrayList<Integer>();
    private ArrayList<ArrayCards> baseAI;
    
    public enum PlayerState {
        NO_STATE, WAIT, PLAY, SKIP, TAKE, PLAYED, CHOOSE, CHANGE, WIN
    }

    public enum PlayerType {
        PERSON, BOT
    }

    public enum SessionType {
        ACTIVE, PASSIVE, SUPPORT, DEACTIVE
    }
    
    public enum Level {
	EASY, MEDIUM, HARD
    }
    
}
