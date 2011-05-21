/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gametan;

import java.awt.Graphics;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import org.jdesktop.application.Application;
import org.jdesktop.application.ResourceMap;

/**
 *
 * @author Linh
 */
public class jSessionPanel extends JPanel{
    
    private static final int CARD_WIDTH  = 60;
    private static final int CARD_HEIGHT = 80;
    private static final int CARD_RAISE = 40;
    
    public jSessionPanel() {
	super();
        jSessionPanel.cards = null;
        jSessionPanel.numCards = 0;
	this.active = false;
        this.resourceMap = Application.getInstance(gametan.GameTanApp.class).getContext().getResourceMap(GameTanView.class);
    }

    // get set method

    public void setCards(ArrayList<Card> cards) {
        jSessionPanel.cards = cards;
    }

    public void setActive(boolean active) {
	this.active = active;
    }

    public boolean isActive() {
	return active;
    }
    
    // addition method
    protected void drawCards(Graphics g) {
        if (jSessionPanel.numCards == 0) return;
        for (int i = 0; i < jSessionPanel.numCards; i++) {
	    Card card = jSessionPanel.cards.get(i);
            ImageIcon image = this.resourceMap.getImageIcon("card.icon["+ card.getIdCard() +"]");
	    int x = this.x_root + (i/2)*jSessionPanel.space;
	    int y = this.y_root;
	    if (i%2 == 1) y += jSessionPanel.CARD_RAISE;
	    g.drawImage(image.getImage(), x, y, jSessionPanel.CARD_WIDTH, jSessionPanel.CARD_HEIGHT, null);
        }
    }

    protected void calculate() {
        if (jSessionPanel.cards == null) return;
	jSessionPanel.numCards = jSessionPanel.cards.size();
	if (jSessionPanel.numCards > 0) {
	    this.pnHeight = this.getHeight();
	    this.pnWidth = this.getWidth();
	    this.x_root = 5;
	    this.y_root = (this.pnHeight-jSessionPanel.CARD_HEIGHT*3/2)/2;
	    jSessionPanel.space = this.pnWidth - 10;
            if (jSessionPanel.numCards > 2)
                jSessionPanel.space /= (jSessionPanel.numCards/2);
	    if (jSessionPanel.space > jSessionPanel.CARD_WIDTH) {
		this.x_root += (jSessionPanel.space-jSessionPanel.CARD_WIDTH)/2;
	    }
	}
    }
    
    private static ArrayList<Card> cards;
    private ResourceMap resourceMap;
    private boolean active;
    private int pnHeight;
    private int pnWidth;
    private int x_root;
    private int y_root;
    private static int space;
    private static int numCards;

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
	if (this.active) {
	    this.calculate();
	    this.drawCards(g);
	}
    }
}
