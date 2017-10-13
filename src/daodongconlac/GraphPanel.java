/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daodongconlac;

import static daodongconlac.MainPanel.rotate;
import static daodongconlac.MainPanel.rotate180;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.util.ArrayList;
import javax.swing.JPanel;

/**
 *
 * @author tuand
 */
public class GraphPanel extends JPanel {

    double t, angle, angle0;
    int x, y;
    int n;
    Point oldPoint;
    ArrayList<Point> listPoint;

    int basex = 50, basey = 50;

    public GraphPanel() {
        listPoint = new ArrayList();
        oldPoint = new Point(0, 0);
    }

    public void paintComponent(Graphics gg) {
        super.paintComponent(gg);
        setBackground(Color.WHITE);
        Graphics2D g = (Graphics2D) gg;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(Properties.colorBackground);
        g.fillRect(basex, basey, 750, 240);

        x = (int) (t * 50 + basex);
        y = (int) (angle * 120 / angle0 + 170);

        listPoint.add(new Point(x, y));
        g.setColor(Color.LIGHT_GRAY);
        //Draw vertical line
        for (int i = 0; i < 16; i++) {
            g.drawLine(i * 50 + basex, 50, i * 50 + basex, 290);

        }

        //Draw horizontal line
        for (int i = -3; i <= 3; i++) {
            g.drawLine(basex, i * 40 + 170, 50 * 15 + basex, i * 40 + 170);

        }

        //Draw String
        g.setColor(Color.BLACK);
        for (int i = 0; i < 16; i++) {
            g.drawString(String.valueOf(i), i * 50 + basex - 5, 310);
        }

        for (int i = -3; i <= 3; i++) {
            g.drawString(String.format("%1$,.2f", i * angle0 / 3), basex - 40, i * 40 + 175);
        }

        //Draw graph
        n = listPoint.size();
        g.setColor(Color.BLUE);
        oldPoint = listPoint.get(0);
        for (Point point : listPoint) {
            g.drawLine(oldPoint.x, oldPoint.y, point.x, point.y);
            oldPoint = point;
        }

    }

    public void UpdateValue(double t, double angle) {
        this.t = t;
        this.angle = angle;
        if (t > 15) {
            return;
        }
        repaint();
    }

    public void setAngle0(double angle) {
        listPoint.clear();
        this.angle0 = Math.abs(angle);

    }
}
