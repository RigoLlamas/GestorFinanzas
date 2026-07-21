package vista;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.prefs.Preferences;

import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.FlatDarkLaf;

public class DialogoConfiguracion extends JDialog {
    private Preferences preferencias;
    private JFrame parent;

    public DialogoConfiguracion(JFrame parent) {
        super(parent, "Configuración", true);
        this.parent = parent;
        
        this.preferencias = Preferences.userNodeForPackage(app.Main.class); 

        setSize(450, 220);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        inicializarComponentes();
    }

    private void inicializarComponentes() {
        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BoxLayout(panelPrincipal, BoxLayout.Y_AXIS));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // --- SECCIÓN: TEMA VISUAL ---
        JPanel panelTema = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelTema.add(new JLabel("Tema visual:"));
        
        String[] opcionesTema = {"Claro", "Oscuro"};
        JComboBox<String> comboTema = new JComboBox<>(opcionesTema);
        String temaActual = preferencias.get("tema_visual", "Claro");
        comboTema.setSelectedItem(temaActual);

        comboTema.addActionListener(e -> cambiarTema((String) comboTema.getSelectedItem()));
        
        panelTema.add(comboTema);
        panelTema.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        // --- SECCIÓN: RUTA DE ARCHIVOS ---
        JPanel panelRuta = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelRuta.add(new JLabel("Ubicación de datos:"));
        
        JTextField campoRuta = new JTextField(preferencias.get("ruta_archivo_datos", "No definida"));
        campoRuta.setEditable(false); // Solo lectura
        campoRuta.setPreferredSize(new Dimension(220, 25));

        JButton btnCambiarRuta = new JButton("Modificar");
        btnCambiarRuta.addActionListener(e -> cambiarRuta(campoRuta));

        panelRuta.add(campoRuta);
        panelRuta.add(btnCambiarRuta);
        panelRuta.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        // Agregar al panel principal
        panelPrincipal.add(panelTema);
        panelPrincipal.add(Box.createVerticalStrut(10));
        panelPrincipal.add(panelRuta);

        add(panelPrincipal, BorderLayout.CENTER);

        // --- BOTÓN CERRAR ---
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> dispose());
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotones.add(btnCerrar);
        
        add(panelBotones, BorderLayout.SOUTH);
    }

    private void cambiarTema(String nuevoTema) {
        try {
            if ("Oscuro".equals(nuevoTema)) {
                UIManager.setLookAndFeel(new FlatDarkLaf());
            } else {
                UIManager.setLookAndFeel(new FlatLightLaf());
            }
            preferencias.put("tema_visual", nuevoTema);
            
            // Refrescar toda la interfaz (Ventana principal y el propio diálogo)
            SwingUtilities.updateComponentTreeUI(parent);
            SwingUtilities.updateComponentTreeUI(this);
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al aplicar el tema.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cambiarRuta(JTextField campoRuta) {
        JFileChooser selectorCarpeta = new JFileChooser();
        selectorCarpeta.setDialogTitle("Selecciona la nueva ubicación");
        selectorCarpeta.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        if (selectorCarpeta.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File carpeta = selectorCarpeta.getSelectedFile();
            String nuevaRuta = carpeta.getAbsolutePath() + File.separator + "datos_financieros.dat";
            
            preferencias.put("ruta_archivo_datos", nuevaRuta);
            campoRuta.setText(nuevaRuta);
            
            // Advertencia necesaria por el ciclo de vida de Main.java
            JOptionPane.showMessageDialog(this,
                "La ruta se ha actualizado.\n\n" +
                "IMPORTANTE: El cambio surtirá efecto la próxima vez que abras la aplicación.\n" +
                "Deberás mover manualmente tu archivo 'datos_financieros.dat' a la nueva\n" +
                "carpeta si deseas conservar tu historial actual.",
                "Reinicio requerido", JOptionPane.WARNING_MESSAGE);
        }
    }
}