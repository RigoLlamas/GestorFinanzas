package modelo;

import excepciones.FondosInsuficientesException;

public class TarjetaDebito extends CuentaBancaria {

	public TarjetaDebito(String numeroCuenta, String titular, BancoEnum banco, double saldoInicial) {
		super(numeroCuenta, titular, banco, saldoInicial);
	}
	
	@Override
	public void retirar(double monto) throws FondosInsuficientesException {
		if(monto <= 0) {
			return;
		}
		if(monto > this.saldo) {
			throw new FondosInsuficientesException("Saldos insuficiente en cuenta de ahorros.");
		}
		setSaldo(getSaldo() - monto);
	}

	@Override
	public void depositar(double monto) {
		if(monto <= 0) {
			return;
		}
		setSaldo(getSaldo() + monto);
	}	
}
