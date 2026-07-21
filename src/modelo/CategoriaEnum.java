package modelo;

public enum CategoriaEnum {
	ALIMENTOS("Alimentos y Súper"),
    TRANSPORTE("Transporte y Gasolina"),
    SERVICIOS("Servicios (Agua, Luz, Internet)"),
    SALUD("Salud y Farmacia"),
    ENTRETENIMIENTO("Entretenimiento"),
    INGRESOS("Ingresos y Salario"),
    OTROS("Otros Gastos");
	
	private final String descripcion;
	
	CategoriaEnum(String descripcion){
		this.descripcion = descripcion;
	}
	
	public String getDescripcion() {
		return descripcion;
	}
}
