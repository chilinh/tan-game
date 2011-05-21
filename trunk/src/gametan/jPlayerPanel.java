/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gametan;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import org.jdesktop.application.Application;
import org.jdesktop.application.ResourceMap;

/**
 *
 * @author Linh
 */
public class jPlayerPanel extends JPanel {
    
    private static final int CARD_WIDTH  = 60;
    private static final int CARD_HEIGHT = 80;
    private static final int CARD_RAISE  = 10;
    private static final Color CARD_SPECIAL = Color.ORANGE;

    public jPlayerPanel(boolean isBottom) {
	super();
	this.player = null;
	jPlayerPanel.cards = null;
        jPlayerPanel.numCards = 0;
        this.indChoose = -1;
	this.showHelp = false;
	this.isBottom = isBottom;
	this.show = false;
        this.resourceMap = Application.getInstance(gametan.GameTanApp.class).getContext().getResourceMap(GameTanView.class);
    }

    // get set method

    public void setShowHelp(boolean showHelp) {
	this.showHelp = showHelp;
    }

    public void setShow(boolean show) {
	this.show = show;
    }

    public void setIndChoose(int indChoose) {
        this.indChoose = indChoose;
    }

    public int getIndChoose() {
        return indChoose;
    }
    
    public void setPlayer(Player player) {
        this.indChoose = -1;
        this.player = player;
    }
    
    // addition method
    protected void drawCards(Graphics g) {
	if (jPlayerPanel.cards == null) return;
        for (int i = 0; i < jPlayerPanel.numCards; i++) {
	    Card card = jPlayerPanel.cards.getCard(i);
            ImageIcon image = this.resourceMap.getImageIcon("card.icon["+ card.getIdCard() +"]");
	    int x = this.x_root + this.space*i;
	    int y = this.y_root;
	    if (i == this.indChoose) y -= jPlayerPanel.CARD_RAISE;
	    g.drawImage(image.getImage(), x, y, jPlayerPanel.CARD_WIDTH, jPlayerPanel.CARD_HEIGHT, null);
	    if (this.showHelp && this.player.canPlay(i)) {
		Color c = g.getColor();
		g.setColor(jPlayerPanel.CARD_SPECIAL);
		g.drawRect(x, y, jPlayerPanel.CARD_WIDTH, jPlayerPanel.CARD_HEIGHT);
		g.drawRect(x+1, y+1, jPlayerPanel.CARD_WIDTH-2, jPlayerPanel.CARD_HEIGHT-2);
		g.setColor(c);
	    }
        }
    }

    protected void calculate() {
        if (this.player == null) return;
	jPlayerPanel.numCards = this.player.GetNumCards();
	jPlayerPanel.cards = this.player.getCards();
	if (jPlayerPanel.numCards > 0) {
	    this.pnHeight = this.getHeight();
	    this.pnWidth = this.getWidth();
	    this.x_root = 5;
	    this.y_root = (this.pnHeight-jPlayerPanel.CARD_HEIGHT)/2;
	    this.space = (this.pnWidth - 10)/jPlayerPanel.numCards;
	    if (this.space > jPlayerPanel.CARD_WIDTH) {
		this.x_root += (this.space-jPlayerPanel.CARD_WIDTH)/2;
	    }
	}
    }
    
    public boolean checkCor(int x, int y) {
	int ind = (x - this.x_root)/this.space;
	if (ind < jPlayerPanel.numCards) {
	    if (this.space >= jPlayerPanel.CARD_WIDTH) {
		int temp = (x - this.x_root)%this.space;
		if (temp > jPlayerPanel.CARD_WIDTH)
		    ind = -2;
	    }
	    if (ind == this.indChoose) {
		if (y >= this.y_root-jPlayerPanel.CARD_RAISE && y <= this.y_root + jPlayerPanel.CARD_HEIGHT-jPlayerPanel.CARD_RAISE) {
		    this.indChoose = -1;
		    return true;
		}
		return false;
	    }
	    if (ind >= 0) {
		if (y >= this.y_root && y <= this.y_root + jPlayerPanel.CARD_HEIGHT) {
		    this.indChoose = ind;
		    return true;
		}
	    }
	}
	return false;
    }
    
    public boolean checkPlay() {
        if (this.indChoose == -1) return false;
        if (this.player.canPlay(this.indChoose))
            return true;
        return false;
    }
    
    private Player player;
    private static ArrayCards cards;
    private boolean showHelp;
    private ResourceMap resourceMap;
    private boolean isBottom;
    private boolean show;
    
    private int pnHeight;
    private int pnWidth;
    private int indChoose;
    private int x_root;
    private int y_root;
    private int space;
    private static int numCards;
    
    @Override
    protected void paintComponent(Graphics g) {
	super.paintComponent(g);
	if (this.isBottom || (!this.isBottom && this.show)) {
	    this.calculate();
	    this.drawCards(g);
	}
    }
}
