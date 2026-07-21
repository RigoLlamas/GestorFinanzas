# Guía de Usuario - Gestor de Finanzas Personales

Bienvenido al Gestor de Finanzas Personales. Esta guía explica cómo utilizar las funciones principales del sistema para llevar un control preciso de tu dinero.

## 1. Configuración Inicial
Al ejecutar la aplicación por primera vez, el sistema solicitará que elijas una carpeta en tu computadora. 
* Selecciona un directorio seguro (como tu carpeta de Documentos). 
* En este lugar se creará el archivo `datos_financieros.dat`, el cual guardará toda tu información automáticamente al cerrar la ventana. No borres este archivo, o perderás tu historial.

## 2. Creación de Cuentas
Antes de registrar movimientos, necesitas crear al menos una cuenta.
1. En el panel izquierdo, haz clic en el botón **"Nueva cuenta"**.
2. Selecciona el **Banco** y el **Tipo de Cuenta** (Ahorro, Débito o Crédito).
3. Ingresa un **Número de Cuenta** único y el nombre del **Titular**.
4. Dependiendo del tipo de cuenta, el sistema te pedirá un **Saldo Inicial** o un **Límite de Crédito**.
5. Haz clic en **Guardar**. La cuenta aparecerá en la lista lateral.

## 3. Registrar Ingresos y Gastos
Para agregar una nueva transacción a tu historial:
1. Haz clic en el botón **"Nuevo movimiento"** en la esquina inferior izquierda.
2. Escribe o selecciona la **Cuenta** afectada. El buscador filtrará automáticamente por titular o número de cuenta.
3. Selecciona si es un **Gasto** (salida de dinero) o un **Ingreso** (entrada de dinero).
4. Ingresa el **Monto** (debe ser un número mayor a 0).
5. Escribe un **Concepto** breve (ej. "Pago de luz", "Quincena").
6. Selecciona la **Categoría** correspondiente.
7. Haz clic en **Guardar**. El saldo de la cuenta se actualizará automáticamente.

## 4. Modificar o Eliminar Cuentas
Si cometiste un error o necesitas actualizar información:
1. Localiza la cuenta en el panel izquierdo.
2. Haz **Clic Derecho** sobre la cuenta para abrir el menú contextual.
3. **Modificar:** Permite cambiar el nombre del titular. (El número y tipo de cuenta no se pueden cambiar por seguridad).
4. **Eliminar:** Borrará la cuenta y todo su historial de transacciones de forma permanente. El sistema pedirá confirmación antes de proceder.

## 5. Explorador Visual (Dashboard)
La aplicación cuenta con un sistema de pestañas en la parte central superior.
* **Pestaña "Cuentas e Historial":** Muestra la lista detallada de todas las transacciones de la cuenta seleccionada.
* **Pestaña "Dashboard Visual":** Muestra dos gráficas generadas en tiempo real:
  * *Gráfica de Pastel:* Ilustra cómo está distribuido tu dinero total entre todas tus cuentas con saldo positivo.
  * *Gráfica de Barras:* Compara el volumen total de Ingresos vs. Gastos de la cuenta que tengas seleccionada en el panel izquierdo.