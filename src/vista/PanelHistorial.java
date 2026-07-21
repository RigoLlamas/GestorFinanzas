package vista;

import javax.swing.*;
import java.awt.*;
import modelo.CuentaBancaria;
import modelo.Transaccion;

public class PanelHistorial extends JPanel {
	private DefaultListModel<Transaccion> modeloTransacciones;
	private JList<Transaccion> listaTransacciones;
	private JLabel labelTitulo;
	
	private JPanel panelCentro;
	private CardLayout cardLayout;
	private JLabel labelEstadoVacio;
	
	public PanelHistorial() {
		setLayout(new BorderLayout());
		
		labelTitulo = new JLabel("Selecciona una cuenta para ver su historial");
		labelTitulo.setFont(new Font("SansSerif", Font.BOLD, 14));
		labelTitulo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		modeloTransacciones = new DefaultListModel<>();
		listaTransacciones = new JList<>(modeloTransacciones);
		listaTransacciones.setFont(new Font("Monospaced", Font.PLAIN, 13));
		JScrollPane scrollLista = new JScrollPane(listaTransacciones);
		
		// CONFIGURACIÓN DE LOS ESTADOS
		cardLayout = new CardLayout();
		panelCentro = new JPanel(cardLayout);
		
		JPanel panelVacio = new JPanel(new GridBagLayout());
		labelEstadoVacio = new JLabel("Selecciona una cuenta para ver su historial");
		labelEstadoVacio.setFont(new Font("SansSerif", Font.ITALIC, 14));
		labelEstadoVacio.setForeground(Color.GRAY);
		panelVacio.add(labelEstadoVacio);
		
		panelCentro.add(scrollLista, "LISTA");
		panelCentro.add(panelVacio, "VACIO");
		
		add(labelTitulo, BorderLayout.NORTH);
		add(panelCentro, BorderLayout.CENTER);
		
		// Iniciamos mostrando la carta de estado vacío
		cardLayout.show(panelCentro, "VACIO");
	}
	
	public void mostrarCuenta(CuentaBancaria cuenta) {
		modeloTransacciones.clear();
		
		// Caso 1: No hay ninguna cuenta seleccionada
		if (cuenta == null) {
			labelTitulo.setText("Historial de movimientos");
			labelEstadoVacio.setText("Selecciona una cuenta en el panel izquierdo");
			// Mostramos la carta vacía
			cardLayout.show(panelCentro, "VACIO"); 
			return;
		}
		
		labelTitulo.setText("Historial de " + cuenta.getNumeroCuenta() + " - " + cuenta.getTitular());
		
		// Caso 2: La cuenta seleccionada existe, pero no tiene transacciones
		if (cuenta.getTransacciones() == null || cuenta.getTransacciones().isEmpty()) {
			labelEstadoVacio.setText("No hay movimientos registrados en esta cuenta.");
			// Mostramos la carta vacía
			cardLayout.show(panelCentro, "VACIO"); 
		} 
		// Caso 3: La cuenta tiene transacciones
		else {
			for (Transaccion transaccion : cuenta.getTransacciones()) {
				modeloTransacciones.addElement(transaccion);
			}
			// Cambiamos a la carta de la lista
			cardLayout.show(panelCentro, "LISTA"); 
		}
	}
}