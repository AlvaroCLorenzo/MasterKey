package GraphicModels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JLabel;

/**
 * @autor: Alvaro
 */

public class RoundedLabel extends JLabel {

    private Color backgroundColor, foregroundColor;
    private int width, height, radius;

    public RoundedLabel(String string, int width, int height, int radius, Color backgroundColor, Color foregroundColor) {
        super(string, JLabel.CENTER);
        this.width = width;
        this.height = height;
        this.radius = radius;
        this.backgroundColor = backgroundColor;
        this.foregroundColor = foregroundColor;

        setOpaque(false);

        Dimension size = getPreferredSize();
        size.width = size.height = Math.max(size.width, size.height);
        setPreferredSize(size);
    }

    @Override
    public void paintComponent(Graphics g) {
        g.setColor(this.backgroundColor); //Color de fondo.
        setForeground(this.foregroundColor); //Color de la inscripcion.
        //Pintamos el elemento
        g.fillRoundRect(0, 0, this.width, this.height, this.radius, this.radius);
        super.paintComponent(g);
    }

}
