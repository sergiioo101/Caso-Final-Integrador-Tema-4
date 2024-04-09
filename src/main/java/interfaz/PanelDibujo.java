package interfaz;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.util.ArrayList;

public class PanelDibujo extends JPanel {
    private ArrayList<Shape> formas = new ArrayList<>();
    private ArrayList<Point> puntos = new ArrayList<>();
    private Shape formaActual;
    private Point puntoInicio;
    private Color color = Color.BLACK;
    private JLabel etiquetaPosicionRaton;

    public PanelDibujo(JLabel etiquetaPosicionRaton) {
        this.etiquetaPosicionRaton = etiquetaPosicionRaton;
        setBackground(Color.WHITE);
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                puntoInicio = e.getPoint();
                if (SwingUtilities.isRightMouseButton(e)) {
                    formaActual = new Ellipse2D.Double(puntoInicio.x, puntoInicio.y, 0, 0);
                } else {
                    formaActual = new Path2D.Double();
                    ((Path2D) formaActual).moveTo(puntoInicio.getX(), puntoInicio.getY());
                }
                actualizarPosicionRaton(e.getX(), e.getY());
                repaint();
            }

            public void mouseReleased(MouseEvent e) {
                if (!SwingUtilities.isRightMouseButton(e)) {
                    formas.add(formaActual);
                    formaActual = null;
                }
                puntos.clear();
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                if (!SwingUtilities.isRightMouseButton(e)) {
                    Point punto = e.getPoint();
                    ((Path2D) formaActual).lineTo(punto.getX(), punto.getY());
                    puntos.add(punto);
                } else {
                    int width = e.getX() - puntoInicio.x;
                    int height = e.getY() - puntoInicio.y;
                    ((Ellipse2D) formaActual).setFrame(puntoInicio.x, puntoInicio.y, width, height);
                }
                actualizarPosicionRaton(e.getX(), e.getY());
                repaint();
            }

            public void mouseMoved(MouseEvent e) {
                actualizarPosicionRaton(e.getX(), e.getY());
            }
        });
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(color);
        for (Shape forma : formas) {
            g2d.draw(forma);
        }

        if (formaActual != null) {
            g2d.draw(formaActual);
        }
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void clear() {
        formas.clear();
        repaint();
    }

    private void actualizarPosicionRaton(int x, int y) {
        etiquetaPosicionRaton.setText("Posición del ratón: " + x + ", " + y);
    }
}
