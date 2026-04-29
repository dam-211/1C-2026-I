# Apunte: aplicación **Ley de los Gases Ideales** con **Jetpack Compose** y una separación simple entre **vista, lógica y datos**

## 1. ¿Qué hace esta aplicación?

La aplicación permite ingresar dos valores:

- **Presión**
- **Volumen**

A partir de esos datos, calcula la **temperatura** usando la ecuación de la ley de los gases ideales:

\[
PV = nRT
\]

En este ejemplo se asume para simplificar que:

- \(n = 1\) mol
- \(R = 0.082\) L·atm/mol·K

Entonces la temperatura se calcula como:

\[
T = \frac{PV}{nR}
\]

Además, la app guarda un pequeño **historial de los últimos 5 cálculos** usando `SharedPreferences`.

---

## 2. ¿Por qué esta app?

Porque combina varias ideas importantes de Android moderno sin volverse demasiado compleja:

- entrada de datos por pantalla
- validación de datos
- cálculo sencillo
- navegación entre dos pantallas
- persistencia simple
- separación entre **UI**, **lógica** y **datos**

Es decir: no es una app "de juguete" completamente trivial, pero tampoco requiere base de datos, red, corrutinas o arquitectura avanzada.

---

## 3. Estructura elegida del proyecto

La aplicación fue reorganizada/refactorizada con esta estructura:

```text
ar.edu.gasesideales
│
├── data
│   └── HistorialRepository.kt
│
├── logic
│   └── GasIdealViewModel.kt
│
├── ui
│   └── GasIdealScreens.kt
│
├── MainActivity.kt
├── ResultadoActivity.kt
└── Utils.kt
```

Esto busca:

- **ordenar** el proyecto
- **separar responsabilidades**
- **no fragmentar demasiado** el código

Si se crean demasiados archivos, demasiados modelos y demasiadas capas, la arquitectura empieza a tapar el bosque.

---

## 4. Idea central: separar vista, lógica y datos

### 4.1. Vista

La **vista** es lo que el usuario ve en pantalla.

En este proyecto, la vista está en:

- `ui/GasIdealScreens.kt`

Allí viven los composables como:

- `PantallaIngreso(...)`
- `PantallaResultado(...)`

La vista **no debería hacer cálculos importantes**, ni guardar datos, ni decidir reglas de negocio. Su tarea principal es:

- mostrar información
- capturar acciones del usuario
- invocar callbacks o funciones del ViewModel

---

### 4.2. Lógica

La **lógica** es la parte que decide qué hacer con los datos.

En este proyecto está en:

- `logic/GasIdealViewModel.kt`

Allí se resuelve:

- qué pasa cuando cambia un texto
- cómo validar presión y volumen
- cómo calcular la temperatura
- qué mensaje mostrar si algo está mal
- cómo preparar los datos de resultado

El ViewModel es, en términos prácticos, una especie de **coordinador** entre la interfaz y los datos.

---

### 4.3. Datos

La capa de **datos** se encarga de guardar o recuperar información.

En este proyecto está en:

- `data/HistorialRepository.kt`

Su responsabilidad es:

- guardar un cálculo en el historial
- recuperar el historial guardado

No le importa cómo se dibuja la pantalla. No le importa cómo se valida un campo. Solo trabaja con el almacenamiento.

---

## 5. Un puente mental

Hay varias ideas que ya conocen, aunque con otros nombres.

### 5.1. Composable ≈ función que construye interfaz

En Python, una función recibe parámetros y devuelve un resultado.

En Compose, una función `@Composable` recibe un estado y “describe” qué interfaz debe mostrarse.

Ejemplo conceptual:

```kotlin
@Composable
fun PantallaIngreso(...) {
    // describe qué se ve en pantalla
}
```

La idea importante es que **la UI también se construye con funciones**.

Pueden pensar en términos de:

- funciones
- parámetros
- estado
- flujo de datos

---

### 5.2. Estado ≈ variables que representan la situación actual de la pantalla

En Python ya existe la idea de guardar valores en variables:

```python
presion = ""
volumen = ""
mensaje = ""
```

En Compose ocurre algo parecido, pero el estado está conectado con la UI. Cuando el estado cambia, la interfaz se vuelve a dibujar.

