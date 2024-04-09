package interfaz;

import javax.swing.*;
import java.awt.*;
import java.io.File;

import utils.AnalizadorTexto;
import utils.UtilidadesArchivo;
import utils.ValidadorEmail;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;


public class PestañaUtilidades {
    public static JPanel crearPestanaUtilidades() {
        JPanel panelUtilidades = new JPanel(new BorderLayout());

        // Panel para la validación de email
        JPanel panelEmail = new JPanel(new FlowLayout());
        JTextField campoEmail = new JTextField(20);
        JLabel etiquetaValidacionEmail = new JLabel();
        campoEmail.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { validarEmail(); }
            public void removeUpdate(DocumentEvent e) { validarEmail(); }
            public void changedUpdate(DocumentEvent e) { validarEmail(); }
            private void validarEmail() {
                if (ValidadorEmail.esEmailValido(campoEmail.getText())) {
                    etiquetaValidacionEmail.setText("Válido");
                    etiquetaValidacionEmail.setForeground(Color.GREEN);
                } else {
                    etiquetaValidacionEmail.setText("Inválido");
                    etiquetaValidacionEmail.setForeground(Color.RED);
                }
            }
        });
        panelEmail.add(new JLabel("Email:"));
        panelEmail.add(campoEmail);
        panelEmail.add(etiquetaValidacionEmail);

        // Panel para el manejo de archivos
        JPanel panelArchivos = new JPanel(new FlowLayout());
        JTextField campoRutaDirectorio = new JTextField(20);
        JButton botonListarArchivos = new JButton("Listar Archivos");
        JTextArea areaResultadoArchivos = new JTextArea(10, 30);
        botonListarArchivos.addActionListener(e -> {
            String rutaDirectorio = campoRutaDirectorio.getText();
            if (!rutaDirectorio.isEmpty()) {
                File[] archivos = UtilidadesArchivo.listarArchivosTexto(rutaDirectorio);
                if (archivos != null) {
                    StringBuilder resultado = new StringBuilder();
                    for (File archivo : archivos) {
                        resultado.append(archivo.getName()).append("\n");
                    }
                    areaResultadoArchivos.setText(resultado.toString());
                } else {
                    areaResultadoArchivos.setText("Directorio no encontrado o sin archivos de texto.");
                }
            } else {
                areaResultadoArchivos.setText("Ingrese una ruta de directorio válida.");
            }
        });
        panelArchivos.add(new JLabel("Ruta de Directorio:"));
        panelArchivos.add(campoRutaDirectorio);
        panelArchivos.add(botonListarArchivos);
        panelArchivos.add(new JScrollPane(areaResultadoArchivos));

        // Panel para el análisis de texto
        JPanel panelTexto = new JPanel(new FlowLayout());
        JTextArea areaTexto = new JTextArea(10, 30);
        JButton botonContarPalabras = new JButton("Contar Palabras");
        JTextArea areaResultadoTexto = new JTextArea(10, 30);
        botonContarPalabras.addActionListener(e -> {
            String texto = areaTexto.getText();
            int numPalabras = AnalizadorTexto.contarPalabras(texto);
            areaResultadoTexto.setText("Número de palabras: " + numPalabras);
        });
        panelTexto.add(new JLabel("Texto:"));
        panelTexto.add(new JScrollPane(areaTexto));
        panelTexto.add(botonContarPalabras);
        panelTexto.add(new JScrollPane(areaResultadoTexto));

        // Combinar todos los paneles en uno solo
        JPanel panelUtilidadesContent = new JPanel(new GridLayout(3, 1));
        panelUtilidadesContent.add(panelEmail);
        panelUtilidadesContent.add(panelArchivos);
        panelUtilidadesContent.add(panelTexto);

        panelUtilidades.add(panelUtilidadesContent, BorderLayout.CENTER);
        return panelUtilidades;
    }
}
