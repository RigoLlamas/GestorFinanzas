package vista;

import javax.swing.*;
import java.awt.*;
import logica.GestorFinanzas;
import modelo.*;

public class DialogoModificarCuenta extends JDialog {
	private GestorFinanzas gestor;
	private CuentaBancaria cuentaAEditar;
	private Runnable alGuardar;
	
	private JTextField campoNumeroCuenta;
	private JTextField campoTitular;
	private JComboBox<BancoEnum> comboBanco;
	private JTextField campoTipoCuenta; 
	
	public DialogoModificarCuenta(JFrame parent, GestorFinanzas gestor, CuentaBancaria cuentaAEditar, Runnable alGuardar) {
		super(parent, "Modificar Cuenta Financiera", true);
		this.gestor = gestor;
		this.cuentaAEditar = cuentaAEditar;
		this.alGuardar = alGuardar;
		
		setSize(400, 250); 
		setLocationRelativeTo(parent);
		setLayout(new BorderLayout(10, 10));
		
		inicializarComponentes();
		cargarDatosActuales();
	}
	
	private void inicializarComponentes() {
		JPanel panelFormulario = new JPanel();
		panelFormulario.setLayout(new BoxLayout(panelFormulario, BoxLayout.Y_AXIS));
		panelFormulario.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
		
		// 1. Fila: Banco (Bloqueado)
		comboBanco = new JComboBox<>(BancoEnum.values());
		comboBanco.setEnabled(false); 
		panelFormulario.add(crearFilaFormulario("Banco:", comboBanco));
		panelFormulario.add(Box.createVerticalStrut(8));
		
		// 2. Fila: Tipo de Cuenta (Bloqueado - Usamos JTextField para visualización estática)
		campoTipoCuenta = new JTextField();
		campoTipoCuenta.setEditable(false);
		panelFormulario.add(crearFilaFormulario("Tipo de Cuenta:", campoTipoCuenta));
		panelFormulario.add(Box.createVerticalStrut(8));
		
		// 3. Fila: Número de Cuenta (Bloqueado)
		campoNumeroCuenta = new JTextField();
		campoNumeroCuenta.setEditable(false);
		panelFormulario.add(crearFilaFormulario("Número de Cuenta:", campoNumeroCuenta));
		panelFormulario.add(Box.createVerticalStrut(8));
		
		// 4. Fila: Titular (El único campo editable)
		campoTitular = new JTextField();
		panelFormulario.add(crearFilaFormulario("Titular:", campoTitular));
		
		// Panel Inferior de Botones
		JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JButton botonGuardar = new JButton("Actualizar");
		JButton botonCancelar = new JButton("Cancelar");
		
		botonGuardar.addActionListener(e -> actualizarCuenta());
		botonCancelar.addActionListener(e -> dispose());
		
		panelBotones.add(botonGuardar);
		panelBotones.add(botonCancelar);
		
		add(panelFormulario, BorderLayout.CENTER);
		add(panelBotones, BorderLayout.SOUTH);
	}
	
	private JPanel crearFilaFormulario(String textoLabel, JComponent componente) {
		JPanel fila = new JPanel(new GridLayout(1, 2, 10, 0));
		JLabel label = new JLabel(textoLabel);
		label.setFont(new Font("SansSerif", Font.PLAIN, 12));
		fila.add(label);
		fila.add(componente);
		fila.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
		return fila;
	}
	
	private void cargarDatosActuales() {
		comboBanco.setSelectedItem(cuentaAEditar.getBanco());
		campoNumeroCuenta.setText(cuentaAEditar.getNumeroCuenta());
		campoTitular.setText(cuentaAEditar.getTitular());
		
		// Identificar el tipo de cuenta
		if (cuentaAEditar instanceof TarjetaCredito) {
			campoTipoCuenta.setText("Tarjeta de Crédito");
		} else if (cuentaAEditar instanceof TarjetaDebito) {
			campoTipoCuenta.setText("Tarjeta de Débito");
		} else if (cuentaAEditar instanceof CuentaAhorro) {
			campoTipoCuenta.setText("Cuenta Ahorro");
		} else {
			campoTipoCuenta.setText("Cuenta Genérica");
		}
	}
	
	private void actualizarCuenta() {
		String titularNuevo = campoTitular.getText().trim();
		
		if (titularNuevo.isEmpty()) {
			JOptionPane.showMessageDialog(this, "El nombre del titular no puede estar vacío.", "Campos Incompletos", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		try {
			cuentaAEditar.setTitular(titularNuevo); 
			
			alGuardar.run();
			dispose();
			
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Error al actualizar la cuenta: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
}