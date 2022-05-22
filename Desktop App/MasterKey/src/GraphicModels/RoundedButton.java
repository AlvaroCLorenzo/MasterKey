package GraphicModels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JButton;

/**
 * @autor: Alvaro
 */
//Clase dedicada a crear un botón con bordes redondeados.
public class RoundedButton extends JButton {

    private Color backgroundColor, foregroundColor;
    private int width, height, radius;

    public RoundedButton(String string, int width, int height, int radius, Color backgroundColor, Color foregroundColor) {
        super(string);
        this.width = width;
        this.height = height;
        this.radius = radius;
        this.backgroundColor = backgroundColor;
        this.foregroundColor = foregroundColor;

        setOpaque(false);

        Dimension size = getPreferredSize();
        size.width = size.height = Math.max(size.width, size.height);
        setPreferredSize(size);

        setContentAreaFilled(false); //Evitar que se rellene el resto del area
    }

    @Override
    public void paintComponent(Graphics g) {

        if (getModel().isArmed()) {
            g.setColor(this.foregroundColor); //Color de fondo cuando se pulsa el botón.
            setForeground(this.backgroundColor); //Color de la inscripción al pulsar el botón.
        } else {
            g.setColor(this.backgroundColor); //Color de fondo.
            setForeground(this.foregroundColor); //Color de la inscripcion.
        }
        //Pintamos el elemento
        g.fillRoundRect(0, 0, this.width, this.height, this.radius, this.radius);
        super.paintComponent(g);
    }

    @Override
    protected void paintBorder(Graphics g) {
        //g.setColor(Color.getForeground()); //Si queremos un borde que cambie de color con el clic
        g.drawRoundRect(0, 0, getSize().width - 1, getSize().height - 1, radius, radius);
    }

}
