package vista;

import javax.swing.*;
import java.awt.*;
import logica.GestorFinanzas;
import modelo.*;


public class DialogoNuevaCuenta extends JDialog {
	private GestorFinanzas gestor;
	private Runnable alGuardar;
	
	private JTextField campoNumeroCuenta;
	private JTextField campoTitular;
	private JComboBox<BancoEnum> comboBanco;
	private JComboBox<String> comboTipoCuenta;
	
	// Campos dinámicos
	private JTextField campoSaldoInicial;
	private JTextField campoLimiteCredito;
	private JPanel panelFilaSaldo;
	private JPanel panelFilaLimite;
	
	public DialogoNuevaCuenta(JFrame parent, GestorFinanzas gestor, Runnable alGuardar) {
		super(parent, "Nueva Cuenta Financiera", true);
		this.gestor = gestor;
		this.alGuardar = alGuardar;
		
		setSize(400, 350);
		setLocationRelativeTo(parent);
		setLayout(new BorderLayout(10, 10));
		
		inicializarComponentes();
	}
	
	private void inicializarComponentes() {
		// Panel principal del formulario con un BoxLayout vertical
		JPanel panelFormulario = new JPanel();
		panelFormulario.setLayout(new BoxLayout(panelFormulario, BoxLayout.Y_AXIS));
		panelFormulario.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
		
		// 1. Fila: Seleccionar Banco
		JPanel panelFilaBanco = crearFilaFormulario("Banco:", comboBanco = new JComboBox<>(BancoEnum.values()));
		panelFormulario.add(panelFilaBanco);
		panelFormulario.add(Box.createVerticalStrut(8));
		
		// 2. Fila: Tipo de Cuenta
		String[] tipos = {"Cuenta Ahorro", "Tarjeta de Débito", "Tarjeta de Crédito"};
		comboTipoCuenta = new JComboBox<>(tipos);
		JPanel panelFilaTipo = crearFilaFormulario("Tipo de Cuenta:", comboTipoCuenta);
		panelFormulario.add(panelFilaTipo);
		panelFormulario.add(Box.createVerticalStrut(8));
		
		// 3. Fila: Número de Cuenta
		campoNumeroCuenta = new JTextField();
		JPanel panelFilaNumero = crearFilaFormulario("Número de Cuenta:", campoNumeroCuenta);
		panelFormulario.add(panelFilaNumero);
		panelFormulario.add(Box.createVerticalStrut(8));
		
		// 4. Fila: Titular
		campoTitular = new JTextField();
		JPanel panelFilaTitular = crearFilaFormulario("Titular:", campoTitular);
		panelFormulario.add(panelFilaTitular);
		panelFormulario.add(Box.createVerticalStrut(8));
		
		// 5. Fila Dinámica: Saldo Inicial (Visible por defecto)
		campoSaldoInicial = new JTextField();
		panelFilaSaldo = crearFilaFormulario("Saldo Inicial ($):", campoSaldoInicial);
		panelFormulario.add(panelFilaSaldo);
		
		// 6. Fila Dinámica: Límite de Crédito (Oculta por defecto)
		campoLimiteCredito = new JTextField();
		panelFilaLimite = crearFilaFormulario("Límite de Crédito ($):", campoLimiteCredito);
		panelFilaLimite.setVisible(false); // Oculto al inicio
		panelFormulario.add(panelFilaLimite);
		
		// Escuchador del Tipo de Cuenta para alterar el formulario en tiempo de ejecución
		comboTipoCuenta.addActionListener(e -> alternarCamposPorTipo());
		
		// Panel Inferior de Botones
		JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JButton botonGuardar = new JButton("Guardar");
		JButton botonCancelar = new JButton("Cancelar");
		
		botonGuardar.addActionListener(e -> guardarCuenta());
		botonCancelar.addActionListener(e -> dispose());
		
		panelBotones.add(botonGuardar);
		panelBotones.add(botonCancelar);
		
		add(panelFormulario, BorderLayout.CENTER);
		add(panelBotones, BorderLayout.SOUTH);
	}
	
