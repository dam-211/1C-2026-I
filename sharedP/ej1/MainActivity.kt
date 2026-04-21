package ar.edu.material32

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext

import ar.edu.material32.ui.theme.Material32Theme

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContent {
            Material32Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) {
                   PantallaPrincipal()
                }
            }
        }
    }
}

class PreferenciasUsuario(context: Context) {

    private val prefs = context.getSharedPreferences("usuario_prefs", Context.MODE_PRIVATE)

    fun guardarNombre(nombre: String) {

        prefs.edit().putString("nombre", nombre).apply()
    }

    fun obtenerNombre(): String {
        return prefs.getString("nombre", "") ?: ""
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaPrincipal() {
    val contexto = LocalContext.current

    val preferencias = remember(contexto) { PreferenciasUsuario(contexto) }

    var nombre by rememberSaveable { mutableStateOf(preferencias.obtenerNombre()) }
    var mensaje by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi app de Material3") }
            )
        }
    ) { paddingInterno ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingInterno)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Ejemplo de Material 3",
                style = MaterialTheme.typography.headlineMedium
            )

            Text(
                text = "Bienvenido a Jetpack Compose",
                style = MaterialTheme.typography.headlineSmall
            )

            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Ingresa tu nombre",
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = nombre,
                        onValueChange = { nombre = it },
                        label = { Text("Nombre") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = {
                            mensaje = if (nombre.isBlank()) {
                                "Por favor ingresa un nombre"
                            } else {
                                Log.d("shared", nombre)
                                preferencias.guardarNombre(nombre)
                                "Hola, $nombre"
                            }
                        },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text("Saludar")
                    }
                }
            }

            if(mensaje.isNotBlank()) {
                Text(
                    text = mensaje,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }

        }
    }
}
