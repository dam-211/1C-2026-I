package ar.edu.gasesideales

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.platform.LocalContext
import ar.edu.gasesideales.logic.GasIdealViewModel
import ar.edu.gasesideales.ui.PantallaIngreso

class MainActivity : ComponentActivity() {

    private val viewModel: GasIdealViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                val context = LocalContext.current

                PantallaIngreso(
                    uiState = viewModel.ingresoUiState,
                    onPresionChange = viewModel::onPresionChange,
                    onVolumenChange = viewModel::onVolumenChange,
                    onCalcularClick = {
                        viewModel.calcularTemperatura { presion, volumen, temperatura ->
                            val intent = ResultadoActivity.crearIntent(
                                context = context,
                                presion = presion,
                                volumen = volumen,
                                temperatura = temperatura
                            )
                            context.startActivity(intent)
                        }
                    }
                )
            }
        }
    }
}

