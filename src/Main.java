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

import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.FlatDarkLaf;

import java.util.prefs.Preferences;


public class Main {

    public static void main(String[] args) {
    	try {
            //UIManager.setLookAndFeel(new FlatLightLaf());
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception ex) {
            System.err.println("No se pudo inicializar FlatLaf. Se usará el diseño por defecto.");
        }
    	
    	GestorFinanzas gestor = new GestorFinanzas();
        GestorArchivos archivos = new GestorArchivos();
        
        // 1. Instanciamos las Preferencias para esta clase
        Preferences preferencias = Preferences.userNodeForPackage(Main.class);
        
        // 2. Buscamos si ya existe una ruta guardada de sesiones anteriores
        String archivoDatos = preferencias.get("ruta_archivo_datos", null);
        
        // 3. Si es null, significa que es la primera vez que el usuario abre la app
        if (archivoDatos == null) {
            JOptionPane.showMessageDialog(null, 
                "¡Bienvenido! Para comenzar, selecciona la carpeta donde deseas guardar tus datos financieros.", 
                "Configuración inicial", JOptionPane.INFORMATION_MESSAGE);
                
            JFileChooser selectorCarpeta = new JFileChooser();
            selectorCarpeta.setDialogTitle("Selecciona la carpeta para tus datos");
            selectorCarpeta.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); // Solo permite elegir carpetas
            
            int eleccion = selectorCarpeta.showOpenDialog(null);
            
            if (eleccion == JFileChooser.APPROVE_OPTION) {
                File carpetaSeleccionada = selectorCarpeta.getSelectedFile();
                // Construimos la ruta final uniendo la carpeta elegida y el nombre del archivo
                archivoDatos = carpetaSeleccionada.getAbsolutePath() + File.separator + "datos_financieros.dat";
                
                // Guardamos esta ruta en las preferencias para que no vuelva a preguntar
                preferencias.put("ruta_archivo_datos", archivoDatos);
            } else {
                // Si el usuario cancela la ventana, usamos una ruta por defecto
                String rutaUsuario = System.getProperty("user.home");
                archivoDatos = rutaUsuario + File.separator + "datos_financieros.dat";
                JOptionPane.showMessageDialog(null, 
                    "No seleccionaste ninguna carpeta. Los datos se guardarán por defecto en:\n" + archivoDatos, 
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        }
        
        final String rutaFinal = archivoDatos;

        // 4. Cargar datos
        try {
            System.out.println("Cargando datos desde: " + rutaFinal);
            List<CuentaBancaria> cuentasCargadas = archivos.cargarDatos(rutaFinal);
            for (CuentaBancaria c : cuentasCargadas) {
                gestor.agregarCuenta(c);
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("No se encontraron datos previos. Iniciando vacía.");
        }

        // 5. Shutdown Hook con la ruta final
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                archivos.guardarDatos(gestor.getListaCuentas(), rutaFinal);
                System.out.println("Datos guardados en: " + rutaFinal);
            } catch (IOException e) {
                System.err.println("Error crítico al guardar datos: " + e.getMessage());
            }
        }));

        // 6. Lanzar interfaz
        SwingUtilities.invokeLater(() -> {
            VentanaPrincipal ventana = new VentanaPrincipal(gestor);
            ventana.setVisible(true);
        });
    }
}