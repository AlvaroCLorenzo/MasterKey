package GraphicModels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

/**
 * @autor: Alvaro
 */

//Clase dedicada a crear un panel con bordes redondeados.
public class RoundedPanel extends JPanel {

    private Color panelColor;
    private int width, height, radius;

    public RoundedPanel(int width, int height, int radius, Color panelColor) {
        this.width = width;
        this.height = height;
        this.radius = radius;
        this.panelColor = panelColor;
        setOpaque(false);
        setPreferredSize(new Dimension(this.width, this.height));
        setMaximumSize(new Dimension(this.width, this.height));
        setMinimumSize(new Dimension(this.width, this.height));
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();

        //Color de fondo.
        g2d.setColor(this.panelColor);
        //Pintamos el elemento
        g2d.fillRoundRect(0, 0, this.width, this.height, this.radius, this.radius);
    }

}
