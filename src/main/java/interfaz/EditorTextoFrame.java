package interfaz;

// Importaciones necesarias
import model.Contacto;
import utils.AnalizadorTexto;
import utils.UtilidadesArchivo;
import utils.ValidadorEmail;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Map;

import static utils.UtilidadesArchivo.obtenerExtensionArchivo;

public class EditorTextoFrame extends JFrame {
    private JTextArea areaTexto = new JTextArea();
    private JFileChooser selectorArchivos = new JFileChooser();
    private JLabel etiquetaPosicionRaton = new JLabel("Posición del ratón: ");
    private JTextField campoEmail = new JTextField(20);
    private JLabel etiquetaValidacionEmail = new JLabel();
    private ArrayList<Contacto> contactos = new ArrayList<>();
    private DefaultListModel<Contacto> modeloListaContactos = new DefaultListModel<>();
    private JList<Contacto> listaContactos = new JList<>(modeloListaContactos);
    private PanelDibujo panelDibujo = new PanelDibujo(etiquetaPosicionRaton);
    private static final String RUTA_DOCUMENTOS = "./Documentos/";
    private static final String RUTA_CONTACTOS = "./Contactos/";
    private static final String RUTA_DIBUJOS = "./Dibujos/";

    public EditorTextoFrame() {
        super("Editor de Texto");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        asegurarCarpetas();
        initUI();
        cargarContactos();
    }

    private void asegurarCarpetas() {
        asegurarCarpeta(RUTA_DOCUMENTOS);
        asegurarCarpeta(RUTA_CONTACTOS);
        asegurarCarpeta(RUTA_DIBUJOS);
    }

    private void asegurarCarpeta(String rutaCarpeta) {
        File carpeta = new File(rutaCarpeta);
        if (!carpeta.exists()) {
            carpeta.mkdirs();
        }
    }

    private void initUI() {
        crearBarraMenu();
        pestañasDocumentosSetup();
        pestañaContactosSetup();
        pestañaDibujoSetup();
        panelEmailSetup();
        etiquetaPosicionRatonSetup();
    }

    private void pestañasDocumentosSetup() {
        JTabbedPane pestañasDocumentos = new JTabbedPane();
        JScrollPane scrollPane = new JScrollPane(areaTexto);
        pestañasDocumentos.addTab("Documento", scrollPane);
        getContentPane().add(pestañasDocumentos, BorderLayout.CENTER);
    }

    private void pestañaContactosSetup() {
        JPanel panelContactos = new JPanel(new BorderLayout());

        JButton botonNuevoContacto = new JButton("Nuevo Contacto");
        botonNuevoContacto.addActionListener(e -> añadirNuevoContacto());
        panelContactos.add(botonNuevoContacto, BorderLayout.NORTH);
        panelContactos.add(new JScrollPane(listaContactos), BorderLayout.CENTER);

        JTabbedPane pestañasDocumentos = (JTabbedPane) getContentPane().getComponent(0);
        pestañasDocumentos.addTab("Contactos", panelContactos);
    }

    private void pestañaDibujoSetup() {
        JPanel panelDibujoWrapper = new JPanel(new BorderLayout());
        panelDibujoWrapper.add(panelDibujo, BorderLayout.CENTER);

        JTabbedPane pestañasDocumentos = (JTabbedPane) getContentPane().getComponent(0);
        pestañasDocumentos.addTab("Dibujo", panelDibujoWrapper);
    }

    private void panelEmailSetup() {
        JPanel panelEmail = new JPanel(new FlowLayout());
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
        getContentPane().add(panelEmail, BorderLayout.NORTH);
    }

