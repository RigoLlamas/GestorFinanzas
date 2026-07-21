package vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import logica.GestorFinanzas;
import modelo.CategoriaEnum;
import modelo.CuentaBancaria;
import excepciones.CuentaInvalidaException;
import excepciones.FondosInsuficientesException;

public class DialogoNuevoGasto extends JDialog {
	private GestorFinanzas gestor;
	private Runnable alGuardar;
	private List<CuentaBancaria> listaCuentasCompleta;
	
	private JComboBox<CuentaBancaria> comboCuentas;
	private JComboBox<CategoriaEnum> comboCategorias;
	private JRadioButton radioGasto;
	private JRadioButton radioIngreso;
	private JTextField campoMonto;
	private JTextField campoConcepto;
	
	public DialogoNuevoGasto(JFrame parent, GestorFinanzas gestor, Runnable alGuardar) {
		super(parent, "Nuevo movimiento", true);
		this.gestor = gestor;
		this.alGuardar = alGuardar;
		this.listaCuentasCompleta = gestor.getListaCuentas();
		
		setSize(350, 360);
		setLocationRelativeTo(parent);
		
		JPanel panelContenido = new JPanel();
		panelContenido.setLayout(new BoxLayout(panelContenido, BoxLayout.Y_AXIS));
		panelContenido.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
		
		comboCuentas = new JComboBox<>(new DefaultComboBoxModel<>(listaCuentasCompleta.toArray(new CuentaBancaria[0])));
		comboCuentas.setRenderer(new CuentaComboRenderer());
		comboCuentas.setEditable(true);
		
		// CORRECCIÓN 2: Editor personalizado para evitar direcciones de memoria
		comboCuentas.setEditor(new javax.swing.plaf.basic.BasicComboBoxEditor() {
			@Override
			public void setItem(Object item) {
				if (item instanceof CuentaBancaria) {
					CuentaBancaria cuenta = (CuentaBancaria) item;
					super.setItem(cuenta.getNumeroCuenta() + " - " + cuenta.getTitular());
				} else {
					super.setItem(item);
				}
			}
		});
		
		JTextField editorTextoCombo = (JTextField) comboCuentas.getEditor().getEditorComponent();
		editorTextoCombo.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_UP || 
					e.getKeyCode() == KeyEvent.VK_DOWN || 
					e.getKeyCode() == KeyEvent.VK_ENTER || 
					e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					return;
				}
				
				String textoFiltro = editorTextoCombo.getText().toLowerCase();
				DefaultComboBoxModel<CuentaBancaria> modeloFiltrado = new DefaultComboBoxModel<>();
				
				for (CuentaBancaria cuenta : listaCuentasCompleta) {
					String titular = cuenta.getTitular().toLowerCase();
					String numero = cuenta.getNumeroCuenta().toLowerCase();
					
					if (titular.contains(textoFiltro) || numero.contains(textoFiltro)) {
						modeloFiltrado.addElement(cuenta);
					}
				}
				
				comboCuentas.setModel(modeloFiltrado);
				editorTextoCombo.setText(textoFiltro);
				
