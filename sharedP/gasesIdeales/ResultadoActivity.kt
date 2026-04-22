package ar.edu.gasesideales

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.activity.compose.setContent
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import java.util.Locale


class ResultadoActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val presion = intent.getDoubleExtra(EXTRA_PRESION, 0.0)
        val volumen = intent.getDoubleExtra(EXTRA_VOLUMEN, 0.0)
        val temperatura = intent.getDoubleExtra(EXTRA_TEMPERATURA, 0.0)

        Log.d("resultado", "Presion: $presion y $volumen y $temperatura")

        val registroActual ="P=${presion} | V=${volumen} | T=${temperatura}"

        val historialRepository = HistorialRepository(this)

        historialRepository.agregarRegistro(registroActual)

        val historial = historialRepository.obtenerHistorial()

        setContent{
            MaterialTheme {
                PantallaResultado(
                    presion = presion,
                    volumen = volumen,
                    temperatura = temperatura,
                    historial = historial
                )
            }
        }

        }

    companion object {
        const val EXTRA_PRESION = "extra_presion"
        const val EXTRA_VOLUMEN = "extra_volumen"
        const val EXTRA_TEMPERATURA = "extra_temperatura"
        }
    }

@SuppressLint("ContextCastToActivity")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaResultado(
    presion: Double,
    volumen: Double,
    temperatura: Double,
    historial: List<String>
) {
    val activity = LocalContext.current as? Activity

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Resultado") }
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
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Cálculo actual",
                        style = MaterialTheme.typography.titleLarge
                    )

                    Text("Presión: ${presion.formato(2)}")
                    Text("Volumen: ${volumen.formato(2)}")
                    Text("Temperatura: ${temperatura.formato(2)} K")
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Últimos 5 cálculos",
                        style = MaterialTheme.typography.titleLarge
                    )

                    // TODO 6:
                    // Mostrar los elementos del historial
                    if (historial.isEmpty()) {
                        Text("No hay cálculos guardados")
                    } else {
                        historial.forEachIndexed { index, item ->
                            Text("${index + 1}. $item")
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { activity?.finish() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Volver")
            }
        }
    }
}

fun Double.formato(decimales: Int): String {
    return "%.${decimales}f".format(Locale.US, this)
}
