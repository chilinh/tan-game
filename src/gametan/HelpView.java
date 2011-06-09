/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gametan;

import java.awt.Graphics;
import javax.swing.ImageIcon;
import org.jdesktop.application.Application;

/**
 *
 * @author linh
 */
public class HelpView extends Drawing {
    
    private static final int W_HELP = 300;
    private static final int H_HELP = 400;

    public HelpView() {
        image = Application.getInstance(gametan.GameTanApp.class).getContext().getResourceMap(GameTanView.class).getImageIcon("help.panel");
    }

    private ImageIcon image;

    @Override
    public void setDrawing(int pnWidth, int pnHeight, int pnX, int pnY) {
        super.setDrawing(pnWidth, pnHeight, pnX, pnY);
        calculate();
    }
    
    @Override
    protected void calculate() {
        x_Root = pnX + (pnWidth-W_HELP)/2;
        y_Root = pnY + (pnHeight-H_HELP)/2;
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(image.getImage(), x_Root, y_Root, W_HELP, H_HELP, null);
    }
    
}
