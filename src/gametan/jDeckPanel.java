/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gametan;

import java.awt.Graphics;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import org.jdesktop.application.Application;
import org.jdesktop.application.ResourceMap;

/**
 *
 * @author Linh
 */
public class jDeckPanel extends JPanel {
    
    private static final int CARD_WIDTH  = 60;
    private static final int CARD_HEIGHT = 80;
    
    public jDeckPanel() {
	super();
        this.speCard = null;
        this.table = null;
        this.numCards = 0;
        this.resourceMap = Application.getInstance(gametan.GameTanApp.class).getContext().getResourceMap(GameTanView.class);
    }

    public void setTable(Table table) {
        this.table = table;
        this.speCard = this.table.getSpecialCard();
    }
       
    protected void drawDecks(Graphics g) {
        if (this.numCards == 0) return;
        ImageIcon image = this.resourceMap.getImageIcon("card.icon["+ this.speCard.getIdCard() +"]");
        g.drawImage(image.getImage(), this.x_root, this.y_root, jDeckPanel.CARD_WIDTH, jDeckPanel.CARD_HEIGHT, null);
        if (this.numCards > 1) {
            image = this.resourceMap.getImageIcon("card.icon[52]");
            g.drawImage(image.getImage(), this.x_root + jDeckPanel.CARD_WIDTH/2, this.y_root, jDeckPanel.CARD_WIDTH, jDeckPanel.CARD_HEIGHT, null);
        }
    }
    
    protected void calculate() {
        if (this.table == null) {
            this.numCards = 0;
            return;
        }
        this.numCards = this.table.getNumCards();
        if (this.numCards == 0) return;
        this.pnHeight = this.getHeight();
	this.pnWidth = this.getWidth();
        this.y_root = (this.pnHeight-jDeckPanel.CARD_HEIGHT)/2;
        if (numCards == 1) this.x_root = (this.pnWidth-jDeckPanel.CARD_WIDTH)/2;
        else this.x_root = (this.pnWidth-jDeckPanel.CARD_WIDTH*3/2)/2;
    }
    
    public boolean checkCor(int x, int y) {
        if (this.numCards == 0) return false;
        if (x < this.x_root || y < this.y_root) return false;
        int h = this.y_root+jDeckPanel.CARD_HEIGHT;
        int w = this.x_root;
        if (this.numCards == 1) w += jDeckPanel.CARD_WIDTH;
        else w += jDeckPanel.CARD_WIDTH*3/2;
        if (x <= w && y <= h) return true;
        return false;
    }
    
    private int pnHeight;
    private int pnWidth;
    private int x_root;
    private int y_root;
    private Table table;
    private int numCards;
    private Card speCard;
    private ResourceMap resourceMap;

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.calculate();
        this.drawDecks(g);
    }
}
