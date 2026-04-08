import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.KeyboardType
import kotlin.random.Random


//Esta es la clase principal de la app de Android que hereda de componentActivity. Trabaja bien con Jetpack Compose-
class MainActivity : ComponentActivity() {

    //Sobreescribe el metodo onCrete, es uno de los primeros métodos que ejecuta android como punto de partida de nuestra app
    //savedInstanceStatate puede almacenar el estado de de la actividad  en caso de recreación.
    override fun onCreate(savedInstanceState: Bundle?) {

        //LLamar a la implementación de la clase padre(ComponentActivity
        super.onCreate(savedInstanceState)

        //aprovecha toda la pantalla de la app
        enableEdgeToEdge()

        //Indica que la interfaz de esta actividad se va a contruir con Compose. Todo lo que este adentro, define la UI
        setContent {
            //Aplica el tema MaterialDesign: colores, típografías, estilos, ect
            MaterialTheme {
                //Estructura de pantalla tipica de Material
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    //LLamar al composable contenedor de la pantalla (SEPARAMOS LOGICA DEL ESTADO DE LA VISTA!!!
                    PantallaOperacionesContainer(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

/*
* Lógica
 */

//Declaro un Data class que se llama EstadoOperacion. Agrupa el resultado de una operación, el nombre, resultado, ect.
//Util en el caso de devolver varios valores juntos.
data class EstadoOperacion(
    val resultado: Int = 0,
    val ultimaOperacion: String = "Ninguna",
    val mensaje: String = ""
)

fun sumar(a: Int, b: Int): Int {
    return a + b
}

fun restar(a: Int, b: Int): Int = a-b //Forma diferetne y más directa de return!

fun multiplicar(a: Int, b: Int): Int {
    return a*b
}

fun generarNumeroRandom(): Int {
    return Random.nextInt(1, 11) // del 1 al 10
}

fun validarNumeros(texto1: String, texto2: String): Pair<Int, Int>? {

    if(texto1.isBlank() || texto2.isBlank()) {
        return null
    }
    val n1 = texto1.toIntOrNull()
    val n2 = texto2.toIntOrNull()

    return if (n1 != null && n2 != null) {
        Pair(n1, n2)
    }else{
        null
    }
}

fun realizarSuma(texto1: String, texto2: String): EstadoOperacion {

    val numeros = validarNumeros(texto1, texto2)

    return if (numeros == null) {
        EstadoOperacion(
            mensaje = "Ingrese dos números válidos"
        )
    } else {

        //Desesctructurar el pair --> guarda el primer valor del n1, n2
        val (n1, n2) = numeros

        EstadoOperacion(
            resultado = sumar(n1, n2),
            ultimaOperacion = "Suma",
            mensaje = "Operacion OK"
        )
    }
}

fun realizarResta(texto1: String, texto2: String): EstadoOperacion {

    val numeros = validarNumeros(texto1, texto2)

    return if (numeros == null) {
        EstadoOperacion(
            mensaje = "Ingrese dos números válidos"
        )
    } else {

        val (n1, n2) = numeros

        EstadoOperacion(
            resultado = restar(n1, n2),
            ultimaOperacion = "Resta",
            mensaje = ""
        )
    }
}

fun realizarMultiplicacion(texto1: String, texto2: String): EstadoOperacion {

    val numeros = validarNumeros(texto1, texto2)

    return if (numeros == null) {
        EstadoOperacion(
            mensaje = "Ingrese dos números válidos"
        )
    } else {

        val (n1, n2) = numeros

        EstadoOperacion(
            resultado = multiplicar(n1, n2),
            ultimaOperacion = "Multiplicacion",
            mensaje = ""
        )
    }
}

fun sumarRandomAlResultado(resultadoActual: Int): EstadoOperacion {

    val random = generarNumeroRandom()
    return EstadoOperacion(
        resultado = resultadoActual + random,
        ultimaOperacion = "Sumó el RND $random",
        mensaje = "Le sume al RND el numero"
    )
}

//Vuelvo a declarar un Composable
@Composable
//Defino la función composable contendor --> ESTE CONECTA LA LOGICA ANTERIOR CON la VISTA
fun PantallaOperacionesContainer(modifier: Modifier = Modifier) {

    //DEclaro estados observables para los campos de texto
    var numero1Texto by remember { mutableStateOf("") }
    //Remember hace que el valor sobreviva a la recomposiciónñ
    //mutableStateOf crea un estado inicial vacio
    //by permite usarlo como variable normal
    var numero2Texto by remember { mutableStateOf("") }
    var resultado by remember { mutableStateOf(0) }
    var ultimaOperacion by remember { mutableStateOf("Ninguna") }
    var mensaje by remember { mutableStateOf("") }


    PantallaOperacionesView(

        numero1Texto = numero1Texto,
        numero2Texto = numero2Texto,
        resultado = resultado,
        ultimaOperacion = ultimaOperacion,
        mensaje = mensaje,

        //Definimos que haccer cuando cambie el primer campo --> actualizar el estado con el nuevo texto
        onNumero1Change = { numero1Texto = it },
        onNumero2Change = { numero2Texto = it },

        //Acciones de los buttons
        onSumarClick = {

            //llama a la logica de la sumar
            val estadoOperacion = realizarSuma(numero1Texto, numero2Texto)
            resultado = estadoOperacion.resultado
            ultimaOperacion = estadoOperacion.ultimaOperacion
            mensaje = estadoOperacion.mensaje
        },

        onRestarClick = {
            val estado = realizarResta(numero1Texto, numero2Texto)
            resultado = estado.resultado
            ultimaOperacion = estado.ultimaOperacion
            mensaje = estado.mensaje
        },

        onMultiplicarClick = {
            val estado = realizarMultiplicacion(numero1Texto, numero2Texto)
            resultado = estado.resultado
            ultimaOperacion = estado.ultimaOperacion
            mensaje = estado.mensaje
        },

        onRandomClick = {
            val estado = sumarRandomAlResultado(resultado)
            resultado = estado.resultado
            ultimaOperacion = estado.ultimaOperacion
            mensaje = estado.mensaje
        },

        onLimpiarClick = {
            numero2Texto = ""
            numero1Texto = ""
            resultado = 0
            ultimaOperacion = "Ninguna"
            mensaje = ""
        },

        modifier = modifier
    )

}

//Declara la vista principal, DIBUJA LA UI
@Composable
fun PantallaOperacionesView(
    numero1Texto: String,
    numero2Texto: String,
    resultado: Int,
    ultimaOperacion: String,
    mensaje: String,

    //Parámetro que recive una función
    onNumero1Change: (String) -> Unit, //es una función que recibe un Sring y no devuelve nada
    //onNumero1Change --> Qué hago cuando el numero cambia???
    onNumero2Change: (String) -> Unit,
    onSumarClick: () -> Unit,
    //EN kotlin Unit equivale a "no devulver un valor útil", es análogo a un VOID
    onRestarClick: () -> Unit,
    onMultiplicarClick: () -> Unit,
    onRandomClick: () -> Unit,
    onLimpiarClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "App de operaciones",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = numero1Texto,
            onValueChange = onNumero1Change,
            label = { Text("Primer número") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = numero2Texto,
            onValueChange = onNumero2Change,
            label = { Text("Segundo número") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onSumarClick) {
            Text("Sumar")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = onRestarClick) {
            Text("Restar")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = onMultiplicarClick) {
            Text("Multiplicar")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = onRandomClick) {
            Text("Sumar número random")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = onLimpiarClick) {
            Text("Limpiar")
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Resultado: $resultado",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Última operación: $ultimaOperacion",
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = mensaje,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
