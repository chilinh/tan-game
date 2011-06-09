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
public class Moving {
    
    private static final int STEP = 15;
    
    public Moving() {
    }
    
    public void setMoving(int xC, int yC, int xTar, int yTar) {
        this.xTar = xTar;
        this.yTar = yTar;
        xD = (xTar - xC)/STEP;
        yD = (yTar - yC)/STEP;
        this.xC = xC + xD;
        this.yC = yC + yD;
    }

    public void setCard(Card card, boolean show, boolean vertical) {
        card.setShow(show);
        card.setShowHelp(false);
        card.setVertical(vertical);
        this.card = card;
    }

    public Card getCard() {
        return card;
    }
    
    public boolean move(Graphics g) {
        card.setDrawing((int) this.xC, (int) this.yC);
        card.draw(g);
        xC += xD;
        yC += yD;
        if ((yD >= 0 && yC + yD >= yTar) || (yD < 0 && yC + yD <= yTar)) return false;
	if ((xD >= 0 && xC + xD >= xTar) || (xD < 0 && xC + xD <= xTar)) return false;
        return true;
    }
    
    private double xTar = 0.0;
    private double yTar = 0.0;
    private double xC = 0.0;
    private double yC = 0.0;
    private double xD = 0.0;
    private double yD = 0.0;
    private Card card = null;
    
}
