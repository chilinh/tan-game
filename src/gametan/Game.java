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
public class Game extends Drawing {

    public static final int TWO_PLAYER = 2;
    public static final int FOUR_PLAYER = 4;
    
    public static final int W_DECK = 100;
    public static final int H_DECK = 100;
    public static final int W_PLAYER_H = 700;
    public static final int H_PLAYER_H = 100;
    public static final int W_PLAYER_V = 100;
    public static final int H_PLAYER_V = 500;
    
    public Game(int type, Player.Level level) {
	this.type = type;
	this.cardsInDeck = new ArrayCardsDeck();
	this.cardsDone = new ArrayCards();
	this.players = new ArrayList<Player>();
        this.sessions = new ArrayList<ArrayCardsSession>();
	
        Player p = new Player("Person");
        p.setShow(true);
        p.setShowHelp(true);
	this.players.add(p);
        this.sessions.add(new ArrayCardsSession());
	for (int i = 0; i < type-1; i++) {
            p = new Player("Bot " + i, level);
            if (type == TWO_PLAYER || i == 1) p.setShow(true);
	    this.players.add(p);
            this.sessions.add(new ArrayCardsSession());
	}
    }
    
    // Get Set method
    public ArrayCards getCardsDone() {
	return cardsDone;
    }

    public boolean isMoving() {
        return isMoving;
    }
    
    // Addition method
    public boolean isDeckEmpty() {
	return cardsInDeck.isEmpty();
    }
    
    public Player getPerson() {
        return players.get(0);
    }
    
    public ArrayCardsSession getActiveSession() {
        return sessions.get(idFirst);
    }
    
    public void addCardDone(ArrayCardsSession cards) {
        int sz = cards.getNumCards();
        for (int i = 0; i < sz; i++) {
            cardsDone.addCard(cards.takeCard());
        }
    }
    
    public ArrayList<Player> getOrderInSession(boolean skip) {
        if (idFirst == -1) idFirst = cardsInDeck.randomArray()%type;
        else if (skip) idFirst = (idFirst + 2)%type;
        else idFirst = (idFirst + 1)%type;
        ArrayList<Player> p = new ArrayList<Player>();
        for (int i = 0; i < type; i++)
            p.add(players.get((idFirst+i)%type));
        return p;
    }
    
    public boolean serveCards() {
        if (isMoving) return true;
        if (movingCard != null) {
            curPlayer.receiveCard(movingCard.getCard());
            movingCard = null;
        }
	if (players.get(type-1).getNumCardsToTake() != 0) {
	    curPlayer = players.get(id);
	    id = (id+1)%type;
            movingCard = new Moving();
	    movingCard.setCard(cardsInDeck.takeCard(), false, curPlayer.isVertical());
	    movingCard.setMoving(cardsInDeck.getX(), cardsInDeck.getY(), curPlayer.getX(), curPlayer.getY());
	    return true;
	}
	movingCard = null;
        return false;
    }
    
    public boolean takeCardInDeck(Player player) {
        if (player.getNumCardsToTake() == 0) return false;
        if (isDeckEmpty() && !justEmpty) return false;
	if (isMoving) return true;
        if (movingCard != null && takeFromDeck) player.receiveCard(movingCard.getCard());
        if (player.getNumCardsToTake() != 0 && !isDeckEmpty()) {
            takeFromDeck = true;
            movingCard = new Moving();
	    movingCard.setCard(cardsInDeck.takeCard(), false, player.isVertical());
            if (cardsInDeck.isEmpty()) justEmpty = true;
            movingCard.setMoving(cardsInDeck.getX(), cardsInDeck.getY(), player.getX(), player.getY());
            return true;
        }
        justEmpty = false;
	movingCard = null;
        takeFromDeck = false;
        return false;
    }
    
    public boolean takeCardInSession(Player player) {
        ArrayCardsSession session = sessions.get(idFirst);
        if (session.isEmpty() && !justEmpty) return false;
        if (isMoving) return true;
        if (movingCard != null && !takeFromDeck) player.receiveCard(movingCard.getCard());
        if (!session.isEmpty()) {
            movingCard = new Moving();
	    movingCard.setCard(session.takeCard(), true, false);
            if (session.isEmpty()) justEmpty = true;
            movingCard.setMoving(session.getX(), session.getY(), player.getX(), player.getY());
            return true;
        }
        justEmpty = false;
	movingCard = null;
        return false;
    }
    
