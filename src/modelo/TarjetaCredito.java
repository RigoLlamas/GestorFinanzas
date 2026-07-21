package modelo;

import excepciones.FondosInsuficientesException;

public class TarjetaCredito extends CuentaBancaria{
	private double limiteCredito;
	
	public TarjetaCredito(String numeroCuenta, String titular, BancoEnum banco, double limiteCredito) {
		super(numeroCuenta, titular, banco, 0.0);
		this.limiteCredito = limiteCredito;
	}
	
	@Override
	public void retirar(double monto) throws FondosInsuficientesException {
		if(monto <= 0) {
			return;
		}
		if((getSaldo() + monto) > this.limiteCredito) {
			throw new FondosInsuficientesException("El monto excede tu crédito disponible.");
		}
		setSaldo(getSaldo() - monto);
	}
	
	@Override
	public void depositar(double monto) {
		if(monto <= 0) {
			throw new IllegalArgumentException("El monto a pagar debe ser positivo.");
		}
		if(monto > getSaldo()) {
			throw new IllegalArgumentException("El pago excede la deuda actual.");
		}
		setSaldo(getSaldo() + monto);
	}
	
	public double getLimiteCredito() {
		return limiteCredito;
	}
	
	public double getCreditoDisponible() {
		return limiteCredito - this.saldo;
	}
}
