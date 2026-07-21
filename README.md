# Gestor de Finanzas Personales

Una aplicación de escritorio desarrollada en Java con Swing para la gestión de cuentas bancarias, tarjetas de crédito y seguimiento detallado de ingresos y gastos.

## Características Principales
* **Gestión de Cuentas:** Soporte para Cuentas de Ahorro, Tarjetas de Débito y Tarjetas de Crédito.
* **Registro de Transacciones:** Control de ingresos y gastos categorizados con actualización de saldo en tiempo real.
* **Persistencia de Datos:** Guardado automático local mediante serialización binaria de objetos (`.dat`).
* **Interfaz Moderna:** Diseño responsivo utilizando la librería FlatLaf (Look and Feel).
* **Dashboard Visual:** Gráficas interactivas de distribución de saldo y análisis de ingresos vs. gastos generadas con JFreeChart.

## Arquitectura del Proyecto
El sistema sigue un patrón de diseño en capas para separar responsabilidades:
* `modelo/`: Entidades de negocio (CuentaBancaria, Transaccion, Enums).
* `logica/`: Reglas de negocio y cálculos (`GestorFinanzas`).
* `vista/`: Interfaces gráficas, diálogos y componentes Swing.
* `conexion/`: Manejo de entrada/salida de datos (`GestorArchivos`).
* `excepciones/`: Control de errores personalizados (ej. `FondosInsuficientesException`).

## Tecnologías Utilizadas
* **Lenguaje:** Java 
* **Interfaz Gráfica:** Java Swing
* **Librerías Externas:** * `FlatLaf` (Tema visual moderno)
  * `JFreeChart` (Renderizado de gráficas)

## Instalación y Ejecución
1. Clonar el repositorio.
2. Asegurar que las librerías `flatlaf.jar` y `jfreechart.jar` estén incluidas en el *Build Path* del proyecto (carpeta `lib/`).
3. Compilar el proyecto.
4. Ejecutar la clase `Main.java`.
