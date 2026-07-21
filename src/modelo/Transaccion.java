package modelo;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Transaccion implements Serializable {
	private static final long serialVersionUID = 2L;
	private final double monto;
	private final LocalDateTime fecha;
	private final String concepto;
	private final CategoriaEnum categoria;
	private final Tipo tipo;
	
	public enum Tipo{
		INGRESO("Ingreso"), 
		GASTO("Gasto");
		
		private final String tipo;
		
		Tipo(String tipo){
			this.tipo = tipo;
		}
		
		public String getNombreOfical() {
			return tipo;
		}
	}
	
	public Transaccion(double monto, LocalDateTime time, String concepto, CategoriaEnum categoria, Tipo tipo) {
		super();
		this.monto = monto;
		this.fecha = time;
		this.concepto = concepto;
		this.categoria = categoria;
		this.tipo = tipo;
	}
	public double getMonto() {
		return monto;
	}
	public LocalDateTime getTime() {
		return fecha;
	}
	public String getConcepto() {
		return concepto;
	}
	public CategoriaEnum getCategoria() {
		return categoria;
	}
	public Tipo getTipo() {
		return tipo;
	}
	
	public String toString() {
		String signo = (tipo == Tipo.GASTO) ? "-" : "+";
		return String.format("%s: [%s] %s: %s$%,.2f (%s)",tipo , fecha.toLocalDate(), concepto, signo, monto, categoria.getDescripcion());
	}
}
