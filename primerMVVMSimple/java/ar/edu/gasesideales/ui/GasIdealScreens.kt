package ar.edu.gasesideales.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import ar.edu.gasesideales.formato
import ar.edu.gasesideales.logic.IngresoUiState
import ar.edu.gasesideales.logic.ResultadoUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaIngreso(
    uiState: IngresoUiState,
    onPresionChange: (String) -> Unit,
    onVolumenChange: (String) -> Unit,
    onCalcularClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Ley de los gases ideales") }
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
                text = "Ingreso de parámetros",
                style = MaterialTheme.typography.headlineMedium
            )

            Text(
                text = "Usaremos PV = nRT",
                style = MaterialTheme.typography.bodyLarge
            )

            Text(
                text = "Supuestos: n = 1 mol, R = 0.082 L·atm/mol·K",
                style = MaterialTheme.typography.bodyMedium
            )

            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = uiState.presionTexto,
                        onValueChange = onPresionChange,
                        label = { Text("Presión (atm)") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                    )

                    OutlinedTextField(
                        value = uiState.volumenTexto,
                        onValueChange = onVolumenChange,
                        label = { Text("Volumen (litros)") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                    )

                    Button(
                        onClick = onCalcularClick,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Calcular")
                    }
                }
            }

            if (uiState.mensaje.isNotBlank()) {
                Text(
                    text = uiState.mensaje,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaResultado(
    uiState: ResultadoUiState,
    onVolverClick: () -> Unit
) {
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

                    Text("Presión: ${uiState.presion.formato(2)} atm")
                    Text("Volumen: ${uiState.volumen.formato(2)} L")
                    Text("Temperatura: ${uiState.temperatura.formato(2)} K")
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

                    if (uiState.historial.isEmpty()) {
                        Text("No hay cálculos guardados")
                    } else {
                        uiState.historial.forEachIndexed { index, item ->
                            Text("${index + 1}. $item")
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = onVolverClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Volver")
            }
        }
    }
}
