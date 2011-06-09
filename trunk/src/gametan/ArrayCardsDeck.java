/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gametan;

import java.awt.Graphics;
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
        this.numCards = this.arrCards.size();
    }
    
    // Addition method
    public int randomArray() {
        Collections.shuffle(arrCards);
        Card card0 = arrCards.get(0);
        for (Card cards : arrCards) {
            if (cards.isSameType(card0)) {
                cards.setSpecial(true);
            }
        }
        card0.setShow(true);
        return card0.getSize();
    }

    @Override
    public void setDrawing(int pnWidth, int pnHeight, int pnX, int pnY) {
        super.setDrawing(pnWidth, pnHeight, pnX, pnY);
        calculate();
    }
    
    @Override
    public void draw(Graphics g) {
        if (arrCards.isEmpty()) return;
        if (numCards == 1) calculate();
        Card card = arrCards.get(0);
        card.setDrawing(x_Root, y_Root);
        card.draw(g);
        if (numCards > 1) {
            arrCards.get(1).setDrawing(x_Root + Card.CARD_WIDTH/2, y_Root);
            arrCards.get(1).draw(g);
        }
    }

    @Override
    protected void calculate() {
        y_Root = pnY + (pnHeight-Card.CARD_HEIGHT)/2;
        x_Root = pnX;
        if (numCards == 1) x_Root += (pnWidth-Card.CARD_WIDTH)/2;
        else x_Root += (pnWidth-Card.CARD_WIDTH*3/2)/2;
    }
    
    @Override
    public boolean checkCor(int x, int y) {
        if (arrCards.isEmpty()) return false;
        boolean ok = false;
        if (super.checkCor(x, y)) {
            if (arrCards.get(0).checkCor(x, y)) ok = true;
            if (numCards > 1)
                if(arrCards.get(1).checkCor(x, y)) ok = true;
        }
        return ok;
    }

    @Override
    public int getX() {
        if (numCards > 1) return (x_Root + Card.CARD_WIDTH/2);
        return x_Root;
    }

    @Override
    public int getY() {
        return y_Root;
    }
    
}
