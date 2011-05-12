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
public class ArrayCards {

    public ArrayCards() {
    }

    public ArrayCards(ArrayList<Cards> arrCards) {
        this.arrCards = arrCards;
    }
    
    public void AddCards(ArrayList<Cards> arrCards) {
        for (Cards cards : arrCards) {
            this.arrCards.add(cards);
        }
    }
    
    public Cards GetCards(int idCards) {
        for (Cards card : this.arrCards) {
            if (card.getIdCard() == idCards) {
                this.arrCards.remove(card);
                return card;
            }
        }
        return null;
    }
    
    public int GetNumCards() {
        return this.arrCards.size();
    }
    
    public int GetNumNormCards() {
        int sum = 0;
        for (Cards card : this.arrCards) {
            if (card.isSpecial()) continue;
            sum++;
        }
        return sum;
    }
    
    public boolean IsEmpty() {
        return this.arrCards.isEmpty();
    }
    
    protected ArrayList<Cards> arrCards;
}
