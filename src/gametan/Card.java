/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gametan;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.ImageIcon;
import org.jdesktop.application.Application;

/**
 *
 * @author linh
 */
public class Card extends Drawing implements Comparable<Card> {
    
    public static final int ACE = 12;
    public static final int CARD_HEIGHT = 80;
    public static final int CARD_WIDTH = 60;
    
    private static final Color CARD_CHOOSE = Color.ORANGE;

    public Card(int idCard) {
        this.idCard = idCard;
        this.special = false;
        this.choose = false;
        this.mark = false;
    }

    // Get Set method
    public int getIdCard() {
        return idCard;
    }

    public void setIdCard(int idCard) {
        this.idCard = idCard;
    }

    public boolean isSpecial() {
        return special;
    }

    public void setSpecial(boolean special) {
        this.special = special;
    }

    public boolean isChoose() {
        return choose;
    }

    public void setChoose(boolean choose) {
        this.choose = choose;
    }

    public boolean isMark() {
        return mark;
    }

    public void setMark(boolean mark) {
        this.mark = mark;
    }
    
    // Addition method
    public boolean isSameType(Card card) {
	if (card.getType() == this.getType())
	    return true;
	return false;
    }
    
    public int getType() {
	return idCard/13;
    }
    
    public boolean isSameSize(Card card) {
	if (card.getSize() == this.getSize())
	    return true;
	return false;
    }
    
    public int getSize() {
	return idCard%13;
    }

    public void setDrawing(int pnX, int pnY) {
        super.setDrawing(pnWidth, pnHeight, pnX, pnY);
        if (vertical) super.setDrawing(CARD_HEIGHT, CARD_WIDTH, pnX, pnY);
        else super.setDrawing(CARD_WIDTH, CARD_HEIGHT, pnX, pnY);
    }
    
    private int idCard;         //id [0..52]
    private boolean special;    //mark special card
    private boolean choose;
    private boolean mark;

    @Override
    public boolean equals(Object obj) {
	if (obj == null) {
	    return false;
	}
	if (getClass() != obj.getClass()) {
	    return false;
	}
	final Card other = (Card) obj;
	if (this.idCard != other.idCard) {
	    return false;
	}
	if (this.special != other.special) {
	    return false;
	}
	return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + this.idCard;
        hash = 97 * hash + (this.special ? 1 : 0);
        return hash;
    }

    @Override
    public int compareTo(Card o) {
        int i = this.getSize() - o.getSize();
        if (i == 0) {
            if (this.isSpecial()) i = 1;
            else if (o.isSpecial()) i = -1;
            else i = this.getType() - o.getType();
        }
	return i;
    }
    
    @Override
    public void draw(Graphics g) {
        ImageIcon image;
        if (vertical) {
            if (show) image = Application.getInstance(gametan.GameTanApp.class).getContext().getResourceMap(GameTanView.class).getImageIcon("cardV.icon["+ this.idCard +"]");
            else image = Application.getInstance(gametan.GameTanApp.class).getContext().getResourceMap(GameTanView.class).getImageIcon("cardV.back");
        }
        else {
            if (show) image = Application.getInstance(gametan.GameTanApp.class).getContext().getResourceMap(GameTanView.class).getImageIcon("card.icon["+ this.idCard +"]");
            else image = Application.getInstance(gametan.GameTanApp.class).getContext().getResourceMap(GameTanView.class).getImageIcon("card.back");
        }
        g.drawImage(image.getImage(), pnX, pnY, pnWidth, pnHeight, null);
        if (choose) {
            Color c = g.getColor();
            g.setColor(CARD_CHOOSE);
            g.drawRect(pnX, pnY, pnWidth, pnHeight);
            g.drawRect(pnX+1, pnY+1, pnWidth-2, pnHeight-2);
            g.setColor(c);
        }
        if (showHelp && special) {
            image = Application.getInstance(gametan.GameTanApp.class).getContext().getResourceMap(GameTanView.class).getImageIcon("help.icon");
            g.drawImage(image.getImage(), pnX-3, pnY-3, 10, 10, null);
        }
    }

    @Override
    protected void calculate() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getX() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getY() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
