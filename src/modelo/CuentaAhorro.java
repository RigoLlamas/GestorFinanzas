package modelo;

import excepciones.FondosInsuficientesException;

public class CuentaAhorro extends CuentaBancaria {
    private static final long serialVersionUID = 1L;


    public CuentaAhorro(String numeroCuenta, String titular, BancoEnum banco, double saldoInicial) {
        super(numeroCuenta, titular, banco, saldoInicial);
    }


	@Override
	public void depositar(double monto) {
		if(monto <= 0) {
			return;
		}
		setSaldo(getSaldo() + monto);
	}

	@Override
	public void retirar(double monto) throws FondosInsuficientesException {
		if (monto > getSaldo()) {
            throw new FondosInsuficientesException("Saldo insuficiente en la cuenta de ahorros.");
        }
        setSaldo(getSaldo() - monto);
	}
}