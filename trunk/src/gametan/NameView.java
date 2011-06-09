/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gametan;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

/**
 *
 * @author linh
 */
public class NameView extends Drawing {
    
    private static final Font FONT = new Font("SansSerif", Font.PLAIN, 12);
    private static final Color COLOR = Color.BLACK;

    public NameView(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    
    private String name;
    private FontMetrics fntMetrics;

    @Override
    public void draw(Graphics g) {
        fntMetrics = g.getFontMetrics(FONT);
        calculate();
        Color c = g.getColor();
        g.setColor(COLOR);
        g.setFont(FONT);
        g.drawString(name, x_Root, y_Root);
        g.setColor(c);
    }

    @Override
    protected void calculate() {
        x_Root = pnX + (pnWidth - fntMetrics.stringWidth(name))/2;
        y_Root = pnY + (pnHeight - fntMetrics.getHeight())/2;
    }
    
}