				if (modeloFiltrado.getSize() > 0) {
					comboCuentas.setPopupVisible(true);
				}
			}
		});
		
		radioGasto = new JRadioButton("Gasto", true);
		radioIngreso = new JRadioButton("Ingreso");
		
		ButtonGroup grupoTipo = new ButtonGroup();
		grupoTipo.add(radioGasto);
		grupoTipo.add(radioIngreso);
		JPanel panelTipo = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panelTipo.add(radioGasto);
		panelTipo.add(radioIngreso);
		panelTipo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
		
		campoMonto = new JTextField();
		campoConcepto = new JTextField();
		comboCategorias = new JComboBox<>(CategoriaEnum.values());
		
		Dimension tamañoCampos = new Dimension(Integer.MAX_VALUE, 25);
		comboCuentas.setMaximumSize(tamañoCampos);
		campoMonto.setMaximumSize(tamañoCampos);
		campoConcepto.setMaximumSize(tamañoCampos);
		comboCategorias.setMaximumSize(tamañoCampos);
		
		JButton botonGuardar = new JButton("Guardar");
		botonGuardar.addActionListener(e -> guardarMovimiento());
		
		JButton botonCancelar = new JButton("Cancelar");
		botonCancelar.addActionListener(e -> dispose());
		
		JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		panelBotones.add(botonCancelar);
		panelBotones.add(botonGuardar);
		panelBotones.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
		
		comboCuentas.setAlignmentX(Component.LEFT_ALIGNMENT);
		panelTipo.setAlignmentX(Component.LEFT_ALIGNMENT);
		campoMonto.setAlignmentX(Component.LEFT_ALIGNMENT);
		campoConcepto.setAlignmentX(Component.LEFT_ALIGNMENT);
		comboCategorias.setAlignmentX(Component.LEFT_ALIGNMENT);
		panelBotones.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		panelContenido.add(crearEtiquetaAlineada("Cuenta:"));
		panelContenido.add(Box.createVerticalStrut(4));
		panelContenido.add(comboCuentas);
		panelContenido.add(Box.createVerticalStrut(8));
		panelContenido.add(panelTipo);
		panelContenido.add(Box.createVerticalStrut(8));
		panelContenido.add(crearEtiquetaAlineada("Monto:"));
		panelContenido.add(Box.createVerticalStrut(4));
		panelContenido.add(campoMonto);
		panelContenido.add(Box.createVerticalStrut(8));
		panelContenido.add(crearEtiquetaAlineada("Concepto:"));
		panelContenido.add(Box.createVerticalStrut(4));
		panelContenido.add(campoConcepto);
		panelContenido.add(Box.createVerticalStrut(8));
		panelContenido.add(crearEtiquetaAlineada("Categoría:"));
		panelContenido.add(Box.createVerticalStrut(4));
		panelContenido.add(comboCategorias);
		panelContenido.add(Box.createVerticalStrut(15));
		panelContenido.add(panelBotones);
		
		setLayout(new BorderLayout());
		add(panelContenido, BorderLayout.CENTER);
	}
	
	private JLabel crearEtiquetaAlineada(String texto) {
		JLabel label = new JLabel(texto);
		label.setAlignmentX(Component.LEFT_ALIGNMENT);
		return label;
	}
	
	private void guardarMovimiento() {
		Object seleccionado = comboCuentas.getSelectedItem();
		CuentaBancaria cuentaSeleccionada = null;
		
		if (seleccionado instanceof CuentaBancaria) {
			cuentaSeleccionada = (CuentaBancaria) seleccionado;
		} else if (seleccionado instanceof String) {
			String texto = ((String) seleccionado).trim();
			
			for (CuentaBancaria cuenta : listaCuentasCompleta) {
				String textoFormateado = cuenta.getNumeroCuenta() + " - " + cuenta.getTitular();
				if (textoFormateado.equals(texto) || 
					cuenta.getNumeroCuenta().equals(texto) || 
					cuenta.getTitular().equalsIgnoreCase(texto)) {
					cuentaSeleccionada = cuenta;
					break;
				}
			}
			if (cuentaSeleccionada == null && comboCuentas.getItemCount() > 0) {
				cuentaSeleccionada = comboCuentas.getItemAt(0);
			}
		}
		
		if (cuentaSeleccionada == null) {
			JOptionPane.showMessageDialog(this, "Seleccione una cuenta válida de la lista.", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		double monto;
		try {
			monto = Double.parseDouble(campoMonto.getText().trim());
			
			// CORRECCIÓN 1: Se reemplaza el while por un if para evitar congelar la interfaz
			if(monto <= 0) {
				JOptionPane.showMessageDialog(this, "El monto debe ser un número positivo.", "Error", JOptionPane.ERROR_MESSAGE);
				return; // Interrumpe el guardado para que el usuario pueda corregir el campo
			}
			
		} catch (NumberFormatException ex) {
			JOptionPane.showMessageDialog(this, "El monto debe ser un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		String concepto = campoConcepto.getText().trim();
		if (concepto.isEmpty()) {
			JOptionPane.showMessageDialog(this, "El concepto no puede estar vacío.", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		CategoriaEnum categoria = (CategoriaEnum) comboCategorias.getSelectedItem();
		String numeroCuenta = cuentaSeleccionada.getNumeroCuenta();
		
		try {
			if (radioGasto.isSelected()) {
				gestor.registrarGasto(numeroCuenta, monto, concepto, categoria);
			} else {
				gestor.registrarIngreso(numeroCuenta, monto, concepto, categoria);
			}
			alGuardar.run();
			dispose();
		} catch (CuentaInvalidaException | FondosInsuficientesException ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		} catch (IllegalArgumentException ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private static class CuentaComboRenderer extends DefaultListCellRenderer {
		@Override
		public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
			super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			if (value instanceof CuentaBancaria) {
				CuentaBancaria cuenta = (CuentaBancaria) value;
				setText(cuenta.getNumeroCuenta() + " - " + cuenta.getTitular());
			}
			return this;
		}
	}
}