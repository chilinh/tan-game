/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gametan;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author linh
 */

public class Player {

    public Player(String name) {
        this.name = name;
        this.type = Player.PlayerType.PERSON;
        this.state = Player.PlayerState.NO_STAGE;
        this.sessionState = PlayerSessionState.WAIT;
        this.sessionType = Player.PlayerSessionType.DEACTIVE;
	this.cards = new ArrayCards();
	this.cardsCanPlay = new ArrayList<Card>();
        this.showHelp = false;
        
        this.level = Level.EASY;
	this.kindCard = -1;
	this.kindRepBySpecial = new ArrayList<Integer>();
	this.baseAI = new ArrayList<ArrayList<Card>>();
    }
    
    public Player(String name, Level level) {
        this.name = name;
        this.type =  Player.PlayerType.BOT;
        this.state = Player.PlayerState.NO_STAGE;
        this.sessionState = PlayerSessionState.WAIT;
        this.sessionType = Player.PlayerSessionType.DEACTIVE;
	this.cards = new ArrayCards();
	this.cardsCanPlay = new ArrayList<Card>();
        this.showHelp = false;
        
	this.level = level;
	this.kindCard = -1;
	this.kindRepBySpecial = new ArrayList<Integer>();
	this.baseAI = new ArrayList<ArrayList<Card>>();
    }

    // Get set method
    public PlayerSessionState getSessionState() {
        return sessionState;
    }

    public void setSessionState(PlayerSessionState sessionState) {
        this.sessionState = sessionState;
    }

    public PlayerSessionType getSessionType() {
        return sessionType;
    }

    public void setSessionType(PlayerSessionType sessionType) {
        this.state = Player.PlayerState.NO_STAGE;
        this.sessionType = sessionType;
    }

    public PlayerState getState() {
        return state;
    }

    public void setState(PlayerState state) {
        this.state = state;
    }

    public boolean isShowHelp() {
        return showHelp;
    }

    public void setShowHelp(boolean showHelp) {
        this.showHelp = showHelp;
    }
    
    public PlayerType getType() {
        return type;
    }

    public String getName() {
	return name;
    }

    public ArrayCards getCards() {
        return cards;
    }

    public ArrayList<Card> getCardsCanPlay() {
        return cardsCanPlay;
    }
    
    // Addition method
    public int GetNumCards() {
        return this.cards.getNumCards();
    }
    
    public int GetNumCardsToTake() {
	int numCards = this.cards.getNumCards();
	if (numCards < 8)
	    return (8-numCards);
	return 0;
    }
    
    public void sortCards() {
	this.cards.sort();
    }
    
    public boolean canPlay(int indexCard) {
        return this.cardsCanPlay.contains(this.cards.getCard(indexCard));
    }
    
    public void ReceiveCards(ArrayList<Card> arrCards) {
	this.cards.addCards(arrCards);
    }
    
    public void ReceiveCard(Card card) {
	this.cards.addCard(card);
    }
    
    public Card PlayCards(int idCard) {
	this.cardsCanPlay.clear();
	return this.cards.takeCard(idCard);
    }
    
