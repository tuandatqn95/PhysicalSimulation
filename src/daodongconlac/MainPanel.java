/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daodongconlac;

import daodongconlac.event.OnFrameSubmitListener;
import daodongconlac.event.OnStartListener;
import daodongconlac.event.OnStopListener;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javafx.scene.Cursor;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 *
 * @author tuand
 */
public final class MainPanel extends JPanel implements MouseListener, MouseMotionListener {

    BufferedImage imgConLac, imgRen, imgObject, imgKhung;

    double T;
    Timer timerConLac;
    double angle, angle0, oldAngle, omega;
    double dt, t;
    int intervalConLac;
    boolean isRotated, isRunning;

    int mouseX, mouseY, mouseX_dragged, mouseY_dragged;
    boolean mouseDragged;

    int N;
    double time;

    OnStartListener startListener;
    OnStopListener stopListener;

    public MainPanel() {
        this.addMouseListener(this);
        this.addMouseMotionListener(this);

        LoadImage();
        LoadImageChanged();
        isRotated = false;
        isRunning = false;

        angle = 0;

        T = 1.150;
        omega = 2 * Math.PI / T;

        t = 0;
        dt = 0.01;
        intervalConLac = (int) (1000 * dt);

        N = 0;
        time = 0.0;

        //Timer
        timerConLac = new Timer(intervalConLac, (ActionEvent e) -> {
            t += dt;
            oldAngle = angle;
            angle = angle0 * Math.pow(Math.E, -Properties.Gamma * t) * Math.cos(omega * t);
            if (Math.abs(oldAngle - angle) < 0.00001) {
                Stop();
            }
            if (oldAngle > 7 && angle <= 7) {
                N++;
            }
            if (N == 1) {
                if (this.startListener != null) {
                    this.startListener.OnStart();
                }
            }
//         

            repaint();

        });

    }

    @Override
    public void paintComponent(Graphics gg) {
        super.paintComponent(gg);
        setBackground(Properties.colorBackground);
        Graphics2D g = (Graphics2D) gg;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.drawImage(imgKhung, 45, 100, null);
        if (Properties.isRotated) {
            g.drawImage(rotate(rotate180(imgObject), angle, 200, 92), 0, 20, null);
        } else {
            g.drawImage(rotate(imgObject, angle, 200, 92), 0, 20, null);
        }
    }

    public static BufferedImage rotate(BufferedImage bufferedImage, double angle, int x, int y) {
        AffineTransform tx = new AffineTransform();
        tx.rotate(Math.toRadians(angle), x, y);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
        bufferedImage = op.filter(bufferedImage, null);
        return bufferedImage;
    }

    public static BufferedImage rotate180(BufferedImage bufferedImage) {
        AffineTransform tx = new AffineTransform();
        tx.rotate(Math.toRadians(180), bufferedImage.getWidth() / 2, bufferedImage.getHeight() / 2);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
        bufferedImage = op.filter(bufferedImage, null);
        return bufferedImage;
    }

    public static BufferedImage mergeImage(BufferedImage image1, BufferedImage image2, int length) {
        int width = image1.getWidth();
        int height = image1.getHeight();
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = bufferedImage.createGraphics();
        g2.drawImage(image1, null, 0, 0);
        g2.drawImage(image2, null, 183, 508 + length);
        return bufferedImage;
    }

    void LoadImage() {
        //Lay anh con lac
        try {
            imgConLac = ImageIO.read(new File(Properties.pathConLac));
            imgRen = ImageIO.read(new File(Properties.pathRenDung));
            imgKhung = ImageIO.read(new File(Properties.pathKhung));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void LoadImageChanged() {
        imgObject = mergeImage(imgConLac, imgRen, (int) (Properties.LBuLong * 0.4));
    }

    public void Start() {
        timerConLac.start();
        isRunning = true;

    }

    public void Stop() {
        timerConLac.stop();
        isRunning = false;
        angle = 0;
        t = 0;
        repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
        mouseDragged = false;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (mouseDragged) {
            if (angle0 != 0) {
                Start();
            }
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (isRunning) {
            return;
        }
        if (mouseX > 187 && mouseX < 213 && mouseY > 145 && mouseY < 590) {
            mouseX_dragged = e.getX();
            mouseY_dragged = e.getY();
            mouseDragged = true;

            angle = Math.toDegrees((200 - mouseX_dragged) / Math.sqrt((200 - mouseX_dragged) * (200 - mouseX_dragged)
                    + (92 - mouseY_dragged) * (92 - mouseY_dragged)));
            if (angle > 9) {
                angle = 9;
            }
            if (angle < -9) {
                angle = -9;
            }
            angle0 = angle;
            repaint();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    public void setOnStartListener(OnStartListener listener) {
        this.startListener = listener;
    }

    public void setOnStopListener(OnStopListener listener) {
        this.stopListener = listener;
    }

    public void UpdateOmega() {
        
    }

  

   
}
