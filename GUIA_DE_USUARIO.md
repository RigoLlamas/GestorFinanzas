# Guía de Usuario - Gestor de Finanzas Personales

Bienvenido al Gestor de Finanzas Personales. Esta guía explica cómo utilizar las funciones principales del sistema para llevar un control preciso de tu dinero.

### 1. Configuración Inicial

* Al ejecutar la aplicación por primera vez, el sistema te pedirá elegir tu **Tema visual** de preferencia (Claro u Oscuro).
* A continuación, solicitará que elijas una carpeta en tu computadora. Selecciona un directorio seguro (como tu carpeta de Documentos).
* En este lugar se creará el archivo `datos_financieros.dat`, el cual guardará toda tu información automáticamente al cerrar la ventana. No borres este archivo, o perderás tu historial.

### 2. Creación de Cuentas

Antes de registrar movimientos, necesitas crear al menos una cuenta.

* En el panel izquierdo, haz clic en el botón **"Nueva cuenta"**.
* Selecciona el **Banco** y el **Tipo de Cuenta** (Ahorro, Débito o Crédito).
* Ingresa un **Número de Cuenta** único y el nombre del **Titular**.
* Dependiendo del tipo de cuenta, el sistema te pedirá un **Saldo Inicial** o un **Límite de Crédito**.
* Haz clic en **Guardar**. La cuenta aparecerá en la lista lateral.

### 3. Registrar Ingresos y Gastos

Para agregar una nueva transacción a tu historial:

* Haz clic en el botón **"Nuevo movimiento"** en la esquina inferior izquierda.
* Escribe o selecciona la **Cuenta** afectada. El buscador filtrará automáticamente por titular o número de cuenta.
* Selecciona si es un **Gasto** (salida de dinero) o un **Ingreso** (entrada de dinero).
* Ingresa el **Monto** (debe ser un número mayor a 0).
* Escribe un **Concepto** breve (ej. "Pago de luz", "Quincena").
* Selecciona la **Categoría** correspondiente.
* Haz clic en **Guardar**. El saldo de la cuenta se actualizará automáticamente.

### 4. Modificar o Eliminar Cuentas

Si cometiste un error o necesitas actualizar información:

* Localiza la cuenta en el panel izquierdo.
* Haz **Clic Derecho** sobre la cuenta para abrir el menú contextual.
* **Modificar:** Permite cambiar el nombre del titular. (El número y tipo de cuenta no se pueden cambiar por seguridad).
* **Eliminar:** Borrará la cuenta y todo su historial de transacciones de forma permanente. El sistema pedirá confirmación antes de proceder.

### 5. Explorador Visual (Dashboard)

La aplicación cuenta con un sistema de pestañas en la parte central superior.

* **Pestaña "Cuentas e Historial":** Muestra la lista detallada de todas las transacciones de la cuenta seleccionada.
* **Pestaña "Dashboard Visual":** Muestra dos gráficas generadas en tiempo real:
* *Gráfica de Pastel:* Ilustra cómo está distribuido tu dinero total entre todas tus cuentas con saldo positivo.
* *Gráfica de Barras:* Compara el volumen total de Ingresos vs. Gastos de la cuenta que tengas seleccionada en el panel izquierdo.



### 6. Configuración General

En cualquier momento puedes ajustar las preferencias del sistema haciendo clic en el botón **"Configuración"** (ubicado en el panel superior):

* **Tema visual:** Alterna entre el modo Claro y Oscuro. El cambio se aplicará de inmediato en toda la interfaz.
* **Ubicación de datos:** Permite visualizar y modificar la carpeta donde se guarda tu archivo de registro.
> **Nota importante:** El cambio de ruta de guardado surtirá efecto la próxima vez que inicies la aplicación. Si deseas conservar tu historial actual en la nueva ruta, deberás mover manualmente tu archivo `datos_financieros.dat` desde la carpeta anterior hacia la nueva ubicación.
