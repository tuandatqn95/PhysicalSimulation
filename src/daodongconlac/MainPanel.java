/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daodongconlac;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 *
 * @author tuand
 */
public final class MainPanel extends JPanel implements MouseListener, MouseMotionListener {

    BufferedImage imgConLac;

    double T;
    Timer timerConLac;
    double angle, angle0, omega;
    double dt, t;
    int intervalConLac;
    boolean isRotated;

    public MainPanel() {
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        LoadImage();
        
        isRotated = false;
        angle0 = 10;

        T = 2.150;
        omega = 2 * Math.PI / T;

        t = 0;
        dt = 0.01;
        intervalConLac = (int) (1000 * dt);

        //Timer
        timerConLac = new Timer(intervalConLac, (ActionEvent e) -> {
            t += dt;
            angle = angle0 * Math.cos(omega * t);
            repaint();
        });

    }

    @Override
    public void paintComponent(Graphics gg) {
        super.paintComponent(gg);
        setBackground(Color.white);
        Graphics2D g = (Graphics2D) gg;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.drawImage(rotate(imgConLac, angle, 200, 92), 0,50, null);
    }

    public static BufferedImage rotate(BufferedImage bufferedImage, double angle, int x, int y) {
        AffineTransform tx = new AffineTransform();
        tx.rotate(Math.toRadians(angle), x, y);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
        bufferedImage = op.filter(bufferedImage, null);
        return bufferedImage;
    }

    void LoadImage() {
        //Lay anh con lac
        try {
            File input = new File(Properties.pathConLac);
            imgConLac = ImageIO.read(input);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        timerConLac.start();
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
