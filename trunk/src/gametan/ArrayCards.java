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
public class ArrayCards extends Drawing {
    
    public static final Card back = new Card(52);
    
    public ArrayCards() {
        this.arrCards = new ArrayList<Card>();
        numCards = 0;
    }
    
    // Addition method
    
    public void addCard(Card card) {
	arrCards.add(card);
        card.setShow(show);
        card.setShowHelp(showHelp);
        card.setVertical(vertical);
        numCards++;
    }
    
    public Card takeCard() {
        if (arrCards.isEmpty()) return null;
	Card card = arrCards.get(arrCards.size()-1);
	arrCards.remove(card);
        numCards--;
	return card;
    }
    
    public Card getCard(int id) {
	return arrCards.get(id);
    }
    
    public int getNumCards() {
        return numCards;
    }
    
    public boolean isEmpty() {
        return arrCards.isEmpty();
    }
    
    public void clear() {
        numCards = 0;
        arrCards.clear();
    }
    
    protected ArrayList<Card> arrCards;
    protected int numCards;
    
    @Override
    public void setShow(boolean show) {
        super.setShow(show);
        for (Card card : arrCards) {
            card.setShow(show);
        }
    }

    @Override
    public void setVertical(boolean vertical) {
        super.setVertical(vertical);
        for (Card card : arrCards) {
            card.setVertical(vertical);
        }
    }

    @Override
    protected void calculate() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void draw(Graphics g) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
