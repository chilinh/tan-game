/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gametan;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 *
 * @author linh
 */
public class ArrayCards {

    public ArrayCards() {
	this.arrCards = new ArrayList<Card>();
    }

    public ArrayCards(ArrayList<Card> arrCards) {
        this.arrCards = arrCards;
    }
 
    public void addCards(ArrayList<Card> arrayCards) {
        for (int i = 0; i < arrayCards.size(); i++) {
            this.arrCards.add(arrayCards.get(i));
        }
    }
    
    public void addCard(Card card) {
	this.arrCards.add(card);
    }
    
    public void sort() {
	Collections.sort(this.arrCards);
    }
    
    public Card takeCard() {
        if (this.arrCards.isEmpty())
	    return null;
	Card card = this.arrCards.get(this.arrCards.size()-1);
	this.arrCards.remove(card);
	return card;
    }
    
    public Card takeCard(int idCard) {
        Card card = this.arrCards.get(idCard);
	this.arrCards.remove(card);
	return card;
    }
    
    public Card getCard(int id) {
	if (id >= this.arrCards.size() || id < 0)
	    return null;
	return this.arrCards.get(id);
    }
    
    public int findCard(Card card) {
        return this.arrCards.indexOf(card);
    }
    
    public int getNumCards() {
        return this.arrCards.size();
    }
    
    public int getNumNormCards() {
        int sum = 0;
        for (Card card : this.arrCards) {
            if (card.isSpecial()) continue;
            sum++;
        }
        return sum;
    }
    
    public boolean isEmpty() {
        return this.arrCards.isEmpty();
    }
    
    protected ArrayList<Card> arrCards;

    @Override
    public String toString() {
	return Arrays.toString(this.arrCards.toArray());
    }
}
