package ar.edu.sensoresintro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val preferences = LastMeasurementPreferences(applicationContext)

        setContent {
            MaterialTheme {
                var currentMeasurement by remember {
                    mutableStateOf<AccelerometerMeasurement?>(null)
                }

                var savedMeasurement by remember {
                    mutableStateOf(preferences.getLastMeasurement())
                }

                var sensorAvailable by remember {
                    mutableStateOf(true)
                }

                val accelerometerReader = remember {
                    AccelerometerReader(applicationContext)
                }

                DisposableEffect(Unit) {
                    sensorAvailable = accelerometerReader.startReading { measurement ->
                        currentMeasurement = measurement
                    }

                    onDispose {
                        accelerometerReader.stopReading()
                    }
                }

                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                Text("Introducción a sensores")
                            }
                        )
                    }
                ) { paddingValues ->

                    SensorScreen(
                        paddingValues = paddingValues,
                        sensorAvailable = sensorAvailable,
                        currentMeasurement = currentMeasurement,
                        savedMeasurement = savedMeasurement,
                        onSaveMeasurement = {
                            currentMeasurement?.let { measurement ->
                                preferences.saveLastMeasurement(measurement)
                                savedMeasurement = measurement
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun SensorScreen(
    paddingValues: PaddingValues,
    sensorAvailable: Boolean,
    currentMeasurement: AccelerometerMeasurement?,
    savedMeasurement: AccelerometerMeasurement?,
    onSaveMeasurement: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Acelerómetro",
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            text = "Esta app lee el acelerómetro del dispositivo y permite guardar la última medición seleccionada usando SharedPreferences.",
            style = MaterialTheme.typography.bodyMedium
        )

        if (!sensorAvailable) {
            Text(
                text = "Este dispositivo no tiene acelerómetro disponible.",
                color = MaterialTheme.colorScheme.error
            )
        }

        MeasurementCard(
            title = "Medición actual",
            measurement = currentMeasurement
        )

        Button(
            onClick = onSaveMeasurement,
            enabled = currentMeasurement != null && sensorAvailable,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar medición actual")
        }

        MeasurementCard(
            title = "Última medición guardada",
            measurement = savedMeasurement
        )

        Text(
            text = "Nota: el acelerómetro incluye la aceleración de la gravedad. Por eso, aunque el teléfono esté quieto, normalmente se observa un valor cercano a 9.8 m/s² en alguno de los ejes.",
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
fun MeasurementCard(
    title: String,
    measurement: AccelerometerMeasurement?
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )

            if (measurement == null) {
                Text("No hay medición disponible.")
            } else {
                Text("X: ${formatNumber(measurement.x)} m/s²")
                Text("Y: ${formatNumber(measurement.y)} m/s²")
                Text("Z: ${formatNumber(measurement.z)} m/s²")
                Text("Hora: ${formatTime(measurement.timeStampMillis)}")
            }
        }
    }
}

fun formatNumber(value: Float): String {
    return String.format(Locale.getDefault(), "%.2f", value)
}

fun formatTime(timestampMillis: Long): String {
    val formatter = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    return formatter.format(Date(timestampMillis))
}
