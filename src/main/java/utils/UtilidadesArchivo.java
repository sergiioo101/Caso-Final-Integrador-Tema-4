package utils;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;

public class UtilidadesArchivo {
    private static final String EXTENSION = ".txt";

    public static File[] listarArchivosTexto(String rutaDirectorio) {
        File dir = new File(rutaDirectorio);
        return dir.listFiles((dir1, nombre) -> nombre.endsWith(EXTENSION));
    }

    public static String obtenerExtensionArchivo(File archivo) {
        String nombre = archivo.getName();
        int ultimoIndice = nombre.lastIndexOf(".");
        if (ultimoIndice == -1) {
            return ""; // extensión vacía
        }
        return nombre.substring(ultimoIndice);
    }
    public static void compararArchivosSeleccionados(JFrame parent) {
        JFileChooser selector = new JFileChooser("./Documentos/");
        selector.setFileFilter(new FileNameExtensionFilter("Archivos de texto", "txt"));
        selector.setDialogTitle("Seleccionar Primer Archivo para Comparar");

        int resultado1 = selector.showOpenDialog(parent);
        if (resultado1 == JFileChooser.APPROVE_OPTION) {
            File primerArchivo = selector.getSelectedFile();

            selector.setDialogTitle("Seleccionar Segundo Archivo para Comparar");
            int resultado2 = selector.showOpenDialog(parent);
            if (resultado2 == JFileChooser.APPROVE_OPTION) {
                File segundoArchivo = selector.getSelectedFile();
                try {
                    String resultadoComparacion = compararArchivos(primerArchivo, segundoArchivo);
                    JOptionPane.showMessageDialog(parent, resultadoComparacion, "Resultado de la Comparación", JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(parent, "Error al comparar archivos", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    public static String compararArchivos(File archivo1, File archivo2) throws IOException {
        // Asegúrate de que este método esté correctamente implementado según tu descripción anterior
        try (BufferedReader lector1 = new BufferedReader(new FileReader(archivo1));
             BufferedReader lector2 = new BufferedReader(new FileReader(archivo2))) {
            String linea1, linea2;
            while ((linea1 = lector1.readLine()) != null && (linea2 = lector2.readLine()) != null) {
                if (!linea1.equals(linea2)) {
                    return "Los archivos difieren";
                }
            }
            if (lector1.readLine() != null || lector2.readLine() != null) {
                return "Los archivos difieren";
            }
        }
        return "Los archivos son iguales";
    }
}
