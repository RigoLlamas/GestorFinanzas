package vista;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

import excepciones.CuentaInvalidaException;

import java.awt.*;
import logica.GestorFinanzas;
import modelo.CuentaBancaria;

public class VentanaPrincipal extends JFrame {
	private GestorFinanzas gestor;
	private DefaultListModel<CuentaBancaria> modeloListaCuentas;
	private JList<CuentaBancaria> listaCuentas;
	private JLabel labelSaldoTotal;
	private PanelHistorial panelHistorial;
	private PanelGraficas panelGraficas;
	
	
	public VentanaPrincipal(GestorFinanzas gestor) {
		this.gestor = gestor;
		setTitle("Gestor de Finanzas Personales");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(800, 500);
		setLocationRelativeTo(null);
		
		modeloListaCuentas = new DefaultListModel<>();
		listaCuentas = new JList<>(modeloListaCuentas);
		listaCuentas.setCellRenderer(new CuentaListRenderer());
		listaCuentas.addListSelectionListener(e -> {
			if (!e.getValueIsAdjusting()) {
				actualizarHistorial();
			}
		});
		
		JPopupMenu menuContextual = new JPopupMenu();
		JMenuItem itemModificar = new JMenuItem("Modificar");
		JMenuItem itemEliminar = new JMenuItem("Eliminar");
		
		menuContextual.add(itemModificar);
		menuContextual.add(itemEliminar);
		
		panelHistorial = new PanelHistorial();
		panelGraficas = new PanelGraficas();
		labelSaldoTotal = new JLabel("Balance total: $0.00");
		labelSaldoTotal.setFont(new Font("SansSerif", Font.BOLD, 16));
		
		// BOTONES DE ACCION
		itemModificar.addActionListener(e -> {
		    CuentaBancaria seleccionada = listaCuentas.getSelectedValue();
		    if (seleccionada != null) {
		    	DialogoModificarCuenta dialogoEdicion = new DialogoModificarCuenta(this, gestor, seleccionada, () -> {
		            actualizarListaCuentas();
		            panelHistorial.mostrarCuenta(seleccionada);
		        });
		        dialogoEdicion.setVisible(true);
		    }
		});

		itemEliminar.addActionListener(e -> {
		    CuentaBancaria seleccionada = listaCuentas.getSelectedValue();
		    if (seleccionada != null) {
		        int confirmacion = JOptionPane.showConfirmDialog(
		            this, 
		            "¿Estás seguro de que deseas eliminar la cuenta " + seleccionada.getNumeroCuenta() + " y todo su historial?", 
		            "Confirmar Eliminación", 
		            JOptionPane.YES_NO_OPTION,
		            JOptionPane.WARNING_MESSAGE
		        );
		        
		        if (confirmacion == JOptionPane.YES_OPTION) {
		        	
		        	try {
						gestor.eliminarCuenta(seleccionada.getNumeroCuenta());
					} catch (CuentaInvalidaException e1) {
						JOptionPane.showMessageDialog(this, "Surgió un problema al eleminar la cuenta", "Error en la eliminacion", JOptionPane.ERROR_MESSAGE);
					}
		            actualizarListaCuentas();
		            panelHistorial.mostrarCuenta(null); 
		        }
		    }
		});
		
		JButton botonNuevoMovimiento = new JButton("Nuevo movimiento");
		botonNuevoMovimiento.addActionListener(e -> abrirDialogoNuevoMovimiento());
		
		JButton botonNuevaCuenta = new JButton("Nueva cuenta");
		botonNuevaCuenta.addActionListener(e -> abrirDialogoNuevaCuenta());
		
		listaCuentas.addMouseListener(new MouseAdapter() {
		    @Override
		    public void mouseReleased(MouseEvent e) {
		        // Detectar si fue click derecho (isPopupTrigger o verificando el botón directamente)
		        if (SwingUtilities.isRightMouseButton(e) && !modeloListaCuentas.isEmpty()) {
		            
		            // Obtener el índice de la fila donde se hizo click
		            int index = listaCuentas.locationToIndex(e.getPoint());
		            
		            // Si el click fue sobre un elemento válido, seleccionarlo y mostrar menú
		            if (index != -1 && listaCuentas.getCellBounds(index, index).contains(e.getPoint())) {
		                listaCuentas.setSelectedIndex(index);
		                menuContextual.show(listaCuentas, e.getX(), e.getY());
		            }
		        }
		    }
		});
		
		// Panel para agrupar los botones en la parte inferior izquierda
		JPanel panelAccionesIzquierda = new JPanel(new GridLayout(2, 1, 5, 5));
		panelAccionesIzquierda.add(botonNuevoMovimiento);
		panelAccionesIzquierda.add(botonNuevaCuenta);
		
		JPanel panelIzquierdo = new JPanel(new BorderLayout());
		panelIzquierdo.add(new JScrollPane(listaCuentas), BorderLayout.CENTER);
		panelIzquierdo.add(panelAccionesIzquierda, BorderLayout.SOUTH);
		panelIzquierdo.setPreferredSize(new Dimension(250, 0));
		
		JPanel panelVistaClasica = new JPanel(new BorderLayout());
	    panelVistaClasica.add(panelIzquierdo, BorderLayout.WEST);
	    panelVistaClasica.add(panelHistorial, BorderLayout.CENTER);
	    
	    JTabbedPane sistemaPestañas = new JTabbedPane();
	    sistemaPestañas.addTab("Cuentas e Historial", panelVistaClasica);
	    sistemaPestañas.addTab("Grafica", panelGraficas);
		
		JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panelSuperior.add(labelSaldoTotal);
		
		setLayout(new BorderLayout());
	    add(panelSuperior, BorderLayout.NORTH);
	    add(sistemaPestañas, BorderLayout.CENTER);
		
		actualizarListaCuentas();
	}
	
	public void actualizarListaCuentas() {
		modeloListaCuentas.clear();
		for (CuentaBancaria cuenta : gestor.getListaCuentas()) {
			modeloListaCuentas.addElement(cuenta);
		}
		actualizarSaldoTotal();
	}
	
	private void actualizarSaldoTotal() {
		labelSaldoTotal.setText(String.format("Balance total: $%,.2f", gestor.calcularBalanceTotal()));
	}
	
	private void actualizarHistorial() {
		CuentaBancaria seleccionada = listaCuentas.getSelectedValue();
		panelHistorial.mostrarCuenta(seleccionada);
		panelGraficas.actualizarGraficas(gestor, seleccionada);
	}
	
	private void abrirDialogoNuevoMovimiento() {
		DialogoNuevoGasto dialogo = new DialogoNuevoGasto(this, gestor, () -> {
			actualizarListaCuentas();
			actualizarHistorial();
		});
		dialogo.setVisible(true);
	}
	
	private void abrirDialogoNuevaCuenta() {
		DialogoNuevaCuenta dialogo = new DialogoNuevaCuenta(this, gestor, () -> {
			actualizarListaCuentas();
			actualizarHistorial();
		});
		dialogo.setVisible(true);
	}
	
	private static class CuentaListRenderer extends DefaultListCellRenderer {
		@Override
		public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
			super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			if (value instanceof CuentaBancaria) {
				CuentaBancaria cuenta = (CuentaBancaria) value;
				setText(cuenta.getNumeroCuenta() + " - " + cuenta.getTitular() + " ($" + String.format("%,.2f", cuenta.getSaldo()) + ")");
			}
			return this;
		}
	}
}
