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
        for (int i = 0; i < 52; i++) {
            this.arrCards.add(new Cards(i));
        }
    }
    
    public void RandomArray() {
        Collections.shuffle(this.arrCards);
    }
    
    public void MarkSpecial(Cards card) {
        int type = card.getIdCard()/13;
        for (Cards cards : this.arrCards) {
            if (cards.getIdCard()/13 == type) {
                cards.setSpecial(true);
            }
        }
    }
}
