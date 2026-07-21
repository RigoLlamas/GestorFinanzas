package vista;

import javax.swing.*;
import java.awt.*;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import logica.GestorFinanzas;
import modelo.CuentaBancaria;
import modelo.Transaccion;

public class PanelGraficas extends JPanel {
    private ChartPanel panelPastel;
    private ChartPanel panelBarras;

    public PanelGraficas() {
        setLayout(new GridLayout(1, 2, 10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        panelPastel = new ChartPanel(null);
        panelBarras = new ChartPanel(null);

        add(panelPastel);
        add(panelBarras);
    }

    public void actualizarGraficas(GestorFinanzas gestor, CuentaBancaria cuentaSeleccionada) {
        // GRÁFICA DE PASTEL (Distribución del dinero)
        DefaultPieDataset datasetPastel = new DefaultPieDataset();
        for (CuentaBancaria cuenta : gestor.getListaCuentas()) {
            if (cuenta.getSaldo() > 0) { 
                datasetPastel.setValue(cuenta.getNumeroCuenta() + " - " + cuenta.getTitular(), cuenta.getSaldo());
            }
        }

        JFreeChart graficaPastel = ChartFactory.createPieChart(
                "Distribución de Saldos", 	// Título
                datasetPastel,           	// Datos
                false,                   	// Leyenda
                true,                    	// Tooltips (Muestra valor al pasar el mouse)
                false                    	// URLs
        );
        panelPastel.setChart(graficaPastel);

        // CREAR GRÁFICA DE BARRAS (Ingresos vs Gastos de la cuenta)
        DefaultCategoryDataset datasetBarras = new DefaultCategoryDataset();
        
        if (cuentaSeleccionada != null && cuentaSeleccionada.getTransacciones() != null) {
            double totalIngresos = 0;
            double totalGastos = 0;

            for (Transaccion t : cuentaSeleccionada.getTransacciones()) {
                if (t.getMonto() > 0 && t.getTipo().equals(Transaccion.Tipo.INGRESO)) {
                    totalIngresos += t.getMonto();
                } else {
                    totalGastos += Math.abs(t.getMonto());
                }
            }
            
            datasetBarras.addValue(totalIngresos, "Ingresos", "Movimientos");
            datasetBarras.addValue(totalGastos, "Gastos", "Movimientos");
        }

        JFreeChart graficaBarras = ChartFactory.createBarChart(
                cuentaSeleccionada != null ? "Análisis de " + cuentaSeleccionada.getNumeroCuenta() : "Seleccione una cuenta",
                "",                    	// Etiqueta Eje X
                "Monto ($)",            // Etiqueta Eje Y
                datasetBarras           // Datos
        );
        
        // Colores personalizados para las barras (Verde y Rojo)
        graficaBarras.getCategoryPlot().getRenderer().setSeriesPaint(0, new Color(34, 139, 34)); // Ingresos
        graficaBarras.getCategoryPlot().getRenderer().setSeriesPaint(1, new Color(220, 20, 60)); // Gastos
        
        panelBarras.setChart(graficaBarras);
    }
}