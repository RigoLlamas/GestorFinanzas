package app;

import logica.GestorFinanzas;
import conexion.GestorArchivos;
import vista.VentanaPrincipal;
import modelo.CuentaBancaria;

import javax.swing.UIManager;
import javax.swing.SwingUtilities;
import javax.swing.JOptionPane;
import javax.swing.JFileChooser;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.prefs.Preferences;

import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.FlatDarkLaf;

public class Main {

    public static void main(String[] args) {
        // 1. Instanciamos las Preferencias
        Preferences preferencias = Preferences.userNodeForPackage(Main.class);
        
        // 2. Configuración del Tema Visual
        String temaPreferido = preferencias.get("tema_visual", null);
        
        if (temaPreferido == null) {
            String[] opcionesTema = {"Claro", "Oscuro"};
            int seleccionTema = JOptionPane.showOptionDialog(null,
                    "¿Qué tema visual prefieres para la aplicación?",
                    "Configuración de Tema",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    opcionesTema,
                    opcionesTema[0]);
            
            // Si el usuario cierra la ventana, asignamos "Claro" por defecto
            temaPreferido = (seleccionTema == 1) ? "Oscuro" : "Claro";
            preferencias.put("tema_visual", temaPreferido);
        }
        
        // 3. Aplicar el Tema seleccionado
        try {
            if ("Oscuro".equals(temaPreferido)) {
                UIManager.setLookAndFeel(new FlatDarkLaf());
            } else {
                UIManager.setLookAndFeel(new FlatLightLaf());
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, 
                "No se pudo inicializar el tema visual. Se usará el diseño por defecto del sistema.", 
                "Error de Interfaz", JOptionPane.ERROR_MESSAGE);
        }

        // 4. Configuración de la ruta de datos
        String archivoDatos = preferencias.get("ruta_archivo_datos", null);
        
        if (archivoDatos == null) {
            JOptionPane.showMessageDialog(null, 
                "¡Bienvenido! Para comenzar, selecciona la carpeta donde deseas guardar tus datos financieros.", 
                "Configuración inicial", JOptionPane.INFORMATION_MESSAGE);
                
            JFileChooser selectorCarpeta = new JFileChooser();
            selectorCarpeta.setDialogTitle("Selecciona la carpeta para tus datos");
            selectorCarpeta.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            
            int eleccion = selectorCarpeta.showOpenDialog(null);
            
            if (eleccion == JFileChooser.APPROVE_OPTION) {
                File carpetaSeleccionada = selectorCarpeta.getSelectedFile();
                archivoDatos = carpetaSeleccionada.getAbsolutePath() + File.separator + "datos_financieros.dat";
                preferencias.put("ruta_archivo_datos", archivoDatos);
            } else {
                String rutaUsuario = System.getProperty("user.home");
                archivoDatos = rutaUsuario + File.separator + "datos_financieros.dat";
                JOptionPane.showMessageDialog(null, 
                    "No seleccionaste ninguna carpeta. Los datos se guardarán por defecto en:\n" + archivoDatos, 
                    "Aviso de Ubicación", JOptionPane.WARNING_MESSAGE);
            }
        }
        
        final String rutaFinal = archivoDatos;

        // 5. Cargar datos
        GestorFinanzas gestor = new GestorFinanzas();
        GestorArchivos archivos = new GestorArchivos();
        
        try {
            List<CuentaBancaria> cuentasCargadas = archivos.cargarDatos(rutaFinal);
            for (CuentaBancaria c : cuentasCargadas) {
                gestor.agregarCuenta(c);
            }
        } catch (IOException | ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, 
                "No se encontraron datos previos o el archivo está vacío. Iniciando con un registro nuevo.", 
                "Aviso de Datos", JOptionPane.INFORMATION_MESSAGE);
        }

        // 6. Shutdown Hook (NO USAR JOptionPane AQUÍ)
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                archivos.guardarDatos(gestor.getListaCuentas(), rutaFinal);
                System.out.println("Datos guardados correctamente en: " + rutaFinal);
            } catch (IOException e) {
                System.err.println("Error crítico al guardar datos durante el cierre: " + e.getMessage());
            }
        }));

        // 7. Lanzar interfaz
        SwingUtilities.invokeLater(() -> {
            VentanaPrincipal ventana = new VentanaPrincipal(gestor);
            ventana.setVisible(true);
        });
    }
}