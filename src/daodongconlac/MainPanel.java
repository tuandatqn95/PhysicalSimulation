/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daodongconlac;

import daodongconlac.event.TimerListener;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
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

    BufferedImage imgConLac, imgRen, imgObject, imgKhung;

    double T;
    Timer timerConLac;
    double angle, angle0, oldAngle, omega, a;
    double dt, t;
    int intervalConLac;
    boolean isRotated, isRunning;

    int mouseX, mouseY, mouseX_dragged, mouseY_dragged;
    boolean mouseDragged;

    int N;
    double time;

    TimerListener timerListener;

    GraphFrame graphFrame;

    public MainPanel() {
        this.addMouseListener(this);
        this.addMouseMotionListener(this);

        graphFrame = new GraphFrame();
        graphFrame.setVisible(true);

        LoadImage();
        LoadImageChanged();
        isRotated = false;
        isRunning = false;

        angle = 0;
        a = 0;
        UpdateOmega();

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

//            if (oldAngle - angle < 0.00001 && oldAngle - angle > -0.00001) {
//                Stop();
//            }
            if (oldAngle > 7 && angle <= 7) {
                N++;
            }

            repaint();
            if (t < 15) {
                graphFrame.jPanel1.UpdateValue(t, -angle);
            }
            if (N >= 1) {

                time += dt;
                if (this.timerListener != null) {
                    this.timerListener.OnTick();
                }
            }

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

        if (mouseDragged == true) {
            g.setColor(Color.ORANGE);
            g.setFont(new Font("Comic Sans MS", Font.ITALIC, 16));
            Arc2D arc = new Arc2D.Double(100, 12, 200, 200, -90, -angle, Arc2D.PIE);
            g.fill(arc);
            g.drawString(String.format("%1$,.2f", -angle), (angle < 0) ? 155 : 205, 180);
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
            mouseDragged = false;
            t = 0;
            time = 0;
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
            if (angle > 90) {
                angle = 90;
            }
            if (angle < -90) {
                angle = -90;
            }
            angle0 = angle;
            graphFrame.jPanel1.setAngle0(angle);
            repaint();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    public void setOnTimerListener(TimerListener listener) {
        this.timerListener = listener;
    }

    public void UpdateA(int a) {
        this.a = a;
        UpdateOmega();
    }

    public void UpdateOmega() {
        if (Properties.isRotated) {
            T = (1.7025 * a + 1.6813 * (50 - a)) / 50;
        } else {
            T = (1.6938 * a + 1.6890 * (50 - a)) / 50;
        }
        omega = 2 * Math.PI / T;
    }
}
