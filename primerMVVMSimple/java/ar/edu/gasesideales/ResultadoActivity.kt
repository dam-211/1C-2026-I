package ar.edu.gasesideales

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import ar.edu.gasesideales.logic.GasIdealViewModel
import ar.edu.gasesideales.ui.PantallaResultado

class ResultadoActivity : ComponentActivity() {

    private val viewModel: GasIdealViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val presion = intent.getDoubleExtra(EXTRA_PRESION, 0.0)
        val volumen = intent.getDoubleExtra(EXTRA_VOLUMEN, 0.0)
        val temperatura = intent.getDoubleExtra(EXTRA_TEMPERATURA, 0.0)

        viewModel.cargarResultado(
            presion = presion,
            volumen = volumen,
            temperatura = temperatura
        )

        setContent {
            MaterialTheme {
                PantallaResultado(
                    uiState = viewModel.resultadoUiState,
                    onVolverClick = { finish() }
                )
            }
        }
    }

    companion object {
        const val EXTRA_PRESION = "extra_presion"
        const val EXTRA_VOLUMEN = "extra_volumen"
        const val EXTRA_TEMPERATURA = "extra_temperatura"

        fun crearIntent(
            context: Context,
            presion: Double,
            volumen: Double,
            temperatura: Double
        ): Intent {
            return Intent(context, ResultadoActivity::class.java).apply {
                putExtra(EXTRA_PRESION, presion)
                putExtra(EXTRA_VOLUMEN, volumen)
                putExtra(EXTRA_TEMPERATURA, temperatura)
            }
        }
    }
}
