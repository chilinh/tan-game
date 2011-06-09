/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gametan;

import java.awt.Graphics;

/**
 *
 * @author Linh
 */
public abstract class Drawing {

    public boolean isShow() {
        return show;
    }

    public void setShow(boolean show) {
        this.show = show;
    }

    public boolean isShowHelp() {
        return showHelp;
    }

    public void setShowHelp(boolean showHelp) {
        this.showHelp = showHelp;
    }

    public boolean isVertical() {
        return vertical;
    }

    public void setVertical(boolean vertical) {
        this.vertical = vertical;
    }
    
    public int getX() {
        return (pnX + (pnWidth - Card.CARD_WIDTH)/2);
    }
    
    public int getY() {
        return (pnY + (pnHeight - Card.CARD_HEIGHT)/2);
    }
    
    public void setDrawing(int pnWidth, int pnHeight, int pnX, int pnY) {
        this.pnWidth = pnWidth;
        this.pnHeight = pnHeight;
        this.pnX = pnX;
        this.pnY = pnY;
    }
    
    public boolean checkCor(int x, int y) {
        if (x < pnX || y < pnY) return false;
        if (x > pnX + pnWidth || y > pnY + pnHeight) return false;
        return true;
    }
    
    protected abstract void calculate();
    public abstract void draw(Graphics g);

    protected int space = 0;
    protected int x_Root = 0;
    protected int y_Root = 0;
    protected int pnHeight = 0;
    protected int pnWidth = 0;
    protected int pnX = 0;
    protected int pnY = 0;
    protected boolean vertical = false;
    protected boolean show = false;
    protected boolean showHelp = false;
}
