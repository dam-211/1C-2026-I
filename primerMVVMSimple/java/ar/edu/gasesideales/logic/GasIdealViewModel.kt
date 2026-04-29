package ar.edu.gasesideales.logic

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import ar.edu.gasesideales.data.HistorialRepository
import ar.edu.gasesideales.formato

data class IngresoUiState(
    val presionTexto: String = "",
    val volumenTexto: String = "",
    val mensaje: String = ""
)

data class ResultadoUiState(
    val presion: Double = 0.0,
    val volumen: Double = 0.0,
    val temperatura: Double = 0.0,
    val historial: List<String> = emptyList()
)

class GasIdealViewModel(application: Application) : AndroidViewModel(application) {

    private val historialRepository = HistorialRepository(application)

    private val n = 1.0
    private val r = 0.082

    var ingresoUiState by mutableStateOf(IngresoUiState())
        private set

    var resultadoUiState by mutableStateOf(ResultadoUiState())
        private set

    fun onPresionChange(texto: String) {
        ingresoUiState = ingresoUiState.copy(
            presionTexto = texto,
            mensaje = ""
        )
    }

    fun onVolumenChange(texto: String) {
        ingresoUiState = ingresoUiState.copy(
            volumenTexto = texto,
            mensaje = ""
        )
    }

    fun calcularTemperatura(onResultadoListo: (Double, Double, Double) -> Unit) {
        val presion = parsearNumero(ingresoUiState.presionTexto)
        val volumen = parsearNumero(ingresoUiState.volumenTexto)

        when {
            presion == null || volumen == null -> {
                ingresoUiState = ingresoUiState.copy(
                    mensaje = "Ingresá datos válidos"
                )
            }

            presion <= 0 || volumen <= 0 -> {
                ingresoUiState = ingresoUiState.copy(
                    mensaje = "La presión y el volumen deben ser mayores que cero"
                )
            }

            else -> {
                ingresoUiState = ingresoUiState.copy(mensaje = "")

                val temperatura = (presion * volumen) / (n * r)

                onResultadoListo(presion, volumen, temperatura)
            }
        }
    }

    fun cargarResultado(presion: Double, volumen: Double, temperatura: Double) {
        val registroActual =
            "P=${presion.formato(2)} | V=${volumen.formato(2)} | T=${temperatura.formato(2)} K"

        historialRepository.agregarRegistro(registroActual)

        resultadoUiState = ResultadoUiState(
            presion = presion,
            volumen = volumen,
            temperatura = temperatura,
            historial = historialRepository.obtenerHistorial()
        )
    }

    private fun parsearNumero(texto: String): Double? {
        return texto.replace(',', '.').toDoubleOrNull()
    }
}