/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gametan;

import java.util.Collections;

/**
 *
 * @author linh
 */
public class ArrayCardsDeck extends ArrayCards {

    public ArrayCardsDeck() {
	super();
        for (int i = 0; i < 52; i++) {
	    this.arrCards.add(new Card(i));
        }
    }
    
    public void randomArray() {
        Collections.shuffle(this.arrCards);
    }
    
    public void markSpecial(Card card) {
        for (Card cards : this.arrCards) {
            if (cards.isSameType(card)) {
                cards.setSpecial(true);
            }
        }
    }
    
    public Card getSpecialRep() {
	if (this.arrCards.isEmpty())
	    return null;
	return this.arrCards.get(0);
    }
}