	/**
	 * Método auxiliar para estandarizar la creación de filas ordenadas (Label + Componente)
	 */
	private JPanel crearFilaFormulario(String textoLabel, JComponent componente) {
		JPanel fila = new JPanel(new GridLayout(1, 2, 10, 0));
		JLabel label = new JLabel(textoLabel);
		label.setFont(new Font("SansSerif", Font.PLAIN, 12));
		fila.add(label);
		fila.add(componente);
		// Forzamos un tamaño máximo para que el BoxLayout no estire los componentes verticalmente
		fila.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
		return fila;
	}
	
	/**
	 * Controla qué filas mostrar u ocultar según el tipo de cuenta seleccionado
	 */
	private void alternarCamposPorTipo() {
		String tipoSeleccionado = (String) comboTipoCuenta.getSelectedItem();
		
		if ("Tarjeta de Crédito".equals(tipoSeleccionado)) {
			panelFilaSaldo.setVisible(false);
			panelFilaLimite.setVisible(true);
		} else {
			// "Cuenta Ahorro" o "Tarjeta de Débito"
			panelFilaSaldo.setVisible(true);
			panelFilaLimite.setVisible(false);
		}
		
		// Le pedimos a Swing que recalcule la distribución del BoxLayout de inmediato
		revalidate();
		repaint();
	}
	
	private void guardarCuenta() {
		BancoEnum bancoSeleccionado = (BancoEnum) comboBanco.getSelectedItem();
		String tipoSeleccionado = (String) comboTipoCuenta.getSelectedItem();
		String numero = campoNumeroCuenta.getText().trim();
		String titular = campoTitular.getText().trim();
		
		if (numero.isEmpty() || titular.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Por favor, completa el número de cuenta y el titular.", "Campos Incompletos", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		try {
			if ("Tarjeta de Crédito".equals(tipoSeleccionado)) {
				String limiteTexto = campoLimiteCredito.getText().trim();
				if (limiteTexto.isEmpty()) throw new IllegalArgumentException("Debe ingresar un límite de crédito.");
				
				double limite = Double.parseDouble(limiteTexto);
				if (limite <= 0) throw new IllegalArgumentException("El límite de crédito debe ser mayor a 0.");
				
				// LLAMADA AL GESTOR (Valida las firmas de métodos con el Desarrollador A)
				gestor.crearTarjetaCredito(numero, titular, bancoSeleccionado, limite);
				
			} else {
				// Cuenta Ahorro o Tarjeta de Débito
				String saldoTexto = campoSaldoInicial.getText().trim();
				if (saldoTexto.isEmpty()) throw new IllegalArgumentException("Debe ingresar el saldo inicial.");
				
				double saldoInicial = Double.parseDouble(saldoTexto);
				if (saldoInicial < 0) throw new IllegalArgumentException("El saldo inicial no puede ser negativo.");
				
				if ("Cuenta Ahorro".equals(tipoSeleccionado)) {
					// Como CuentaBancaria es abstracta, aquí llamarías al método que crea una cuenta ahorro concreta
					gestor.crearCuentaAhorro(numero, titular, bancoSeleccionado, saldoInicial);
				} else {
					// Tarjeta de Débito
					gestor.crearTarjetaDebito(numero, titular, bancoSeleccionado, saldoInicial);
				}
			}
			
			// Si todo sale bien, ejecutamos el callback reactivo y cerramos
			alGuardar.run();
			dispose();
			
		} catch (NumberFormatException ex) {
			JOptionPane.showMessageDialog(this, "El valor monetario ingresado no es válido.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
		} catch (IllegalArgumentException ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Validación", JOptionPane.WARNING_MESSAGE);
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Error de lógica de negocio: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
}