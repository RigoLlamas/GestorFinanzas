package logica;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import modelo.CategoriaEnum;
import modelo.BancoEnum;
import modelo.CuentaBancaria;
import modelo.Transaccion;
import modelo.Transaccion.Tipo;
import modelo.TarjetaCredito;
import modelo.TarjetaDebito;
import modelo.CuentaAhorro;

import excepciones.CuentaInvalidaException;
import excepciones.FondosInsuficientesException;


public class GestorFinanzas {
	private List<CuentaBancaria> listaCuentas;
	
	public GestorFinanzas() {
		this.listaCuentas = new ArrayList<>();
	}
	
	// CREAR TARJETAS
	public void crearTarjetaDebito(String numeroCuenta, String titular, BancoEnum banco, double saldo) throws CuentaInvalidaException {
	    if (existeCuenta(numeroCuenta)) {
	        throw new CuentaInvalidaException("El número de cuenta ya existe.");
	    }
	    if (saldo < 0) {
	        throw new CuentaInvalidaException("El saldo inicial no puede ser negativo.");
	    }

	    TarjetaDebito nueva = new TarjetaDebito(numeroCuenta, titular, banco, saldo);
	    listaCuentas.add(nueva);
	}
	
	public void crearTarjetaCredito(String numeroCuenta, String titular, BancoEnum banco, double limiteCredito) throws CuentaInvalidaException {
        if (existeCuenta(numeroCuenta)) {
            throw new CuentaInvalidaException("Ya existe una cuenta con el número: " + numeroCuenta);
        }
        if (limiteCredito <= 0) {
            throw new CuentaInvalidaException("El límite de crédito debe ser mayor a cero.");
        }

        // Instanciación (saldo inicial para crédito es 0.0)
        TarjetaCredito nuevaCredito = new TarjetaCredito(numeroCuenta, titular, banco, limiteCredito);
        listaCuentas.add(nuevaCredito);
    }
	
	public void crearCuentaAhorro(String numeroCuenta, String titular, BancoEnum banco, double saldo) throws CuentaInvalidaException {
		if (existeCuenta(numeroCuenta)) {
            throw new CuentaInvalidaException("Ya existe una cuenta con el número: " + numeroCuenta);
        }
		if (saldo < 0) {
	        throw new CuentaInvalidaException("El saldo inicial no puede ser negativo.");
	    }
		
		CuentaAhorro nuevaCuenta = new CuentaAhorro(numeroCuenta, titular, banco, saldo);
		listaCuentas.add(nuevaCuenta);
		
	}
	
	// METODOS PARA MANEJO DE CUENTA
	public void agregarCuenta(CuentaBancaria cuenta) {
		this.listaCuentas.add(cuenta);
	}
	
	public boolean existeCuenta(String numeroCuenta) {
		for(CuentaBancaria cuenta :listaCuentas) {
			if(cuenta.getNumeroCuenta().equals(numeroCuenta)) {
				return true;
			}
		}
		return false;
	}
	
	public CuentaBancaria buscarCuenta(String numeroCuenta) throws CuentaInvalidaException {
		for(CuentaBancaria cuenta : listaCuentas) {
			if(cuenta.getNumeroCuenta().equals(numeroCuenta)) {
				return cuenta;
			}
		}
		throw new CuentaInvalidaException("La cuenta número " + numeroCuenta + " no existe.");
	}
	
	public void eliminarCuenta(String numeroCuenta) throws CuentaInvalidaException{
		CuentaBancaria cuentaAEliminar = buscarCuenta(numeroCuenta);
		listaCuentas.remove(cuentaAEliminar);
	}
	
	public void registrarGasto(String numeroCuenta, double monto, String concepto, CategoriaEnum categoria) 
			throws CuentaInvalidaException, FondosInsuficientesException {
		CuentaBancaria cuenta = buscarCuenta(numeroCuenta);
		cuenta.retirar(monto);
		Transaccion transaccion = new Transaccion(monto, LocalDateTime.now(), concepto, categoria, Tipo.GASTO);
		cuenta.agregarTransaccion(transaccion);
	}
	
	public void registrarIngreso(String numeroCuenta, double monto, String concepto, CategoriaEnum categoria) 
			throws CuentaInvalidaException {
		CuentaBancaria cuenta = buscarCuenta(numeroCuenta);
		cuenta.depositar(monto);
		Transaccion transaccion = new Transaccion(monto, LocalDateTime.now(), concepto, categoria, Tipo.INGRESO);
		cuenta.agregarTransaccion(transaccion);
	}
	
	public double calcularBalanceTotal() {
		double total = 0;
		for(CuentaBancaria cuenta : listaCuentas) {
			total += cuenta.getSaldo();
		}
		return total;
	}
	
	public List<CuentaBancaria> getListaCuentas() {
		return listaCuentas;
	}
	
}