    private void etiquetaPosicionRatonSetup() {
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                etiquetaPosicionRaton.setText("Posición del ratón: " + e.getX() + ", " + e.getY());
            }
        });
        getContentPane().add(etiquetaPosicionRaton, BorderLayout.SOUTH);
    }

    private void crearBarraMenu() {
        JMenuBar barraMenu = new JMenuBar();

        // Menú Archivo
        JMenu menuArchivo = new JMenu("Archivo");

        // Nuevo documento
        JMenuItem itemNuevo = new JMenuItem("Nuevo");
        itemNuevo.addActionListener(e -> crearNuevoTexto());
        menuArchivo.add(itemNuevo);

        // Abrir archivo
        JMenuItem itemAbrir = new JMenuItem("Abrir");
        itemAbrir.addActionListener(e -> abrirArchivoConSelector());
        menuArchivo.add(itemAbrir);

        // Guardar como
        JMenuItem itemGuardarComo = new JMenuItem("Guardar Como...");
        itemGuardarComo.addActionListener(e -> guardarTextoComo());
        menuArchivo.add(itemGuardarComo);

        JMenuItem itemCompararArchivos = new JMenuItem("Comparar Archivos...");
        itemCompararArchivos.addActionListener(e -> UtilidadesArchivo.compararArchivosSeleccionados(this));
        menuArchivo.add(itemCompararArchivos);

        barraMenu.add(menuArchivo);

        // Menú Analizar
        JMenu menuAnalizar = new JMenu("Analizar");
        JMenuItem itemContarPalabras = new JMenuItem("Contar Palabras");
        itemContarPalabras.addActionListener(e -> contarPalabrasTexto());
        menuAnalizar.add(itemContarPalabras);

        JMenuItem itemFrecuenciaPalabras = new JMenuItem("Frecuencia de Palabras");
        itemFrecuenciaPalabras.addActionListener(e -> mostrarFrecuenciaPalabras());
        menuAnalizar.add(itemFrecuenciaPalabras);

        barraMenu.add(menuAnalizar);

        setJMenuBar(barraMenu);
    }

    private void contarPalabrasTexto() {
        int cantidad = AnalizadorTexto.contarPalabras(areaTexto.getText());
        JOptionPane.showMessageDialog(this, "Total de palabras: " + cantidad, "Análisis de Texto", JOptionPane.INFORMATION_MESSAGE);
    }

    private void mostrarFrecuenciaPalabras() {
        Map<String, Integer> frecuencias = AnalizadorTexto.frecuenciaPalabras(areaTexto.getText());
        StringBuilder mensaje = new StringBuilder("<html>Frecuencia de Palabras:<br>");
        frecuencias.forEach((palabra, frecuencia) -> mensaje.append(palabra).append(": ").append(frecuencia).append("<br>"));
        mensaje.append("</html>");
        JOptionPane.showMessageDialog(this, mensaje.toString(), "Frecuencia de Palabras", JOptionPane.INFORMATION_MESSAGE);
    }


    private void crearNuevoTexto() {
        areaTexto.setText("");
        setTitle("Nuevo Documento");
    }

    private void abrirArchivoConSelector() {
        selectorArchivos.setCurrentDirectory(new File(RUTA_DOCUMENTOS));
        selectorArchivos.setFileFilter(new FileNameExtensionFilter("Archivos de texto", "txt"));
        int resultado = selectorArchivos.showOpenDialog(this);
        if (resultado == JFileChooser.APPROVE_OPTION) {
            File archivo = selectorArchivos.getSelectedFile();
            leerArchivo(archivo);
        }
    }

    private void leerArchivo(File archivo) {
        try (BufferedReader lector = new BufferedReader(new FileReader(archivo))) {
            areaTexto.read(lector, null);
            setTitle(archivo.getName());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "No se pudo leer el archivo", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void guardarTextoComo() {
        selectorArchivos.setCurrentDirectory(new File(RUTA_DOCUMENTOS));
        selectorArchivos.setFileFilter(new FileNameExtensionFilter("Archivo de texto", "txt"));
        int resultado = selectorArchivos.showSaveDialog(this);
        if (resultado == JFileChooser.APPROVE_OPTION) {
            File archivo = selectorArchivos.getSelectedFile();
            if (!".txt".equals(obtenerExtensionArchivo(archivo))) {
                archivo = new File(archivo.getPath() + ".txt");
            }
            try (FileWriter writer = new FileWriter(archivo)) {
                writer.write(areaTexto.getText());
                JOptionPane.showMessageDialog(this, "Archivo guardado con extensión .txt", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                setTitle(archivo.getName());
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error al guardar el archivo", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    private void cargarContactos() {
        File archivoContactos = new File(RUTA_CONTACTOS + "contactos.dat");
        if (archivoContactos.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivoContactos))) {
                contactos = (ArrayList<Contacto>) ois.readObject();
                actualizarListaContactos();
            } catch (IOException | ClassNotFoundException e) {
                JOptionPane.showMessageDialog(this, "No se pudo cargar los contactos", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void guardarContactos() {
        File archivoContactos = new File(RUTA_CONTACTOS + "contactos.dat");
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(archivoContactos))) {
            oos.writeObject(contactos);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "No se pudo guardar los contactos", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void añadirNuevoContacto() {
        JTextField nombreField = new JTextField(10);
        JTextField emailField = new JTextField(10);
        JTextField telefonoField = new JTextField(10);
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Nombre:"));
        panel.add(nombreField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(new JLabel("Teléfono:"));
        panel.add(telefonoField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Ingrese los detalles del nuevo contacto", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            Contacto nuevoContacto = new Contacto(nombreField.getText(), emailField.getText(), telefonoField.getText());
            contactos.add(nuevoContacto);
            guardarContactos();
            actualizarListaContactos();
            JOptionPane.showMessageDialog(this, "Contacto añadido con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void actualizarListaContactos() {
        modeloListaContactos.clear();
        for (Contacto contacto : contactos) {
            modeloListaContactos.addElement(contacto);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new EditorTextoFrame().setVisible(true));
    }
}