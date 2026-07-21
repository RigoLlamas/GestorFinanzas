package conexion;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import modelo.CuentaBancaria;

public class GestorArchivos {
	
	public void guardarDatos(List<CuentaBancaria> listaCuentas, String rutaArchivo) throws IOException {
		
		File archivo = new File(rutaArchivo);
		File carpeta = archivo.getParentFile();
		
		if (carpeta != null && !carpeta.exists()) {
			carpeta.mkdirs(); 
		}
		
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(archivo))) {
			oos.writeObject(listaCuentas);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<CuentaBancaria> cargarDatos(String rutaArchivo) throws IOException, ClassNotFoundException {
		File archivo = new File(rutaArchivo);
		
		if(!archivo.exists()) {
			return new ArrayList<>();
		}
		
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
			return (List<CuentaBancaria>) ois.readObject();
		}
	}
}