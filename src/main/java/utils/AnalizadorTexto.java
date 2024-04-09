package utils;
import java.util.HashMap;
import java.util.Map;

public class AnalizadorTexto {

    public static int contarPalabras(String texto) {
        if (texto == null || texto.isEmpty()) {
            return 0;
        }
        String[] palabras = texto.split("\\s+");
        return palabras.length;
    }

    public static Map<String, Integer> frecuenciaPalabras(String texto) {
        Map<String, Integer> frecuencias = new HashMap<>();
        String[] palabras = texto.split("\\s+");
        for (String palabra : palabras) {
            frecuencias.put(palabra, frecuencias.getOrDefault(palabra, 0) + 1);
        }
        return frecuencias;
    }
}
