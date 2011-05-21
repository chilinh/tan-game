/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gametan;

import java.util.ArrayList;

/**
 *
 * @author linh
 */
public class Table {

    public static final int TWO_PLAYER = 2;
    public static final int THREE_PLAYER = 3;
    public static final int FOUR_PLAYER = 4;
    
    public Table(int type, int numBot) {
	this.type = type;
	this.cardsInDeck = new ArrayCardsDeck();
	this.cardsDone = new ArrayCards();
	this.players = new ArrayList<Player>();
	
	// Initilize player
	for (int i = 0; i < type-numBot; i++) {
	    this.players.add(new Player("Person " + i));
	}
	for (int i = 0; i < numBot; i++) {
	    this.players.add(new Player("Bot " + i, Player.Level.HARD));
	}
	
	this.cardsInDeck.randomArray();
	this.cardsInDeck.markSpecial(this.cardsInDeck.getSpecialRep());
    }
    
    // Get Set method
    public ArrayCards getCardsDone() {
	return cardsDone;
    }

    public Player getPlayer(int index) {
	return players.get(index);
    }

    public int getType() {
        return type;
    }
    
    // Addition method
    public boolean isDeckEmpty() {
	return this.cardsInDeck.isEmpty();
    }
    
    public int getNumCards() {
        return this.cardsInDeck.getNumCards();
    }
    
    public int getFisrtPlayer() {
	return (this.cardsInDeck.getSpecialRep().getSize()%this.type);
    }
    
    public ArrayList<Player> getOrderInSession(int idFirstPlayer) {
        ArrayList<Player> p = new ArrayList<Player>();
        for (int i = 0; i < this.type; i++)
            p.add(this.players.get((idFirstPlayer+i)%this.type));
        while (p.size() < 4) p.add(null);
        return p;
    }
    
    public void serveCards() {
	for (int i = 0; i < 8; i++)
	    for (Player player : players)
		player.ReceiveCard(this.cardsInDeck.takeCard());
    }
    
    public void takeCardInDeck(Player player) {
	if (player == null) return;
	int numTake = player.GetNumCardsToTake();
	if (numTake != 0)
	    for (int i = 0; i < numTake; i++) {
		if (this.isDeckEmpty()) return;
		player.ReceiveCard(this.cardsInDeck.takeCard());
            }
    }
    
    public Card getSpecialCard() {
	return this.cardsInDeck.getSpecialRep();
    }
    
    private ArrayCardsDeck cardsInDeck;
    private ArrayCards cardsDone;
    private ArrayList<Player> players;
    private int type;
}