En este proyecto aparece algo así:

```kotlin
data class IngresoUiState(
    val presionTexto: String = "",
    val volumenTexto: String = "",
    val mensaje: String = ""
)
```

Este objeto representa el **estado actual de la pantalla de ingreso**.

---

### 5.3. ViewModel ≈ objeto que concentra comportamiento y estado

Si en Python se trabajo con objetos, puede verse al `ViewModel` como un objeto que:

- guarda el estado de la pantalla
- ofrece métodos para modificarlo
- encapsula reglas de validación y cálculo

Por ejemplo:

- `onPresionChange(...)`
- `onVolumenChange(...)`
- `calcularTemperatura(...)`
- `cargarResultado(...)`

Es decir, el `ViewModel` no es solo “datos”; también es **comportamiento**.

---

### 5.4. Repository ≈ clase de acceso a datos

En Python podría pensarse como una clase que maneja un archivo, una lista persistente o un pequeño almacenamiento.

En este proyecto:

- `HistorialRepository` guarda y recupera el historial

La palabra **Repository** ayuda a comunicar una idea simple: “esta clase sabe cómo acceder a los datos”.

---

## 6. Flujo general de la aplicación

El flujo de la app puede explicarse paso a paso.

### Paso 1: el usuario abre la app

Android ejecuta `MainActivity`.

`MainActivity` crea la pantalla principal con `setContent { ... }` y muestra `PantallaIngreso(...)`.

---

### Paso 2: el usuario escribe presión y volumen

Cada vez que cambia un campo, la pantalla llama funciones del ViewModel:

- `onPresionChange(...)`
- `onVolumenChange(...)`

Estas funciones actualizan el `IngresoUiState`.

---

### Paso 3: el usuario toca **Calcular**

La UI no calcula por sí sola. En cambio, llama al ViewModel.

El ViewModel:

1. intenta convertir texto a número
2. valida que los valores existan
3. valida que sean mayores que cero
4. si todo es correcto, calcula la temperatura
5. devuelve los datos listos para navegar a la otra pantalla

---

### Paso 4: se abre la pantalla de resultado

`MainActivity` crea un `Intent` y envía:

- presión
- volumen
- temperatura

`ResultadoActivity` recibe esos valores.

---

### Paso 5: se guarda el cálculo en el historial

`ResultadoActivity` le pide al ViewModel que cargue el resultado.

El ViewModel usa `HistorialRepository` para:

- guardar el cálculo actual
- recuperar la lista actualizada

---

### Paso 6: se muestra el resultado final

`PantallaResultado(...)` recibe un `ResultadoUiState` y simplemente lo dibuja en pantalla.

---

## 7. Análisis por archivo

---

## 7.1. `data/HistorialRepository.kt`

### Responsabilidad

Se encarga del historial de cálculos usando `SharedPreferences`.

### Idea importante

Esta clase **no dibuja pantallas** y **no calcula temperatura**.

Solo hace dos cosas:

- `agregarRegistro(registro: String)`
- `obtenerHistorial()`

### ¿Qué es `SharedPreferences`?

Es un mecanismo simple de Android para guardar pares clave-valor.

Por ejemplo:

- una clave: `"clave_historial"`
- un valor: un `String` con todos los registros unidos por un separador

### ¿Cómo guarda varios elementos si `SharedPreferences` trabaja bien con datos simples?

En este ejemplo, varios registros se convierten en un solo texto usando un separador:

```kotlin
private const val SEPARADOR = ";"
```

Entonces el historial se guarda como un único `String`, y luego se recupera dividiéndolo con `split(...)`.

### Limitación

No es la solución ideal para datos complejos. Es una estrategia simple, adecuada para una app chica y con fines didácticos.

---

## 7.2. `logic/GasIdealViewModel.kt`

Este archivo es el corazón lógico de la aplicación.

### a. `IngresoUiState`

Representa el estado de la pantalla de ingreso:

- texto de presión
- texto de volumen
- mensaje de error

```kotlin
data class IngresoUiState(
    val presionTexto: String = "",
    val volumenTexto: String = "",
    val mensaje: String = ""
)
```

### b. `ResultadoUiState`

