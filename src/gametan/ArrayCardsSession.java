/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gametan;

import java.awt.Graphics;

/**
 *
 * @author linh
 */
public class ArrayCardsSession extends ArrayCards {

    private static final int CARD_RAISE = 40;
    
    public ArrayCardsSession() {
        super();
        this.show = true;
        this.vertical = false;
    }
    
    @Override
    public void draw(Graphics g) {
        if (arrCards.isEmpty()) return;
        calculate();
        for (int i = 0; i < numCards; i++) {
            Card card = arrCards.get(i);
            if (i%2 == 1) card.setDrawing(x_Root + (i/2)*space, y_Root + CARD_RAISE);
            else card.setDrawing(x_Root + (i/2)*space, y_Root);
            card.draw(g);
        }
    }

    @Override
    protected void calculate() {
	if (numCards > 0) {
	    x_Root = pnX + 5;
	    y_Root = pnY + (pnHeight-Card.CARD_HEIGHT*3/2)/2;
	    space = pnWidth - 10;
            if (numCards > 2) {
		if (numCards%2 == 1) space /= (numCards/2 + 1);
		else space /= (numCards/2);
            }
	    if (space > Card.CARD_WIDTH) x_Root += (space - Card.CARD_WIDTH)/2;
	}
    }
    
    @Override
    public boolean checkCor(int x, int y) {
        if (!arrCards.isEmpty() && super.checkCor(x, y)) return true;
        return false;
    }
    
}
