/*
 * To change this template, chooseCard Tools | Templates
 * and open the template in the editor.
 */
package gametan;

import java.awt.Graphics;
import java.util.Collections;

/**
 *
 * @author linh
 */
public class ArrayCardsPlayer extends ArrayCards {
    
    private static final int CARD_RAISE  = 10;

    public ArrayCardsPlayer() {
        super();
        this.chooseCard = null;
    }

    public void setChooseCard(Card choose) {
        this.chooseCard = choose;
    }
        
    // Addition method
    public void sort() {
	Collections.sort(arrCards);
    }
    
    public int getNumNormCards() {
        int sum = 0;
        for (Card card : arrCards) {
            if (card.isSpecial()) continue;
            sum++;
        }
        return sum;
    }
    
    public int getNumCardToTake() {
        if (arrCards.size() < 8) return (8-arrCards.size());
	return 0;
    }
    
    private Card chooseCard;

    @Override
    public void addCard(Card card) {
        super.addCard(card);
        if (showHelp) Collections.sort(arrCards);
    }

    @Override
    public void setShowHelp(boolean showHelp) {
        super.setShowHelp(showHelp);
        for (Card card : arrCards) {
            card.setShowHelp(showHelp);
        }
    }
    
    @Override
    public Card takeCard() {
        if (!arrCards.isEmpty()) {
            chooseCard.setShowHelp(false);
            chooseCard.setMark(false);
            chooseCard.setChoose(false);
            chooseCard.setVertical(false);
            chooseCard.setShow(false);
            arrCards.remove(chooseCard);
            numCards--;
            Card card = chooseCard;
            chooseCard = null;
            return card;
        }
	return null;
    }
    
    @Override
    public void draw(Graphics g) {
        if (arrCards.isEmpty()) return;
        calculate();
        int sz = numCards;
        if (!show && sz > 8) sz = 8;
        for (int i = 0; i < sz; i++) {
            Card card = arrCards.get(i);
            int r = 0;
            if (showHelp && card.isMark()) r = CARD_RAISE;
            if (vertical) card.setDrawing(x_Root - r, y_Root + space*i);
            else card.setDrawing(x_Root + space*i, y_Root - r);
            card.draw(g);
        }
    }
    
    @Override
    protected void calculate() {
        int sz = numCards;
        if (!show && sz > 8) sz = 8;
        if (vertical && !show) {
            x_Root = pnX + (pnWidth-Card.CARD_HEIGHT)/2;
            y_Root = pnY + 5;
            space = (pnHeight - 10)/sz;
            if (space > Card.CARD_WIDTH) y_Root += (space-Card.CARD_WIDTH)/2;
        } else {
            x_Root = pnX + 5;
            y_Root = pnY + (pnHeight-Card.CARD_HEIGHT)/2;
            space = (pnWidth - 10)/sz;
            if (space > Card.CARD_WIDTH) x_Root += (space-Card.CARD_WIDTH)/2;
        }
    }
    
    @Override
    public boolean checkCor(int x, int y) {
        if (arrCards.isEmpty()) return false;
        if (super.checkCor(x, y)) {
            for (int i = 0; i < numCards; i++) {
                Card card = arrCards.get(i);
                if (card.checkCor(x, y)) {
                    if (card.isChoose() && card.isMark()) return true;
                    if (chooseCard != null) chooseCard.setChoose(false);
                    chooseCard = card;
                    chooseCard.setChoose(true);
                }
            }
        }
        return false;
    }   
    
}