Representa lo que necesita la pantalla de resultado:

- presión
- volumen
- temperatura
- historial

```kotlin
data class ResultadoUiState(
    val presion: Double = 0.0,
    val volumen: Double = 0.0,
    val temperatura: Double = 0.0,
    val historial: List<String> = emptyList()
)
```

### c. ¿Por qué usar `UiState`?

Porque ayuda a pensar la pantalla como un **estado completo**, en lugar de tener muchas variables sueltas dispersas.

Esto tiene un valor conceptual muy grande:

- hace el código más legible
- organiza mejor la información
- facilita entender qué necesita la UI

Esto puede verse como agrupar datos relacionados en una estructura clara, en vez de tener muchas variables globales o muchos atributos sin organización.

### d. Funciones del ViewModel

#### `onPresionChange(texto: String)`
Actualiza el texto de presión y limpia el mensaje.

#### `onVolumenChange(texto: String)`
Actualiza el texto de volumen y limpia el mensaje.

#### `calcularTemperatura(...)`
Hace la parte importante:

- parsea texto a `Double`
- valida valores
- calcula la temperatura
- informa a la Activity que ya puede navegar

#### `cargarResultado(...)`
Construye el registro del historial, lo guarda y actualiza el `ResultadoUiState`.

### e. ¿Por qué el cálculo no está en la UI?

Porque la UI debería concentrarse en mostrar cosas.

Si metemos validación, parseo, cálculo y persistencia dentro de los composables, la pantalla empieza a mezclar demasiadas responsabilidades.

Eso produce:

- código más difícil de leer
- código más difícil de probar
- más acoplamiento entre interfaz y lógica

---

## 7.3. `ui/GasIdealScreens.kt`

Aquí vive la **interfaz visual** de la aplicación.

### `PantallaIngreso(...)`

Recibe:

- el estado actual (`IngresoUiState`)
- callbacks para cambios de texto
- callback para el botón calcular

La idea didáctica fuerte es esta:

> la pantalla no decide qué hacer con el negocio; solo emite eventos

Por ejemplo:

- si el usuario escribe, la pantalla llama `onPresionChange(...)`
- si toca calcular, la pantalla llama `onCalcularClick()`

La pantalla no sabe cómo se valida ni cómo se calcula la temperatura.

### `PantallaResultado(...)`

Recibe:

- `ResultadoUiState`
- callback para volver

Y dibuja:

- el cálculo actual
- el historial
- el botón para regresar

### ¿Qué ventaja tiene este diseño?

Que las pantallas son más limpias y fáciles de entender:

- reciben datos
- muestran datos
- invocan acciones


---

## 7.4. `MainActivity.kt`

`MainActivity` cumple el papel de pantalla de entrada de la aplicación.

### ¿Qué hace?

- obtiene una instancia de `GasIdealViewModel`
- muestra `PantallaIngreso(...)`
- conecta la UI con el ViewModel
- cuando el ViewModel indica que hay un resultado listo, crea el `Intent` para abrir `ResultadoActivity`

### Idea pedagógica

La Activity no debería contener toda la lógica de negocio. En este refactor queda más como una **coordinadora del ciclo de vida y la navegación**.

---

## 7.5. `ResultadoActivity.kt`

### ¿Qué hace?

- recibe los extras enviados por el `Intent`
- llama al ViewModel para cargar el resultado
- muestra `PantallaResultado(...)`

### Observación importante

Acá hay una decisión didáctica deliberada: se mantiene el uso de dos Activities porque eso ayuda a que entiendan:

- paso de datos entre pantallas
- `Intent`
- extras
- ciclo de vida básico

Más adelante podría migrarse a `Navigation Compose`, pero para una primera etapa esta solución es muy clara. Pero por ahora, no quiero seguir mezclando :)

---

## 7.6. `Utils.kt`

Agregué una unidad simple:

```kotlin
fun Double.formato(decimales: Int): String
```

Esta función permite mostrar números con una cantidad fija de decimales.

Por ejemplo:

- `12.34567` → `12.35`

Es una buena práctica separar utilidades pequeñas cuando no pertenecen a una pantalla ni a una clase de datos.

OJO con volvera una GodClass!