    public void CheckAvailable(ArrayList<Card> cardsInTurn, int numCardsOfDefender, boolean deckEmpty) {
        if (this.type == Player.PlayerType.BOT) this.sortCards();
	this.cardsCanPlay.clear();
	
	// Normal rule: have no card
	if (this.cards.isEmpty()) {
	    if (!deckEmpty) this.state = Player.PlayerState.MUST_TAKE_CARDS;
	    else this.state = Player.PlayerState.WIN;
	    return;
	}
	
	// Addition rule: have only one normal card, only play special cards
	boolean onlySpecial = false;
	if (this.cards.getNumCards() > 1 && this.cards.getNumNormCards() == 1)
	    onlySpecial = true;
	
	// Check available cards for attacker or supporter
	if (this.sessionType == Player.PlayerSessionType.ACTIVE || this.sessionType == Player.PlayerSessionType.SUPPORT) {
	    // Addition rule: defender have only one card, can not play Ace cards or special cards
	    boolean noAce = false;
	    if (numCardsOfDefender == 1) {
		if (onlySpecial) {
		    this.state = Player.PlayerState.CANT_ATTACK;
		    return;
		} else noAce = true;
	    }
	    // Check if not a new session
	    if (cardsInTurn.isEmpty()) {
		for (int j = 0; j < this.cards.getNumCards(); j++) {
		    Card card = this.cards.getCard(j);
		    if (noAce && card.getSize() == Card.ACE) continue;
		    if (!onlySpecial) this.cardsCanPlay.add(card);
		    else if (card.isSpecial()) this.cardsCanPlay.add(card);
		}
	    } else {
		for (int i = 0; i < cardsInTurn.size(); i++) {
		    Card card = cardsInTurn.get(i);
		    if (noAce && card.getSize() == Card.ACE) continue;
		    for (int j = 0; j < this.cards.getNumCards(); j++) {
			Card card2 = this.cards.getCard(j);
			if (card.isSameSize(card2)) {
			    if (!onlySpecial) this.cardsCanPlay.add(card2);
			    else if (card2.isSpecial()) this.cardsCanPlay.add(card2);
			}
		    }
		}
	    }
	    // Change stage of player
	    if (this.cardsCanPlay.size() > 0) this.state = Player.PlayerState.CAN_ATTACK;
	    else this.state = Player.PlayerState.CANT_ATTACK;
	    return;
	}
	// Check available cards for defender
	if (this.sessionType == Player.PlayerSessionType.PASSIVE) {
	    Card card = cardsInTurn.get(cardsInTurn.size()-1);
	    for (int j = 0; j < this.cards.getNumCards(); j++) {
		Card card2 = this.cards.getCard(j);
		// If card is not special, all special card can be added
		if (!card.isSpecial() && card2.isSpecial()) {
		    this.cardsCanPlay.add(card2);
		    continue;
		}
		if (card.isSameType(card2)) {
		    if (card.getSize() < card2.getSize()) {
			if (!onlySpecial) this.cardsCanPlay.add(card2);
			else if (card2.isSpecial()) this.cardsCanPlay.add(card2);
		    }
		}
	    }
	    // Change stage of player
	    if (this.cardsCanPlay.size() > 0) this.state = Player.PlayerState.CAN_DEFEND;
	    else this.state = Player.PlayerState.CANT_DEFEND;
	}
    }
    
    // For AI
    public void UpdateAI() {
        
    }
    
    public int ChooseCard() {
        return this.cards.findCard(this.cardsCanPlay.get(0));
    }
    
    private String name;
    private PlayerType type;
    private PlayerState state;
    private PlayerSessionType sessionType;
    private PlayerSessionState sessionState;
    private ArrayCards cards;
    private ArrayList<Card> cardsCanPlay;
    private boolean showHelp;
    
    // for AI
    private Level level;
    private int kindCard;
    private ArrayList<Integer> kindRepBySpecial;
    private ArrayList<ArrayList<Card>> baseAI;
    
    public static enum PlayerState {
        NO_STAGE, CAN_DEFEND, CANT_DEFEND, CAN_ATTACK, CANT_ATTACK, WIN, MUST_TAKE_CARDS
    }

    public enum PlayerType {
        PERSON, BOT
    }

    public enum PlayerSessionType {
        ACTIVE, PASSIVE, SUPPORT, DEACTIVE
    }

    public enum PlayerSessionState {
        PLAY, WAIT
    }
    
    public enum Level {
	EASY, MEDIUM, HARD
    }

    @Override
    public String toString() {
	return "Player{" + name + ", " + type + ", state=" + state + ", sessionType=" + sessionType + ", sessionState=" + sessionState
		+ "\n\tcards=" + cards + "\n\tcardsCanPlay=" + Arrays.toString(cardsCanPlay.toArray()) + "\n}";
    }
}
