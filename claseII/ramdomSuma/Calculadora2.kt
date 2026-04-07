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


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    PantallaSuma(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

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

@Composable
fun PantallaSuma(modifier: Modifier = Modifier) {
    var numero1Texto by remember { mutableStateOf("") }
    var numero2Texto by remember { mutableStateOf("") }
    var resultado by remember { mutableStateOf(0) }
    var ultimaOperacion by remember { mutableStateOf("Ninguna") }
    var mensajeRandom by remember { mutableStateOf("") }

    //Idea de recomposición de compose

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),

        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "App de operaciones",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = numero1Texto,
            onValueChange = { nuevoTexto -> numero1Texto = nuevoTexto },
            label = { Text("Primer número") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = numero2Texto,
            onValueChange = { numero2Texto = it },
            label = { Text("Segundo número") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {

            if(numero1Texto.isBlank() || numero2Texto.isBlank()) {
                mensajeRandom = "Ingrese ambos números"
            } else {

                val n1 = numero1Texto.toIntOrNull() ?: 0
                val n2 = numero2Texto.toIntOrNull() ?: 0
                resultado = sumar(n1, n2)
                ultimaOperacion = "Suma"
                mensajeRandom = ""
            }

        }) {
            Text("Sumar")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = {
            if(numero1Texto.isBlank() || numero2Texto.isBlank()) {
                mensajeRandom = "Ingrese ambos números"
            } else {

                val n1 = numero1Texto.toIntOrNull() ?: 0
                val n2 = numero2Texto.toIntOrNull() ?: 0
                resultado = restar(n1, n2)
                ultimaOperacion = "Resta"
                mensajeRandom = ""
            }

        }) {
            Text("Restar")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = {
            if(numero1Texto.isBlank() || numero2Texto.isBlank()) {
                mensajeRandom = "Ingrese ambos números"
            } else {

                val n1 = numero1Texto.toIntOrNull() ?: 0
                val n2 = numero2Texto.toIntOrNull() ?: 0
                resultado = multiplicar(n1, n2)
                ultimaOperacion = "Multiplicar"
                mensajeRandom = ""
            }

        }) {
            Text("Multiplicar")
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(onClick = {
            val random = generarNumeroRandom()
            resultado += random
            mensajeRandom = "Se sumó el número aleatorio: $random"
        }) {
            Text("Sumar número random")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = {
            numero1Texto = ""
            numero2Texto = ""
            resultado = 0
            ultimaOperacion = "Ninguna"
            mensajeRandom = ""
        }) {
            Text("Limipiar")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Resultado: $resultado",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = mensajeRandom,
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Última operación: $ultimaOperacion",
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(8.dp))
    }
}
