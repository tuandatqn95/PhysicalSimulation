/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daodongconlac;

import static daodongconlac.MainPanel.rotate;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 *
 * @author tuand
 */
public final class CustomizePanel extends JPanel implements MouseListener, MouseMotionListener {

    BufferedImage imgConLac, imgRen;

    public static int length;

    public CustomizePanel() {
        this.addMouseListener(this);
        this.addMouseMotionListener(this);

        length = (int) Properties.LBuLong;

        LoadImage();
    }

    @Override
    public void paintComponent(Graphics gg) {
        super.paintComponent(gg);
        setBackground(Color.white);
        Graphics2D g = (Graphics2D) gg;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.drawImage(imgConLac, 50, 40, null);
        g.drawImage(imgRen, 520 + length, 39, null);
    }

    void LoadImage() {
        try {
            File input = new File(Properties.pathConLacNamNgang);
            imgConLac = ImageIO.read(input);
            input = new File(Properties.pathRenNgang);
            imgRen = ImageIO.read(input);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    
   

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }
}
