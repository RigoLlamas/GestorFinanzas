package vista;

import javax.swing.*;
import java.awt.*;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
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
                "Distribución de Saldos",   // Título
                datasetPastel,              // Datos
                false,                      // Leyenda
                true,                       // Tooltips
                false                       // URLs
        );
        
        aplicarTema(graficaPastel, true); // Aplicar colores del tema actual
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
                "",                     // Etiqueta Eje X
                "Monto ($)",            // Etiqueta Eje Y
                datasetBarras           // Datos
        );
        
        aplicarTema(graficaBarras, false); // Aplicar colores del tema actual
        
        // Colores personalizados para las barras (Verde y Rojo) se mantienen igual
        CategoryPlot plotBarras = graficaBarras.getCategoryPlot();
        plotBarras.getRenderer().setSeriesPaint(0, new Color(34, 139, 34)); // Ingresos
        plotBarras.getRenderer().setSeriesPaint(1, new Color(220, 20, 60)); // Gastos
        
        panelBarras.setChart(graficaBarras);
    }

    // Método auxiliar para adaptar JFreeChart al tema oscuro o claro de FlatLaf.
    private void aplicarTema(JFreeChart chart, boolean esPastel) {
        // Leer colores directamente del Look And Feel activo
        Color colorFondo = UIManager.getColor("Panel.background");
        Color colorTexto = UIManager.getColor("Label.foreground");
        
        // FlatLaf guarda una bandera booleana para saber si el tema es oscuro
        boolean esOscuro = UIManager.getBoolean("laf.dark");
        Color colorCuadricula = esOscuro ? new Color(80, 80, 80) : new Color(220, 220, 220);

        // Fondo y título general
        chart.setBackgroundPaint(colorFondo);
        chart.getTitle().setPaint(colorTexto);

        if (esPastel) {
            PiePlot plot = (PiePlot) chart.getPlot();
            plot.setBackgroundPaint(colorFondo);
            plot.setOutlineVisible(false); // Quitar borde
            plot.setLabelBackgroundPaint(colorFondo);
            plot.setLabelPaint(colorTexto);
            plot.setLabelShadowPaint(null); // Quitar sombra anticuada
        } else {
            CategoryPlot plot = (CategoryPlot) chart.getPlot();
            plot.setBackgroundPaint(colorFondo);
            plot.setOutlineVisible(false);
            plot.setDomainGridlinePaint(colorCuadricula);
            plot.setRangeGridlinePaint(colorCuadricula);
            
            // Colorear texto de los ejes
            plot.getDomainAxis().setTickLabelPaint(colorTexto);
            plot.getDomainAxis().setLabelPaint(colorTexto);
            plot.getRangeAxis().setTickLabelPaint(colorTexto);
            plot.getRangeAxis().setLabelPaint(colorTexto);
        }
    }
}