    public boolean playCardToSession(Player player) {
        if (isMoving) return true;
	if (movingCard == null) {
            movingCard = new Moving();
	    movingCard.setCard(player.PlayCards(), true, false);
            movingCard.setMoving(player.getX(), player.getY(), sessions.get(idFirst).getX(), sessions.get(idFirst).getY());
            return true;
        }
        sessions.get(idFirst).addCard(movingCard.getCard());
        movingCard = null;
        return false;
    }

    
    private ArrayCardsDeck cardsInDeck;
    private ArrayCards cardsDone;
    private ArrayList<Player> players;
    private ArrayList<ArrayCardsSession> sessions;
    private int type;
    private int idFirst = -1;
    private int id = 0;
    
    private Player curPlayer = null;
    private Moving movingCard = null;
    private boolean isMoving = false;
    private boolean justEmpty = false;
    private boolean takeFromDeck = false;

    @Override
    protected void calculate() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setDrawing(int pnWidth, int pnHeight, int pnX, int pnY) {
        int w_Session = (pnWidth - 2*W_PLAYER_V - W_DECK)/2;
        int h_Session = (pnHeight - 2*H_PLAYER_H - H_DECK)/2;
        int x = (pnWidth - W_DECK)/2 - 1;
        int y = (pnHeight - H_DECK)/2 - 1;
        cardsInDeck.setDrawing(W_DECK, H_DECK, x, y);
        if (type == TWO_PLAYER) {
            x = 0; y = pnHeight - H_PLAYER_H - 1;
            players.get(0).setDrawing(W_PLAYER_H, H_PLAYER_H, x, y, false);
            x = W_PLAYER_V - 1; y -= h_Session;
            sessions.get(1).setDrawing(w_Session, h_Session, x, y);
            
            x = pnWidth - W_PLAYER_H - 1; y = 0;
            players.get(1).setDrawing(W_PLAYER_H, H_PLAYER_H, x, y, false);
            x = pnWidth - W_PLAYER_V - w_Session - 1; y = H_PLAYER_H - 1;
            sessions.get(0).setDrawing(w_Session, h_Session, x, y);
        } else {
            x = 0; y = pnHeight - H_PLAYER_H - 1;
            players.get(0).setDrawing(W_PLAYER_H, H_PLAYER_H, x, y, false);
            x = pnWidth - W_PLAYER_V - w_Session - 1; y -= h_Session;
            sessions.get(0).setDrawing(w_Session, h_Session, x, y);
            
            x = pnWidth - W_PLAYER_V - 1; y = pnHeight - H_PLAYER_V - 1;
            players.get(1).setDrawing(W_PLAYER_V, H_PLAYER_V, x, y, true);
            x -= w_Session;
            sessions.get(1).setDrawing(w_Session, h_Session, x, y);
            
            x = pnWidth - W_PLAYER_H - 1; y = 0;
            players.get(2).setDrawing(W_PLAYER_H, H_PLAYER_H, x, y, false);
            y = H_PLAYER_H - 1;
            sessions.get(2).setDrawing(w_Session, h_Session, x, y);
            
            x = 0; y = 0;
            players.get(3).setDrawing(W_PLAYER_V, H_PLAYER_V, x, y, true);
            x = W_PLAYER_V - 1; y = pnHeight - H_PLAYER_H - h_Session - 1;
            sessions.get(3).setDrawing(w_Session, h_Session, x, y);
        }
    }
    
    @Override
    public void draw(Graphics g) {
        cardsInDeck.draw(g);
        for (int i = 0; i < type; i++) {
            players.get(i).draw(g);
            sessions.get(i).draw(g);
        }
        if (movingCard != null) isMoving = movingCard.move(g);
    }
    
    @Override
    public boolean checkCor(int x, int y) {
        return cardsInDeck.checkCor(x, y);
    }  
    
}
