package modelo;

public enum BancoEnum {
	BBVA("BBVA Bancomer"),
	SANTANDER("Banco Santander"), 
	NU("Nubank"),
	MERCADO_PAGO("Mecado Pago"), 
	BANAMEX("Banamex");
	
	private final String nombreOfical;
	
	BancoEnum(String nombreOficial){
		this.nombreOfical = nombreOficial;
	}
	
	public String getNombreOfical() {
		return nombreOfical;
	}
}
