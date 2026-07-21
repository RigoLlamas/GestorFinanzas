package modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import excepciones.FondosInsuficientesException;

public abstract class CuentaBancaria implements Serializable{
	private static final long serialVersionUID = 1L;
	private String numeroCuenta;
	private String titular;
	private BancoEnum banco;
	protected double saldo;
	private List<Transaccion> transacciones;
	
	public CuentaBancaria(String numeroCuenta, String titular, BancoEnum banco, double saldoInicial) {
		setNumeroCuenta(numeroCuenta);
		setTitular(titular);
		setBanco(banco);
		setSaldo(saldoInicial);
		this.transacciones = new ArrayList<>();
	}
	
	// GETTERS
	public String getNumeroCuenta() {
		return numeroCuenta;
	}
	public String getTitular() {
		return titular;
	}
	public double getSaldo() {
		return saldo;
	}
	
	public BancoEnum getBanco() {
		return banco;
	}
	public List<Transaccion> getTransacciones() {
		return transacciones;
	}
	
	// SETTERS
	public void setNumeroCuenta(String numeroCuenta) {
		this.numeroCuenta = numeroCuenta;
	}
	public void setTitular(String titular) {
		this.titular = titular;
	}
	public void setSaldo(double saldo) {
		this.saldo = saldo;
	}
	
	public void setBanco(BancoEnum banco) {
		this.banco = banco;
	}
	public void setTransacciones(List<Transaccion> transacciones) {
		this.transacciones = transacciones;
	}
	
	// METODOS
	public abstract void depositar(double monto);
	
	public abstract void retirar(double monto) throws FondosInsuficientesException;
	
	public void agregarTransaccion(Transaccion T) {
		this.transacciones.add(T);
	}
	
	@Override
	public String toString() {
		return numeroCuenta + " - " + titular;
	}
	
}
